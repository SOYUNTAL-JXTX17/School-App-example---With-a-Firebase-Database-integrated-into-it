package com.example.miinstitutoapp.juegosApartado.juegos.memorizarColores

sealed class MensajesCategory {
    object Aviso : MensajesCategory()
    object Exito: MensajesCategory()
    object Fracaso: MensajesCategory()
}