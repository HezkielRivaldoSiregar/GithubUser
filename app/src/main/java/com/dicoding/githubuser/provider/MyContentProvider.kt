package com.dicoding.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.githubuser.database.DatabaseUser
import com.dicoding.githubuser.database.UserFavoriteDao

class MyContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.dicoding.githubuser"
        const val TABLE_NAME = "user_favorite"
        const val USER_FAVORITE_ID = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_FAVORITE_ID)
        }
    }

    private lateinit var favoriteDao: UserFavoriteDao

    override fun onCreate(): Boolean {
        favoriteDao = context?.let {
            DatabaseUser.getDatabase(it)?.userFavoriteDao()
        }!!
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var cursor: Cursor?
        when (uriMatcher.match(uri)) {
            USER_FAVORITE_ID -> {
                cursor = favoriteDao.returnAll()
                if (context != null) {
                    cursor.setNotificationUri(context?.contentResolver, uri)
                }
            }
            else -> {
                cursor = null
            }
        }
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0
}