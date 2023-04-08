package hs.project.cof.data.db

import android.content.Context
import androidx.room.*

@Database(
    entities = [ChatList::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3)]
)
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