package com.example.ecopulse.data.models


data class Part(val text: String)
data class GeminiMessage(val role: String, val parts: List<Part>) {
}