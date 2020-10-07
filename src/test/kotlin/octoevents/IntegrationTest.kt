package octoevents

import io.mockk.MockKAnnotations
import io.mockk.mockkClass
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import octoevents.models.unparsed.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import java.time.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class IntegrationTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(makeInjectableDependencies())
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Should return status code 201 from POST events when created`() {
        val response: HttpResponse<String> =
            Unirest
                .post("http://localhost:9999/events")
                .header("X-GitHub-Event", "testEvent")
                .header("Content-Type", "application/json")
                .body(UnparsedWebhook(
                    Issue(1, "Test", "Test Issue", LocalDateTime.now()),
                    "testAction",
                    Sender("testSender"),
                    Repository("testRepo"),
                    null
                )).asString()
        assertEquals(201, response.status)
    }

    companion object {
        private var app = makeTestApp()
        private val PORT = 9999

        @BeforeClass
        @JvmStatic
        fun startUp() {
            app.start(PORT)
        }

        @AfterClass
        @JvmStatic
        fun shutDown() {
            app.stop()
        }
    }
}