package hs.project.cof.data.model

data class ChatResponse(
    val choices: List<ChatChoice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: ChatUsage
)

data class ChatChoice(
    val finish_reason: String,
    val index: Int,
    val message: ChatResponseMessage
)

data class ChatResponseMessage(
    val content: String,
    val role: String
)

data class ChatUsage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)