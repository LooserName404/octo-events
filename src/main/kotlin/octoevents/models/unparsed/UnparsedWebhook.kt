package octoevents.models.unparsed

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sender(val name: String = "")

@JsonIgnoreProperties(ignoreUnknown = true)
data class Repository(val full_name: String = "")

@JsonIgnoreProperties(ignoreUnknown = true)
data class Organization(val login: String = "")

@JsonIgnoreProperties(ignoreUnknown = true)
data class UnparsedWebhook(
    val action: String? = null,
    val sender: Sender = Sender(),
    val repository: Repository? = null,
    val organization: Organization? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)