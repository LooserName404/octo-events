package octoevents.models.repositories

import octoevents.models.entities.Webhook
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.LocalDateTime

class PgWebhookRepository : KoinComponent, WebhookRepository {
    private val db by inject<Database>()

    override fun insert(webhook: Webhook) {
        transaction(db) {
            WebhookEntity.new {
                event = webhook.event
                action = webhook.action
                sender = webhook.sender
                repository = webhook.repository
                organization = webhook.organization
                createdAt = webhook.createdAt
            }
        }
    }
}

object WebhookTable : LongIdTable(name = "Webhook") {
    val event: Column<String> = varchar("event", 32)
    val action: Column<String?> = varchar("action", 32).nullable()
    val sender: Column<String> = varchar("sender", 64)
    val repository: Column<String?> = varchar("repository", 128).nullable()
    val organization: Column<String?> = varchar("organization", 128).nullable()
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
}

class WebhookEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<WebhookEntity>(WebhookTable)

    var event by WebhookTable.event
    var action by WebhookTable.action
    var sender by WebhookTable.sender
    var repository by WebhookTable.repository
    var organization by WebhookTable.organization
    var createdAt by WebhookTable.createdAt
}