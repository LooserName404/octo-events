package octoevents

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.mockk.*
import octoevents.controllers.WebhookController
import octoevents.models.entities.Webhook
import octoevents.models.services.WebhookService
import octoevents.models.unparsed.Organization
import octoevents.models.unparsed.Repository
import octoevents.models.unparsed.Sender
import octoevents.models.unparsed.UnparsedWebhook
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class WebhookControllerTest : KoinTest {

    private val ctx = mockk<Context>(relaxed = true)

    private val webhookServiceStub by lazy {
        spyk(WebhookService())
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(module {
            single { webhookServiceStub }
        })
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
    fun `Should call create from WebhookService when its create is called`() {
        val sut = WebhookController()
        val date = Date()
        every { ctx.body<UnparsedWebhook>() } answers { UnparsedWebhook(createdAt = date) }
        sut.create(ctx)
        verify { webhookServiceStub.create(Webhook(createdAt = date)) }
    }

    @Test
    fun `Should pass the right data to WebhookService create method`() {
        val event = "TestEvent"
        val action = "TestAction"
        val sender = Sender("TestSender")
        val repository = Repository("TestRepo")
        val organization = Organization("TestOrg")
        val createdAt = Date(0)
        val unparsedWebhook = UnparsedWebhook(event, action, sender, repository, organization, createdAt)

        every { ctx.body<UnparsedWebhook>() } answers { unparsedWebhook }
        val sut = WebhookController()
        sut.create(ctx)
        verify {
            webhookServiceStub.create(
                Webhook(event, action, sender.name, repository.full_name, organization.login, createdAt)
            )
        }
    }

    @Test(expected = Exception::class)
    fun `Should throw if WebhookService throws`() {
        every { webhookServiceStub.create(Webhook()) } throws Exception("Test Exception")
        val sut = WebhookController()
        sut.create(ctx)
    }

}