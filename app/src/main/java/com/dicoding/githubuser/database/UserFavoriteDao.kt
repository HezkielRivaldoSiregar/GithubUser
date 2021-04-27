package com.dicoding.githubuser.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserFavoriteDao {
    @Insert
    suspend fun addToFavorite(favorite: UserFavorite)

    @Query("SELECT * FROM user_favorite")
    fun getFavoriteUser(): LiveData<List<UserFavorite>>

    @Query("SELECT count(*) FROM user_favorite WHERE user_favorite.id = :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM user_favorite WHERE user_favorite.id = :id")
    suspend fun removeUserFromFavorite(id: Int): Int

    @Query("SELECT * FROM user_favorite")
    fun returnAll(): Cursor

}