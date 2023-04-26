package hs.project.cof.data.api

import hs.project.cof.data.model.ChatRequest
import hs.project.cof.data.model.ChatResponse
import hs.project.cof.data.model.CompletionRequest
import hs.project.cof.data.model.CompletionResponse
import hs.project.cof.data.model.EditRequest
import hs.project.cof.data.model.EditResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ChatGPTInterface {
    @POST("/v1/chat/completions")
    suspend fun getChatMessage(
        @Header("Authorization") apiKey : String,
        @Body chatRequestMsg : ChatRequest
    ): ChatResponse

    @POST("/v1/edits")
    suspend fun getEditMessage(
        @Header("Authorization") apiKey : String,
        @Body editRequestMsg : EditRequest
    ): EditResponse

    @POST("/v1/completions")
    suspend fun getCompletionMessage(
        @Header("Authorization") apiKey : String,
        @Body completionRequestMsg : CompletionRequest
    ): CompletionResponse
}