package com.example.appmedisync.app.screens.home.reportes

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DeepSeekApi {
    @POST("v1/chat/completions")
    fun generateResponse(
        @Body request: DeepSeekRequest
    ): Call<DeepSeekResponse>
}