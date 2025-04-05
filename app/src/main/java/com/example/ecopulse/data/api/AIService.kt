package com.example.ecopulse.data.api

import com.example.ecopulse.data.models.AIInput
import com.example.ecopulse.data.models.AIResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface AIService {
    @Multipart
    @POST("predict")
    suspend fun getPrediction(
        @Part("os")os: RequestBody,
        @Part("screen_size")screenSize: RequestBody,
        @Part("five_g")fiveG: RequestBody,
        @Part("internal_memory")internalMemory: RequestBody,
        @Part("ram")ram: RequestBody,
        @Part("battery")battery: RequestBody,
        @Part("release_year")releaseYear: RequestBody,
        @Part("days_used")daysUsed: RequestBody,
        @Part("normalized_new_price")normalizedNewPrice: RequestBody,
        @Part("device_brand")deviceBrand: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<AIResponse>

}