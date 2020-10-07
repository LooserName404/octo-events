package octoevents.config

import octoevents.models.entities.WebhookTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.FileInputStream
import java.util.*

fun getDbConnection(): Database {
    val propertiesFile = FileInputStream("application.properties")
    val prop = Properties()
    prop.load(propertiesFile)

    val db = Database.connect(
        url = prop["db_url"] as String,
        driver = prop["db_driver"] as String,
        user = prop["db_user"] as String,
        password = prop["db_password"] as String
    )
    startSchemas(db)
    return db
}

fun startSchemas(db: Database) {
    transaction(db) {
        SchemaUtils.create(WebhookTable)
    }
}