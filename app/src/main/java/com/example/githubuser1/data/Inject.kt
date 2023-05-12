package com.example.githubuser1.data

import android.content.Context
import com.example.githubuser1.remote.api.Config
import com.example.githubuser1.remote.UserRep

object Inject {
    fun provideRepository(context: Context): UserRep {
        val apiService = Config.getApi()
        val database = UserDb.getInstance(context)
        val dao = database.userDao()
        return UserRep.getInstance(apiService, dao)
    }
}