package octoevents.config

import octoevents.models.repositories.PgWebhookRepository
import octoevents.models.repositories.WebhookRepository
import octoevents.models.services.WebhookService
import org.koin.dsl.module

val injectableModules = module {
    single { getPgConnection() }
    single<WebhookRepository> { PgWebhookRepository() }
    single { WebhookService() }
}