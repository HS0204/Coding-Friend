package hs.project.cof.data.remote.model

data class CompletionRequest(
    val model: String,
    val prompt: String,
    val temperature: Float
)