package com.example.appmedisync.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.navigation.MedisyncScreens
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun TransicionScreenPreview(){
    //Para navegar entre screens
    val navController = rememberNavController()
    TransicionScreen(navController = navController)
}

@Composable
fun TransicionScreen(navController: NavController){

    LaunchedEffect(Unit) {
        delay(2000) // 2000 milisegundos = 2 segundos
        navController.navigate(MedisyncScreens.HomeScreen.name) {
            popUpTo(MedisyncScreens.ConfigurarPerfil.name) {
                inclusive = true
            }
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF3674B5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ){
            Text(
                text = "¡Bienvenido!",
                fontSize = 40.sp,
                color = Color.White
            )
        }

    }
}