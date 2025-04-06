package com.example.ecopulse.utils


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

data class InfoForm(
    var deviceBrand: String = "",
    var model: String = "",
    var internalMemory: String = "",
    var ram: String = "",
    var batteryInfo: String = "",
    var screenSize: String = "",
    var os: String = "",
    var is5G: String = "",
    var releaseYear: String = "",
    var daysUsed: String = "",
    var normalizedNewPrice: String = "",
)
class InfoFormViewModel : ViewModel() {
    var formState by mutableStateOf(InfoForm())
    var errorMessage by mutableStateOf("")
    var capturedImageFile by mutableStateOf<File?>(null)
        private set

    fun updateCapturedImageFile(file: File?) {
        capturedImageFile = file
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateForm(): Boolean {
        val daysUsedInt = formState.daysUsed.trim().toIntOrNull()
        val minPrice = 1000.0
        val maxPrice = 50000.0
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val today = LocalDate.now()
        val releaseYearTrimmed = formState.releaseYear.trim()
        val releaseYearInt = releaseYearTrimmed.toIntOrNull()
        if (releaseYearInt == null || releaseYearInt > currentYear) {
            errorMessage = "Please enter a valid Release Year (<= $currentYear)"
            println(errorMessage)
            return false
        }
        return when {

            formState.deviceBrand.isEmpty() -> {
                errorMessage = "Select Device"
                Log.d("Validation", errorMessage)
                false
            }

            formState.model.isEmpty() -> {
                errorMessage = "Model name is Required !"
                Log.d("Validation", errorMessage)

                false
            }

            formState.internalMemory.isEmpty() || formState.internalMemory.filter { it.isDigit() }.isEmpty() -> {
                errorMessage = "Enter Internal Memory (Only Number)"
                Log.d("Validation", errorMessage)
                false
            }

            formState.ram.isEmpty() || formState.ram.any { !it.isDigit() } -> {
                errorMessage = "Enter valid input "
                Log.d("Validation", errorMessage)
                false
            }

            formState.batteryInfo.isEmpty() || formState.batteryInfo.any { !it.isDigit() } -> {
                errorMessage = "Enter valid input"
                Log.d("Validation", errorMessage)
                false
            }

            formState.screenSize.isEmpty() || formState.screenSize.toFloatOrNull() == null || formState.screenSize.toFloat() < 4.0 || formState.screenSize.toFloat() > 7.5 -> {
                errorMessage = "Screen size must be between 4.0 and 7.5 inches"
                Log.d("Validation", errorMessage)
                false
            }

            formState.os.isEmpty() -> {
                errorMessage = "Enter OS"
                Log.d("Validation", errorMessage)
                false
            }

            formState.is5G.isEmpty() -> {
                errorMessage = "Please specify if the device supports 5G (yes/no)"
                Log.d("Validation", errorMessage)
                false
            }
            formState.releaseYear.isEmpty() || releaseYearInt == null -> {
                errorMessage = "Please enter a valid Release Year"
                false
            }

            releaseYearInt > currentYear -> {
                errorMessage = "Release Year cannot be in the future"
                false
            }

            formState.daysUsed.isEmpty() || daysUsedInt == null || daysUsedInt <= 0 -> {
                errorMessage = "Enter valid Days Used"
                false
            }

            daysUsedInt > ChronoUnit.DAYS.between(LocalDate.of(releaseYearInt, 1, 1), today)
                -> {
                errorMessage = "Days Used cannot exceed the number of days since release year"
                false
            }

            formState.normalizedNewPrice.isEmpty() || formState.normalizedNewPrice.toDoubleOrNull() == null ||
                    formState.normalizedNewPrice.toDouble() < minPrice || formState.normalizedNewPrice.toDouble() > maxPrice -> {
                errorMessage = "Enter valid price between $minPrice and $maxPrice"
                Log.d("Validation", errorMessage)
                false
            }
            else -> {
                true
            }
        }
    }
}