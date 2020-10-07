package octoevents.config

import octoevents.models.entities.WebhookTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun getPgConnection(): Database {
    val db = Database.connect(
        url = "jdbc:postgresql://localhost:5432/octo",
        driver = "org.postgresql.Driver",
        user = "octo",
        password = "octo"
    )
    startSchemas(db)
    return db
}

fun startSchemas(db: Database) {
    transaction(db) {
        SchemaUtils.create(WebhookTable)
    }
}