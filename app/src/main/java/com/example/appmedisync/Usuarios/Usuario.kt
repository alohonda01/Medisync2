package com.example.appmedisync.Usuarios

data class Usuario(

    //DATOS POR PARTE DEL FORMULARIO
    val nombre: String = "",
    val apellidos: String = "",
    val edad: Int = 0,
    val peso: Double = 0.0,
    val altura: Double = 0.0,
    val genero: String = "",
    val numeroTelefono: String = "",
    val medicamentos: String = "",
    val enfermedades: String = ""

    //DATOS POR PARTE DEL RELOJ
)
