package com.example.githubuser1.setting

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser1.databinding.SettingActBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingAct : AppCompatActivity() {

    private lateinit var binding: SettingActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingActBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val switchTheme = binding.switchTheme

        val pref = SettingPre.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(
            this,
            SettingVMF(pref)
        )[SettingVM::class.java]
        mainViewModel.getThemeSettings().observe(this)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }
}