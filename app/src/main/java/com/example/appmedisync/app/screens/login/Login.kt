package com.example.appmedisync.app.screens.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmedisync.R
import com.example.appmedisync.app.screens.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    val navController = rememberNavController()
    Login(navController = navController)
}

@Composable
fun Login(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        GoogleSignInUtils.doGoogleSignIn(
            context = context,
            scope = scope,
            launcher = null,
            login = {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                navController.navigate(Screens.ConfiguracionScreen.name)
            }
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_azul),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            if(showLoginForm.value){
                Text(
                    text = "Iniciar sesión",
                    modifier = Modifier.padding(vertical = 20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp)

                UserForm(isCreateAccount = false) { email, password ->
                    val auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            // 1) UID del usuario autenticado
                            val uid = result.user?.uid
                            if (uid == null) {
                                Toast.makeText(context, "Error interno: UID nulo", Toast.LENGTH_LONG).show()
                                return@addOnSuccessListener
                            }
                            // 2) Consultar Firestore si existe el perfil
                            db.collection("users")
                                .document(uid)
                                .get()
                                .addOnSuccessListener { doc ->
                                    val dest = if (doc.exists()) {
                                        // Perfil ya creado → Home
                                        navController.navigate(Screens.HomeScreen.name) {
                                            popUpTo(Screens.LoginScreen.name) { inclusive = true }
                                        }
                                    } else {
                                        // Sin perfil → Configuración
                                        navController.navigate(Screens.ConfiguracionScreen.name) {
                                            popUpTo(Screens.LoginScreen.name) { inclusive = true }
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        "Error al leer perfil: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            // Capturar error de usuario no encontrado
                            val errorCode = (exception as? FirebaseAuthException)?.errorCode
                            if (errorCode == "ERROR_USER_NOT_FOUND") {
                                Toast.makeText(
                                    context,
                                    "Usuario no encontrado. Regístrate primero.",
                                    Toast.LENGTH_LONG
                                ).show()
                                showLoginForm.value = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error al iniciar sesión: Crea una cuenta",
                                    Toast.LENGTH_LONG
                                ).show()
                                showLoginForm.value = false
                            }
                        }
                }

            }else{
                Text(text = "Crea una cuenta",
                    modifier = Modifier.padding(vertical = 20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp)
                UserForm(
                    isCreateAccount = true
                ){
                    email, password ->
                    Log.d("Medisync","Creando cuenta con $email y $password")
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(Screens.ConfiguracionScreen.name )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                val text1 =
                    if (showLoginForm.value) "¿No tienes cuenta?"
                    else "¿Ya tienes cuenta?"
                val text2 =
                    if (showLoginForm.value) "Registrate"
                    else "Inicia sesión"
                Text(text = text1,
                    fontWeight = FontWeight.SemiBold)
                Text(
                    text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = Color(0xFF0000EE)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            Divider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(40.dp))

            //Boton Google
            Button(onClick = {
                GoogleSignInUtils.doGoogleSignIn(
                    context = context,
                    scope   = scope,
                    launcher = launcher,
                    login   = {
                        // Este bloque se llama cuando GoogleSignInUtils ya ha
                        // completado el sign-in con FirebaseAuth.
                        val auth = FirebaseAuth.getInstance()
                        val user = auth.currentUser
                        if (user == null) {
                            Toast.makeText(context, "Error al autenticar con Google", Toast.LENGTH_LONG).show()
                            return@doGoogleSignIn
                        }

                        val uid = user.uid
                        db.collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                // Perfil ya existe → Home
                                navController.navigate(Screens.HomeScreen.name) {
                                    popUpTo(Screens.LoginScreen.name) { inclusive = true }
                                }
                            } else {
                                // No hay perfil → Configuración
                                navController.navigate(Screens.ConfiguracionScreen.name) {
                                    popUpTo(Screens.LoginScreen.name) { inclusive = true }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error al leer perfil: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )

            }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ), border = BorderStroke(1.dp, Color.Gray)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter            = painterResource(id = R.drawable.google),
                        contentDescription = "Icono Google",
                        modifier           = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Continuar con Google")
                }
            }


    }
}


}
@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String) -> Unit = {email, pwd ->}
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        EmailInput(
            emailState = email
        )

        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        val isEmailValid = email.value.matches(emailRegex)

        if (!isEmailValid && email.value.isNotEmpty()) {
            Text("Correo inválido",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 10.dp),
                textAlign = TextAlign.Start)
        }

        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )

        val isPasswordValid = password.value.length >= 6

        if (!isPasswordValid && password.value.isNotEmpty()) {
            Text("Contraseña debe tener al menos 6 caracteres",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 10.dp),
                textAlign = TextAlign.Start)
        }

        val valido = isEmailValid && isPasswordValid

        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Login",
            inputValido = valido
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: ()->Unit
) {
    Button(onClick = onClic,
        modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValido,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3674B5)
        )
    ) {
        Text(text = textId,
            modifier = Modifier
                .padding(5.dp))
    }
}

@Composable
fun PasswordInput(passwordState: MutableState<String>, labelId: String, passwordVisible: MutableState<Boolean>) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = {Text(text = labelId)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()){
                PasswordVisibleIcon(passwordVisible)
            }else{
                null
            }
        }
    )
}

@Composable
fun PasswordVisibleIcon(passwordVisible: MutableState<Boolean>) {
    val image =
        if (passwordVisible.value)
            Icons.Default.VisibilityOff
        else
            Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
        label = { Text (text = labelId)},
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}

