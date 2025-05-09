package com.example.appmedisync.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.Usuarios.UsuarioViewModel
import com.example.appmedisync.navigation.MedisyncScreens

@Preview(showBackground = true)
@Composable
fun ConfigurarPerfil2Preview(){
    //Para navegar entre screens
    val navController = rememberNavController()
    ConfigurarPerfil2(navController = navController)


}

@Composable
fun ConfigurarPerfil2(navController: NavController){
    //Para recibir los datos del usuario y mandarlos
    val viewModel: UsuarioViewModel = viewModel()
    val usuario by viewModel.usuario.collectAsState()
    var showWhyWeNeedThis by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.logo_azul),
                contentDescription = "Logo de la app",
                modifier = Modifier.size(200.dp)
            )
        }

        item {
            Text(
                text = "Configuremos tu perfil",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = "Mencionanos cuales medicamentos tomas",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 20.dp, bottom = 10.dp),
                textAlign = TextAlign.Start
            )
        }

//        item {
//            OutLineTextFieldSample2(
//                "Medicamentos",
//                "Ej. Paracetamol, Ibuprofeno, Metformina",
//            )
//        }

        item{
            UsuarioTextField(
                labelText = "Medicamentos",
                placeholderText = "Ej. Paracetamol, Ibuprofeno, Metformina",
                value = usuario.medicamentos,
                onValueChange = {viewModel.actualizarMedicamentos(it)}
            )
        }

        item {
            Text(
                text = "Enfermedades que padeces o padeciste",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 20.dp, bottom = 10.dp),
                textAlign = TextAlign.Start
            )
        }

//        item {
//            OutLineTextFieldSample2("Enfermedades", "Ej. Diabetes, Hipertensión, Neumonía")
//        }

        item{
            UsuarioTextField(
                labelText = "Enfermedades",
                placeholderText = "Ej. Diabetes, Hipertensión, Neumonía",
                value = usuario.enfermedades,
                onValueChange = {viewModel.actualizarEnfermedades(it)}
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp)
                    .clickable { showWhyWeNeedThis = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Informacion",
                    modifier = Modifier.padding(top = 20.dp),
                    tint = Color(0xFF578FCA)
                )
                Text(
                    text = "¿Por qué necesitamos saber esto?",
                    modifier = Modifier.padding(top = 20.dp),
                    color = Color(0xFF578FCA),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }

        item {
            Button(
                onClick = {
                    navController.navigate(MedisyncScreens.VincularReloj.name)
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF578FCA))
            ) {
                Text(text = "Siguiente")
            }
        }
    }

    // Coloca esto al final de tu LazyColumn, antes del cierre del composable
    if (showWhyWeNeedThis) {
        AlertDialog(
            onDismissRequest = { showWhyWeNeedThis = false },
//            title = {
//                Text(
//                    text = "Por qué solicitamos esta información",
//                    style = MaterialTheme.typography.headlineSmall
//                )
//            },
            text = {
                Text(
                    text = "Necesitamos esta información para:\n\n" +
                            "• Personalizar tus recomendaciones de salud\n" +
                            "• Brindarte alertas y consejos personalizados\n" +
                            "• Mejorar continuamente nuestro servicio\n\n" +
                            "Tus datos están protegidos y solo se usarán para estos fines.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = { showWhyWeNeedThis = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF578FCA)
                    )
                ) {
                    Text("Entendido")
                }
            }
        )
    }

}

//@Composable
//fun OutLineTextFieldSample2(labelText: String, placeholderText: String){
//    var text by remember {mutableStateOf(TextFieldValue(""))}
//    OutlinedTextField(
//        value = text,
//        placeholder = { Text(text = placeholderText)},
//        label = { Text(text = labelText)},
//        onValueChange = {
//            text = it
//        },
//        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp),
//    )
//}


