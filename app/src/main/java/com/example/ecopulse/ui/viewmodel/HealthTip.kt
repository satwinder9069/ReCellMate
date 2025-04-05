package com.example.ecopulse.ui.viewmodel

data class HealthTip(
    val title: String,
    val buttonText: String,
    val imageRes: Int
)

data class TopicDetails(
    val title: String,
    val subtitle: String,
    val description: String,
    val effects: List<String>,
    val preventionTips: List<String>,
    val imageRes: List<Int>
)