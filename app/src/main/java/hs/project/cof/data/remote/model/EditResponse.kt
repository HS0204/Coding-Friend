package hs.project.cof.data.remote.model

data class EditResponse(
    val choices: List<EditChoice>,
    val created: Int,
    val `object`: String,
    val usage: EditUsage
)

data class EditChoice(
    val index: Int,
    val text: String
)

data class EditUsage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)