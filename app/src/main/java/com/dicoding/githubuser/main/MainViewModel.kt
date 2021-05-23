package com.dicoding.githubuser.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel: ViewModel() {

    private val listUser =  MutableLiveData<ArrayList<User>>()

    fun searchListUser(context : Context, username : String) {
        val client = AsyncHttpClient()
        val url =  " https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization","token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val listSearchUser = ArrayList<User>()
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val jsonArray = jsonObject.getJSONArray("items")
                    for (i in 0 until jsonArray.length()) {
                        val jsonUser = jsonArray.getJSONObject(i)
                        val image = jsonUser.getString("avatar_url")
                        val name = jsonUser.getString("login")
                        val gitURL = jsonUser.getString("html_url")
                        val id = jsonUser.getInt("id")
                        val user = User()
                        user.username = name
                        user.avatar = image
                        user.githubUrl = gitURL
                        user.id = id
                        listSearchUser.add(user)
                        Log.d("tes2", user.id.toString())
                    }
                   listUser.postValue(listSearchUser)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getListUser(context : Context) {
        val client = AsyncHttpClient()
        val url =  "https://api.github.com/users"
        client.addHeader("Authorization","token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val DataUser = ArrayList<User>()
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonUser = jsonArray.getJSONObject(i)
                        val image = jsonUser.getString("avatar_url")
                        val name = jsonUser.getString("login")
                        val gitURL = jsonUser.getString("html_url")
                        val id = jsonUser.getInt("id")
                        val user = User()
                        user.username = name
                        user.avatar = image
                        user.githubUrl = gitURL
                        user.id = id
                        DataUser.add(user)
                    }
                    listUser.postValue(DataUser)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>>{
        return listUser
    }

}