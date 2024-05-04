package com.example.finalassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalassignment.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    val binding get() = _binding!!

    private val viewModel: JournalViewModel by activityViewModels {
        JournalViewModelFactory(
            (activity?.application as JournalApplication).database.journalDoa()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

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