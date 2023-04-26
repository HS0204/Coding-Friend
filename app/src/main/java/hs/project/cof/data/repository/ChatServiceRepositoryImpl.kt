package hs.project.cof.data.repository

import hs.project.cof.BuildConfig
import hs.project.cof.data.api.ChatGPTInterface
import hs.project.cof.data.model.ChatRequest
import hs.project.cof.data.model.CompletionRequest
import hs.project.cof.data.model.EditRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatServiceRepositoryImpl @Inject constructor(private val apiService: ChatGPTInterface): ChatServiceRepository {
    override suspend fun getChatVerMessage(request: ChatRequest): String {
        return apiService.getChatMessage(BuildConfig.API_KEY, request).choices[0].message.content
    }

    override suspend fun getEditVerMessage(request: EditRequest): String {
        return apiService.getEditMessage(BuildConfig.API_KEY, request).choices[0].text.replace("\n", "")
    }

    override suspend fun getCompletionVerMessage(request: CompletionRequest): String {
        return apiService.getCompletionMessage(BuildConfig.API_KEY, request).choices[0].text.replace("\n", "")
    }
}