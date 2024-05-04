import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.finalassignment.MenuFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import com.example.finalassignment.MainActivity
import com.example.finalassignment.R
import org.robolectric.annotation.Config
import java.util.regex.Pattern.matches

@MediumTest
@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class MenuFragmentTest {

    private lateinit var navController: TestNavHostController

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)
    }

    @Test
    fun navigationFromMenuFragmentToListFragment() {
        launchFragmentInContainer<MenuFragment>()
        onView(withId(R.id.startButton)).perform(click())
        assert(navController.currentDestination?.id == R.id.listFragment)
    }

}