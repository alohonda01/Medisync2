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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.Usuarios.UsuarioViewModel
import com.example.appmedisync.navigation.MedisyncScreens
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun ConfigurarPerfilPreview(){
    val navController = rememberNavController()
    ConfigurarPerfil(navController = navController)
}

@Composable
fun ConfigurarPerfil(navController: NavController){

    val viewModel: UsuarioViewModel = viewModel()
    val usuario by viewModel.usuario.collectAsState()
    val camposCompletos = remember(usuario) {
        usuario.nombre.isNotBlank() &&
                usuario.apellidos.isNotBlank() &&
                usuario.edad > 0 &&
                usuario.peso > 0.0 &&
                usuario.altura > 0.0 &&
                usuario.genero.isNotBlank() &&
                usuario.numeroTelefono.isNotBlank()
    }

    var mostrarErrores by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    // Dialogo para errores
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text("Ocurrió un error al guardar los datos. Intenta nuevamente.") },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //OutLineTextFieldSample("Nombre(s)")
                UsuarioTextField(
                    labelText = "Nombre(s)",
                    placeholderText = "Nombres(s)",
                    value = usuario.nombre,
                    onValueChange = {viewModel.actualizarNombre(it)},
                    isError = mostrarErrores && usuario.nombre.isBlank(),
                    errorMessage = if (mostrarErrores && usuario.nombre.isBlank()) "Campo requerido" else null
                )

                // OutLineTextFieldSample("Apellidos")
                UsuarioTextField(
                    labelText = "Apellidos",
                    placeholderText = "Apellidos",
                    value = usuario.apellidos,
                    onValueChange = {viewModel.actualizarApellidos(it)},
                    isError = mostrarErrores && usuario.nombre.isBlank(),
                    errorMessage = if (mostrarErrores && usuario.nombre.isBlank()) "Campo requerido" else null
                )

                //OutLineTextFieldSample("Edad")
                UsuarioTextField(
                    labelText = "Edad",
                    placeholderText = "Ingrese su edad",
                    value = if (usuario.edad == 0) "" else usuario.edad.toString(),
                    onValueChange = { viewModel.actualizarEdad(it)},
                    keyboardType = KeyboardType.Number,
                    isError = mostrarErrores && usuario.nombre.isBlank(),
                    errorMessage = if (mostrarErrores && usuario.nombre.isBlank()) "Campo requerido" else null
                )

                //OutLineTextFieldSample("Peso")
                UsuarioTextField(
                    labelText = "Peso (kg)",
                    placeholderText = "Ej: 68.5",
                    value = if (usuario.peso == 0.0) "" else usuario.peso.toString(),
                    onValueChange = { viewModel.actualizarPeso(it) },
                    keyboardType = KeyboardType.Number,
                    isError = mostrarErrores && usuario.nombre.isBlank(),
                    errorMessage = if (mostrarErrores && usuario.nombre.isBlank()) "Campo requerido" else null
                )

                //OutLineTextFieldSample("Altura")
                UsuarioTextField(
                    labelText = "Altura (m)",
                    placeholderText = "Ej: 1.75",
                    value = if (usuario.altura == 0.0) "" else usuario.altura.toString(),
                    onValueChange = { viewModel.actualizarAltura(it) },
                    keyboardType = KeyboardType.Number,
                    isError = mostrarErrores && usuario.nombre.isBlank(),
                    errorMessage = if (mostrarErrores && usuario.nombre.isBlank()) "Campo requerido" else null
                )

                UsuarioTextField(
                    labelText = "Número de Teléfono",
                    placeholderText = "Ej: 8715065835",
                    value = usuario.numeroTelefono,
                    onValueChange = { viewModel.actualizarNumeroTelefono(it) },
                    keyboardType = KeyboardType.Number,
                    isError = mostrarErrores && (usuario.numeroTelefono.isBlank() || usuario.numeroTelefono.length != 10),
                    errorMessage = when {
                        mostrarErrores && usuario.numeroTelefono.isBlank() -> "Campo requerido"
                        mostrarErrores && usuario.numeroTelefono.length != 10 -> "Debe tener 10 dígitos"
                        else -> null
                    },
                    maxLength = 10,
                    filter = { it.all { char -> char.isDigit() } }
                )

                SelectorGenero(
                    generoActual = usuario.genero,
                    onGeneroSeleccionado = { viewModel.actualizarGenero(it) },
                )

                if (mostrarErrores && usuario.genero.isBlank()) {
                    Text(
                        text = "Seleccione un género",
                        color = Color.Red,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                }

                Button(
                    onClick = {
                        if (camposCompletos) {
                            isLoading = true
                            scope.launch {
                                val exito = viewModel.guardarUsuarioEnFirebase()
                                isLoading = false

                                if (exito) {
                                    navController.navigate(MedisyncScreens.ConfigurarPerfil2.name)
                                } else {
                                    showErrorDialog = true
                                }
                            }
                        } else {
                            mostrarErrores = true
                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        //.padding(horizontal = 30.dp),
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 150.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF578FCA),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    enabled = camposCompletos && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Siguiente",
                            color = if (camposCompletos) Color.White else Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun UsuarioTextField(
    labelText: String,
    placeholderText: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null,
    maxLength: Int? = null,
    filter: (String) -> Boolean = { true }
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            placeholder = { Text(text = placeholderText) },
            label = { Text(text = labelText) },
            onValueChange = { newText ->
                if (filter(newText)) {
                    if (maxLength == null || newText.length <= maxLength) {
                        onValueChange(newText)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            isError = isError,
            supportingText = {
                if (isError && errorMessage != null) {
                    Text(text = errorMessage, color = Color.Red)
                } else if (maxLength != null) {
                    Text(text = "${value.length}/$maxLength", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            visualTransformation = if (keyboardType == KeyboardType.NumberPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        )
    }
}

@Composable
fun SelectorGenero(
    generoActual: String,
    onGeneroSeleccionado: (String) -> Unit
) {
    val opcionesGenero = listOf("Hombre", "Mujer")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 15.dp)

    ) {
        Text(
            text = "Género",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            opcionesGenero.forEach { genero ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onGeneroSeleccionado(genero) }
                ) {
                    RadioButton(
                        selected = generoActual == genero,
                        onClick = { onGeneroSeleccionado(genero) }
                    )
                    Text(
                        text = genero,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}