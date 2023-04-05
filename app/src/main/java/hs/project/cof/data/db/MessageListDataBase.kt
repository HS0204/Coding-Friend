package hs.project.cof.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MessageList::class], version = 1, exportSchema = false)
@TypeConverters(MessageListConverters::class)
abstract class MessageListDataBase : RoomDatabase() {

    abstract fun MessageListDao(): MessageListDao

    companion object {
        @Volatile
        private var INSTANCE: MessageListDataBase? = null

        fun getDatabase(context: Context): MessageListDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageListDataBase::class.java,
                    "message_list_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}