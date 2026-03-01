package com.example.albasurgames.mensajesApartado

data class Message(
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
