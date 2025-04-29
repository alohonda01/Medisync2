package com.example.appmedisync.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Preview
@Composable
fun HomePreview(){
    val navController = rememberNavController()
    Home(navController = navController)
}

@Composable
fun Home(navController: NavController){
    //Text(text = "Estamos en Home!!")
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



        OutLineTextFieldSample("Nombre(s)")
        OutLineTextFieldSample("Apellidos")
        OutLineTextFieldSample("Edad")
        OutLineTextFieldSample("Peso")
        OutLineTextFieldSample("Altura")
        OutLineTextFieldSample("Genero")

        Button(onClick = {

        }, modifier = Modifier.padding(top = 20.dp)
        ){
            Text(text = "Siguiente")
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
        }
    )
}
