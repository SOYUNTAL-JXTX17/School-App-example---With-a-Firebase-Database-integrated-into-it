package com.example.miinstitutoapp.actividadesApartado

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miinstitutoapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.toString

class ParticipantesActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var listaParticipantes: RecyclerView

    private lateinit var tvNombreEvento: TextView
    private lateinit var lista: MutableList<Participante>
    private lateinit var adapter: ParticipanteAdapter

    private lateinit var database: DatabaseReference

    private lateinit var eventoId: String

    private var nombreEvento: String = "VACIO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participantes)
        initComponents()

        FirebaseDatabase.getInstance().getReference("eventos")
            .child(eventoId)
            .child("nombre")
            .get()
            .addOnSuccessListener {
                nombreEvento = it.value.toString()
                tvNombreEvento.text = nombreEvento // Agregar nombre del evento a la TextView
            }



        adapter = ParticipanteAdapter(lista)
        listaParticipantes.layoutManager = LinearLayoutManager(this)
        listaParticipantes.adapter = adapter

        cargarParticipantes()

        backButton.setOnClickListener { finish() }
    }

    private fun initComponents()
    {
        listaParticipantes = findViewById(R.id.listaParticipantes)
        backButton = findViewById(R.id.backButton)
        tvNombreEvento = findViewById(R.id.tvNombreEvento)
        lista = mutableListOf()
        eventoId = intent.getStringExtra("eventoId") ?: ""
        if (eventoId.isEmpty()) {
            Toast.makeText(this, "Evento no encontrado", Toast.LENGTH_SHORT).show()
        }
        database = FirebaseDatabase.getInstance().reference
            .child("eventos")
            .child(eventoId)
            .child("participantes")
    }

    private fun cargarParticipantes() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                lista.clear()

                for (participanteSnapshot in snapshot.children) {
                    val participante = participanteSnapshot.getValue(String::class.java)
                    if (participante != null) {
                        lista.add(Participante(participante))
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ParticipantesActivity,
                    "Error al cargar participantes: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}