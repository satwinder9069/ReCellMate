package com.example.ecopulse.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecopulse.HomeScreen
import com.example.ecopulse.ui.screens.InfoFormScreen
import com.example.ecopulse.ui.screens.LearnMore
import com.example.ecopulse.ui.screens.PredictionScreen
import com.example.ecopulse.ui.screens.ReviewScreen
import com.example.ecopulse.ui.screens.ScanDevice
import com.example.ecopulse.ui.screens.User
import com.example.ecopulse.utils.InfoFormViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecopulse.data.api.RetrofitInstance
import com.example.ecopulse.data.repository.AIRepository
import com.example.ecopulse.ui.viewmodel.AIViewModel
import com.example.ecopulse.ui.viewmodel.AIViewModelFactory


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val infoFormViewModel: InfoFormViewModel = viewModel()
    val api = RetrofitInstance.api
    val repository = AIRepository(api)
    val aiViewModel: AIViewModel = viewModel(factory = AIViewModelFactory(repository))

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.User.route) {
            User(navController = navController)
        }
        composable(Screen.ScanDevice.route) {
            ScanDevice(navController = navController)
        }
        composable(Screen.LearnMore.route) {
            LearnMore(navController = navController)
        }
        composable(
            route = Screen.ReviewScreen.route,
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            ReviewScreen(imageUri = imageUri, navController = navController, viewModel = infoFormViewModel)
        }

        composable(
            route = Screen.InfoFormScreen.route,
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->

            val imageUri = backStackEntry.arguments?.getString("imageUri")
            InfoFormScreen(
                imageUri = imageUri,
                navController = navController,
                viewModel = infoFormViewModel
            )
        }
        composable(
            route = Screen.PredictionScreen.route,
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")

            PredictionScreen(
                imageUri = imageUri,
                infoFormViewModel = infoFormViewModel,
                navController = navController
            )
        }
    }
}