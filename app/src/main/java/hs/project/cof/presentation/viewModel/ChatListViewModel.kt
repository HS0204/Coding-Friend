package hs.project.cof.presentation.viewModel

import androidx.lifecycle.*
import hs.project.cof.data.db.ChatList
import hs.project.cof.data.db.ChatListDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatListViewModel(private val msgListDao: ChatListDao) : ViewModel() {

    val allMsgList: LiveData<List<ChatList>> = msgListDao.getChatLists().asLiveData()

    /**
     * Database
     */
    fun insertChatList(chatList: ChatList) {
        viewModelScope.launch {
            msgListDao.insert(chatList)
        }
    }

    fun updateChatList(chatList: ChatList) {
        viewModelScope.launch {
            msgListDao.update(chatList)
        }
    }

    fun deleteChatList(id: Int) {
        viewModelScope.launch {
            msgListDao.delete(id)
        }
    }

    fun getAllChatList() : Flow<List<ChatList>> = msgListDao.getChatLists()

    fun retrieveChatList(id: Int): LiveData<ChatList> {
        return msgListDao.getChatList(id).asLiveData()
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