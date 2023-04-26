package hs.project.cof.data.repository

import hs.project.cof.data.model.ChatRequest
import hs.project.cof.data.model.CompletionRequest
import hs.project.cof.data.model.EditRequest

interface ChatServiceRepository {

    suspend fun getChatVerMessage(request: ChatRequest): String

    suspend fun getEditVerMessage(request: EditRequest): String

    suspend fun getCompletionVerMessage(request: CompletionRequest): String
}