package hs.project.cof.data.remote.api

import hs.project.cof.base.ApplicationClass.Companion.retrofit
import hs.project.cof.data.remote.model.Chat
import hs.project.cof.data.remote.model.ChatResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatAPIInterface {
    @POST("/v1/chat/completions")
    suspend fun getMessage(
        @Header("Authorization") api : String, // API-key
        @Body chatMsg : Chat,
    ): ChatResponse
}
object ChattingApi {
    val retrofitService : ChatAPIInterface by lazy {
        retrofit.create(ChatAPIInterface::class.java)
    }
}