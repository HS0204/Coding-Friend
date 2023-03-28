package hs.project.cof.data.remote.api

import hs.project.cof.base.ApplicationClass.Companion.retrofit
import hs.project.cof.data.remote.model.Chat
import hs.project.cof.data.remote.model.ChatResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class chattingService(val chatView: chatView) {
    fun requestMessage(api : String, chatMsg: Chat){
        val chatService = retrofit.create(chatRetrofitInterface::class.java)

        chatService.requestMessage(api, chatMsg).enqueue(object : Callback<ChatResponse> {

            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                val result = response.body()!!
                if (result.id != null) {
                    chatView.onGetChatSuccess(result.choices[0].message.content)
                } else {
                    chatView.onGetChatFailure(result.toString())
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                chatView.onGetChatSuccess("응답에 실패했습니다.")
            }

        })
    }
}