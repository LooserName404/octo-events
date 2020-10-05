package octoevents.config

import octoevents.models.services.WebhookService
import org.koin.dsl.module

val injectableModules = module {
    single { WebhookService() }
}