package octoevents.models.unparsed

import java.util.*

data class Sender(val name: String = "")
data class Repository(val full_name: String = "")
data class Organization(val login: String = "")

data class UnparsedWebhook(
    val event: String = "",
    val action: String? = "",
    val sender: Sender = Sender(),
    val repository: Repository? = null,
    val organization: Organization? = null,
    val createdAt: Date = Date()
)