package com.example.githubuser1

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser1.databinding.FavoritActBinding
import com.example.githubuser1.remote.response.itemsSearch
import com.example.githubuser1.ui.adapter.ListUser
import com.example.githubuser1.ui.detail.DetailAct
import com.example.githubuser1.ui.detail.DetailVM
import com.example.githubuser1.ui.detail.DetailVMF

class Favorit : AppCompatActivity() {
    private lateinit var binding: FavoritActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FavoritActBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_name3)

        val factory: DetailVMF = DetailVMF.getInstance(this)
        val viewModel: DetailVM by viewModels {
            factory
        }
        viewModel.deleteAll()
        viewModel.getFavoriteUsers().observe(this) { user ->
            binding.progressbar.visibility = View.GONE
            val userList = user.map {
                itemsSearch(it.login, it.avatar)
            }
            val userAdapter = ListUser(userList as ArrayList<itemsSearch>)
            binding.rvu.adapter = userAdapter
            userAdapter.setOnItemClickCallback(object : ListUser.OnItemClickCallback {
                override fun onItemClicked(data: itemsSearch) {
                    showSelectedUser(data)
                }
            })
        }
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvu.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvu.layoutManager = LinearLayoutManager(this)
        }
        binding.rvu.setHasFixedSize(true)
    }

    private fun showSelectedUser(user: itemsSearch) {
        val detailUserIntent = Intent(this, DetailAct::class.java)
        detailUserIntent.putExtra(DetailAct.EXTRA_USER, user.login)
        startActivity(detailUserIntent)
    }
}