package com.picpay.desafio.android

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.RecyclerViewMatchers.checkRecyclerViewItem
import com.picpay.desafio.android.modules.provideOkHttpClient
import com.picpay.desafio.android.modules.providePicPayService
import com.picpay.desafio.android.util.loadJSONFromAsset
import junit.framework.TestCase.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivityTest : KoinTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    val server: MockWebServer = MockWebServer()
    private val baseUrl = server.url("/").toString()

    val testModule = module {
        single { provideOkHttpClient() }
        single {
            Retrofit.Builder()
                .baseUrl(baseUrl)  // Use the server instance directly
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        single { providePicPayService(get()) }
    }

    @Before
    fun setup() {
        loadKoinModules(testModule)

        // Set up dispatcher
    }

    @After
    fun tearDown() {
        server.shutdown()
        stopKoin()
    }

    @Test
    fun shouldDisplayListItem() {
        server.enqueue(successResponse)

        val scenario = ActivityScenario.launch(MainActivity::class.java)
        checkRecyclerViewItem(R.id.recyclerView,0,isDisplayed())
        checkRecyclerViewItem(R.id.recyclerView,0, withText("Sandrine Spinka"))
        onView(withId(R.id.recyclerView)).check( { view, noViewFoundException ->
            val adapter = (view as? RecyclerView)?.adapter
            val itemCount = adapter?.itemCount

            assertEquals("RecyclerView item count", 50, itemCount)
        })
    }

    @Test
    fun shouldDisplayBrokenListItem() {
        server.enqueue(brokenResponse)

        val scenario = ActivityScenario.launch(MainActivity::class.java)
        checkRecyclerViewItem(R.id.recyclerView,0,isDisplayed())
        checkRecyclerViewItem(R.id.recyclerView,1, withText("Carli Carroll"))
    }

    @Test
    fun shouldNotDisplayListItem() {
        server.enqueue(errorResponse)

        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recyclerView)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)
            )
        )
    }

    @Test
    fun shouldDisplayEmptyListMessage() {
        server.enqueue(emptySuccessResponse)
        val emptytMessage = context.getString(R.string.empty_message)

        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recyclerView)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withText(emptytMessage)).check(matches(isDisplayed()))
    }

    @Test
    fun checkTitle() {
        val expectedTitle = context.getString(R.string.title)
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withText(expectedTitle)).check(matches(isDisplayed()))
    }

    companion object {
        private val successResponse by lazy {

            val body = loadJSONFromAsset("users.json")
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }
        private val emptySuccessResponse by lazy{
            val body = "[]"
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val brokenResponse by lazy{
            val body = loadJSONFromAsset("users-broken.json")
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val errorResponse by lazy { MockResponse().setResponseCode(400) }
    }
}