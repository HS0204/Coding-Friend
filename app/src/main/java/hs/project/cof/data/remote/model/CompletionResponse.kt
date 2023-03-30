package hs.project.cof.data.remote.model

data class CompletionResponse(
    val choices: List<CompletionChoice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: CompletionUsage
)

data class CompletionChoice(
    val finish_reason: String,
    val index: Int,
    val logprobs: Any,
    val text: String
)

data class CompletionUsage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)