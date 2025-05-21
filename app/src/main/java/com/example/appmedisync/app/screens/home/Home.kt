package com.example.appmedisync.app.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun HomePreview(){
    val navController = rememberNavController()
    Home(navController = navController)
}

@Composable
fun Home(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Home Screen")
    }
}