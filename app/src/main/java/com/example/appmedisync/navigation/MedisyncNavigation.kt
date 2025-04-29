package com.example.appmedisync.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.screens.MedisyncSplashScreen
import com.example.appmedisync.screens.home.Home
import com.example.appmedisync.screens.login.MedisyncLoginScreen

@Composable
fun MedisyncNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = MedisyncScreens.SplashScreen.name)
    {
        composable(MedisyncScreens.SplashScreen.name){
            MedisyncSplashScreen(navController = navController)
        }
        composable(MedisyncScreens.LoginScreen.name){
            MedisyncLoginScreen(navController = navController)
        }
        composable(MedisyncScreens.HomeScreen.name){
            Home(navController = navController)
        }
    }
}