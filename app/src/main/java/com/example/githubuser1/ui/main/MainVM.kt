package com.example.githubuser1.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser1.remote.api.Config
import com.example.githubuser1.event.Event
import com.example.githubuser1.remote.response.itemsSearch
import com.example.githubuser1.remote.response.resSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainVM : ViewModel() {

    private val _userList = MutableLiveData<List<itemsSearch>>()
    val userList: LiveData<List<itemsSearch>> = _userList

    private val _userCount = MutableLiveData<Int?>()
    val userCount: LiveData<Int?> = _userCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun findUser(username : String) {
        _isLoading.value = true
        val client = Config.getApi().getUser(username)
        client.enqueue(object : Callback<resSearch> {
            override fun onResponse(
                call: Call<resSearch>,
                response: Response<resSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()?.items as List<itemsSearch>
                    _userCount.value = response.body()?.totalCount
                    _toastText.value = Event("Success")
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }
            override fun onFailure(call: Call<resSearch>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }
}
