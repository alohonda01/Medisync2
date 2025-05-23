package com.example.appmedisync.app.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.app.screens.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}

@Composable
fun SplashScreen(navController: NavController){

    //Muestra cuanto dura la screen antes de pasar a Login
    LaunchedEffect(key1 = true){
        delay(0L)
        //Para uso de pruebas se va directamente a login screen
        //navController.navigate(Screens.LoginScreen.name)
       // navController.navigate(Screens.MedicamentosScreen.name)

        //Descomentar para que funcione correctamente
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(Screens.LoginScreen.name)
        }else{
            navController.navigate(Screens.HomeScreen.name){
                popUpTo(Screens.SplashScreen.name){
                    inclusive = true
                }
            }
        }
    }
}
