package com.dicoding.githubuser.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.R
import com.dicoding.githubuser.User
import com.dicoding.githubuser.alarm.AlarmActivity
import com.dicoding.githubuser.detail.DetailActivity
import com.dicoding.githubuser.favorite.FavoriteActivity

class MainActivity : AppCompatActivity() {
    private lateinit var rvUser: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListUserAdapter
    private lateinit var progressBar: ProgressBar

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUser = findViewById(R.id.rv_user)
        progressBar = findViewById(R.id.progressBar)
        adapter = ListUserAdapter()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        mainViewModel.getUser().observe(this, { SearchUser ->
            if (SearchUser.size > 0) {
                adapter.setUser(SearchUser)
                showLoading(false)
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    R.string.userNotFound,
                    Toast.LENGTH_SHORT
                )
                toast.show()
                showLoading(false)
            }
        })
        mainViewModel.getListUser(this)
        showList()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showList() {
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.apply {
            putExtra(EXTRA_ID, user.id)
            putExtra(EXTRA_USERNAME, user.username)
            putExtra(EXTRA_AVATAR, user.avatar)
            putExtra(EXTRA_URL, user.githubUrl)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchListUser(this@MainActivity, query)
                showLoading(true)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        } else if (item.itemId == R.id.favoriteUserMenu) {
            val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(mIntent)
        } else if (item.itemId == R.id.setAlarm) {
            val mIntent = Intent(this@MainActivity, AlarmActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}

