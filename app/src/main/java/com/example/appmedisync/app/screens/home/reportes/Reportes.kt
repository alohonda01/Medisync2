package com.example.appmedisync.app.screens.home.reportes

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.app.screens.home.reportes.PdfGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Preview(showBackground = true)
@Composable
fun ReportesPreview(){
    val navController = rememberNavController()
    Reportes(navController = navController)
}
data class ChatMessage(val role: String, val content: String)

@Composable
fun Reportes(navController: NavController){
    var message by remember { mutableStateOf("") }
    var chatHistory by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    val scrollState = rememberLazyListState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Historial de chat con scroll
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = scrollState
        ) {
            items(chatHistory.size) { index ->
                val chatMessage = chatHistory[index]
                MessageBubble(chatMessage = chatMessage)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de entrada y botón Enviar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Escribe tu mensaje") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (message.isNotBlank()) {
                        chatHistory = chatHistory + ChatMessage("user", message)
                        val currentMessage = message
                        message = ""
                        consultarMedicoVirtual(currentMessage) { respuesta ->
                            chatHistory = chatHistory + ChatMessage("assistant", respuesta)
                        }
                    }
                }
            ) {
                Text("Enviar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botones de exportar PDF y guardar en Firestore
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val texto = chatHistory.lastOrNull()?.content ?: "Sin contenido"
                PdfGenerator.createPdf(context, texto)
            }) {
                Text("Exportar PDF")
            }

            Button(onClick = {
                val texto = chatHistory.lastOrNull()?.content ?: "Sin contenido"
                // FirestoreManager.guardarConsulta("usuario_demo", texto)
            }) {
                Text("Guardar en Firestore")
            }
        }
    }
}

@Composable
fun MessageBubble(chatMessage: ChatMessage) {
    val isUser = chatMessage.role == "user"
    val backgroundColor = if (isUser) Color(0xFFE8F5E9) else Color(0xFFB3E5FC)
    val horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = chatMessage.content,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
                textAlign = if (isUser) TextAlign.End else TextAlign.Start
            )
        }
    }
}

fun consultarMedicoVirtual(
    preguntaUsuario: String,
    onResponse: (String) -> Unit
) {
    val messages = listOf(
        Message(
            role = "system",
            content = MedicalPrompt.SYSTEM_PROMPT
        ),
        Message(
            role = "user",
            content = preguntaUsuario
        )
    )

    val request = DeepSeekRequest(messages = messages)

    ApiConfig.apiService.generateResponse(request).enqueue(
        object : Callback<DeepSeekResponse> {
            override fun onResponse(
                call: Call<DeepSeekResponse>,
                response: Response<DeepSeekResponse>
            ) {
                if (response.isSuccessful) {
                    val respuesta = response.body()?.choices?.firstOrNull()?.message?.content
                    onResponse(respuesta ?: "No se recibió respuesta válida")
                } else {
                    onResponse("Error del servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DeepSeekResponse>, t: Throwable) {
                onResponse("Error de conexión: ${t.message ?: "Desconocido"}")
            }
        }
    )
}

// Si necesitas funciones adicionales para logging
private fun mostrarRespuesta(texto: String) {
    Log.d("MEDISYNC_RESPONSE", texto)
}

private fun mostrarError(mensaje: String) {
    Log.e("MEDISYNC_ERROR", mensaje)
}