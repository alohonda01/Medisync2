package com.example.appmedisync.app.screens.home.medicinas

import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun AgregarMedicamentoDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var frecuencia by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            hora = String.format("%02d:%02d", hour, minute)
        },
        20, 30, true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Medicamento") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = dosis,
                    onValueChange = { dosis = it },
                    label = { Text("Dosis (mg)") }
                )
                OutlinedTextField(
                    value = frecuencia,
                    onValueChange = { frecuencia = it },
                    label = { Text("Frecuencia (días)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { timePickerDialog.show() }) {
                    Text(if (hora.isNotEmpty()) "Hora: $hora" else "Seleccionar hora")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val uid = FirebaseAuth.getInstance().currentUser?.uid

                if (uid == null) {
                    Toast.makeText(context, "No hay sesión iniciada", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val frecuenciaInt = frecuencia.toIntOrNull()
                if (frecuenciaInt == null || nombre.isBlank() || dosis.isBlank() || hora.isBlank()) {
                    Toast.makeText(context, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val datos = mapOf(
                    "nombre" to nombre,
                    "dosis" to dosis,
                    "frecuencia" to frecuenciaInt,
                    "hora" to hora,
                    "uid" to uid,
                    "fechaInicio" to Timestamp.now()
                )

                Firebase.firestore.collection("medicamentos")
                    .add(datos)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Medicamento guardado", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        Log.e("Firestore", "Error al guardar medicamento", e)
                    }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


