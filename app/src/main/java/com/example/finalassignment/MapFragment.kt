package com.example.finalassignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.finalassignment.data.Journal
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient

class MapFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener, OnInfoWindowClickListener {

    // PlacesClient is used to interact with Google Places API
    private lateinit var placesClient: PlacesClient
    // List of Journal entries
    private lateinit var journals: List<Journal>
    // List of places to be marked on the map
    private lateinit var placeBundleList: ArrayList<PlaceBundle>
    // Google Map object
    private lateinit var map: GoogleMap
    private lateinit var options: RadioGroup

    // ViewModel for managing and observing changes in Journal data
    private val viewModel: JournalViewModel by activityViewModels {
        JournalViewModelFactory(
            (activity?.application as JournalApplication).database.journalDoa()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the Google Map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the Places API
        val apiKey = BuildConfig.PLACES_API_KEY
        Places.initialize(requireContext(), apiKey)

        // Create a new Places client
        placesClient = Places.createClient(requireContext());

        // Initialize the list of places
        placeBundleList = ArrayList<PlaceBundle>()

        // Observe changes in the journal data
        viewModel.allJournals.observe(viewLifecycleOwner) { journals ->
            if (journals != null) {
                this.journals = journals
                getPlaces()
            }
        }
    }

    // onMapReady is called when the Google Map is ready to be used
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (placeBundleList.isNotEmpty()){
            addMarkers()
        }

        map.setOnMarkerClickListener(this)
        map.setOnInfoWindowClickListener(this)
    }

    // getPlaces fetches the corresponding place for each journal entry and adds a marker on the map
    private fun getPlaces() {
        for (journal in journals) {
            val task = getTask(journal.location)
            task.addOnSuccessListener { response ->
                val placeBundle = PlaceBundle(journal, response.place)
                if (map != null) {
                    val marker = map.addMarker(
                        MarkerOptions()
                            .position(placeBundle.place.latLng!!)
                            .title(placeBundle.place.name)
                            .snippet(placeBundle.journal.title)
                    )
                    marker?.tag = placeBundle
                } else {
                    placeBundleList.add(placeBundle)
                }
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("MapActivity", "Place not found: ${exception.message}")
                }
            }
        }
    }

    // getTask fetches a place from the Places API using the location ID
    private fun getTask(locationId: String): Task<FetchPlaceResponse> {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(locationId, placeFields)

        return placesClient.fetchPlace(request)
    }

    // addMarkers adds markers for each place in listOfPlaces on the map
    private fun addMarkers() {
        for (placeBundle in placeBundleList) {
            val marker = map.addMarker(
                MarkerOptions()
                    .position(placeBundle.place.latLng!!)
                    .title(placeBundle.place.name)
                    .snippet(placeBundle.journal.title)
            )
            marker?.tag = placeBundle
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("Marker clicked", "Marker clicked")
        p0.showInfoWindow()
        return true
    }

    override fun onInfoWindowClick(p0: Marker) {
        Log.d("Info window clicked", "Info window clicked");
        findNavController().navigate(
            MapFragmentDirections.actionMapFragmentToEntryFragment(
                (p0.tag as PlaceBundle).journal.id
            )
        )
        Log.d("Info window clicked", "navigating to entry fragment")
        Log.d("Info window clicked", "journal id: ${(p0.tag as PlaceBundle).journal.title}")
    }

}

data class PlaceBundle(val journal: Journal, val place: Place)