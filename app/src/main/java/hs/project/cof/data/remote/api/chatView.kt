package hs.project.cof.data.remote.api

interface chatView {
    fun onGetChatSuccess(message: String)
    fun onGetChatFailure(message: String)
}