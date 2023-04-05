package hs.project.cof.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(msgList: MessageList)

    @Update
    suspend fun update(msgList: MessageList)

    @Delete
    suspend fun delete(msgList: MessageList)

    @Query("SELECT * from messageList WHERE id = :id")
    fun getMsgList(id: Int): Flow<MessageList>

    @Query("SELECT * from messageList ORDER BY modify_date DESC, id ASC")
    fun getMsgLists(): Flow<List<MessageList>>
}
