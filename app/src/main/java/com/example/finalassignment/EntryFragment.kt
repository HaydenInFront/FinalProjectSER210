package com.example.finalassignment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.finalassignment.data.Journal
import com.example.finalassignment.databinding.FragmentEntryBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class EntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!

    lateinit var placesClient: PlacesClient

    lateinit var autocompleteFragment: AutocompleteSupportFragment
    lateinit var locationId: String

    lateinit var previousEntryLocation: String

    private val viewModel: JournalViewModel by activityViewModels {
        JournalViewModelFactory(
            (activity?.application as JournalApplication).database.journalDoa()
        )
    }

    private var journalId: Int = 0

    lateinit var journal: Journal



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)

        val apiKey = BuildConfig.PLACES_API_KEY
        Places.initialize(requireContext(), apiKey)

        placesClient = Places.createClient(requireContext());

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationId = ""

        autocompleteFragment = childFragmentManager.findFragmentById(R.id.locationInput)
                as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                locationId = place.id!!;
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Log.i(TAG, "An error occurred: $status")
            }
        })

        journalId = EntryFragmentArgs.fromBundle(requireArguments()).journalId
        if (journalId != 0) {
            previousEntryLocation = resources.getString(R.string.loadingPlaceholder)
            viewModel.retrieveJournal(journalId).observe(viewLifecycleOwner) { journal ->
                journal?.let {
                    this.journal = journal
                    binding.titleInput.setText(this.journal.title)
                    autocompleteFragment.setText(previousEntryLocation)
                    locationId = this.journal.location
                    retrievePlace(this.journal.location)
                    //binding.locationInput.setText(this.journal.location)
                    binding.dateInput.setText(this.journal.date)
                    binding.entryInput.setText(this.journal.entry)
                }
            }
        } else {
            binding.deleteButton.visibility = View.INVISIBLE
        }

        binding.submitButton.setOnClickListener() {
            if (journalId == 0) {
                addNewItem()
            } else {
                updateItem()
                findNavController().navigateUp()
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteJournal(journal)
            findNavController().navigateUp()
        }
    }

    private fun retrievePlace(locationId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(locationId, placeFields)


        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            previousEntryLocation = place.name?.toString() ?: "Unknown"
            autocompleteFragment.setText(previousEntryLocation)
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e(TAG, "Place not found: ${exception.message}")
                //val statusCode = exception.statusCode
            }
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateJournal(
                Journal(
                    journalId,
                    binding.titleInput.text.toString(),
                    locationId,
                    //binding.locationInput.text.toString(),
                    binding.dateInput.text.toString(),
                    binding.entryInput.text.toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Hide the keyboard
        val inputMethodManager = requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as
                android.view.inputmethod.InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    private fun isEntryValid() : Boolean{
        return viewModel.isEntryValid(
            binding.titleInput.text.toString(),
            locationId,
            //binding.locationInput.text.toString(),
            binding.dateInput.text.toString(),
            binding.entryInput.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewJournal(
                binding.titleInput.text.toString(),
                locationId,
                //binding.locationInput.text.toString(),
                binding.dateInput.text.toString(),
                binding.entryInput.text.toString()
            )
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(),"Please fill out all fields", Toast.LENGTH_SHORT).show()
        }

    }
}