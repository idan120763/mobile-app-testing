package com.example.githubuser1.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao{
    @Query("SELECT * FROM user where favorite = 1 order by login")
    fun getNewsFavUser(): LiveData<List<User>>

    @Query("SELECT EXISTS(SELECT * FROM user where login = :username AND favorite = 1)")
    suspend fun isUserFav(username: String?): Boolean

    @Query("SELECT EXISTS(SELECT * FROM user where login = :username)")
    suspend fun isUser(username: String?): Boolean

    @Query("DELETE FROM user WHERE login = :username")
    suspend fun delete(username: String?)

    @Query("SELECT * FROM user where login = :username")
    fun getUser(username: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM user WHERE favorite = 0")
    suspend fun deleteAll()
}