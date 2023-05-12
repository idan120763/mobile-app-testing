package com.example.githubuser1.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.githubuser1.R
import com.example.githubuser1.databinding.DetailActBinding
import com.example.githubuser1.remote.Result
import com.example.githubuser1.ui.adapter.SectionPage
import com.google.android.material.tabs.TabLayoutMediator

class DetailAct : AppCompatActivity() {

    private lateinit var binding: DetailActBinding
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_name2)

        val factory: DetailVMF = DetailVMF.getInstance(this)
        val detailViewModel: DetailVM by viewModels {
            factory
        }

        username = intent.getStringExtra(EXTRA_USER) as String

        detailViewModel.getUser(username)
        val sectionsPagerAdapter = SectionPage(this)
        sectionsPagerAdapter.username = username

        binding.viewpager.adapter = sectionsPagerAdapter
        supportActionBar?.elevation = 0f

        detailViewModel.getUser(username).observe(this){ result ->
            if (result != null){
                when(result) {
                    is Result.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressbar.visibility = View.GONE
                        val user = result.data
                        binding.apply {
                            tvnm.text = user.name
                            tvu.text = user.login
                            tvcom.text = user.company
                            tvloc.text = user.location
                            tvres.text = resources.getString(R.string.publicrepos, user.publicRepos)
                            if (user.isFavorite) {
                                ivf.setImageDrawable(ContextCompat.getDrawable(ivf.context, R.drawable.baseline_favorite_24_red))
                            } else {
                                ivf.setImageDrawable(ContextCompat.getDrawable(ivf.context, R.drawable.baseline_favorite_border_24))
                            }
                            ivf.setOnClickListener {
                                if (user.isFavorite){
                                    detailViewModel.deleteFavorite(user)
                                } else {
                                    detailViewModel.saveFavorite(user)
                                }
                            }
                        }
                        Glide.with(this)
                            .load(user.avatar)
                            .circleCrop()
                            .into(binding.iavatar)
                        val countFollow = arrayOf(user.follower, user.following)
                        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
                            tab.text = resources.getString(TAB_TITLES[position], countFollow[position])
                        }.attach()
                    }
                    is Result.Error -> {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share, username))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        return true
    }
    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_2,
            R.string.tab_1
        )
    }
}