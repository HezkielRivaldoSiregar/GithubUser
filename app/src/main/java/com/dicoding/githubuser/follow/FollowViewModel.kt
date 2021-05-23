package com.dicoding.githubuser.follow

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

class FollowViewModel:ViewModel() {

    private val listFollow =  MutableLiveData<ArrayList<User>>()

    fun setFollowers(context: Context?, username: String?) {
        val client = AsyncHttpClient()
        val url =  " https://api.github.com/users/$username/followers"
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
                    listFollow.postValue(DataUser)
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

    fun setFollowing(context: Context?, username: String?) {
        val client = AsyncHttpClient()
        val url =  "https://api.github.com/users/$username/following"
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
                        val user = User()
                        user.username = name
                        user.avatar = image
                        user.githubUrl = gitURL
                        DataUser.add(user)
                    }
                    listFollow.postValue(DataUser)
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

    fun getUser(): LiveData<ArrayList<User>> {
        return listFollow
    }
}