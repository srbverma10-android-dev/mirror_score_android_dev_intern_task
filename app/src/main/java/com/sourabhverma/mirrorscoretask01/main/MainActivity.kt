package com.sourabhverma.mirrorscoretask01.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sourabhverma.mirrorscoretask01.R
import com.sourabhverma.mirrorscoretask01.databinding.ActivityMainBinding
import com.sourabhverma.mirrorscoretask01.main.adapters.RecyclerViewAdapter
import com.sourabhverma.mirrorscoretask01.main.pojo.bodyForAddNewPost

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val REQUEST_CODE = 12
    
    private var imageUri : Uri = "nothing is selected".toUri()

    private val TAG= "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val recyclerView = binding.recyclerView
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayout
        val recyclerViewAdapter = RecyclerViewAdapter()
        val auth = getSharedPreferences("USER_DATA", MODE_PRIVATE).getString("ACCESS_TOKEN", "")
        val userId = getSharedPreferences("USER_DATA", MODE_PRIVATE).getInt("USER_ID", -1)
        if(!auth.equals("") && userId != -1){
            viewModel.getAllPost(userId, "Bearer $auth")
        } else {
            Toast.makeText(this, "Something went wrong, can not fetch the posts.", Toast.LENGTH_SHORT).show()
        }
        binding.addNewPost.setOnClickListener{
            if(binding.inculdedAddNewPost.root.visibility == GONE){
                binding.inculdedAddNewPost.root.visibility = VISIBLE
            } else {
                binding.inculdedAddNewPost.root.visibility = GONE
            }
        }
        binding.inculdedAddNewPost.addImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
        binding.inculdedAddNewPost.submitButton.setOnClickListener{
            viewModel.addNewPost(userId,"Bearer $auth", body = bodyForAddNewPost(imageUri.toString(), binding.inculdedAddNewPost.desc.text.toString().toInt(), binding.inculdedAddNewPost.text.text.toString() ), this)
        }
        viewModel.itemObserver().observe(this@MainActivity, {
            Log.d(TAG, "onCreate: observer ${it.Result}")
            recyclerViewAdapter.setDataList(it.Result.data, this)
            recyclerView.adapter = recyclerViewAdapter
        })
        viewModel.addNewPostObserver().observe(this@MainActivity, {
            Log.d(TAG, "onCreate: observer ${it.ReponseMessage}")
            if (it.ReponseMessage == "SUCCESS"){
                Toast.makeText(this, "Post Successful.", Toast.LENGTH_SHORT).show()
                if(!auth.equals("") && userId != -1){
                    viewModel.getAllPost(userId, "Bearer $auth")
                }
            } else {
                Toast.makeText(this, "Post Failed.", Toast.LENGTH_SHORT).show()
            }
            binding.inculdedAddNewPost.root.visibility = GONE
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){ 
            imageUri = data?.data!!
            binding.inculdedAddNewPost.addImage.text = "Image Added"
        }
    }
}