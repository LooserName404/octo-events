package octoevents.config

import octoevents.models.repositories.DbWebhookRepository
import octoevents.models.repositories.WebhookRepository
import octoevents.models.services.WebhookService
import org.koin.dsl.module

val injectableModules = module {
    single { getPgConnection() }
    single<WebhookRepository> { DbWebhookRepository() }
    single { WebhookService() }
}