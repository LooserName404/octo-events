package octoevents

import io.mockk.mockkClass
import io.mockk.spyk
import io.mockk.verify
import octoevents.models.entities.Webhook
import octoevents.models.services.WebhookService
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
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
        val webhook = Webhook()
        sut.create(webhook)
        verify { webhookRepositoryStub.insert(webhook) }
    }

}