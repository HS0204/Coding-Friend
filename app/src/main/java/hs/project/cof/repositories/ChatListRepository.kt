package hs.project.cof.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import hs.project.cof.data.db.ChatList
import hs.project.cof.data.db.ChatListDao
import hs.project.cof.data.remote.model.Message
import kotlinx.coroutines.flow.Flow

class ChatListRepository(private val chatListDao: ChatListDao) {

    val getAllChatList: Flow<List<ChatList>> = chatListDao.getAllChatList()
    val getChatListCount: LiveData<Int> = chatListDao.getAllChatListCount()

    fun selectChatList(id: Int): LiveData<ChatList> {
        return chatListDao.getChatList(id).asLiveData()
    }

    suspend fun insertChatList(chatList: ChatList) {
        chatListDao.insert(chatList)
    }

    suspend fun updateChatList(id: Int, currentTimeMillis: Long, chatList: List<Message>) {
        chatListDao.update(id, currentTimeMillis, chatList)
    }

    suspend fun deleteChatList(id: Int) {
        chatListDao.delete(id)
    }
}