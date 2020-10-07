package octoevents

import io.mockk.*
import octoevents.models.entities.Webhook
import octoevents.models.services.WebhookService
import octoevents.models.unparsed.Issue
import octoevents.models.unparsed.Repository
import octoevents.models.unparsed.Sender
import octoevents.models.unparsed.UnparsedWebhook
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import java.time.LocalDateTime
import kotlin.test.Test

class WebhookServiceTest : KoinTest {

    private val webhookRepositoryStub by lazy {
        spyk(makeWebhookRepository())
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(module {
            single { webhookRepositoryStub }
        })
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Test
    fun `Should call insert from WebhookRepository when create method is called`() {
        val sut = WebhookService()
        val date = LocalDateTime.now()
        val unparsedWebhook = UnparsedWebhook(
            Issue(1, "Test", "Test Issue"),
            "TestAction",
            Sender("TestLogin"),
            Repository("TestRepo"),
            date,
            null
        )
        val webhook = Webhook(
            "TestEvent",
            1,
            "Test: Test Issue",
            "TestAction",
            "TestLogin",
            "TestRepo",
            date
        )
        sut.create(unparsedWebhook, "TestEvent")
        verify { webhookRepositoryStub.insert(webhook) }
    }

    @Test
    fun `Should pass the correct data to WebhookRepository on create method call`() {
        val sut = WebhookService()
        val unparsedWebhook = makeUnparsedWebhook()
        val webhook = makeWebhook()
        sut.create(unparsedWebhook, "TestEvent")
        verify { webhookRepositoryStub.insert(webhook) }
    }

    @Test(expected = Exception::class)
    fun `Should throw when WebhookRepository throws`() {
        val webhook = makeWebhook()
        every { webhookRepositoryStub.insert(webhook) } throws Exception("TestException")
        val sut = WebhookService()
        sut.create(makeUnparsedWebhook(), "TestEvent")
    }
}