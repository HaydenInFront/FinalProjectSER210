package com.example.finalassignment

import android.content.ContentValues.TAG
import android.graphics.Bitmap
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
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest

/**
 * Adapter for the RecyclerView that displays the list of journal entries.
 * @property onJournalClicked function to be called when a journal entry is clicked.
 */
class JournalAdapter(private val onJournalClicked: (Journal) -> Unit): ListAdapter<Journal, JournalAdapter.JournalViewHolder>(DiffCallback) {
    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        return JournalViewHolder(JournalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener() {
            onJournalClicked(current)
        }
        holder.bind(current)
    }

    /**
     * ViewHolder for the RecyclerView items.
     */
    class JournalViewHolder(private var binding: JournalItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        val apiKey = BuildConfig.PLACES_API_KEY
        init {
            Places.initialize(binding.root.context, apiKey)
        }

        private var placesClient = Places.createClient(binding.root.context)

        /**
         * Binds the data to the ViewHolder.
         */
        fun bind(journal: Journal) {
            val locationId = journal.location
            val placeFields = listOf(Place.Field.NAME, Place.Field.PHOTO_METADATAS)

            val request = FetchPlaceRequest.newInstance(locationId, placeFields)

            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                val metadata = place.photoMetadatas

                binding.apply {
                    journalItemTitle.text = journal.title
                    journalItemDate.text = journal.date
                    journalItemLocation.text = place.name
                    journalItemEntry.text = journal.entry
                }

                if (metadata == null || metadata.isEmpty()) {
                    Log.w(TAG, "No photo metadata.")

                    return@addOnSuccessListener
                }
                val photoMetadata = metadata.first()
                val photoRequest = photoMetadata?.let {
                    FetchPhotoRequest.builder(it)
                        .setMaxHeight(400)
                        .setMaxWidth(400)
                        .build()
                }

                var bitmap: Bitmap

                placesClient.fetchPhoto(photoRequest!!).addOnSuccessListener { fetchPhotoResponse ->
                    bitmap = fetchPhotoResponse.bitmap
                    binding.journalItemImage.setImageBitmap(bitmap)

                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Log.e("JournalViewHolder", "Photo not found: ${exception.message}")
                    }
                }
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("JournalViewHolder", "Place not found: ${exception.message}")
                }
            }
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     */
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