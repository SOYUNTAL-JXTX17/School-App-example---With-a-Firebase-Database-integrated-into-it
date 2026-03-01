package com.example.miinstitutoapp.actividadesApartado

data class Evento2(
    var nombre: String? = null,
    var descripcion: String? = null,
    var portada: String? = null,
    var participantes: Map<String, String>? = null,
    var id: String? = null
)