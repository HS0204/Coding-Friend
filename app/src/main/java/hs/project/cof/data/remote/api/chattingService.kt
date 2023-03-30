package hs.project.cof.data.remote.api

import hs.project.cof.base.ApplicationClass.Companion.retrofit
import hs.project.cof.data.remote.model.*
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

object ChatGPTAPI {
    val retrofitService : ChatGPTInterface by lazy {
        retrofit.create(ChatGPTInterface::class.java)
    }
}

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