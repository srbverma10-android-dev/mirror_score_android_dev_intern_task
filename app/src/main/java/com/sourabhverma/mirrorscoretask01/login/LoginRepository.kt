package com.sourabhverma.mirrorscoretask01.login

import com.sourabhverma.mirrorscoretask01.login.data.LoginAPIHelper
import com.sourabhverma.mirrorscoretask01.login.pojo.UserInfo
import com.sourabhverma.mirrorscoretask01.login.pojo.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    fun addUser(userData: UserInfo, onResult: (LoginResponse?) -> Unit){
        LoginAPIHelper().loginApi(userData).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val user : LoginResponse? = response.body()
                onResult(user)
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}