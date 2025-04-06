package com.example.ecopulse.data.api

import com.example.ecopulse.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitChatInstance {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url

            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("key", BuildConfig.GEMINI_API_KEY)
                .build()

            val newRequest = original.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
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