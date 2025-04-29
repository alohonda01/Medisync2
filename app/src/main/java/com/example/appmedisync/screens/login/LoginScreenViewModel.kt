package com.example.appmedisync.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(email: String, password: String, home: ()-> Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d("Medisync","signInWithEmailAndPassword logueado!!")
                        home()
                    }else{
                        Log.d("Medisync","signInWithEmailAndPassword: ${task.result.toString()}!!")
                    }
                }
        }
        catch(ex: Exception){
            Log.d("Medisync","signInWithEmailAndPassword: ${ex.message}")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true // <- ¡Corregido!
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName, password)
                        home()
                    } else {
                        Log.e("Medisync", "Error al crear usuario: ${task.exception?.message}")
                    }
                    _loading.value = false
                }
                .addOnFailureListener { e ->
                    Log.e("Medisync", "Exception: ${e.message}")
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?, password: String) {
        val userId = auth.currentUser?.uid ?: run {
            Log.e("Medisync", "Usuario no autenticado")
            return
        }

        val userData = hashMapOf(
            "contraseña" to password,
            "email" to auth.currentUser?.email,
            "fecha_registro" to FieldValue.serverTimestamp(),
            "nombre" to (displayName ?: "Usuario"),
            "tipo_paciente_medico" to "1",
            "usuario_id" to userId
        )

        FirebaseFirestore.getInstance().collection("usuarios")
            .document(userId)
            .set(userData)
            .addOnSuccessListener {
                Log.d("Medisync", "Documento de usuario creado con ID: $userId")
            }
            .addOnFailureListener { e ->
                Log.e("Medisync", "Error al crear documento de usuario", e)
            }
    }

}




