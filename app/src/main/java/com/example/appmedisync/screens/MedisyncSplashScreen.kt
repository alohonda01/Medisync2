package com.example.appmedisync.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmedisync.R
import com.example.appmedisync.navigation.MedisyncScreens
import com.example.appmedisync.screens.login.MedisyncLoginScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

//@Composable
//fun MedisyncSplashScreen(navController: NavController){
//    val scale = remember {
//        androidx.compose.animation.core.Animatable(0f)
//    }
//    LaunchedEffect(key1 = true) {
//        scale.animateTo(targetValue = 0.9f,
//            animationSpec = tween(durationMillis = 800,
//                easing = {
//                    OvershootInterpolator(8f)
//                        .getInterpolation(it)
//                }
//            )
//        )
//        delay(3500L)
//        navController.navigate(MedisyncScreens.LoginScreen.name)
//
////        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
////            navController.navigate(MedisyncScreens.LoginScreen.name)
////        }else{
////            navController.navigate(MedisyncScreens.HomeScreen.name){
////                popUpTo(MedisyncScreens.SplashScreen.name){
////                    inclusive=true
////                }
////            }
////        }
//
//    }
//
//}

@Composable
fun MedisyncSplashScreen(navController: NavController) {
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

        // Usamos LaunchedEffect para manejar la navegación después de un delay
        LaunchedEffect(Unit) {
            delay(3500L)
            navController.navigate(MedisyncScreens.LoginScreen.name)


//            if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
//                navController.navigate(MedisyncScreens.LoginScreen.name) {
//                    popUpTo(MedisyncScreens.SplashScreen.name) { inclusive = true }
//                }
//            } else {
//                navController.navigate(MedisyncScreens.HomeScreen.name) {
//                    popUpTo(MedisyncScreens.SplashScreen.name) { inclusive = true }
//                }
//            }
        }
    }
}