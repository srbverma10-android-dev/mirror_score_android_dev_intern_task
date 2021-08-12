package com.sourabhverma.mirrorscoretask01.main.pojo

data class ResultX(
    val incorrectAnswer: Boolean,
    val incorrectQuestion: Boolean,
    val missingImage: Boolean,
    val missingOption: Boolean,
    val reportId: Int,
    val spellingMistake: Boolean
)