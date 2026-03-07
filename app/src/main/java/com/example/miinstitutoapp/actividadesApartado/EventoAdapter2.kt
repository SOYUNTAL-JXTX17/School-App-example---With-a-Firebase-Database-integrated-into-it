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

class EventoAdapter2(
    private val lista: List<Evento>,
    private val miUid: String?,
    private val onUnirseClick: (Evento) -> Unit,
    private val onBorrarClick: (Evento) -> Unit,
    private val onEditarClick: (Evento) -> Unit,
    private val onVerParticipantesClick: (Evento) -> Unit) : RecyclerView.Adapter<EventoAdapter2.EventoViewHolder2>()
{

    class EventoViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

        val nombre: TextView = view.findViewById(R.id.tituloEvento)
        val descripcion: TextView = view.findViewById(R.id.descripcionEvento)

        val portada: ImageView = view.findViewById(R.id.imgEvento)

        val fecha: TextView = view.findViewById(R.id.fechaEvento)

        val numeroparticipantes: TextView = view.findViewById(R.id.numpersonasEvento)

        val numeroparticipantesmax: TextView = view.findViewById(R.id.maxpersonasEvento)

        val botonUnirse: CardView = view.findViewById(R.id.botonUnirse)
        val tvBotonUnirse: TextView = view.findViewById(R.id.tvBotonUnirse)

        val borrarEvento: CardView = view.findViewById(R.id.borrarEvento)

        val editarEvento: CardView = view.findViewById(R.id.editarEvento)

        val participantesEvento: CardView = view.findViewById(R.id.participantesEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder2 {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cajaevento2, parent, false)
        return EventoViewHolder2(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder2, position: Int) {
        val evento = lista[position]
        holder.itemView.tag = evento.id
        holder.nombre.text = evento.nombre
        holder.descripcion.text = evento.descripcion
        holder.fecha.text = evento.fecha
        holder.numeroparticipantes.text = evento.numeroparticipantes.toString()
        holder.numeroparticipantesmax.text = evento.numeroparticipantesmax.toString()

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
            .placeholder(R.drawable.imagen_carga) // Imagen que se muestra mientras la imagen se carga
            .error(R.drawable.imagen_error)       // Imagen que se muestra si la imagen no se carga, falla o no se añade.
            .into(holder.portada)

        holder.botonUnirse.setOnClickListener {
            onUnirseClick(evento)
        }

        holder.borrarEvento.setOnClickListener {
            onBorrarClick(evento)
        }

        holder.editarEvento.setOnClickListener {
            onEditarClick(evento)
        }

        holder.participantesEvento.setOnClickListener {
            onVerParticipantesClick(evento)
        }
    }

    override fun getItemCount(): Int = lista.size
}