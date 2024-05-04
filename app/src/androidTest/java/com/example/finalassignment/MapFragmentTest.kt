import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.finalassignment.MapFragment
import com.example.finalassignment.PlaceBundle
import com.example.finalassignment.R
import com.example.finalassignment.data.Journal
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapFragmentTest {

    private lateinit var navController: TestNavHostController
    private lateinit var scenario: FragmentScenario<MapFragment>

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)

        scenario = launchFragmentInContainer(themeResId = R.style.Base_Theme_FinalAssignment) {
            MapFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
    }

    @Test
    fun navigatesToEntryFragment_whenInfoWindowClicked() {
        val placeBundle = PlaceBundle(Journal(1, "title", "locationId"), Place.builder().setName("placeName").setLatLng(LatLng(0.0, 0.0)).build())
        val markerOptions = MarkerOptions().position(placeBundle.place.latLng!!).title(placeBundle.place.name).snippet(placeBundle.journal.title)
        val marker = mockk<Marker>(relaxed = true)
        every { marker.tag } returns placeBundle

        scenario.onFragment { fragment ->
            fragment.onInfoWindowClick(marker)
        }

        assertEquals(navController.currentDestination?.id, R.id.entryFragment)
    }

    @Test
    fun showsInfoWindow_whenMarkerClicked() {
        val marker = mockk<Marker>(relaxed = true)
        every { marker.showInfoWindow() } returns Unit

        scenario.onFragment { fragment ->
            fragment.onMarkerClick(marker)
        }

        verify { marker.showInfoWindow() }
    }
}