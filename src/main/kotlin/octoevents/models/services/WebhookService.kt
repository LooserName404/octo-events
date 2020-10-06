package octoevents.models.services

import octoevents.models.entities.Webhook
import octoevents.models.repositories.WebhookRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class WebhookService : KoinComponent {
    private val webhookRepository by inject<WebhookRepository>()

    fun create(webhook: Webhook) {
        webhookRepository.insert(webhook)
    }
}