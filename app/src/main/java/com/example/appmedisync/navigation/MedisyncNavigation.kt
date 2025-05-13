package com.example.appmedisync.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.Usuarios.UsuarioViewModel
import com.example.appmedisync.screens.MedisyncSplashScreen
import com.example.appmedisync.screens.home.ConfigurarPerfil
import com.example.appmedisync.screens.home.ConfigurarPerfil2
import com.example.appmedisync.screens.home.HomeScreen
import com.example.appmedisync.screens.home.TransicionScreen
import com.example.appmedisync.screens.home.VincularReloj
//import com.example.appmedisync.screens.home.Home
import com.example.appmedisync.screens.login.MedisyncLoginScreen
import com.example.appmedisync.screens.secundarias.EnfermedadesScreen
import com.example.appmedisync.screens.secundarias.MedicamentosScreen
import com.example.appmedisync.screens.secundarias.ReportesScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedisyncNavigation(){
    val navController = rememberNavController()
    val viewModel: UsuarioViewModel = viewModel()

    NavHost(navController = navController,
        startDestination = MedisyncScreens.SplashScreen.name)
    {
        composable(MedisyncScreens.SplashScreen.name){
            MedisyncSplashScreen(navController = navController)
        }
        composable(MedisyncScreens.LoginScreen.name){
            MedisyncLoginScreen(navController = navController)
        }
        composable(MedisyncScreens.ConfigurarPerfil.name){
            ConfigurarPerfil(navController = navController)
        }
        composable(MedisyncScreens.ConfigurarPerfil2.name){
            ConfigurarPerfil2(navController = navController)
        }
        composable(MedisyncScreens.VincularReloj.name){
            VincularReloj(navController = navController)
        }
        composable(MedisyncScreens.TransicionScreen.name){
            TransicionScreen(navController = navController)
        }
        composable(MedisyncScreens.HomeScreen.name){
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(MedisyncScreens.Enfermedades.name){
            EnfermedadesScreen(navController = navController)
        }
        composable(MedisyncScreens.Medicamentos.name){
            MedicamentosScreen(navController = navController)
        }
        composable(MedisyncScreens.Reportes.name){
            ReportesScreen(navController = navController)
        }

    }
}