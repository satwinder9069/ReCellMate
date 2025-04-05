package com.example.ecopulse.data.repository

import android.util.Log
import com.example.ecopulse.data.api.ChatService
import com.example.ecopulse.data.models.GeminiMessage
import com.example.ecopulse.data.models.GeminiRequest
import retrofit2.HttpException

class GeminiRepository(private val api: ChatService) {
    suspend fun fetchGeminiResponse(messages: List<GeminiMessage>): String {
        return try {
            val request = GeminiRequest(contents = messages)
            val response = api.getResponse(request)
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No Response"

        } catch (e: HttpException) {
            Log.e("GeminiAPI", "Error: ${e.response()?.errorBody()?.string()}")
            "Error: ${e.response()?.errorBody()?.string() ?: "Unknown error"}"
        } catch (e: Exception) {
            Log.e("GeminiAPI", "Request failed: ${e.message}")
            "Request failed: ${e.message}"
        }

    }
}
