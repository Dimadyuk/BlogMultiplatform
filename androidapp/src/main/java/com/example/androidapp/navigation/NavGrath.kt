package com.example.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(route = Screen.HomeScreen.route) {
            // HomeScreen(navController)
        }
        composable(route = Screen.CategoryScreen.route) {
            // HomeScreen(navController)
        }
        composable(route = Screen.DetailScreen.route) {
            // HomeScreen(navController)
        }
    }
}
