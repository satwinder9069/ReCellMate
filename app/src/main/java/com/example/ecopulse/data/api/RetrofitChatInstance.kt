package com.example.ecopulse.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitChatInstance {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS) // Increased to prevent early timeout
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()
    val geminiApi: ChatService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ChatService::class.java)
    }
}