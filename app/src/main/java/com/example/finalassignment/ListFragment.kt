package com.example.finalassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalassignment.databinding.FragmentListBinding

/**
 * ListFragment is a Fragment that displays a list of journal entries.
 * It uses a RecyclerView to display the list, and each item in the list is clickable,
 * leading to the EntryFragment where the details of the clicked journal entry can be viewed and edited.
 * There are also buttons to navigate to the MapFragment and to add a new journal entry.
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    val binding get() = _binding!!

    /**
     * ViewModel for managing the data related to Journals.
     */
    private val viewModel: JournalViewModel by activityViewModels {
        JournalViewModelFactory(
            (activity?.application as JournalApplication).database.journalDoa()
        )
    }

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Sets up the view after it has been created. This includes setting up the RecyclerView
     * and its adapter, and setting up the buttons to navigate to the MapFragment and to add a new journal entry.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = JournalAdapter {
            val action = ListFragmentDirections.actionListFragmentToEntryFragment(it.id)
            view.findNavController().navigate(action)
        }
        binding.listRecycler.adapter = adapter
        binding.listRecycler.layoutManager = LinearLayoutManager(this.context)
        viewModel.allJournals.observe(viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        val mapButton = binding.mapButton

        mapButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_listFragment_to_mapFragment)
        }

        val addButton = binding.addButton

        addButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToEntryFragment(0)
            view.findNavController().navigate(action)
        }
    }
}