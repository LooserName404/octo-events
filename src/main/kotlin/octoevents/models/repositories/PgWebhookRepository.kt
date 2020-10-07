package octoevents.models.repositories

import octoevents.models.entities.Webhook
import octoevents.models.entities.WebhookEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import org.koin.core.inject

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