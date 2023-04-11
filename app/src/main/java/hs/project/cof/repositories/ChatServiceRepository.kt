package hs.project.cof.repositories

import hs.project.cof.BuildConfig
import hs.project.cof.data.remote.api.ChatGPTInterface
import hs.project.cof.data.remote.model.ChatRequest
import hs.project.cof.data.remote.model.CompletionRequest
import hs.project.cof.data.remote.model.EditRequest

class ChatServiceRepository(private val apiService: ChatGPTInterface) {
    suspend fun getChatVerMessage(request: ChatRequest): String {
        return apiService.getChatMessage(BuildConfig.API_KEY, request).choices[0].message.content
    }

    suspend fun getEditVerMessage(request: EditRequest): String {
        return apiService.getEditMessage(BuildConfig.API_KEY, request).choices[0].text.replace("\n", "")
    }

    suspend fun getCompletionVerMessage(request: CompletionRequest): String {
        return apiService.getCompletionMessage(BuildConfig.API_KEY, request).choices[0].text.replace("\n", "")
    }
}