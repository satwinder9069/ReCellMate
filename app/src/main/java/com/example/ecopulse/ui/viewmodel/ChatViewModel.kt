package com.example.ecopulse.ui.viewmodel

import android.os.Message
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecopulse.data.api.RetrofitChatInstance
import com.example.ecopulse.data.models.GeminiMessage
import com.example.ecopulse.data.models.Part
import com.example.ecopulse.data.repository.GeminiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: GeminiRepository): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var messages by mutableStateOf<List<Message>>(emptyList())
        private set

    private val _chatHistory = MutableLiveData<List<GeminiMessage>>(emptyList())
    val chatHistory: LiveData<List<GeminiMessage>> get() = _chatHistory

    fun sendMessage(userInput: String) {
        _isLoading.value = true
        val userMessage = GeminiMessage(role = "user", parts = listOf(Part(userInput)))

        val updatedHistory = _chatHistory.value.orEmpty() + userMessage
        _chatHistory.value = updatedHistory

        viewModelScope.launch {
            try {


                val reply = repository.fetchGeminiResponse(updatedHistory)
                val botMessage = GeminiMessage(role = "model", parts = listOf(Part(text = reply)))
                _chatHistory.value = _chatHistory.value.orEmpty() + botMessage
            }catch (e: Exception) {
                val errorMessage = GeminiMessage(role = "model", parts = listOf(Part("Error: Unable to fetch response.")))
                _chatHistory.value = _chatHistory.value.orEmpty() + errorMessage
            }finally {
                _isLoading.value = false
            }
        }
    }
}