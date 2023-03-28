package hs.project.cof.data.remote.api

import hs.project.cof.data.remote.model.Chat
import hs.project.cof.data.remote.model.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface chatRetrofitInterface {
    // 채팅
    @POST("/v1/chat/completions")
    fun requestMessage(
        @Header("Authorization") api : String, // API-key
        @Body chatMsg : Chat,
    ): Call<ChatResponse>
}