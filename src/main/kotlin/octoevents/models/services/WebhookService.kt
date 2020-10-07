package octoevents.models.services

import io.javalin.http.BadRequestResponse
import octoevents.models.entities.Webhook
import octoevents.models.repositories.WebhookRepository
import octoevents.models.unparsed.UnparsedWebhook
import org.koin.core.KoinComponent
import org.koin.core.inject

class WebhookService : KoinComponent {
    private val webhookRepository by inject<WebhookRepository>()

    fun create(unparsedWebhook: UnparsedWebhook, event: String) {
        val (issue, action, sender, repository, createdAt, comment) = unparsedWebhook

        val text = if (comment != null && comment.body.isNotEmpty()) {
            comment.body
        } else {
            val issueBody = if (issue.body.length > 200) {
                issue.body.substring(0..200) + "..."
            } else {
                issue.body
            }
            "${issue.title}: $issueBody"
        }

        val webhook = Webhook(event, issue.number, text, action, sender.login, repository.full_name, createdAt)

        webhookRepository.insert(webhook)
    }

    fun listAll(issue: Int): Map<Int, Webhook> {
        return mapOf()
    }
}