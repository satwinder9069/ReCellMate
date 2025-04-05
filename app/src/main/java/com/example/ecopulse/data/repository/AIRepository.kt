package com.example.ecopulse.data.repository

import android.util.Log
import com.example.ecopulse.data.api.AIService
import com.example.ecopulse.data.models.AIInput
import com.example.ecopulse.data.models.AIResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response


class AIRepository(private val api: AIService)  {
    suspend fun getPrediction(aiInput: AIInput, imagePart: MultipartBody.Part): Response<AIResponse> {
        Log.d("AIRepository", "Sending Request to API ...")

        val os = aiInput.os.toRequestBody("text/plain".toMediaTypeOrNull())
        val screenSize = aiInput.screenSize.toRequestBody("text/plain".toMediaTypeOrNull())
        val fiveG = aiInput.fiveG.toRequestBody("text/plain".toMediaTypeOrNull())
        val internalMemory = aiInput.internalMemory.toRequestBody("text/plain".toMediaTypeOrNull())
        val ram = aiInput.ram.toRequestBody("text/plain".toMediaTypeOrNull())
        val battery = aiInput.battery.toRequestBody("text/plain".toMediaTypeOrNull())
        val releaseYear = aiInput.releaseYear.toRequestBody("text/plain".toMediaTypeOrNull())
        val daysUsed = aiInput.daysUsed.toRequestBody("text/plain".toMediaTypeOrNull())
        val normalizedNewPrice = aiInput.normalizedNewPrice.toRequestBody("text/plain".toMediaTypeOrNull())
        val deviceBrand = aiInput.deviceBrand.toRequestBody("text/plain".toMediaTypeOrNull())
        return api.getPrediction(
            os,
            screenSize,
            fiveG,
            internalMemory,
            ram,
            battery,
            releaseYear,
            daysUsed,
            normalizedNewPrice,
            deviceBrand,
            imagePart
        )
    }
}
