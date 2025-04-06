package com.example.ecopulse.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecopulse.routes.Screen

@Composable
fun SplashScreen(navController: NavController) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("animation.json")
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        speed = 1.5f
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.SplashScreen.route) {inclusive = true}
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = {progress},
            modifier = Modifier.size(200.dp)
        )
    }
}
