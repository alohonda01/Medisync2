package com.example.appmedisync.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.navigation.MedisyncScreens

@Preview(showBackground = true)
@Composable
fun VincularRelojPreview(){
    val navController = rememberNavController()
    VincularReloj(navController = navController)
}

@Composable
fun VincularReloj(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "Dispositivos",
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 30.dp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3674B5)
        )
        Image(
            painter = painterResource(id = R.drawable.reloj2),
            contentDescription = "Logo de la app",
            modifier = Modifier.size(200.dp).padding(top = 30.dp)
        )
        Text(
            text = "Dispositivos conectados",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 30.dp, top = 30.dp).fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )
        Card(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(2.dp, Color.Black)
        ){
            Text(
                text = "No hay dispositivos conectados",
                modifier = Modifier.padding(40.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Button(onClick = {
            navController.navigate(MedisyncScreens.ConfigurarPerfil2.name)
        },
            modifier = Modifier.padding(top = 20.dp).fillMaxWidth().padding(start = 30.dp, end = 30.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF578FCA))
        ){
            Text(text = "Conectar", color = Color.White)

        }

        Button(onClick = {
            navController.navigate(MedisyncScreens.ConfigurarPerfil2.name)
        },
            modifier = Modifier.padding(top = 20.dp).fillMaxWidth().padding(start = 30.dp, end = 30.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF578FCA))
        ){
            Text(text = "Omitir", color = Color.White)

        }


    }
}