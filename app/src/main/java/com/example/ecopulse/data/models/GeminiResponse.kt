package com.example.ecopulse.data.models

data class GeminiResponse(val candidates: List<Candidate>)

data class Candidate(val content: Content)

data class Content(
    val parts: List<Part>
)