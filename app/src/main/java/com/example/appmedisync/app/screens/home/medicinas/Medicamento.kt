package com.example.appmedisync.app.screens.home.medicinas

data class Medicamento(
    val nombre: String = "",
    val dosis: String = "",
    val frecuencia: Int = 1,
    val hora: String = "",
    val uid: String = "",
    val id: String = "" // ← importante
)
