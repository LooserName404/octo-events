package octoevents

import io.javalin.http.Context
import io.mockk.*
import octoevents.controllers.WebhookController
import octoevents.models.entities.Webhook
import octoevents.models.services.WebhookService
import octoevents.models.unparsed.*
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import java.time.LocalDateTime
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
            single { makeWebhookRepository() }
            single { webhookServiceStub }
        })
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @BeforeTest
    fun setUp() {
        every { ctx.header<String>("X-GitHub-Event").get() } answers { "TestEvent" }
        every { ctx.body<UnparsedWebhook>() } answers { makeUnparsedWebhook() }
        every { ctx.pathParam<Int>("issue").get() } answers { 1 }
    }

    @Test
    fun `Should call create from WebhookService when its create is called`() {
        val sut = WebhookController()
        val date = LocalDateTime.now()
        every { ctx.body<UnparsedWebhook>() } answers {
            UnparsedWebhook(
                Issue(1, "Test", "Test Issue"),
                "TestAction",
                Sender("TestLogin"),
                Repository("TestRepo"),
                date,
                null
            )
        }
        sut.create(ctx)
        verify {
            webhookServiceStub.create(
                UnparsedWebhook(
                    Issue(1, "Test", "Test Issue"),
                    "TestAction",
                    Sender("TestLogin"),
                    Repository("TestRepo"),
                    date,
                    null
                ),
                "TestEvent"
            )
        }
    }

    @Test
    fun `Should pass the correct data to WebhookService create method`() {
        val sut = WebhookController()
        sut.create(ctx)
        verify { webhookServiceStub.create(makeUnparsedWebhook(), "TestEvent") }
    }

    @Test(expected = Exception::class)
    fun `Should throw if WebhookService create throws`() {
        every { webhookServiceStub.create(makeUnparsedWebhook(), "TestEvent") } throws Exception("Test Exception")
        val sut = WebhookController()
        sut.create(ctx)
    }

    @Test
    fun `Should respond with status code 201 when create method runs correctly`() {
        val sut = WebhookController()
        sut.create(ctx)
        verify { ctx.status(201) }
    }

    @Test
    fun `Should call listAll from WebhookService when listAll is called`() {
        val sut = WebhookController()
        sut.listAll(ctx)
        verify { webhookServiceStub.listAll(1) }
    }

    @Test
    fun `Should pass the correct data to WebhookService listAll`() {
        val sut = WebhookController()
        sut.listAll(ctx)
        verify { webhookServiceStub.listAll(1) }

        every { ctx.pathParam<Int>("issue").get() } answers { 2 }
        sut.listAll(ctx)
        verify { webhookServiceStub.listAll(2) }

        every { ctx.pathParam<Int>("issue").get() } answers { 100 }
        sut.listAll(ctx)
        verify { webhookServiceStub.listAll(100) }
    }

    @Test(expected = Exception::class)
    fun `Should throw if WebhookService listAll throws`() {
        every { webhookServiceStub.listAll(1) } throws Exception()

        val sut = WebhookController()
        sut.listAll(ctx)
    }

    @Test
    fun `Should respond with Webhook list when listAll runs correctly`() {
        every { webhookServiceStub.listAll(1) } answers { listOf<Webhook>() }
        val sut = WebhookController()
        sut.listAll(ctx)
        verify { ctx.json(listOf<Webhook>()) }
    }
}