package hs.project.cof.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ChatList::class], version = 1, exportSchema = false)
@TypeConverters(ChatListConverters::class)
abstract class ChatListDataBase : RoomDatabase() {

    abstract fun ChatListDao(): ChatListDao

    companion object {
        @Volatile
        private var INSTANCE: ChatListDataBase? = null

        fun getDatabase(context: Context): ChatListDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatListDataBase::class.java,
                    "chat_list_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}