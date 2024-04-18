package com.example.finalassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapButton = view.findViewById<Button>(R.id.mapButton)

        mapButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_listFragment_to_mapFragment)
        }

        val addButton = view.findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_listFragment_to_entryFragment)
        }
    }
}