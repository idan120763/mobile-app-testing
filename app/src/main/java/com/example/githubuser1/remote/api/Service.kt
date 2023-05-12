package com.example.githubuser1.remote.api

import com.example.githubuser1.remote.response.itemsSearch
import com.example.githubuser1.remote.response.ResSD
import com.example.githubuser1.remote.response.resSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("search/users")
    fun getUser(
        @Query("q") username: String
    ): Call<resSearch>

    @GET("users/{username}")
    suspend fun getUD(
        @Path("username") username: String
    ): ResSD

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String?
    ): Call<List<itemsSearch>>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String?
    ): Call<List<itemsSearch>>

}