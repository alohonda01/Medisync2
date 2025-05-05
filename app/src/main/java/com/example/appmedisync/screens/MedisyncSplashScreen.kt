package com.example.appmedisync.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.navigation.MedisyncScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    val navController = rememberNavController()
    MedisyncSplashScreen(navController = navController)
}

@Composable
fun MedisyncSplashScreen(navController: NavController) {
    val context = LocalContext.current
    val alreadyHandled = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_azul),
            contentDescription = "Logo de la app",
            modifier = Modifier.size(200.dp)
        )
    }

    LaunchedEffect(key1 = Unit) {
        if (!alreadyHandled.value) {
            alreadyHandled.value = true
            delay(3500L)

            val currentUser = FirebaseAuth.getInstance().currentUser
            val destination = if (currentUser?.email.isNullOrEmpty()) {
                MedisyncScreens.LoginScreen.name
            } else {
                //CAMBIAR ESTO A HOME CUANDO ESTE TERMINADO
                MedisyncScreens.LoginScreen.name
            }

            withContext(Dispatchers.Main) {
                navController.navigate(destination) {
                    popUpTo(MedisyncScreens.SplashScreen.name) { inclusive = true }
                }
            }
        }
    }
}