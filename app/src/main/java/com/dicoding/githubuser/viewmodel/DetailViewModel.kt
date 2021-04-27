package com.dicoding.githubuser.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubuser.database.DatabaseUser
import com.dicoding.githubuser.User
import com.dicoding.githubuser.database.UserFavorite
import com.dicoding.githubuser.database.UserFavoriteDao
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val listUser = MutableLiveData<User>()

    private var userFavoriteDao : UserFavoriteDao?
    private var dbUser: DatabaseUser?

    init{
        dbUser = DatabaseUser.getDatabase(application)
        userFavoriteDao = dbUser?.userFavoriteDao()
    }


    fun DataListUser(context: Context, username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val image = responseObject.getString("avatar_url")
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val repository = responseObject.getString("public_repos")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")
                    val id = responseObject.getInt("id")
                    val user = User()
                    user.avatar = image
                    user.name = name
                    user.location = location
                    user.company = company
                    user.repository = repository
                    user.followers = followers
                    user.following = following
                    user.id = id
                    listUser.postValue(user)
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

    fun getUser(): LiveData<User> {
        return listUser
    }

    fun addFavoriteUser(username: String, id:Int, avatar: String, githubUrl:String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserFavorite(
                username,
                id,
                avatar,
                githubUrl
            )
            userFavoriteDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userFavoriteDao?.checkUser(id)

    fun removeFavoriteUser(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userFavoriteDao?.removeUserFromFavorite(id)
        }
    }

}