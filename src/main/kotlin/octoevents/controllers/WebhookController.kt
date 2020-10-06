package octoevents.controllers

import io.javalin.http.Context
import octoevents.models.entities.Webhook
import octoevents.models.services.WebhookService
import octoevents.models.unparsed.UnparsedWebhook
import org.koin.core.KoinComponent
import org.koin.core.inject

class WebhookController : KoinComponent {

    private val webhookService by inject<WebhookService>()

    fun create(ctx: Context) {
        val (action, sender, repository, organization, createdAt) = ctx.body<UnparsedWebhook>()
        val event = ctx.header<String>("X-GitHub-Event").get()
        webhookService.create(Webhook(event, action, sender.name, repository?.full_name, organization?.login, createdAt))
        ctx.status(201)
    }
}