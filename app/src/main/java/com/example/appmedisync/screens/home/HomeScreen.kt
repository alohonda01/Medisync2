package com.example.appmedisync.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.Usuarios.UsuarioViewModel
import com.example.appmedisync.navigation.MedisyncScreens
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: UsuarioViewModel = viewModel()) {
    val currentDate = remember { LocalDate.now() }
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale("es")))

    val usuario by viewModel.usuario.collectAsState()
    
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
                        text = "Hola, ${usuario.nombre}" ,
                        //text = "Hola, $userName",
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
                        painter = painterResource(id = R.drawable.prueba),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 3.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HealthMetricCard(
                title = "Heart Rate",
                value = "96 bpm",
                icon = Icons.Default.Favorite,
                color = Color(0xFFFF6B6B),
                fontSizeUnit = 28.sp,
                colorText = Color.Black,
                onClick = {
                    // Navegar a pantalla de detalle de ritmo cardíaco
                    //navController.navigate(MedisyncScreens.TransicionScreen.name)
                }
            )
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            HealthMetricCard(
                title = "Pasos",
                value = "7000",
                icon = Icons.Default.Coronavirus,
                color = Color(0xFF4ECDC4),
                fontSizeUnit = 28.sp,
                colorText = Color.Black,
                onClick = {
                    // Navegar a pantalla de detalle de ritmo cardíaco
                    navController.navigate(MedisyncScreens.Enfermedades.name)
                }
            )

        }


        //FILA 2
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
                HealthMetricCard(
                    title = "Sueño",
                    value = "8,542",
                    icon = Icons.Default.AccessTimeFilled,
                    color = Color(0xFF4ECDC4),
                    fontSizeUnit = 28.sp,
                    colorText = Color.Black,
                    onClick = {
                        // Navegar a pantalla de detalle de ritmo cardíaco
                        //navController.navigate(MedisyncScreens.TransicionScreen.name)
                    }
                )
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
                HealthMetricCard(
                    title = "Estrés",
                    value = "Nivel de estrés",
                    icon = Icons.Default.CrisisAlert,
                    color = Color(0xFF4ECDC4),
                    fontSizeUnit = 18.sp,
                    colorText = Color.Gray,
                    onClick = {
                        // Navegar a pantalla de detalle de ritmo cardíaco
                        navController.navigate(MedisyncScreens.Enfermedades.name)
                    }
                )

        }

        //FILA 3
        HealthMetricCard(
            title = "Enfermedades",
            value = "Tus enfermedades",
            icon = Icons.Default.Coronavirus,
            color = Color(0xFF4ECDC4),
            fontSizeUnit = 18.sp,
            colorText = Color.Gray,
            onClick = {
                // Navegar a pantalla de detalle de ritmo cardíaco
                navController.navigate(MedisyncScreens.Enfermedades.name)
            },
            modifier = Modifier
                .padding(top = 10.dp, start = 12.dp, end = 10.dp)
                .fillMaxSize()
        )

        //FILA 4
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
                // Navegar a pantalla de detalle de ritmo cardíaco
                navController.navigate(MedisyncScreens.Medicamentos.name)
            }
        )

        //FILA 5
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
                // Navegar a pantalla de detalle de ritmo cardíaco
                navController.navigate(MedisyncScreens.Reportes.name)
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