package octoevents.models.entities

import java.time.LocalDateTime

data class Webhook(
    val event: String = "",
    val action: String? = "",
    val sender: String = "",
    val repository: String? = null,
    val organization: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)