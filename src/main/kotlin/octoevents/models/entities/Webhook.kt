package octoevents.models.entities

import java.util.*

data class Webhook(
    val event: String = "",
    val action: String? = "",
    val sender: String = "",
    val repository: String? = null,
    val organization: String? = null,
    val createdAt: Date = Date()
)