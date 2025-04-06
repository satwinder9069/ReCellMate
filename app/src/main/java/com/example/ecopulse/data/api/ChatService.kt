package com.example.ecopulse.data.api

import com.example.ecopulse.data.models.GeminiRequest
import com.example.ecopulse.data.models.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatService {
    @POST("v1/models/gemini-1.5-pro:generateContent")
    suspend fun getResponse(
        @Body request: GeminiRequest,
    ): GeminiResponse
}