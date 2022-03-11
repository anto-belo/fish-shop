package com.ab.lab.db.dao

import androidx.room.*
import com.ab.lab.db.entity.Favorites

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorites: Favorites)

    @Delete
    suspend fun delete(favorites: Favorites)

    @Query("SELECT * FROM favorites WHERE user_id = :userId")
    suspend fun getAllFavorites(userId: String): List<Favorites>

    @Query("SELECT * FROM favorites WHERE user_id = :userId AND product_key = :productKey")
    suspend fun existFavorites(userId: String, productKey: String): List<Favorites>
}
