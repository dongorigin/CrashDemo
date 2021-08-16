package cn.dong.crash

import androidx.room.Room

object DatabaseManager {

    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            App.application,
            AppDatabase::class.java, "app"
        ).build()
    }
}
