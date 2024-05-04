package com.example.finalassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

/**
 * MenuFragment is a Fragment that displays the main menu of the application.
 * It contains a button that, when clicked, navigates to the ListFragment where the list of journal entries is displayed.
 */
class MenuFragment : Fragment() {

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    /**
     * Sets up the view after it has been created. This includes setting up the start button
     * to navigate to the ListFragment when clicked.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startButton = view.findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_menuFragment_to_listFragment)
        }
    }
}