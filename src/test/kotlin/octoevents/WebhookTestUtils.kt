package octoevents

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJackson
import octoevents.config.AppFactory
import octoevents.config.startSchemas
import octoevents.models.entities.Webhook
import octoevents.models.entities.WebhookTable
import octoevents.models.repositories.DbWebhookRepository
import octoevents.models.repositories.WebhookRepository
import octoevents.models.services.WebhookService
import octoevents.models.unparsed.Organization
import octoevents.models.unparsed.Repository
import octoevents.models.unparsed.Sender
import octoevents.models.unparsed.UnparsedWebhook
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.module.Module
import org.koin.dsl.module
import java.time.LocalDateTime
import java.util.*

fun makeUnparsedWebhook(): UnparsedWebhook {
    val action = "TestAction"
    val sender = Sender("TestSender")
    val repository = Repository("TestRepo")
    val organization = Organization("TestOrg")
    val createdAt = LocalDateTime.MIN
    return UnparsedWebhook(action, sender, repository, organization, createdAt)
}

fun makeWebhook(): Webhook {
    val event = "TestEvent"
    val action = "TestAction"
    val sender = "TestSender"
    val repository = "TestRepo"
    val organization = "TestOrg"
    val createdAt = LocalDateTime.MIN
    return Webhook(event, action, sender, repository, organization, createdAt)
}

fun makeWebhookRepository(): WebhookRepository {
    return object : WebhookRepository {
        override fun insert(webhook: Webhook) { }
    }
}

fun makeInjectableDependencies(): Module {
    return module {
        single { getH2Connection() }
        single<WebhookRepository> { DbWebhookRepository() }
        single { WebhookService() }
    }
}

fun makeTestApp(): Javalin {
    JavalinJackson.configure(jacksonObjectMapper().findAndRegisterModules())
    val app = Javalin.create()
    AppFactory.makeRoutes(app)
    return app
}

fun getH2Connection(): Database {
    val db = Database.connect("jdbc:h2:./testdb", "org.h2.Driver")
    transaction {
        SchemaUtils.createMissingTablesAndColumns(WebhookTable)
    }
    return db
}
