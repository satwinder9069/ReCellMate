package com.example.ecopulse.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecopulse.data.models.AIInput
import com.example.ecopulse.data.models.AIResponse
import com.example.ecopulse.data.repository.AIRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AIViewModel(private val repository: AIRepository): ViewModel() {
    var predictionResult by mutableStateOf<AIResponse?>(null)
        private set

    var isLoading by mutableStateOf(false)

    fun fetchPrediction(aiInput: AIInput, imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            isLoading = true
            try {
                Log.d("PredictionAPI", "API Call started with input: $aiInput and image: ${imagePart?.body}")

                val response = repository.getPrediction(aiInput, imagePart)
                if (response.isSuccessful) {
                    predictionResult = response.body()
                    predictionResult?.let { result ->
                        if (!result.condition.isNullOrEmpty()) {
                            Log.d("PredictionAPI", "Condition: ${result.condition}, Predicted Price: ${result.predictedPrice}")
                        } else {
                            Log.d("PredictionAPI", "Condition is null or empty")
                        }
                    }
                    Log.d("PredictionAPI", "Prediction: ${predictionResult?.predictedPrice}")
                } else {
                    Log.e("PredictionAPI", "API Error: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.e("PredictionAPI", "Exception: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }


}

