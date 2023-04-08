package hs.project.cof.data.db

import androidx.room.*
import hs.project.cof.data.remote.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chatList: ChatList)

    @Query("UPDATE chatList SET modify_date = :modDate, chatting_list = :chatList WHERE id = :id")
    suspend fun update(id: Int, modDate: Long, chatList: List<Message>)

    @Query("DELETE FROM chatList WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM chatList WHERE id = :id")
    fun getChatList(id: Int): Flow<ChatList>

    @Query("SELECT * FROM chatList ORDER BY modify_date DESC, id ASC")
    fun getChatLists(): Flow<List<ChatList>>
}
