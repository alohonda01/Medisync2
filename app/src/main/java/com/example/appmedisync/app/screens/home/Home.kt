package com.example.appmedisync.app.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.app.screens.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomePreview(){
    val navController = rememberNavController()
    Home(navController = navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavController ){
    val currentDate = remember { LocalDate.now() }
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale("es")))
    val auth = FirebaseAuth.getInstance()
    val db   = FirebaseFirestore.getInstance()

    // Estado local para el nombre
    var userName by remember { mutableStateOf<String>("") }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            try {
                // 1) Obtén el documento
                val snapshot = db.collection("users")
                    .document(uid)
                    .get()
                    .await()

                // 2) Extrae el nombre
                userName = snapshot.getString("nombre")
                    ?: snapshot.getString("userId")
                            ?: "Usuario"
            } catch (e: Exception) {
                Toast.makeText(context, "Error cargando perfil", Toast.LENGTH_SHORT).show()
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF578FCA)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Hola, $userName",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(60.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp) // Borde interno opcional
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.astronaut),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape), // Esto hace la imagen circular
                        contentScale = ContentScale.Crop // Asegura que la imagen llene el círculo
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .background(Color(0xFFF2F2F2))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            item{
                WeekSelector(modifier = Modifier.padding(innerPadding))
            }

            item{
                HealthMetricsDashboard(navController)
            }
        }
    }
}

@Composable
fun HealthMetricsDashboard(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(16.dp)
    ) {

        //FILA 1
        HealthMetricCard(
            title = "Medicamentos",
            value = "Agregar o quitar medicamentos",
            icon = Icons.Default.MedicalInformation,
            color = Color(0xFF5E72E4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp),
            fontSizeUnit = 18.sp,
            colorText = Color.Gray,
            onClick = {
                // Navegar a pantalla de detalle de medicamentos
                navController.navigate(Screens.MedicamentosScreen.name)
            }
        )

        //FILA 1
        HealthMetricCard(
            title = "Reportes",
            value = "Ver o escribir reporte",
            icon = Icons.Default.Book,
            color = Color(0xFF5E72E4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp),
            fontSizeUnit = 18.sp,
            colorText = Color.Gray,
            onClick = {
                // Navegar a pantalla de detalle de reporte
                navController.navigate(Screens.ReportesScreen.name)
            }
        )
    }
}


@Composable
fun HealthMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    fontSizeUnit: TextUnit,
    colorText : Color,
    cardWidth: Dp = 160.dp,
    cardHeight: Dp = 120.dp,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .width(cardWidth)
            .height(cardHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp),
                //fontSize = 40.sp
                fontSize = fontSizeUnit,
                color = colorText
            )
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekSelector(
    modifier: Modifier = Modifier
) {
    var currentWeekStart by remember { mutableStateOf(
        LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    )}

    val daysOfWeek = remember { listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Controles de navegación
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
            // Días de la semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                daysOfWeek.forEachIndexed { index, dayName ->
                    val dayDate = currentWeekStart.plusDays(index.toLong())
                    val isToday = dayDate == LocalDate.now()

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Manejar selección de día */ }
                            .padding(top = 50.dp)
                            .background(
                                if (isToday) Color(0xFF578FCA) else Color.Transparent,
                                CircleShape
                            )
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dayName,
                            color = if (isToday) Color.White else Color.Gray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = dayDate.dayOfMonth.toString(),
                            color = if (isToday) Color.White else MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
