package octoevents.models.unparsed

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sender(val login: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Repository(val full_name: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Issue(val number: Int, val title: String, val body: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Comment(val body: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UnparsedWebhook(
    val issue: Issue,
    val action: String,
    val sender: Sender,
    val repository: Repository,
    val createdAt: LocalDateTime,
    val comment: Comment?
)