package octoevents

import io.javalin.Javalin
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.SpyK
import io.mockk.mockkClass
import io.mockk.spyk
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import octoevents.config.AppFactory
import octoevents.models.unparsed.Organization
import octoevents.models.unparsed.Repository
import octoevents.models.unparsed.Sender
import octoevents.models.unparsed.UnparsedWebhook
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import java.time.LocalDateTime
import kotlin.test.AfterTest
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
                .body(UnparsedWebhook(
                    "testAction",
                    Sender("testSender"),
                    Repository("testRepo"),
                    Organization("testOrg"),
                    LocalDateTime.now()
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