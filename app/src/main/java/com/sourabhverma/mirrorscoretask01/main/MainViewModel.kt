package com.sourabhverma.mirrorscoretask01.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sourabhverma.mirrorscoretask01.main.pojo.bodyForAddNewPost
import com.sourabhverma.mirrorscoretask01.main.pojo.getAllPostsResponse
import com.sourabhverma.mirrorscoretask01.main.pojo.responseForAddNewPost

class MainViewModel : ViewModel() {

    private val repository: MainRepository = MainRepository()

    private val TAG = "TAG"

    private var item : MutableLiveData<getAllPostsResponse> = MutableLiveData()
    private var addNewPostResponse : MutableLiveData<responseForAddNewPost> = MutableLiveData()

    fun getAllPost(userId:Int,
                   authHeader:String) {
        repository.getAllPost(userId, authHeader){
            if(it!=null){
                Log.d(TAG, "getAllPost: ${it.Result.data.size}")
                item.postValue(it)
            } else {
                Log.d(TAG, "getAllPost: it is null")
            }
        }
    }

    fun addNewPost(userId: Int,
                    authHeader: String,
                    body:bodyForAddNewPost, context : Context) {

        Log.d(TAG, "addNewPost: userdId : $userId and auth : $authHeader and body : ${body.toString()}")

        repository.addNewPost(userId, authHeader, body){
            if (it!=null){
                Log.d(TAG, "addNewPost: ${it.ReponseMessage}")
                addNewPostResponse.postValue(it)
            } else {
                Log.d(TAG, "addNewPost: it is null")
                Toast.makeText(context, "Something went Wrong.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addNewPostObserver () : MutableLiveData<responseForAddNewPost>{
        return addNewPostResponse
    }

    fun itemObserver() : MutableLiveData<getAllPostsResponse>{
        return item
    }

}