package com.example.albasurgames.actividadesApartado

data class Evento(
    var descripcion: String? = null,
    var fecha: String? = null,
    var nombre: String? = null,
    var portada: String? = null,
    var numeroparticipantes: Int? = null,
    var numeroparticipantesmax: Int? = null,
    var participantes: Map<String, String>? = null,
    var creador: String? = null,
    var id: String? = null
)