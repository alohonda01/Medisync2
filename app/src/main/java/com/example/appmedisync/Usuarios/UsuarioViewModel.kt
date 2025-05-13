package com.example.appmedisync.Usuarios

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsuarioViewModel : ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _usuario = MutableStateFlow(Usuario())
    val usuario: StateFlow<Usuario> = _usuario.asStateFlow()

    fun actualizarNombre(nombre: String) {
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(nombre = nombre))
            Log.d("UsuarioViewModel", "Nombre actualizado: $nombre")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    fun actualizarApellidos(apellidos: String){
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(apellidos = apellidos))
            Log.d("UsuarioViewModel", "Apellidos actualizados: $apellidos")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    fun actualizarEdad(edad: String) {
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(edad = edad.toIntOrNull() ?: 0))
            Log.d("UsuarioViewModel", "Edad actualizada: $edad")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    fun actualizarPeso(peso: String) {
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(peso = peso.toDoubleOrNull() ?: 0.0))
            Log.d("UsuarioViewModel", "Peso actualizado: $peso")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    fun actualizarAltura(altura: String) {
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(altura = altura.toDoubleOrNull() ?: 0.0))
            Log.d("UsuarioViewModel", "Altura actualizada: $altura")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    //POSIBLEMENTE CAMBIAR
//    fun actualizarGenero(genero: String){
//        viewModelScope.launch {
//            _usuario.emit(_usuario.value.copy(genero = genero))
//        }
//    }
    fun actualizarGenero(genero: String) {
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(genero = genero))
            Log.d("UsuarioViewModel", "Genero actualizado: $genero")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    //POSIBLEMENTE CAMBIAR
//    fun actualizarNumeroTelefono(numeroTelefono : String){
//        viewModelScope.launch {
//            _usuario.emit(_usuario.value.copy(numeroTelefono = numeroTelefono))
//            Log.d("UsuarioViewModel", "Numero de telefono actualizado: $numeroTelefono")
//            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
//        }
//    }

    fun actualizarNumeroTelefono(numero: String) {
        // Solo actualiza si son solo dígitos y no excede 10 caracteres
        if (numero.all { it.isDigit() } && numero.length <= 10) {
            viewModelScope.launch {
                _usuario.emit(_usuario.value.copy(numeroTelefono = numero))
            }
        }
    }

    //POSIBLEMENTE CAMBIAR
    fun actualizarMedicamentos(medicamentos: String){
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(medicamentos = medicamentos))
            Log.d("UsuarioViewModel", "Medicamentos actualizados: $medicamentos")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    //POSIBLEMENTE CAMBIAR
    fun actualizarEnfermedades(enfermedades: String){
        viewModelScope.launch {
            _usuario.emit(_usuario.value.copy(enfermedades = enfermedades))
                Log.d("UsuarioViewModel", "Enfermedades actualizadas: $enfermedades")
            Log.d("UsuarioViewModel", "Estado completo: ${_usuario.value}")
        }
    }

    fun camposEstanCompletos(): Boolean {
        return usuario.value.nombre.isNotBlank() &&
                usuario.value.apellidos.isNotBlank() &&
                usuario.value.edad > 0 &&
                usuario.value.peso > 0 &&
                usuario.value.altura > 0 &&
                usuario.value.genero.isNotBlank() &&
                usuario.value.numeroTelefono.length == 10 && usuario.value.numeroTelefono.all { it.isDigit() }
    }

    suspend fun guardarUsuarioEnFirebase(): Boolean {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")

            val usuarioData = hashMapOf(
                "nombre" to usuario.value.nombre,
                "apellidos" to usuario.value.apellidos,
                "edad" to usuario.value.edad,
                "altura" to usuario.value.altura,
                "peso" to usuario.value.peso,
                "genero" to usuario.value.genero,
                "numero_telefono" to usuario.value.numeroTelefono,
                "enfermedades" to usuario.value.enfermedades,
                "medicamentos" to usuario.value.medicamentos
            )

            db.collection("usuarios")
                .document(userId)
                .set(usuarioData)
                .await()

            true
        } catch (e: Exception) {
            Log.e("Firebase", "Error al guardar usuario", e)
            false
        }
    }

    suspend fun guardarEnfermedadesyMedicamentosFirebase(): Boolean {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val usuarioData = hashMapOf(
                "enfermedades" to usuario.value.enfermedades,
                "medicamentos" to usuario.value.medicamentos
            )
            true
        } catch (e: Exception) {
            Log.e("Firebase", "Error al guardar usuario", e)
            false
        }
    }

    fun cargarDatosUsuario() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                val document = db.collection("usuarios").document(userId).get().await()
                if (document.exists()) {
                    _usuario.emit(Usuario(
                        nombre = document.getString("nombre") ?: "",
                        apellidos = document.getString("apellidos") ?: "",
                        edad = document.getLong("edad")?.toInt() ?: 0,
                        altura = document.getDouble("altura") ?: 0.0,
                        peso = document.getDouble("peso") ?: 0.0,
                        genero = document.getString("genero") ?: "",
                        numeroTelefono = document.getString("numero_telefono") ?: ""
                    ))
                }
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error al cargar datos", e)
            }
        }
    }

}