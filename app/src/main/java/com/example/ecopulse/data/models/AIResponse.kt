package com.example.ecopulse.data.models

import com.google.gson.annotations.SerializedName

data class AIResponse(
    @SerializedName("Condition")
    val condition: String?,

    @SerializedName("Final Adjusted Price")
    val finalAdjustedPrice: Double,

    @SerializedName("Predicted Price")
    val predictedPrice: Double
)
