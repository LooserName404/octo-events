package octoevents.controllers

import io.javalin.http.Context
import octoevents.models.services.WebhookService
import org.koin.core.KoinComponent
import org.koin.core.inject

class WebhookController : KoinComponent {

    private val webhookService by inject<WebhookService>()

    fun create(ctx: Context) {
        webhookService.create()
    }
}