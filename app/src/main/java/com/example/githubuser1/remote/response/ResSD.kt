package com.example.githubuser1.remote.response

import com.google.gson.annotations.SerializedName

data class ResSD(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("public_repos")
    val publicRepos: Int? = null,

    @field:SerializedName("followers")
    val follower: Int? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    )