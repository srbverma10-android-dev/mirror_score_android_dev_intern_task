package com.sourabhverma.mirrorscoretask01.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.sourabhverma.mirrorscoretask01.login.pojo.UserInfo
import com.sourabhverma.mirrorscoretask01.main.MainActivity

class LoginViewModel : ViewModel() {
    private val repository: LoginRepository = LoginRepository()
    fun onLoginButtonClick(username:String, password:String, context: Context){
        Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show()
        val userInfo = UserInfo(email = username, password = password)
        repository.addUser(userInfo){
            if(it != null){
                if (saveTokenToSharedPref(context, it.Result.userId, it.Result.token.refresh, it.Result.token.access)) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveTokenToSharedPref(context : Context,
                                        userId : Int,
                                        refreshToken : String,
                                        accessToken : String): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putInt("USER_ID", userId)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.putString("ACCESS_TOKEN", accessToken)
        return  editor.commit()
    }
}