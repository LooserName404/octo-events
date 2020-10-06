package octoevents

import io.javalin.Javalin
import octoevents.config.injectableModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun main(args: Array<String>) {
    startKoin {
        printLogger(Level.NONE)
        modules(injectableModules)
    }

    val app = Javalin.create().start()

    app.get("/") {

    }
}