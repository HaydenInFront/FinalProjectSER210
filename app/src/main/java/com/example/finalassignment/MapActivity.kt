package com.example.finalassignment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.example.finalassignment.data.Journal
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    /*
    the places variable holds all the places. refer to their coordinates by doing .latlng and create markers for every item in places
     */

    lateinit var placesClient: PlacesClient
    lateinit var journals: LiveData<List<Journal>>
    lateinit var places: ArrayList<Place>

    private val viewModel: JournalViewModel by viewModels {
        JournalViewModelFactory(
            (application as JournalApplication).database.journalDoa()
        )
    }

    /*private val viewModel: JournalViewModel by activityViewModels {
        JournalViewModelFactory(
            (activity?.application as JournalApplication).database.journalDoa()
        )
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val apiKey = BuildConfig.PLACES_API_KEY
        Places.initialize(this, apiKey)

        placesClient = Places.createClient(this);

        journals = viewModel.allJournals
        places = getPlaces()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )

        /*
        add a single map marker from places
        googleMap.addMarker(
            MarkerOptions()
                .position(places[0].latLng(0.0, 0.0))
                .title(places[0].name)
        )
         */
    }

    private fun getPlaces(): ArrayList<Place> {
        val list = ArrayList<Place>()
        for (journal in journals.value!!) {
            val place = getPlace(journal.location)
            list.add(place)
        }
        return list
    }

    private fun getPlace(locationId: String): Place {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(locationId, placeFields)

        var place: Place? = null

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            place = response.place
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e("MapActivity", "Place not found: ${exception.message}")
            }
        }
        return place!!

        /*placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            previousEntryLocation = place.name?.toString() ?: "Unknown"
            autocompleteFragment.setText(previousEntryLocation)
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e(ContentValues.TAG, "Place not found: ${exception.message}")
                //val statusCode = exception.statusCode
            }
        }*/
    }
}