package com.dicoding.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.*
import com.dicoding.githubuser.activity.DetailActivity
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.viewmodel.FollowViewModel

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private lateinit var followingViewModel: FollowViewModel
    private lateinit var adapter: ListUserAdapter
    private var binding: FragmentFollowBinding? = null
    private val _binding get() = binding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getString(EXTRA_USERNAME)

        binding = FragmentFollowBinding.bind(view)
        adapter = ListUserAdapter()

        _binding?.rvUser?.layoutManager = LinearLayoutManager(activity)
        _binding?.rvUser?.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, data.username)
                intent.putExtra(EXTRA_ID, data.id)
                intent.putExtra(EXTRA_AVATAR, data.avatar)
                intent.putExtra(EXTRA_URL, data.githubUrl)
                startActivity(intent)
            }
        })

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowViewModel::class.java
        )
        followingViewModel.getUser().observe(viewLifecycleOwner, { userFollower ->
            if (userFollower != null) {
                adapter.setUser(userFollower)
                showLoading(false)
            }
        })
        followingViewModel.setFollowing(context, user)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            _binding?.progressBar?.visibility = View.VISIBLE
        } else {
            _binding?.progressBar?.visibility = View.GONE
        }
    }
}