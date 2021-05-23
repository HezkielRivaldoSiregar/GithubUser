package com.dicoding.githubuser.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.User
import com.dicoding.githubuser.database.UserFavorite
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.detail.DetailActivity
import com.dicoding.githubuser.main.MainActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        actionBar?.setTitle("Favorited User")
        setTitle("Favorited User")

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()
        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.adapter = adapter
        }
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        favoriteViewModel.getUser()?.observe(this, {
            val listUser: ArrayList<User>
            if (it.isNotEmpty()) {
                listUser = mappedList(it)
                adapter.setUser(listUser)
                showLoading(false)
                favoriteNotFound(false)
            } else {
                listUser = mappedList(it)
                adapter.setUser(listUser)
                showLoading(false)
                favoriteNotFound(true)
            }
        })
    }

    private fun mappedList(users: List<UserFavorite>): ArrayList<User> {
        val listUser = ArrayList<User>()
        for (favUser in users) {
            val user = User()
            user.id = favUser.id
            user.avatar = favUser.avatar
            user.username = favUser.username
            user.githubUrl = favUser.githubUrl

            listUser.add(user)
        }
        return listUser
    }

    private fun showSelectedUser(data: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.apply {
            putExtra(DetailActivity.EXTRA_ID, data.id)
            putExtra(DetailActivity.EXTRA_USERNAME, data.username)
            putExtra(DetailActivity.EXTRA_AVATAR, data.avatar)
            putExtra(DetailActivity.EXTRA_URL, data.githubUrl)
        }
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun favoriteNotFound(state: Boolean) {
        if (state) {
            binding.notFound.visibility = View.VISIBLE
            binding.notFoundText.visibility = View.VISIBLE
        } else {
            binding.notFound.visibility = View.GONE
            binding.notFoundText.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this@FavoriteActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}