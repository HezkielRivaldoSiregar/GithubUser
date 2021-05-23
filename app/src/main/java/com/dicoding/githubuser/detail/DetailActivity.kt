package com.dicoding.githubuser.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.main.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        val url = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        detailViewModel.DataListUser(this, username)
        setTitle("${username}")

        detailViewModel.getUser().observe(this, { dataUser ->
            if (dataUser != null) {
                binding.tvName.text = if (dataUser.name == "null") "-" else dataUser.name
                binding.tvRepository.text = dataUser.repository
                binding.tvFollowings.text = dataUser.following
                binding.tvFollowers.text = dataUser.followers
                binding.tvLocation.text =
                    if (dataUser.location == "null") "-" else dataUser.location
                binding.tvCompany.text = if (dataUser.company == "null") "-" else dataUser.company
                Glide.with(applicationContext)
                    .load(dataUser.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.userImg)
            }
        })

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        isChecked = true
                        binding.floatingFavorite.setImageResource(R.drawable.ic_favorite_true)
                    } else {
                        isChecked = false
                        binding.floatingFavorite.setImageResource(R.drawable.ic_favorite_false)
                    }
                }
            }
        }

        binding.floatingFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                binding.floatingFavorite.setImageResource(R.drawable.ic_favorite_true)
                detailViewModel.addFavoriteUser(
                    username.toString(),
                    id,
                    avatar.toString(),
                    url.toString()
                )
            } else {
                binding.floatingFavorite.setImageResource(R.drawable.ic_favorite_false)
                detailViewModel.removeFavoriteUser(id)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val url = intent.getStringExtra(EXTRA_URL)
        if (item.itemId == R.id.share) {
            val share = Intent()
            share.action = Intent.ACTION_SEND
            share.type = "text/plain"
            share.putExtra(
                Intent.EXTRA_TEXT,
                "Hey I suggest you to go and check \"${username}\" profile. Here's The link to the Github: \"${url}\" "
            )
            startActivity(Intent.createChooser(share, "Share this user to"))
            val toast = Toast.makeText(
                applicationContext,
                "You are Sharing \"${username}\" Profile",
                Toast.LENGTH_SHORT
            )
            toast.show()
        } else if (item.itemId == android.R.id.home) {
            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


}