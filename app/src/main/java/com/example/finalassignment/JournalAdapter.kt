package com.example.finalassignment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalassignment.data.Journal
import com.example.finalassignment.databinding.JournalItemBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class JournalAdapter(private val onJournalClicked: (Journal) -> Unit): ListAdapter<Journal, JournalAdapter.JournalViewHolder>(DiffCallback) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        return JournalViewHolder(JournalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener() {
            onJournalClicked(current)
        }
        holder.bind(current)
    }

    class JournalViewHolder(private var binding: JournalItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        val apiKey = BuildConfig.PLACES_API_KEY
        init {
            Places.initialize(binding.root.context, apiKey)
        }

        private var placesClient = Places.createClient(binding.root.context)
        fun bind(journal: Journal) {
            val locationId = journal.location
            val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

            val request = FetchPlaceRequest.newInstance(locationId, placeFields)

            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                binding.apply {
                    journalItemTitle.text = journal.title
                    journalItemDate.text = journal.date
                    journalItemLocation.text = place.name
                }
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("JournalViewHolder", "Place not found: ${exception.message}")
                }
            }


            Log.d("JournalViewHolder", "Binding journal: $journal")
            /*binding.apply {
                journalItemTitle.text = journal.title
                journalItemDate.text = journal.date
                journalItemLocation.text = "Loading..."
            }*/
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Journal>() {
            override fun areItemsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}