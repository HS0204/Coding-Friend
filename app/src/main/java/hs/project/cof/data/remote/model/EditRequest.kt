package hs.project.cof.data.remote.model

data class EditRequest(
    val input: String,
    val instruction: String,
    val model: String,
    val temperature: Float
)