package com.dicoding.consumerapp

import android.database.Cursor

object MapHelper {
    fun toArrayList(cursor: Cursor?): ArrayList<User> {
        val list = ArrayList<User>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.ID))
                val username =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                val githubUrl =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.GITHUBURL))

                list.add(User(username, avatar, githubUrl, id))
            }
        }
        return list
    }
}