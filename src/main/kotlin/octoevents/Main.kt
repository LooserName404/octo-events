@file:JvmName("Main")

package octoevents

import octoevents.config.AppFactory
import octoevents.config.injectableModules
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    startKoin {
        modules(injectableModules)
    }

    AppFactory.makeApp()
}