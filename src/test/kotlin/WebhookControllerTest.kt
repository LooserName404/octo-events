import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.mockk.*
import octoevents.controllers.WebhookController
import octoevents.models.services.WebhookService
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import kotlin.test.BeforeTest
import kotlin.test.Test

class WebhookControllerTest : KoinTest {

    private val ctx = mockk<Context>(relaxed = true)

    private val webhookServiceStub by lazy {
        declareMock<WebhookService> {
            every { create() } answers { ctx.status(201) }
        }
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

        sut.create(ctx)

        verify { webhookServiceStub.create() }
    }


}