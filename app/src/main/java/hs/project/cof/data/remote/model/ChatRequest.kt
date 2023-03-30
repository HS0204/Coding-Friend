package hs.project.cof.data.remote.model

data class ChatRequest(
    val messages: List<ChatRequestMessage>,
    val model: String
)

data class ChatRequestMessage(
    val content: String,
    val role: String
)