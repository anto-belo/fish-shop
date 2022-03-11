package com.ab.lab.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ab.lab.db.dao.FavoritesDao
import com.ab.lab.db.entity.Favorites


@Database(entities = [Favorites::class], version = 1, exportSchema = false)
abstract class CatfoodRoomDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {

        @Volatile
        private var INSTANCE: CatfoodRoomDatabase? = null

        fun getDatabase(context: Context): CatfoodRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        CatfoodRoomDatabase::class.java,
                        "catfood_database"
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
