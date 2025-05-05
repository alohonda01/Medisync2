package com.example.appmedisync.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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

@Preview(showBackground = true)
@Composable
fun ConfigurarPerfil2Preview(){
    val navController = rememberNavController()
    ConfigurarPerfil2(navController = navController)
}

@Composable
fun ConfigurarPerfil2(navController: NavController){
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

        Text(
            text = "Mencionanos cuales medicamentos tomas",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 20.dp, bottom = 10.dp).fillMaxWidth()
            ,
            textAlign = TextAlign.Start
        )

        OutLineTextFieldSample2("Medicamentos","Ej. Paracetamol, Ibuprofeno, Metformina")

        Text(
            text = "Enfermedades que padeces o padeciste",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 20.dp, bottom = 10.dp).fillMaxWidth()
            ,
            textAlign = TextAlign.Start
        )

        OutLineTextFieldSample2("Enfermedades","Ej. Diabetes, Hipertensión, Neumonía")

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 30.dp)
        ){
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Ícono de inicio",
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            Text(
                text = "¿Por qué necesitamos saber esto?",
                modifier = Modifier
                    .padding(top = 20.dp)
            )
        }

        Button(onClick = {
            navController.navigate(MedisyncScreens.VincularReloj.name)
        },
            modifier = Modifier.padding(top = 20.dp).fillMaxWidth().padding(start = 30.dp, end = 30.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF578FCA))
        ){
            Text(text = "Siguiente")
        }

    }
}

@Composable
fun OutLineTextFieldSample2(labelText: String, placeholderText: String){
    var text by remember {mutableStateOf(TextFieldValue(""))}
    OutlinedTextField(
        value = text,
        placeholder = { Text(text = placeholderText)},
        label = { Text(text = labelText)},
        onValueChange = {
            text = it
        },
        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp),
    )
}
