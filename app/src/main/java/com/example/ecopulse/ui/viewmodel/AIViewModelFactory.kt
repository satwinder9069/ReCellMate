package com.example.ecopulse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecopulse.data.repository.AIRepository

class AIViewModelFactory(private val repository: AIRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AIViewModel::class.java)) {
            return AIViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
