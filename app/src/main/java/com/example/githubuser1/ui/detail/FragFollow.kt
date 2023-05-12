package com.example.githubuser1.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser1.databinding.FragFollowBinding
import com.example.githubuser1.remote.response.itemsSearch
import com.example.githubuser1.ui.adapter.ListUser

class FragFollow: Fragment() {

    private var _binding: FragFollowBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailVM by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvf?.layoutManager = LinearLayoutManager(activity)
        when(arguments?.getInt(ARG_SECTION_NUMBER, 0)){
            1 -> {
                detailViewModel.getFollowing(arguments?.getString(USERNAME))
                detailViewModel.following.observe(requireActivity()) { users ->
                    setUserData(users)
                }
            }
            2 -> {
                detailViewModel.getFollower(arguments?.getString(USERNAME))
                detailViewModel.follower.observe(requireActivity()) { users ->
                    setUserData(users)
                }
            }
        }
        binding?.rvf?.setHasFixedSize(true)
    }

    private fun showSelectedUser(user: itemsSearch) {
        val detailUserIntent = Intent(requireActivity(), DetailAct::class.java)
        detailUserIntent.putExtra(DetailAct.EXTRA_USER, user.login)
        startActivity(detailUserIntent)
    }

    private fun setUserData(users: List<itemsSearch>?) {
        val listUserAdapter = ListUser(users as ArrayList<itemsSearch>)
        binding?.rvf?.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUser.OnItemClickCallback {
            override fun onItemClicked(data: itemsSearch) {
                showSelectedUser(data)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout? {
        _binding = FragFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "login"
    }
}