package com.example.appmedisync.app.screens.home.medicinas

import android.util.Log
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.app.screens.navigation.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun Medicamentos(navController: NavController) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val medicamentos = remember { mutableStateListOf<Medicamento>() }

    // Escuchar en tiempo real los medicamentos
    LaunchedEffect(uid) {
        if (uid != null) {
            Firebase.firestore
                .collection("medicamentos")
                .whereEqualTo("uid", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("Firestore", "Error al leer medicamentos", error)
                        return@addSnapshotListener
                    }

                    val lista = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Medicamento::class.java)?.copy(id = doc.id)
                    } ?: emptyList()

                    medicamentos.clear()
                    medicamentos.addAll(lista)
                }
        }
    }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF578FCA)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Medicamentos",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 15.dp),
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Default.MedicalInformation,
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier.padding(end = 15.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F2))
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(10.dp)
        ) {
            // Mostrar todos los medicamentos
            items(medicamentos) { medicamento ->
                MedicamentoCard(
                    title = medicamento.nombre,
                    dosis = "Dosis: ${medicamento.dosis}",
                    frecuencia = "Frecuencia: ${medicamento.frecuencia} días",
                    hora = "Hora: ${medicamento.hora}",
                    icon = Icons.Default.MedicalInformation,
                    colorIcon = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    fontSizeUnit = 18.sp,
                    onClick = {}
                )
            }

            // Botón vacío al final
            item {
                CardVacia(
                    value = "Agrega un medicamento",
                    icon = Icons.Default.Add,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp),
                    colorText = Color.Gray,
                    onClick = {
                        println("CARD PRESIONADA")
                        mostrarDialogo = true
                    }
                )
            }
        }

        if (mostrarDialogo) {
            AgregarMedicamentoDialog(onDismiss = { mostrarDialogo = false })
        }
    }
}



@Composable
fun MedicamentoCard(
    title: String,
    dosis: String,
    frecuencia : String,
    hora : String,
    icon: ImageVector,
    colorIcon: Color,
    modifier: Modifier = Modifier,
    fontSizeUnit: TextUnit,
    cardWidth: Dp = 160.dp,
    cardHeight: Dp = 180.dp,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA1E3F9))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
            }
        }


        Card(
            modifier = modifier
                .width(cardWidth)
                .height(cardHeight)
                .clickable { onClick() },
                //.padding(start = 15.dp , top = 10.dp, end = 15.dp),

            shape = RoundedCornerShape(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = dosis,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = fontSizeUnit,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Text(
                    text = frecuencia,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = fontSizeUnit,
                    color = Color.Black
                )

                Text(
                    text = hora,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = fontSizeUnit,
                    color = Color.Black
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    //BOTON ELIMINAR
                    Button(
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA1E3F9),
                            contentColor = Color.Black,
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier.padding(top = 15.dp, end = 10.dp)
                    )
                    {
                        Image(
                            painter = rememberVectorPainter(Icons.Default.Delete),
                            contentDescription = "Boton de eliminar medicamento",
                            modifier = Modifier
                                .size(20.dp)

                        )
                    }

                    //BOTON MODIFICAR
                    Button(
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA1E3F9),
                            contentColor = Color.Black,
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier.padding(top = 15.dp, end = 10.dp)
                    )
                    {
                        Image(
                            painter = rememberVectorPainter(Icons.Default.ModeEdit),
                            contentDescription = "Boton de modificar medicamento",
                            modifier = Modifier
                                .size(20.dp)

                        )
                    }


                }
            }
        }
    }
}

@Composable
fun CardVacia(
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    colorText : Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFA1E3F9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberVectorPainter(icon),
                    contentDescription = "icono más",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}