package hs.project.cof.data.model

data class CompletionRequest(
    val model: String,
    val prompt: String,
    val temperature: Float
)