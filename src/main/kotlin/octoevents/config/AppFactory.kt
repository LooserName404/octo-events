package octoevents.config

import io.javalin.Javalin
import octoevents.controllers.WebhookController
import org.koin.core.context.startKoin

class AppFactory {
    companion object {
        var PORT = 9090
        fun makeApp(): Javalin {
            val app = Javalin.create().start(PORT)
            makeRoutes(app)

            return app
        }

        private fun makeRoutes(app: Javalin) {
            app.post("/events", WebhookController()::create)
        }
    }
}