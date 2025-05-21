package com.example.appmedisync.app.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(email: String, password: String, home: ()-> Unit)
    = viewModelScope.launch {
        try{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Medisync", "signInWithEmailAndPassword logueado!")
                        home()
                    }else{
                        Log.d("Medisync", "signInWithEmailAndPassword: ${task.result.toString()}")
                    }
                }
        }catch (ex: Exception){
            Log.d("Medisync", "signInWithEmailAndPassword ${ex.message}")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        home()
                    }
                    else {
                        Log.d("Medisync", "createUserWithEmailAndPassword: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }

//    private fun createUser(displayName: String?){
//        val userId = auth.currentUser?.uid
//        //val user = mutableMapOf<String, Any>()
//
//        val user = User(
//            userId = userId.toString(),
//            displayName = displayName.toString(),
//            avatarUrl = "",
//            quote = "Lo dificil ya paso",
//            profession = "Android Dev",
//            id = null
//        ).toMap()
//
//        FirebaseFirestore.getInstance().collection("users")
//            .add(user)
//            .addOnSuccessListener {
//                Log.d("Medisync", "Creado ${it.id}")
//            }.addOnFailureListener {
//                Log.d("Medisync", "Ocurrió un error ${it}")
//            }
//    }

}