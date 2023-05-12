package com.example.githubuser1.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuser1.data.User
import com.example.githubuser1.data.UserDao
import com.example.githubuser1.remote.api.Service

class UserRep private constructor(
    private val apiService: Service,
    private val userDao: UserDao
) {

    fun getFavouriteUsers() : LiveData<List<User>> {
        return userDao.getNewsFavUser()
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    suspend fun setUsersFavourite(user: User, favouriteState: Boolean){
        user.isFavorite = favouriteState
        userDao.updateUser(user)
    }

    fun getUser(username: String): LiveData<Result<User>> = liveData{
        emit(Result.Loading)
        try {
            val user = apiService.getUD(username)
            val isFavourite = userDao.isUserFav(user.login)
            userDao.delete(user.login)
            userDao.insertUser(
                User(
                user.login,
                user.name,
                user.avatarUrl,
                user.company,
                user.follower,
                user.following,
                user.publicRepos,
                user.location,
                isFavourite
            )
            )
        }catch (e : Exception){
            Log.d("UsersRepository", "getUser: ${e.message.toString()} ")
            emit(com.example.githubuser1.remote.Result.Error(e.message.toString()))
        }
        val isFavourite = userDao.isUser(username)
        if(isFavourite){
            val localData : LiveData<com.example.githubuser1.remote.Result<User>> = userDao.getUser(username).map { com.example.githubuser1.remote.Result.Success(it) }
            emitSource(localData)
        }else{
            emit(com.example.githubuser1.remote.Result.Error("Not Found!"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRep? = null
        fun getInstance(
            apiService: Service,
            UserDao: UserDao
        ): UserRep =
            instance ?: synchronized(this) {
                instance ?: UserRep(apiService, UserDao)
            }.also { instance = it }
    }
}