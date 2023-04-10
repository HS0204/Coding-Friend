package hs.project.cof.presentation.viewModel

import androidx.lifecycle.*
import hs.project.cof.base.ApplicationClass.Companion.getViewName
import hs.project.cof.data.db.ChatList
import hs.project.cof.data.db.ChatListDao
import hs.project.cof.data.remote.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class ChatListViewModel(private val msgListDao: ChatListDao) : ViewModel() {

    private val currentDate = Date()
    private var currentTimeMillis = currentDate.time

    fun isEntryValid(messageList: List<Message>): Boolean {
        if (messageList.isEmpty()) {
            return false
        }
        return true
    }

    /**
     * CREATE
     */
    private fun insertChatList(chatList: ChatList) {
        viewModelScope.launch {
            msgListDao.insert(chatList)
        }
    }

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

    fun addNewChatList(messageList: List<Message>) {
        val newChatList = getNewChatList(messageList)
        insertChatList(newChatList)
    }


    /**
     * READ
     */
    fun getAllChatList(): Flow<List<ChatList>> = msgListDao.getChatLists()

    fun retrieveChatList(id: Int): LiveData<ChatList> {
        return msgListDao.getChatList(id).asLiveData()
    }

    /**
     * UPDATE
     */
    fun updateChatList(id: Int, chatList: List<Message>) {
        currentTimeMillis = currentDate.time
        viewModelScope.launch {
            msgListDao.update(id, currentTimeMillis, chatList)
        }
    }

    /**
     * DELETE
     */
    fun deleteChatList(id: Int) {
        viewModelScope.launch {
            msgListDao.delete(id)
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