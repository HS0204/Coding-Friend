package hs.project.cof.presentation.viewModel

import androidx.lifecycle.*
import hs.project.cof.BuildConfig
import hs.project.cof.base.ApplicationClass.Companion.CHAT
import hs.project.cof.base.ApplicationClass.Companion.COMPLETION
import hs.project.cof.base.ApplicationClass.Companion.EDIT
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_BOT
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_LINE
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_TYPING
import hs.project.cof.data.db.MessageList
import hs.project.cof.data.db.MessageListDao
import hs.project.cof.data.remote.api.ChatGPTAPI
import hs.project.cof.data.remote.model.*
import kotlinx.coroutines.launch

class ChatViewModel(private val msgListDao: MessageListDao) : ViewModel() {

    enum class MessageApiStatus { NONESTARTED, LOADING, ERROR, DONE }

    private val _apiStatus = MutableLiveData<MessageApiStatus>()
    val apiStatus: LiveData<MessageApiStatus> = _apiStatus

    // model setting
    private var _model = String()
    private var _version = MutableLiveData<String>()
    val version: LiveData<String> = _version
    private val _temperature = MutableLiveData<Int>()
    val temperature: LiveData<Int> = _temperature

    private var _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> = _messageList

    // Database
    val allMessageList: LiveData<List<MessageList>> = msgListDao.getMsgLists().asLiveData()    // flow를 반환하기 때문에 liveData로 사용하기 위해 asLiveData()

    init {
        clearMessageList()
        _model = CHAT
        _version.value = "채팅"
        _temperature.value = 10
        _apiStatus.value = MessageApiStatus.NONESTARTED
    }

    /**
     * ChatGPT API
     */
    fun setModel(model: String, version: String) {
        _model = model
        _version.value = version
        addMessage(Message("[ $version ] 버전으로 동작합니다.", SEND_BY_LINE))
    }

    fun setTemperature(temp: Int) {
        _temperature.value = temp
    }

    fun addMessage(msg: Message) {
        _messageList.value = _messageList.value?.toMutableList()?.apply { add(msg) }
    }

    fun addTypingMessage() {
        addMessage(Message("              ", SEND_BY_TYPING))
    }

    private fun removeLastMessage() {
        _messageList.value = _messageList.value?.toMutableList()?.apply { removeLastOrNull() }
    }

    fun clearMessageList() {
        _apiStatus.value = MessageApiStatus.NONESTARTED
        _messageList.value = ArrayList<Message>()
    }

    fun getMessageFromChatGPT(msg: String) {
        viewModelScope.launch {
            _apiStatus.value = MessageApiStatus.LOADING
            try {
                val response: Message
                val temperature = _temperature.value!!.toFloat()/10f

                when(_model){
                    CHAT -> {
                        val chat = ChatRequest(model = CHAT, messages = listOf(ChatRequestMessage(content = msg, role = "user")), temperature = temperature)
                        response = Message(ChatGPTAPI.retrofitService.getChatMessage(BuildConfig.API_KEY, chat).choices[0].message.content, SEND_BY_BOT)
                    }
                    EDIT -> {
                        val edit = EditRequest(model = EDIT, input = msg, instruction = "Fix the spelling and grammar mistakes", temperature = temperature)
                        response = Message(ChatGPTAPI.retrofitService.getEditMessage(BuildConfig.API_KEY, edit).choices[0].text.replace("\n", ""), SEND_BY_BOT)
                    }
                    COMPLETION -> {
                        val completion = CompletionRequest(model = COMPLETION, prompt = msg, temperature = temperature)
                        response = Message(ChatGPTAPI.retrofitService.getCompletionMessage(BuildConfig.API_KEY, completion).choices[0].text.replace("\n", ""), SEND_BY_BOT)
                    }
                    else -> {
                        response = Message("버전에 오류가 있습니다. 설정에서 선택해주세요.", SEND_BY_BOT)
                     }
                }

                if (_messageList.value?.size != 0) {
                    removeLastMessage()
                    addMessage(response)
                }
                _apiStatus.value = MessageApiStatus.DONE
            } catch (e: Exception) {
                removeLastMessage()
                addMessage(Message("다음 사유로 응답에 실패했습니다.\n\n$e", SEND_BY_BOT))
                _apiStatus.value = MessageApiStatus.ERROR
            }
        }
    }

    /**
     * Database
     */
    fun insertMessageList(msgList: MessageList) {
        viewModelScope.launch {
            msgListDao.insert(msgList)
        }
    }

    fun retrieveMessageList(id: Int): LiveData<MessageList> {
        return msgListDao.getMsgList(id).asLiveData()
    }

}

class ChatViewModelFactory(private val msgListDao: MessageListDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(msgListDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}