package com.sourabhverma.mirrorscoretask01.main.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sourabhverma.mirrorscoretask01.R
import com.sourabhverma.mirrorscoretask01.databinding.RecyclerViewBinding
import com.sourabhverma.mirrorscoretask01.main.adapters.RecyclerViewAdapter.MyViewHolder
import com.sourabhverma.mirrorscoretask01.main.data.MainActivityAPIHelper
import com.sourabhverma.mirrorscoretask01.main.pojo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewAdapter: RecyclerView.Adapter<MyViewHolder>() {

    private lateinit var items : List<Data>
    private lateinit var context : Context
    fun setDataList(data: List<Data>, context: Context){
        this.items = data
        this.context = context
    }
    class MyViewHolder(private val binding: RecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data : Data, context: Context){
            binding.resultItem = data

            binding.upVoteButton.setOnClickListener {
                if (!data.upvoted) {
                    binding.upVoteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumb_up_true, 0, 0, 0)
                    val userId = context.getSharedPreferences("USER_DATA", 0).getInt("USER_ID", -1)
                    val auth = context.getSharedPreferences("USER_DATA", 0).getString("ACCESS_TOKEN", "")
                    if(!auth.equals("") && userId!=-1){
                        val body = bodyForUpVoteAPost(data.postId)
                        MainActivityAPIHelper().upVoteAPost(userId, "Bearer $auth", body).enqueue(object : Callback<upVoteAPostResponse>{
                            override fun onResponse(
                                call: Call<upVoteAPostResponse>,
                                response: Response<upVoteAPostResponse>
                            ) {
                                val res : upVoteAPostResponse? = response.body()
                                if(res?.ReponseMessage.equals("SUCCESS")){
                                    Toast.makeText(context, "Upvoted Successfully.", Toast.LENGTH_SHORT).show()
                                    binding.resultItem?.upvoted = true
                                    binding.resultItem?.upvoteCount = binding.resultItem?.upvoteCount?.plus(1)!!
                                    binding.upVoteButton.text = context.getString(R.string.up_votes)
                                        .plus("(").plus(binding.resultItem?.upvoteCount).plus(")")
                                } else {
                                    Toast.makeText(context, "Upvoted Failed..", Toast.LENGTH_SHORT).show()
                                    binding.upVoteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumb_up_false, 0, 0, 0)
                                }
                            }
                            override fun onFailure(call: Call<upVoteAPostResponse>, t: Throwable) {
                                Log.d("TAG", "onFailure: ${t.message}")
                                binding.upVoteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumb_up_false, 0, 0, 0)
                            }
                        })
                    } else {
                        Toast.makeText(context, "Something went wrong, can not able to get your userId and access token.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Already Upvoted.", Toast.LENGTH_SHORT).show()
                }

            }

            binding.replyButton.setOnClickListener {
                if (binding.includedAnser.root.visibility == VISIBLE) {
                    binding.includedAnser.replyEditText.text = null
                    binding.includedAnser.root.visibility = GONE
                } else {
                    binding.includedAnser.root.visibility = VISIBLE
                }
            }

            binding.includedAnser.sendButton.setOnClickListener{
                if (binding.includedAnser.replyEditText.text.toString().isNotEmpty()) {
                    binding.includedAnser.root.visibility = GONE
                    val userId = context.getSharedPreferences("USER_DATA", 0).getInt("USER_ID", -1)
                    val auth = context.getSharedPreferences("USER_DATA", 0).getString("ACCESS_TOKEN", "")
                    if(!auth.equals("") && userId!=-1){
                        val body = bodyForReplyToAPost("I Dont Know What should be here.", data.postId, binding.includedAnser.replyEditText.text.toString())
                        MainActivityAPIHelper().replyAPost(userId, "Bearer $auth", body).enqueue(object : Callback<upVoteAPostResponse>{
                            override fun onResponse(
                                call: Call<upVoteAPostResponse>,
                                response: Response<upVoteAPostResponse>
                            ) {
                                val res : upVoteAPostResponse? = response.body()
                                if(res?.ReponseMessage.equals("SUCCESS")){
                                    Toast.makeText(context, "Replied Successfully.", Toast.LENGTH_SHORT).show()
                                    binding.resultItem?.answerCount = binding.resultItem?.answerCount?.plus(1)!!
                                    binding.replyButton.text = context.getString(R.string.answer)
                                        .plus("(").plus(binding.resultItem?.answerCount).plus(")")
                                } else {
                                    Toast.makeText(context, "Replied Failed..", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<upVoteAPostResponse>, t: Throwable) {
                                Log.d("TAG", "onFailure: ${t.message}")
                            }
                        })
                    } else {
                        Toast.makeText(context, "Something went wrong, can not able to get your userId and access token.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Reply should contains something", Toast.LENGTH_SHORT).show()
                }
            }

            binding.reportButton.setOnClickListener{
                if (!binding.resultItem?.reported!!) {
                    if (binding.includedReport.root.visibility == VISIBLE) {
                        binding.includedReport.root.visibility = GONE
                    } else {
                        binding.includedReport.root.visibility = VISIBLE
                    }
                }
            }

            binding.rootLayout.setOnClickListener{
                if (binding.includedAnser.root.visibility == VISIBLE) {
                    binding.includedAnser.replyEditText.text = null
                    binding.includedAnser.root.visibility = GONE
                }
            }

            binding.includedReport.submitButton.setOnClickListener{
                if (!data.reported) {
                    if (binding.includedReport.incorrectAnswer.isChecked || binding.includedReport.incorrectQuestion.isChecked ||
                        binding.includedReport.missingImage.isChecked || binding.includedReport.missingOption.isChecked ||
                        binding.includedReport.spellingMistake.isChecked) {
                            binding.includedReport.root.visibility = GONE
                            binding.reportButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.report_true, 0, 0, 0)

                            val userId = context.getSharedPreferences("USER_DATA", 0).getInt("USER_ID", -1)
                            val auth = context.getSharedPreferences("USER_DATA", 0).getString("ACCESS_TOKEN", "")

                            Log.d("TAG", "bind: userId : $userId and auth : $auth")

                            if(!auth.equals("") && userId!=-1 && !auth.isNullOrEmpty()){
                                val body = bodyForReportAPost(binding.includedReport.incorrectAnswer.isChecked.toString(), binding.includedReport.incorrectQuestion.isChecked.toString(),
                                    binding.includedReport.missingImage.isChecked.toString(),binding.includedReport.missingOption.isChecked.toString(),data.postId,
                                    binding.includedReport.spellingMistake.isChecked.toString())

                                Log.d("TAG", "bind: $body")

                                MainActivityAPIHelper().reportAPost(userId, "Bearer $auth", body).enqueue(object : Callback<responseForReportAPost>{
                                    override fun onResponse(
                                        call: Call<responseForReportAPost>,
                                        response: Response<responseForReportAPost>
                                    ) {
                                        val res : responseForReportAPost? = response.body()
                                        Log.d("TAG", "onResponse: ${res.toString()}")
                                        if(res?.ReponseMessage.equals("SUCCESS")){
                                            Toast.makeText(context, "Reported Successfully.", Toast.LENGTH_SHORT).show()
                                            binding.resultItem?.reported = true
                                            binding.resultItem?.reportCount = binding.resultItem?.reportCount?.plus(1)!!
                                            binding.reportButton.text = context.getString(R.string.report)
                                                .plus("(").plus(binding.resultItem?.reportCount).plus(")")
                                        } else {
                                            Log.d("TAG", "onResponse: ${res?.ReponseMessage.toString()}")
                                            Toast.makeText(context, "Reported Failed..", Toast.LENGTH_SHORT).show()
                                            binding.reportButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.report_false, 0, 0, 0)
                                        }
                                    }
                                    override fun onFailure(
                                        call: Call<responseForReportAPost>,
                                        t: Throwable
                                    ) {
                                        Log.d("TAG", "onFailure: ${t.message}")
                                        binding.reportButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.report_false, 0, 0, 0)
                                        Toast.makeText(context, "Reported Failed.", Toast.LENGTH_SHORT).show()
                                    }
                                })

                            } else {
                            Toast.makeText(context, "Something went wrong, can not able to get your userId and access token.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Report should contains something", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Already Reported.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        items[position].let {
            holder.bind(it, context)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}