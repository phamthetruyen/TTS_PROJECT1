package vn.mobifone.tts_project1.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import vn.mobifone.tts_project1.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Alo alo"))
                dao.insert(Task("Sinh nhat"))
                dao.insert(Task("Mua xe", important = true))
                dao.insert(Task("Di choi", important = true))
                dao.insert(Task("Do xang", completed = true))
                dao.insert(Task("Alo 123123", completed = true))
            }
        }
    }
}