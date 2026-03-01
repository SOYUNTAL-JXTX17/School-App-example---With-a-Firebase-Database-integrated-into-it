package com.example.miinstitutoapp.juegosApartado.juegos.memorizarColores

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.miinstitutoapp.R

class MensajesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvMensaje: TextView = view.findViewById(R.id.tvMensaje)
    private val llMensaje: LinearLayout = view.findViewById(R.id.llMensaje)

    fun render(mensaje: Mensaje) {
        tvMensaje.text = mensaje.contenido

        val color = when (mensaje.category) {
            MensajesCategory.Aviso -> R.color.Gold
            MensajesCategory.Exito -> R.color.LimeGreen
            MensajesCategory.Fracaso -> R.color.Red
        }

        llMensaje.background = ContextCompat.getDrawable(llMensaje.context, color)
    }
}