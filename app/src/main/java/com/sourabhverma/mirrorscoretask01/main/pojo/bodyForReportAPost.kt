package com.sourabhverma.mirrorscoretask01.main.pojo

data class bodyForReportAPost(
    val incorrectAnswer: String,
    val incorrectQuestion: String,
    val missingImage: String,
    val missingOption: String,
    val postId: Int,
    val spellingMistake: String
)