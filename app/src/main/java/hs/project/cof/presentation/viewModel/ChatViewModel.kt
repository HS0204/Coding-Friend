package hs.project.cof.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hs.project.cof.BuildConfig
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_BOT
import hs.project.cof.data.remote.api.ChattingApi
import hs.project.cof.data.remote.model.Chat
import hs.project.cof.data.remote.model.Message
import hs.project.cof.data.remote.model.RequestMessage
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    enum class MessageApiStatus { NONESTARTED, LOADING, ERROR, DONE }

    private val _apiStatus = MutableLiveData<MessageApiStatus>()
    val apiStatus: LiveData<MessageApiStatus> = _apiStatus

    private var _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> = _messageList

    init {
        clearMessageList()
        _apiStatus.value = MessageApiStatus.NONESTARTED
    }

    fun addMessage(msg: Message) {
        _messageList.value = _messageList.value?.toMutableList()?.apply { add(msg) }
    }

    private fun removeLastMessage() {
        _messageList.value = _messageList.value?.toMutableList()?.apply { removeLastOrNull() }
    }

    fun clearMessageList() {
        _messageList.value = ArrayList<Message>()
    }

    fun getMessage(msg: String) {
        viewModelScope.launch {
            addMessage(Message("입력 중...", SEND_BY_BOT))
            _apiStatus.value = MessageApiStatus.LOADING
            try {
                removeLastMessage()
                val chat = Chat(model = "gpt-3.5-turbo", messages = listOf(RequestMessage(content = msg, role = "user")))
                addMessage(Message(ChattingApi.retrofitService.getMessage(BuildConfig.API_KEY, chat).choices[0].message.content, SEND_BY_BOT))
                _apiStatus.value = MessageApiStatus.DONE
            } catch (e: Exception) {
                addMessage(Message("다음 사유로 응답에 실패했습니다.\n$e", SEND_BY_BOT))
                _apiStatus.value = MessageApiStatus.ERROR
            }
        }
    }

}