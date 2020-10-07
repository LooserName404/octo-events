package octoevents.models.entities

import java.time.LocalDateTime

data class Webhook(
    val event: String,
    val issue: Int,
    val text: String,
    val action: String,
    val sender: String,
    val repository: String,
    val createdAt: LocalDateTime
)