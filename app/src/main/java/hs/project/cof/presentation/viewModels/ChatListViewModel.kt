package hs.project.cof.presentation.viewModels

import androidx.lifecycle.*
import hs.project.cof.base.ApplicationClass.Companion.getViewName
import hs.project.cof.data.db.ChatList
import hs.project.cof.data.db.ChatListDao
import hs.project.cof.data.remote.model.Message
import hs.project.cof.repositories.ChatListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class ChatListViewModel(private val chatListDao: ChatListDao) : ViewModel() {

    private val currentDate = Date()
    private var currentTimeMillis = currentDate.time
    private val repository: ChatListRepository = ChatListRepository(chatListDao)

    /**
     * CREATE
     */
    private fun getNewChatList(messageList: List<Message>): ChatList {
        currentTimeMillis = currentDate.time

        return ChatList(
            regDate = currentTimeMillis,
            modDate = currentTimeMillis,
            title = messageList[0].message,
            version = getViewName(messageList[0].sendBy),
            chatList = messageList
        )
    }

    // check valid
    fun isEntryValid(messageList: List<Message>): Boolean {
        if (messageList.isEmpty()) {
            return false
        }
        return true
    }

    fun addNewChatList(messageList: List<Message>) {
        viewModelScope.launch {
            val newChatList = getNewChatList(messageList)
            repository.insertChatList(newChatList)
        }
    }

    /**
     * READ
     */
    val getAllChatList: Flow<List<ChatList>> = repository.getAllChatList
    val getChatListCount: LiveData<Int> = repository.getChatListCount

    fun getChatList(id: Int): LiveData<ChatList> {
        return repository.selectChatList(id)
    }

    /**
     * UPDATE
     */
    fun addNewMessagesToChatList(id: Int, chatList: List<Message>) {
        currentTimeMillis = currentDate.time
        viewModelScope.launch {
            repository.updateChatList(id, currentDate.time, chatList)
        }
    }

    /**
     * DELETE
     */
    fun removeChatLog(id: Int) {
        viewModelScope.launch {
            repository.deleteChatList(id)
        }
    }

}

class ChatListViewModelFactory(private val chatListDao: ChatListDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatListViewModel(chatListDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}