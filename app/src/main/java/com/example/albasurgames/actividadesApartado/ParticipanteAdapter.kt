package com.example.albasurgames.actividadesApartado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.albasurgames.R

class ParticipanteAdapter(
    private val lista: List<Participante>) : RecyclerView.Adapter<ParticipanteAdapter.ParticipanteViewHolder>()
{

    class ParticipanteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvUserName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipanteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ParticipanteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipanteViewHolder, position: Int) {
        val participante = lista[position]
        holder.nombre.text = participante.nombre ?: "Desconocido"
    }

    override fun getItemCount(): Int = lista.size
}