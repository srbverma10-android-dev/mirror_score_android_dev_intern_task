package com.sourabhverma.mirrorscoretask01.main

import android.util.Log
import com.sourabhverma.mirrorscoretask01.main.data.MainActivityAPIHelper
import com.sourabhverma.mirrorscoretask01.main.pojo.bodyForAddNewPost
import com.sourabhverma.mirrorscoretask01.main.pojo.getAllPostsResponse
import com.sourabhverma.mirrorscoretask01.main.pojo.responseForAddNewPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository {

    private val TAG = "TAG"

    fun getAllPost(userId:Int,
                   authHeader:String,
                   onResult: (getAllPostsResponse?) -> Unit){
        MainActivityAPIHelper().getAllPosts(userId, authHeader).enqueue(object : Callback<getAllPostsResponse>{
            override fun onResponse(
                call: Call<getAllPostsResponse>,
                response: Response<getAllPostsResponse>
            ) {
                val user : getAllPostsResponse? = response.body()
                onResult(user)
            }

            override fun onFailure(call: Call<getAllPostsResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                onResult(null)
            }

        })
    }

    fun addNewPost(userId: Int, authHeader: String, bodyForAddNewPost: bodyForAddNewPost, onResult: (responseForAddNewPost?) -> Unit){
        MainActivityAPIHelper().addNewPost(userId, authHeader, bodyForAddNewPost).enqueue(object : Callback<responseForAddNewPost>{
            override fun onResponse(
                call: Call<responseForAddNewPost>,
                response: Response<responseForAddNewPost>
            ) {
                Log.d(TAG, "onResponse: ")
                val post : responseForAddNewPost? = response.body()
                onResult(post)
            }

            override fun onFailure(call: Call<responseForAddNewPost>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                onResult(null)
            }

        })
    }

}