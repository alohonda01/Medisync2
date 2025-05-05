package com.example.appmedisync.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.navigation.MedisyncScreens

@Preview
@Composable
fun ConfigurarPerfilPreview(){
    val navController = rememberNavController()
    ConfigurarPerfil(navController = navController)
}

@Composable
fun ConfigurarPerfil(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Image(
            painter = painterResource(id = R.drawable.logo_azul),
            contentDescription = "Logo de la app",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "Configuremos tu perfil",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutLineTextFieldSample("Nombre(s)")
            OutLineTextFieldSample("Apellidos")
            OutLineTextFieldSample("Edad")
            OutLineTextFieldSample("Peso")
            OutLineTextFieldSample("Altura")
            OutLineTextFieldSample("Genero")
            OutLineTextFieldSample("Numero de Telefono")

            Button(onClick = {
                navController.navigate(MedisyncScreens.ConfigurarPerfil2.name)
            },
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth().padding(start = 30.dp, end = 30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF578FCA))
            ){
                Text(text = "Siguiente")
            }
        }

    }
}

@Composable
fun OutLineTextFieldSample(labelText: String){
    var text by remember {mutableStateOf(TextFieldValue(""))}
    OutlinedTextField(
        value = text,
        label = { Text(text = labelText)},
        onValueChange = {
            text = it
        },
        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp),
    )
}