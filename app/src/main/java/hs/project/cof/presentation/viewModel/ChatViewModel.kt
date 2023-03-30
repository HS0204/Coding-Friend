package hs.project.cof.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hs.project.cof.BuildConfig
import hs.project.cof.base.ApplicationClass.Companion.CHAT
import hs.project.cof.base.ApplicationClass.Companion.COMPLETION
import hs.project.cof.base.ApplicationClass.Companion.EDIT
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_BOT
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_LINE
import hs.project.cof.data.remote.api.ChatGPTAPI
import hs.project.cof.data.remote.model.*
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    enum class MessageApiStatus { NONESTARTED, LOADING, ERROR, DONE }

    private val _apiStatus = MutableLiveData<MessageApiStatus>()
    val apiStatus: LiveData<MessageApiStatus> = _apiStatus

    private var _model = String()

    private var _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> = _messageList

    init {
        clearMessageList()
        _model = CHAT
        _apiStatus.value = MessageApiStatus.NONESTARTED
    }

    /**
     * ChatGPT API
     */
    fun setModel(model: String, version: String) {
        _model = model
        addMessage(Message("[ $version ] 버전으로 동작합니다.", SEND_BY_LINE))
    }

    fun addMessage(msg: Message) {
        _messageList.value = _messageList.value?.toMutableList()?.apply { add(msg) }
    }

    private fun clearMessageList() {
        _messageList.value = ArrayList<Message>()
    }

    fun getMessage(msg: String) {
        viewModelScope.launch {
            _apiStatus.value = MessageApiStatus.LOADING
            try {
                when(_model){
                    CHAT -> {
                        val chat = ChatRequest(model = _model, messages = listOf(ChatRequestMessage(content = msg, role = "user")))
                        addMessage(Message(ChatGPTAPI.retrofitService.getChatMessage(BuildConfig.API_KEY, chat).choices[0].message.content, SEND_BY_BOT))
                    }
                    EDIT -> {
                        val edit = EditRequest(model = _model, input = msg, instruction = "Fix the spelling and grammar mistakes")
                        addMessage(Message(ChatGPTAPI.retrofitService.getEditMessage(BuildConfig.API_KEY, edit).choices[0].text.replace("\n", ""), SEND_BY_BOT))
                    }
                    COMPLETION -> {
                        val completion = CompletionRequest(model = _model, prompt = msg, max_tokens = 1000)
                        addMessage(Message(ChatGPTAPI.retrofitService.getCompletionMessage(BuildConfig.API_KEY, completion).choices[0].text.replace("\n", ""), SEND_BY_BOT))
                    }
                    else -> {
                        addMessage(Message("버전에 오류가 있습니다. 설정에서 선택해주세요.", SEND_BY_BOT))
                     }
                }

                _apiStatus.value = MessageApiStatus.DONE
            } catch (e: Exception) {
                addMessage(Message("다음 사유로 응답에 실패했습니다.\n$e", SEND_BY_BOT))
                _apiStatus.value = MessageApiStatus.ERROR
            }
        }
    }

}