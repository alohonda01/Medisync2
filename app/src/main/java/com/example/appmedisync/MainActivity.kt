package com.example.appmedisync

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmedisync.app.screens.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    // 5 minutos en milisegundos
    private val inactivityTimeout = 5 * 60 * 1000L
    //private val inactivityTimeout = 60 * 1000L

    private lateinit var handler: Handler
    private val logoutRunnable = Runnable {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handler = Handler(Looper.getMainLooper())
        startInactivityTimer()

        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    MedisyncApp()
                }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        restartInactivityTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(logoutRunnable)
    }

    private fun startInactivityTimer() {
        handler.postDelayed(logoutRunnable, inactivityTimeout)
    }

    private fun restartInactivityTimer() {
        handler.removeCallbacks(logoutRunnable)
        handler.postDelayed(logoutRunnable, inactivityTimeout)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedisyncApp(){
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(top=46.dp),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Navigation()
        }
    }
}
