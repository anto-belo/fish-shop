package com.ab.lab

import android.app.Application
import com.ab.lab.data.FavoritesRepository
import com.ab.lab.db.CatfoodRoomDatabase

class App : Application() {

    val database by lazy { CatfoodRoomDatabase.getDatabase(this) }
    val favoritesRepository by lazy { FavoritesRepository(database.favoritesDao()) }

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}