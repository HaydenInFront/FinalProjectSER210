package com.example.finalassignment

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class EntryFragmentTest {
    private var entryFragment = EntryFragment()
    private lateinit var placesClient: PlacesClient
    private var previousEntry = ""
    private lateinit var context: Context
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private var autoCompleteFragmentResourceId: Int = 0

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        Places.initialize(context, BuildConfig.PLACES_API_KEY)
        placesClient = Places.createClient(context)
        entryFragment.placesClient = placesClient

        autocompleteFragment = AutocompleteSupportFragment()

        previousEntry = "test"
        autoCompleteFragmentResourceId = context.resources.getIdentifier("autocomplete_fragment", "id", context.packageName)
    }

    @Test
    fun getPlacesClient() {
        val test = entryFragment.placesClient
        assertEquals(placesClient, test)
    }

    @Test
    fun setPlacesClient() {
        entryFragment.placesClient = placesClient
        val test = entryFragment.placesClient
        assertEquals(test, placesClient)
    }

    @Test
    fun getLocationId() {
        entryFragment.locationId = ""
        val test = entryFragment.locationId
        assertEquals("", test)
    }

    @Test
    fun setLocationId() {
        entryFragment.locationId = "test"
        val test = entryFragment.locationId
        assertEquals("test", test)
    }

    @Test
    fun getPreviousEntryLocation() {
        entryFragment.previousEntryLocation = previousEntry
        assertEquals(previousEntry, entryFragment.previousEntryLocation)

    }

    @Test
    fun setPreviousEntryLocation() {
        entryFragment.previousEntryLocation = "test"
        val test = entryFragment.previousEntryLocation
        assertEquals("test", test)
    }
}