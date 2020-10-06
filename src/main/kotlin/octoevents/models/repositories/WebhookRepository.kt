package octoevents.models.repositories

import octoevents.models.entities.Webhook

interface WebhookRepository {
    fun insert(webhook: Webhook)
}