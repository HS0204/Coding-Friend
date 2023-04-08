package hs.project.cof.presentation.viewModel

import androidx.lifecycle.*
import hs.project.cof.BuildConfig
import hs.project.cof.base.ApplicationClass.Companion.getChatModel
import hs.project.cof.base.ApplicationClass.Companion.getChatVerNm
import hs.project.cof.base.ApplicationClass.Companion.ChatVersion
import hs.project.cof.base.ApplicationClass.Companion.SendBy
import hs.project.cof.base.ApplicationClass.Companion.getViewType
import hs.project.cof.data.remote.api.ChatGPTAPI
import hs.project.cof.data.remote.model.*
import kotlinx.coroutines.launch

class ChatViewModel() : ViewModel() {

    enum class MessageApiStatus { NONESTARTED, LOADING, ERROR, DONE }
    enum class ViewModeStatus {CHAT, LOG}

    private val _apiStatus = MutableLiveData<MessageApiStatus>()
    val apiStatus: LiveData<MessageApiStatus> = _apiStatus
    private val _viewModeStatus = MutableLiveData<ViewModeStatus>()
    val viewModeStatus: LiveData<ViewModeStatus> = _viewModeStatus

    // model setting
    private var _model = String()
    private var _version = MutableLiveData<String>()
    val version: LiveData<String> = _version
    private val _temperature = MutableLiveData<Int>()
    val temperature: LiveData<Int> = _temperature

    private var _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> = _messageList

    init {
        clearMessageList()
        _model = getChatModel(ChatVersion.CHAT)
        _version.value = getChatVerNm(ChatVersion.CHAT)
        _temperature.value = 10
        _apiStatus.value = MessageApiStatus.NONESTARTED
        _viewModeStatus.value = ViewModeStatus.CHAT
    }

    /**
     * ChatGPT API
     */
    fun setModel(model: String, version: String) {
        _model = model
        _version.value = version
        addMessage(Message("[ $version ] 버전으로 동작합니다.", getViewType(SendBy.VERSION)))
    }

    fun setTemperature(temp: Int) {
        _temperature.value = temp
    }

    fun addMessage(msg: Message) {
        _messageList.value = _messageList.value?.toMutableList()?.apply { add(msg) }
    }

    fun addTypingMessage() {
        addMessage(Message("              ", getViewType(SendBy.TYPING)))
    }

    private fun removeLastMessage() {
        _messageList.value = _messageList.value?.toMutableList()?.apply { removeLastOrNull() }
    }

    fun clearMessageList() {
        _apiStatus.value = MessageApiStatus.NONESTARTED
        _viewModeStatus.value = ViewModeStatus.CHAT
        _messageList.value = ArrayList<Message>()
    }

    fun setViewModeStatus(mode: ViewModeStatus) {
        _viewModeStatus.value = mode
    }

    fun retrieveMessageListFromList(msgList: List<Message>) {
        _viewModeStatus.value = ViewModeStatus.LOG
        _messageList.value = msgList.toMutableList()
    }

    fun getMessageFromChatGPT(msg: String) {
        viewModelScope.launch {
            _apiStatus.value = MessageApiStatus.LOADING
            try {
                val response: Message
                val temperature = _temperature.value!!.toFloat()/10f

                when(_model){
                    getChatModel(ChatVersion.CHAT) -> {
                        val chat = ChatRequest(model = getChatModel(ChatVersion.CHAT), messages = listOf(ChatRequestMessage(content = msg, role = "user")), temperature = temperature)
                        response = Message(ChatGPTAPI.retrofitService.getChatMessage(BuildConfig.API_KEY, chat).choices[0].message.content, getViewType(SendBy.BOT))
                    }
                    getChatModel(ChatVersion.EDIT) -> {
                        val edit = EditRequest(model = getChatModel(ChatVersion.EDIT), input = msg, instruction = "Fix the spelling and grammar mistakes", temperature = temperature)
                        response = Message(ChatGPTAPI.retrofitService.getEditMessage(BuildConfig.API_KEY, edit).choices[0].text.replace("\n", ""), getViewType(SendBy.BOT))
                    }
                    getChatModel(ChatVersion.COMPLETION) -> {
                        val completion = CompletionRequest(model = getChatModel(ChatVersion.COMPLETION), prompt = msg, temperature = temperature)
                        response = Message(ChatGPTAPI.retrofitService.getCompletionMessage(BuildConfig.API_KEY, completion).choices[0].text.replace("\n", ""), getViewType(SendBy.BOT))
                    }
                    else -> {
                        response = Message("버전에 오류가 있습니다. 설정에서 선택해주세요.", getViewType(SendBy.BOT))
                     }
                }

                if (_messageList.value?.size != 0) {
                    removeLastMessage()
                    addMessage(response)
                }
                _apiStatus.value = MessageApiStatus.DONE
            } catch (e: Exception) {
                removeLastMessage()
                addMessage(Message("다음 사유로 응답에 실패했습니다.\n\n$e", getViewType(SendBy.BOT)))
                _apiStatus.value = MessageApiStatus.ERROR
            }
        }
    }

}

class ChatViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}