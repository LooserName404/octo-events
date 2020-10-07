package octoevents.models.repositories

import octoevents.models.entities.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import org.koin.core.inject

class DbWebhookRepository : KoinComponent, WebhookRepository {
    private val db by inject<Database>()

    override fun insert(webhook: Webhook) {
        transaction(db) {
            WebhookEntity.new {
                event = webhook.event
                issue = webhook.issue
                text = webhook.text
                action = webhook.action
                sender = webhook.sender
                repository = webhook.repository
                createdAt = webhook.createdAt
            }
        }
    }
}