package com.example.githubuser1.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser1.Favorit
import com.example.githubuser1.R
import com.example.githubuser1.databinding.ActivityMainBinding
import com.example.githubuser1.remote.response.itemsSearch
import com.example.githubuser1.setting.SettingAct
import com.example.githubuser1.setting.SettingPre
import com.example.githubuser1.setting.SettingVM
import com.example.githubuser1.setting.SettingVMF
import com.example.githubuser1.ui.adapter.ListUser
import com.example.githubuser1.ui.detail.DetailAct

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvru.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvru.layoutManager = LinearLayoutManager(this)
        }

        mainViewModel.userList.observe(this) { users ->
            setUserData(users)
        }
        mainViewModel.userCount.observe(this){
            binding.tvru.text = resources.getString(R.string.tv_resultData, it)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }
        val pref = SettingPre.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, SettingVMF(pref))[SettingVM::class.java]
        mainViewModel.getThemeSettings().observe(this)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
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
                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
    private fun setUserData(users: List<itemsSearch>?) {
        val listUserAdapter = ListUser(users as ArrayList<itemsSearch>)
        binding.rvru.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUser.OnItemClickCallback {
            override fun onItemClicked(data: itemsSearch) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: itemsSearch) {
        val detailUserIntent = Intent(this, DetailAct::class.java)
        detailUserIntent.putExtra(DetailAct.EXTRA_USER, user.login)
        startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorit -> {
                val i = Intent(this, Favorit::class.java)
                startActivity(i)
                true
            }
            R.id.setting -> {
                val i = Intent(this, SettingAct::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }
}
