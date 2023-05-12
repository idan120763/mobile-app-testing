package com.example.githubuser1.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser1.ui.detail.FragFollow

class SectionPage(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FragFollow()
        fragment.arguments = Bundle().apply {
            putInt(FragFollow.ARG_SECTION_NUMBER, position + 1)
            putString(FragFollow.USERNAME, username)
        }
        return fragment
    }
}