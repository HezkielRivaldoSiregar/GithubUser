package com.dicoding.githubuser.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_favorite")
class UserFavorite(
    val username: String,
    @PrimaryKey
    val id: Int,
    val avatar: String,
    val githubUrl: String
) : Serializable