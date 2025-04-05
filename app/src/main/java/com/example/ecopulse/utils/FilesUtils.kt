package com.example.ecopulse.utils

import android.content.Context
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.example.ecopulse.BuildConfig

fun prepareFilePart(partName: String, imageUri: String?, context: Context): MultipartBody.Part? {
    Log.d("MultipartDebug", "prepareFilePart called with imageUri: $imageUri")

    val file = getFileFromUri(context, imageUri)
    if (file == null) {
        Log.d("MultipartDebug", "File is null. Exiting prepareFilePart")
        return null
    } else {
        Log.d("MultipartDebug", "File obtained: ${file.absolutePath}")
    }

    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    Log.d("MultipartDebug", "RequestBody created successfully")

    val multipartBody = MultipartBody.Part.createFormData(partName, file.name, requestFile)
    Log.d("MultipartDebug", "MultipartBody.Part created with file name: ${file.name}")

    return multipartBody
}



fun testApiKey() {
    val apiKey = BuildConfig.GEMINI_API_KEY
    Log.d("API_KEY", "Gemini API Key: $apiKey")
}


