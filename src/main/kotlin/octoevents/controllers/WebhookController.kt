package octoevents.controllers

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import octoevents.models.entities.Webhook
import octoevents.models.services.WebhookService
import octoevents.models.unparsed.UnparsedWebhook
import org.koin.core.KoinComponent
import org.koin.core.inject

class WebhookController : KoinComponent {

    private val webhookService by inject<WebhookService>()

    fun create(ctx: Context) {
        if (ctx.contentType() != "application/json") {
            throw BadRequestResponse("Content-Type doesn't match application/json")
        }
        val event = ctx.header<String>("X-GitHub-Event").get()
        if (event == "ping") {
            ctx.status(204)
            return
        }
        val unparsedWebhook = ctx.body<UnparsedWebhook>()
        webhookService.create(unparsedWebhook, event)
        ctx.status(201)
    }

    fun listByIssue(ctx: Context) {
        val issue = ctx.pathParam<Int>("issue").get()
        val webhooks = webhookService.listByIssue(issue)
        if (webhooks.isEmpty()) {
            throw NotFoundResponse("No event found for issue $issue.")
        }
        ctx.json(webhooks)
    }
}