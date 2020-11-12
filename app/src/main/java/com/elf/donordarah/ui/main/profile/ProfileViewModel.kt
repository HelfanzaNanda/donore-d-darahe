package com.elf.donordarah.ui.main.profile

import android.media.Image
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.User
import com.elf.donordarah.repositories.UserRepository
import com.elf.donordarah.utils.SingleLiveEvent
import com.elf.donordarah.utils.SingleResponse

class ProfileViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProfileState> = SingleLiveEvent()
    private val currentUser = MutableLiveData<User>()
    private val imagePath = MutableLiveData<String>()

    private fun isLoading(b : Boolean){ state.value = ProfileState.Loading(b) }
    private fun toast(m : String){ state.value = ProfileState.ShowToast(m) }
    private fun success(){ state.value = ProfileState.Success }

    fun fetchCurrentUser(token : String){
        isLoading(true)
        userRepository.profile(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { currentUser.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun updateProfile(token: String, user: User){
        isLoading(true)
        userRepository.updateUser(token, user, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun updatePhoto(token: String, image: String){
        isLoading(true)
        userRepository.updatePhoto(token, image, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun setPathImage(path : String){
        imagePath.postValue(path)
    }

    fun listenToState() = state
    fun listenCurrentUser() = currentUser
    fun listenImagePath() = imagePath
}

sealed class ProfileState{
    object Success : ProfileState()
    data class Loading(var state : Boolean = false) : ProfileState()
    data class ShowToast(var message : String) : ProfileState()
}