package octoevents

import octoevents.models.entities.Webhook
import octoevents.models.repositories.WebhookRepository
import octoevents.models.unparsed.Organization
import octoevents.models.unparsed.Repository
import octoevents.models.unparsed.Sender
import octoevents.models.unparsed.UnparsedWebhook
import java.util.*

fun makeUnparsedWebhook(): UnparsedWebhook {
    val action = "TestAction"
    val sender = Sender("TestSender")
    val repository = Repository("TestRepo")
    val organization = Organization("TestOrg")
    val createdAt = Date(0)
    return UnparsedWebhook(action, sender, repository, organization, createdAt)
}

fun makeWebhook(): Webhook {
    val event = "TestEvent"
    val action = "TestAction"
    val sender = "TestSender"
    val repository = "TestRepo"
    val organization = "TestOrg"
    val createdAt = Date(0)
    return Webhook(event, action, sender, repository, organization, createdAt)
}

fun makeWebhookRepository(): WebhookRepository {
    return object : WebhookRepository {
        override fun insert(webhook: Webhook) { }
    }
}
