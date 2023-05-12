package com.example.githubuser1.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser1.data.Inject
import com.example.githubuser1.remote.UserRep

class DetailVMF private constructor(private val userRepository: UserRep):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailVM::class.java)) {
            return DetailVM(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }

    companion object{
        @Volatile
        private var instance : DetailVMF? = null
        fun getInstance(context: Context): DetailVMF =
            instance ?: synchronized(this){
                instance ?: DetailVMF(Inject.provideRepository(context))
            }.also { instance = it }
    }
}
