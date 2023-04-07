package hs.project.cof.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chatList: ChatList)

    @Update
    suspend fun update(chatList: ChatList)

    @Query("DELETE FROM chatList WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM chatList WHERE id = :id")
    fun getChatList(id: Int): Flow<ChatList>

    @Query("SELECT * FROM chatList ORDER BY modify_date DESC, id ASC")
    fun getChatLists(): Flow<List<ChatList>>
}
