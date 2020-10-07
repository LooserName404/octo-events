package octoevents.controllers

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import octoevents.models.entities.Webhook
import octoevents.models.services.WebhookService
import octoevents.models.unparsed.UnparsedWebhook
import org.koin.core.KoinComponent
import org.koin.core.inject

class WebhookController : KoinComponent {

    private val webhookService by inject<WebhookService>()

    fun create(ctx: Context) {
        val (issue, action, sender, repository, createdAt, comment) = ctx.body<UnparsedWebhook>()
        if (sender.login.isEmpty()) {
            throw BadRequestResponse("Missing param: Sender login")
        }

        val event = ctx.header<String>("X-GitHub-Event").get()
        val text: String

        if (comment != null && comment.body.isNotEmpty()) {
            text = comment.body
        } else {
            val issueBody = if (issue.body.length > 200) {
                issue.body.substring(0..200) + "..."
            } else {
                issue.body
            }
            text = "${issue.title}: $issueBody"
        }

        webhookService.create(Webhook(event, issue.number, text, action, sender.login, repository.full_name, createdAt))
        ctx.status(201)
    }
}