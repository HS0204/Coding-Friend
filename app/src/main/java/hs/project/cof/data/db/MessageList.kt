package hs.project.cof.data.db

import androidx.room.*
import com.google.gson.Gson
import hs.project.cof.data.remote.model.Message
import java.util.Date

@Entity(tableName = "messageList")
data class MessageList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "reg_date")
    val regDate: Long,
    @ColumnInfo(name = "modify_date")
    val modDate: Long,
    val version: String,
    @ColumnInfo(name = "message_list")
    val messageList: List<Message>?
)

class MessageListConverters {
    @TypeConverter
    fun listToJson(value: List<Message>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Message>::class.java).toList()
}
