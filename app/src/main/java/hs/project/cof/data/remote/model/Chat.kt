package hs.project.cof.data.remote.model

data class Chat(
    val messages: List<RequestMessage>,
    val model: String
)

data class RequestMessage(
    val content: String,
    val role: String
)