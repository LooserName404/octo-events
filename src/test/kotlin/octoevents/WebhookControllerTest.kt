package octoevents

import io.javalin.http.BadRequestResponse
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
        every { ctx.contentType() } answers { "application/json" }
        every { ctx.header<String>("X-GitHub-Event").get() } answers { "TestEvent" }
        every { ctx.body<UnparsedWebhook>() } answers { makeUnparsedWebhook() }
        every { ctx.pathParam<Int>("issue").get() } answers { 1 }
        every { webhookServiceStub.listByIssue(1) } answers { listOf(makeWebhook()) }
    }

    @Test
    fun `Should call create from WebhookService when its create is called`() {
        val sut = WebhookController()
        val date = LocalDateTime.now()
        every { ctx.body<UnparsedWebhook>() } answers {
            UnparsedWebhook(
                Issue(1, "Test", "Test Issue", date),
                "TestAction",
                Sender("TestLogin"),
                Repository("TestRepo"),
                null
            )
        }
        sut.create(ctx)
        verify {
            webhookServiceStub.create(
                UnparsedWebhook(
                    Issue(1, "Test", "Test Issue", date),
                    "TestAction",
                    Sender("TestLogin"),
                    Repository("TestRepo"),
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
    fun `Should respond with status code 204 when GitHub Event is ping`() {
        every { ctx.header<String>("X-GitHub-Event").get() } answers { "ping" }
        val sut = WebhookController()
        sut.create(ctx)
        verify { ctx.status(204) }
        verify(exactly = 0) { webhookServiceStub.create(makeUnparsedWebhook(), "ping") }
    }

    @Test(expected = BadRequestResponse::class)
    fun `Should respond with status code 400 when content type is not json`() {
        every { ctx.contentType() } answers { "application/x-www-form-urlencoded" }
        val sut = WebhookController()
        sut.create(ctx)
    }

    @Test
    fun `Should call listByIssue from WebhookService when listByIssue is called`() {
        val sut = WebhookController()
        sut.listByIssue(ctx)
        verify { webhookServiceStub.listByIssue(1) }
    }

    @Test
    fun `Should pass the correct data to WebhookService listByIssue`() {
        val sut = WebhookController()
        sut.listByIssue(ctx)
        verify { webhookServiceStub.listByIssue(1) }

        every { ctx.pathParam<Int>("issue").get() } answers { 2 }
        sut.listByIssue(ctx)
        verify { webhookServiceStub.listByIssue(2) }

        every { ctx.pathParam<Int>("issue").get() } answers { 100 }
        sut.listByIssue(ctx)
        verify { webhookServiceStub.listByIssue(100) }
    }

    @Test(expected = Exception::class)
    fun `Should throw if WebhookService listByIssue throws`() {
        every { webhookServiceStub.listByIssue(1) } throws Exception()

        val sut = WebhookController()
        sut.listByIssue(ctx)
    }

    @Test
    fun `Should respond with Webhook list when listByIssue runs correctly`() {
        val sut = WebhookController()
        sut.listByIssue(ctx)
        verify { ctx.json(listOf(makeWebhook())) }
    }

    @Test
    fun `Should respond with status code 404 when cannot find the given Issue`() {
        every { webhookServiceStub.listByIssue(1) } answers { listOf() }
        val sut = WebhookController()
        sut.listByIssue(ctx)
        verify { ctx.status(404) }
    }
}