package com.example.miinstitutoapp.actividadesApartado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.miinstitutoapp.R

class EventoAdapter(
    private val lista: List<Evento2>,
    private val miUid: String?,
    private val onUnirseClick: (Evento2) -> Unit) :
    RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tituloEvento)
        val descripcion: TextView = view.findViewById(R.id.descripcionEvento)

        val portada: ImageView = view.findViewById(R.id.imgEvento)

        val botonUnirse: CardView = view.findViewById(R.id.botonUnirse)

        val tvBotonUnirse: TextView = view.findViewById(R.id.tvBotonUnirse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cajaevento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = lista[position]
        holder.nombre.text = evento.nombre
        holder.descripcion.text = evento.descripcion

        val cardWidth = 600

        holder.itemView.layoutParams.width = cardWidth

        // Comprobar si el usuario actual está unido al evento
        if (miUid != null && evento.participantes?.containsKey(miUid) == true) {
            holder.botonUnirse.setCardBackgroundColor(holder.itemView.context.getColor(R.color.red))
            holder.tvBotonUnirse.text = "SALIRSE"
        } else {
            // Si no está unido, cambiamos el color del botón a verde y el texto a "UNIRSE"
            holder.botonUnirse.setCardBackgroundColor(holder.itemView.context.getColor(R.color.green))
            holder.tvBotonUnirse.text = "UNIRSE"
        }

        Glide.with(holder.itemView.context)
            .load(evento.portada)
            .placeholder(R.drawable.imagen_carga) // opcional, mientras carga
            .error(R.drawable.imagen_error)       // opcional, si falla
            .into(holder.portada)

        holder.botonUnirse.setOnClickListener {
            onUnirseClick(evento)
        }
    }

    override fun getItemCount(): Int = lista.size
}