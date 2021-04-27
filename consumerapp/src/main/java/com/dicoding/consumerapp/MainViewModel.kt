package com.dicoding.consumerapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var list = MutableLiveData<ArrayList<User>>()

    fun setFavorite(context: Context) {
        val cursor = context.contentResolver.query(
            DatabaseContract.UserColumns.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val convert = MapHelper.toArrayList(cursor)
        list.postValue(convert)
    }

    fun getUser(): LiveData<ArrayList<User>>? {
        return list
    }
}