package com.dicoding.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.database.DatabaseUser
import com.dicoding.githubuser.database.UserFavorite
import com.dicoding.githubuser.database.UserFavoriteDao

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var userFavoriteDao: UserFavoriteDao?
    private var dbUser: DatabaseUser?

    init {
        dbUser = DatabaseUser.getDatabase(application)
        userFavoriteDao = dbUser?.userFavoriteDao()
    }

    fun getUser(): LiveData<List<UserFavorite>>?{
        return userFavoriteDao?.getFavoriteUser()
    }
}