package com.example.appmedisync.app.screens.configuracion

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.app.screens.login.Usuario
import com.example.appmedisync.app.screens.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Preview(showBackground = true)
@Composable
fun ConfiguracionPreview(){
    val navController = rememberNavController()
    Configuracion(navController = navController)
}

@SuppressLint("RestrictedApi")
@Composable
fun Configuracion(navController: NavController){

    val auth = FirebaseAuth.getInstance()
    val db   = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var nombre by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var altura by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var genero by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize().padding(20.dp)
    )
    {

        item{
            Image(
                painter = painterResource(id = R.drawable.logo_azul),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
        }

        item{
            Text(text = "Configuración",
                modifier = Modifier.padding(vertical = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp)
        }

        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
            )
            {
                CampoTexto(valor = nombre,
                    onValueChange = { nombre = it },
                    etiqueta = "Nombre",
                    modifier = Modifier.weight(1f))

                CampoTexto(valor = apellidos,
                    onValueChange = { apellidos = it },
                    etiqueta = "Apellidos",
                    modifier = Modifier.weight(1f))
            }
        }

        item {
            CampoTexto(valor = edad,
                onValueChange = { edad = it },
                etiqueta = "Edad")
        }

        item {
            CampoTexto(valor = peso,
                onValueChange = { peso = it },
                etiqueta = "Peso")
        }

        item{
            CampoTexto(valor = altura,
                onValueChange = { altura = it },
                etiqueta = "Altura")
        }

        item{
            CampoTexto(valor = telefono,
                onValueChange = { telefono = it },
                etiqueta = "Telefono")
        }

        item{
            ComboBoxGenero(
                value = genero,
                onValueChange = { genero = it },
                etiqueta = "Género"
            )

        }

        item {
            Button(
                onClick = {

                    val uid   = auth.currentUser?.uid
                    val mail  = auth.currentUser?.email.orEmpty()
                    val edad   = edad.toIntOrNull()    ?: 0
                    val peso   = peso.toDoubleOrNull() ?: 0.0
                    val altura    = altura.toDoubleOrNull() ?: 0.0

                    val user = Usuario(
                        id = null,
                        userId = uid.toString(),
                        correo = mail,
                        nombre = nombre,
                        edad = edad,
                        peso = peso,
                        altura = altura,
                        telefono = telefono,
                        genero = genero
                    )

                    db.collection("users")
                        .document(uid.toString())
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screens.HomeScreen.name) {
                                popUpTo(Screens.ConfiguracionScreen.name) { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error al crear usuario: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier
                    .padding(top = 15.dp , bottom = 50.dp)
                    .padding(3.dp)
                    .fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3674B5)
                )
            ) {
                Text(text = "Siguiente", modifier = Modifier.padding(5.dp))
            }
        }


    }
}

@Composable
fun CampoTexto(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 8.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxGenero(
    value: String,
    onValueChange: (String) -> Unit,
    opciones: List<String> = listOf("Masculino", "Femenino"),
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(etiqueta) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(8.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { genero ->
                DropdownMenuItem(
                    text = { Text(genero) },
                    onClick = {
                        onValueChange(genero)
                        expanded = false
                    }
                )
            }
        }
    }
}


