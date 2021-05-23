package com.dicoding.githubuser.follow

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.*
import com.dicoding.githubuser.detail.DetailActivity
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.FragmentFollowBinding

class FollowerFragment : Fragment(R.layout.fragment_follow) {

    private lateinit var followerViewModel: FollowViewModel
    private lateinit var adapter: ListUserAdapter
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getString(EXTRA_USERNAME)
        _binding = FragmentFollowBinding.bind(view)
        adapter = ListUserAdapter()

        binding?.rvUser?.layoutManager = LinearLayoutManager(activity)
        binding?.rvUser?.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, data.username)
                intent.putExtra(EXTRA_ID, data.id)
                intent.putExtra(EXTRA_AVATAR, data.avatar)
                intent.putExtra(EXTRA_URL, data.githubUrl)
                startActivity(intent)
            }
        })

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowViewModel::class.java
        )
        followerViewModel.getUser().observe(viewLifecycleOwner, { userFollower ->
            if (userFollower != null) {
                adapter.setUser(userFollower)
                showLoading(false)
            }
        })
        followerViewModel.setFollowers(context, user)
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }
}