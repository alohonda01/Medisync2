package com.example.appmedisync.screens.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmedisync.R
import com.example.appmedisync.navigation.MedisyncScreens
import com.example.appmedisync.signin.GoogleSignInUtils

@Composable
fun MedisyncLoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
){
    val viewModel: LoginScreenViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){
        GoogleSignInUtils.doGoogleSignIn(
            context = context,
            scope = scope,
            launcher = null,
            login = {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            }
        )
    }

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    Surface(modifier = Modifier
        .fillMaxSize()){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(top = 30.dp, start = 30.dp, end = 30.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.logo_azul),
                contentDescription = "Logo de la app",
                modifier = Modifier.size(150.dp)
            )
            if (showLoginForm.value){
                Text(text = "Inicia sesion", fontWeight = FontWeight.Bold, fontSize = 25.sp, modifier = Modifier.fillMaxWidth().padding(start = 13.dp, top = 20.dp, bottom = 20.dp), textAlign = TextAlign.Start)
                UserForm(
                    isCreateAccount = false
                ){
                    email, password ->
                    Log.d("Medisync","Logueando con $ $email y $password")
                    viewModel.signInWithEmailAndPassword(email, password){
                        //navController.navigate(MedisyncScreens.HomeScreen.name)
                        navController.navigate(MedisyncScreens.ConfigurarPerfil2.name)
                    }
                }
                Button(
                    onClick = {
                        GoogleSignInUtils.doGoogleSignIn(
                            context = context,
                            scope = scope,
                            launcher = launcher,
                            login = {
                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                navController.navigate(MedisyncScreens.ConfigurarPerfil.name)
                            }
                        )

                    },
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ){
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "icono de google",
                        modifier = Modifier.size(60.dp).padding(3.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text("Continuar con Google")
                }

            }else{
                Text(text = "Crea una cuenta", fontWeight = FontWeight.Bold, fontSize = 25.sp, modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 20.dp), textAlign = TextAlign.Start)
                UserForm(
                    isCreateAccount = true
                ){
                    email, password ->
                    Log.d("Medisync","Creando cuenta con $ $email y $password")
                    viewModel.createUserWithEmailAndPassword(email, password){
                        navController.navigate(MedisyncScreens.ConfigurarPerfil.name)
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
                    else "Inicia sesion"
                Text(text = text1)
                Text(text = text2,
                    modifier = Modifier
                        .clickable{ showLoginForm.value = !showLoginForm.value}
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
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
    val valido = remember(email.value, password.value){
        email.value.trim().isNotEmpty() &&
                password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        EmailInput(
            emailState = email
        )
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
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
            .padding(5.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValido
    ){
        Text(text = textId,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
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
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            }
            else null
        }
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if (passwordVisible.value)
            Icons.Default.VisibilityOff
        else
            Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }){
        Icon(
            imageVector = image,
            contentDescription = "")
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId : String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@Composable
fun InputField(valueState: MutableState<String>,
               labelId: String,
               isSingleLine: Boolean = true,
               keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
        label = {Text(text = labelId)},
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}