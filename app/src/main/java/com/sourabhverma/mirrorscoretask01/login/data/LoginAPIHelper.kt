package com.sourabhverma.mirrorscoretask01.login.data

import com.sourabhverma.mirrorscoretask01.login.pojo.UserInfo
import com.sourabhverma.mirrorscoretask01.login.pojo.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginAPIHelper {

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginApi(@Body userData: UserInfo) : Call<LoginResponse>

    companion object{
        operator fun invoke() : LoginAPIHelper {
            return Retrofit.Builder()
                .baseUrl("https://mirrorscore-android.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginAPIHelper::class.java)
        }
    }

}