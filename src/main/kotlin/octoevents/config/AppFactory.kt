package octoevents.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJackson
import octoevents.controllers.WebhookController

class AppFactory {
    companion object {
        var PORT = 9090
        fun makeApp(): Javalin {
            JavalinJackson.configure(jacksonObjectMapper().findAndRegisterModules())
            val app = Javalin.create().start(PORT)
            makeRoutes(app)

            return app
        }

        private fun makeRoutes(app: Javalin) {
            app.post("/events", WebhookController()::create)
        }
    }
}