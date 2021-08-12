package com.sourabhverma.mirrorscoretask01.main.data

import com.sourabhverma.mirrorscoretask01.main.pojo.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MainActivityAPIHelper {

    @GET("discussionWall/post")
    fun getAllPosts(@Query("userId") userId : Int, @Header("Authorization") authHeader:String) : Call<getAllPostsResponse>

    @POST("discussionWall/postupvote")
    fun upVoteAPost(@Query("userId")userId: Int, @Header("Authorization") authHeader: String, @Body postId: bodyForUpVoteAPost) : Call<upVoteAPostResponse>

    @POST("discussionWall/answer")
    fun replyAPost(@Query("userId")userId: Int, @Header("Authorization") authHeader: String, @Body postId: bodyForReplyToAPost) : Call<upVoteAPostResponse>

    @Headers("Content-Type: application/json")
    @POST("discussionWall/postreport")
    fun reportAPost(@Query("userId")userId: Int, @Header("Authorization") authHeader: String, @Body body: bodyForReportAPost) : Call<responseForReportAPost>

    @POST("discussionWall/post")
    fun addNewPost(@Query("userId")userId: Int, @Header("Authorization") authHeader: String, @Body body: bodyForAddNewPost) : Call<responseForAddNewPost>


    companion object{
        operator fun invoke() : MainActivityAPIHelper {
            return Retrofit.Builder()
                .baseUrl("https://mirrorscore-android.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MainActivityAPIHelper::class.java)
        }
    }


}