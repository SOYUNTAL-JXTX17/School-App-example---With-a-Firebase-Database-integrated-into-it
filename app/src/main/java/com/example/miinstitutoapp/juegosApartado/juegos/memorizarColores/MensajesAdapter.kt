package com.example.miinstitutoapp.juegosApartado.juegos.memorizarColores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miinstitutoapp.R

class MensajesAdapter(var tasks: List<Mensaje>) :
    RecyclerView.Adapter<MensajesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_juegomemoria_mensaje, parent, false)
        return MensajesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensajesViewHolder, position: Int) {
        holder.render(tasks[position])
    }

    override fun getItemCount() = tasks.size

}