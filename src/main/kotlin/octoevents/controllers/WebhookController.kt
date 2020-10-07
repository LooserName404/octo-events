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
        val unparsedWebhook = ctx.body<UnparsedWebhook>()
        val event = ctx.header<String>("X-GitHub-Event").get()
        webhookService.create(unparsedWebhook, event)
        ctx.status(201)
    }

    fun listByIssue(ctx: Context) {
        val issue = ctx.pathParam<Int>("issue").get()
        val webhooks = webhookService.listByIssue(issue)
        ctx.json(webhooks)
    }
}