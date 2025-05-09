package com.example.appmedisync.signin

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.appmedisync.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleSignInUtils {
    companion object {
        private val db = FirebaseFirestore.getInstance()

        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            login: () -> Unit
        ) {
            val credentialManager = androidx.credentials.CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()

            scope.launch {
                try {
                    val result = credentialManager.getCredential(context, request)
                    when(result.credential) {
                        is CustomCredential -> {
                            if(result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
                                val authResult = Firebase.auth.signInWithCredential(authCredential).await()

                                authResult.user?.let { user ->
                                    if(user.isAnonymous.not()) {
                                        // Guardar datos del usuario en Firestore
                                        saveGoogleUserData(
                                            userId = user.uid,
                                            email = user.email ?: "",
                                            displayName = user.displayName ?: "",
                                            //photoUrl = user.photoUrl?.toString()
                                        )
                                        login.invoke()
                                    }
                                }
                            }
                        }
                        else -> {
                            Log.w("GoogleSignIn", "Tipo de credencial no soportado")
                        }
                    }
                } catch (e: NoCredentialException) {
                    launcher?.launch(getIntent())
                } catch (e: GetCredentialException) {
                    Log.e("GoogleSignIn", "Error al obtener credencial", e)
                } catch (e: Exception) {
                    Log.e("GoogleSignIn", "Error general", e)
                }
            }
        }

        private suspend fun saveGoogleUserData(
            userId: String,
            email: String,
            displayName: String,
        ) {
            try {
                val (nombre) = parseDisplayName(displayName)

                val userData = hashMapOf(
                    "email" to email,
                    "nombre" to nombre,
                    "fecha_registro" to FieldValue.serverTimestamp(),
                    "usuario_id" to userId,
                )

                db.collection("usuarios")
                    .document(userId)
                    .set(userData)
                    .await()

                Log.d("GoogleSignIn", "Datos del usuario guardados en Firestore")
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Error al guardar datos del usuario", e)
            }
        }

        private fun parseDisplayName(displayName: String): Pair<String, String> {
            return if (displayName.contains(" ")) {
                val parts = displayName.split(" ")
                val firstName = parts.first()
                val lastName = parts.drop(1).joinToString(" ")
                firstName to lastName
            } else {
                displayName to ""
            }
        }

        fun getIntent(): Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }

        fun getCredentialOptions(context: Context): CredentialOption {
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()
        }
    }
}