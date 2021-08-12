package com.sourabhverma.mirrorscoretask01.main.pojo

data class Data(
    var answerCount: Int,
    val createdOn: String,
    val image: String,
    val postId: Int,
    var reportCount: Int,
    var reported: Boolean,
    val studentBoard: String,
    val studentClass: String,
    val subject: String,
    val text: String,
    val updatedOn: String,
    var upvoteCount: Int,
    var upvoted: Boolean,
    val userId: Int,
    val userName: String
)