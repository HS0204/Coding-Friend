package hs.project.cof.data.db

import android.os.Parcelable
import androidx.room.*
import com.google.gson.Gson
import hs.project.cof.data.remote.model.Message
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "chatList")
data class ChatList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "reg_date")
    val regDate: Long,
    @ColumnInfo(name = "modify_date")
    val modDate: Long,
    val title: String,
    val version: String,
    @ColumnInfo(name = "chatting_list")
    val chatList: List<Message>?
) : Parcelable

class ChatListConverters {
    @TypeConverter
    fun listToJson(value: List<Message>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Message>::class.java).toList()
}
