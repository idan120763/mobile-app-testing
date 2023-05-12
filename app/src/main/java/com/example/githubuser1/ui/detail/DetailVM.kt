package com.example.githubuser1.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser1.data.User
import com.example.githubuser1.remote.api.Config
import com.example.githubuser1.event.Event
import com.example.githubuser1.remote.UserRep
import com.example.githubuser1.remote.response.itemsSearch
import com.example.githubuser1.remote.response.ResSD
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailVM(private val usersRepository: UserRep) : ViewModel() {

    private val _userDetail = MutableLiveData<ResSD>()
    val userDetail: LiveData<ResSD> = _userDetail

    private val _follower = MutableLiveData<List<itemsSearch>>()
    val follower: LiveData<List<itemsSearch>> = _follower

    private val _following = MutableLiveData<List<itemsSearch>>()
    val following: LiveData<List<itemsSearch>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun getUser(username: String) = usersRepository.getUser(username)

    fun saveFavorite(user: User) {
        viewModelScope.launch {
            usersRepository.setUsersFavourite(user, true)
        }
    }

    fun deleteFavorite(user: User) {
        viewModelScope.launch {
            usersRepository.setUsersFavourite(user, false)
        }
    }

    fun getFavoriteUsers() = usersRepository.getFavouriteUsers()

    fun deleteAll(){
        viewModelScope.launch {
            usersRepository.deleteAll()
        }
    }

    fun getFollower(username: String?) {
        _isLoading.value = true
        val client = Config.getApi().getFollower(username)
        client.enqueue(object : Callback<List<itemsSearch>> {
            override fun onResponse(
                call: Call<List<itemsSearch>>,
                response: Response<List<itemsSearch>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _follower.value = response.body()
                    _toastText.value = Event("Success")
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }
            override fun onFailure(call: Call<List<itemsSearch>>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String?) {
        _isLoading.value = true
        val client = Config.getApi().getFollowing(username)
        client.enqueue(object : Callback<List<itemsSearch>> {
            override fun onResponse(
                call: Call<List<itemsSearch>>,
                response: Response<List<itemsSearch>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }
    override fun onFailure(call: Call<List<itemsSearch>>, t: Throwable) {
        _isLoading.value = false
        _toastText.value = Event("onFailure: ${t.message.toString()}")
    }
})
}
}