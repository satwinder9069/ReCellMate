package com.example.ecopulse.routes

import android.net.Uri

sealed class Screen(val route: String) {
    object SplashScreen: Screen("splash_screen")
    object HomeScreen : Screen("home_screen")
    object User: Screen("user_screen")
    object LearnMore: Screen("learn_more")
    object ScanDevice : Screen("scan_device")
    object ReviewScreen : Screen("reviewScreen/{imageUri}") {
        fun createRoute(imageUri: String) = "reviewScreen/$imageUri"
    }
    object InfoFormScreen : Screen("device_info/{imageUri}") {
        fun createRoute(imageUri: String) = "device_info/${Uri.encode(imageUri)}"
    }

    object PredictionScreen : Screen("prediction_screen/{imageUri}") {
        fun createRoute(imageUri: String) = "prediction_screen/${Uri.encode(imageUri)}"
    }
}