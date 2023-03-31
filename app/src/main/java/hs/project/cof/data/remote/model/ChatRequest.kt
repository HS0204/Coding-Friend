package hs.project.cof.data.remote.model

data class ChatRequest(
    val messages: List<ChatRequestMessage>,
    val model: String,
    val temperature: Float
)

data class ChatRequestMessage(
    val content: String,
    val role: String
)