package com.example.appmedisync.app.screens.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.app.screens.configuracion.Configuracion
import com.example.appmedisync.app.screens.home.Home
import com.example.appmedisync.app.screens.home.enfermedades.Enfermedades
import com.example.appmedisync.app.screens.home.medicinas.Medicamentos
import com.example.appmedisync.app.screens.home.reportes.Reportes
import com.example.appmedisync.app.screens.login.Login
import com.example.appmedisync.app.screens.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = Screens.SplashScreen.name)
    {
        composable(Screens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(Screens.LoginScreen.name){
            Login(navController = navController)
        }
        composable(Screens.ConfiguracionScreen.name){
            Configuracion(navController = navController)
        }
        composable(Screens.HomeScreen.name){
            Home(navController = navController)
        }
        composable(Screens.ReportesScreen.name){
            Reportes(navController = navController)
        }
        composable(Screens.EnfermedadesScreen.name){
            Enfermedades(navController = navController)
        }
        composable(Screens.MedicamentosScreen.name){
            Medicamentos(navController = navController)
        }
    }
}
