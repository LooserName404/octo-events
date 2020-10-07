package octoevents.models.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime


object WebhookTable : LongIdTable(name = "Webhook") {
    val event: Column<String> = varchar("event", 32)
    val issue: Column<Int> = integer("issue")
    val text: Column<String> = varchar("text", 512)
    val action: Column<String> = varchar("action", 32)
    val sender: Column<String> = varchar("sender", 64)
    val repository: Column<String> = varchar("repository", 128)
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
}

class WebhookEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<WebhookEntity>(WebhookTable)

    var event by WebhookTable.event
    var issue by WebhookTable.issue
    var text by WebhookTable.text
    var action by WebhookTable.action
    var sender by WebhookTable.sender
    var repository by WebhookTable.repository
    var createdAt by WebhookTable.createdAt
}