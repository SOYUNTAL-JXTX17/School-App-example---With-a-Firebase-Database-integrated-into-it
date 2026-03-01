package com.example.albasurgames.actividadesApartado
import android.R.attr.id
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albasurgames.R
import com.example.albasurgames.inicioApartado.InicioActivity
import com.example.albasurgames.juegosApartado.lista.ListaJuegosActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.text.clear

class ActividadesActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private var myName = "" // Variable que almacena el nombre del usuario con el que se ha iniciado sesion ahora
    private var tipoUsuario: String = "" // Variable que almacena el tipo del usuario con el que se ha iniciado sesion ahora
    private lateinit var contenedorEventos: RecyclerView
    private lateinit var listaEventos: MutableList<Evento>
    private lateinit var adapter: EventoAdapter2

    private lateinit var hacerEvento: CardView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)

        val jugarButton = findViewById<LinearLayout>(R.id.jugarButton)
        jugarButton.setOnClickListener { navigateJuegos() }

        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener { navigateInicio() }

        initComponents()

        val uid = auth.uid!! // Usuario con el que se ha iniciado sesion ahora

        // 1. Obtener el nombre del usuario
        FirebaseDatabase.getInstance().getReference("users")
            .child(uid)
            .child("name")
            .get()
            .addOnSuccessListener {
                myName = it.value.toString()
            }

        // 2. Obtener el tipo del usuario
        FirebaseDatabase.getInstance().getReference("users")
            .child(uid)
            .child("tipo")
            .get()
            .addOnSuccessListener {
                tipoUsuario = it.value.toString()
                // 2.1. Eliminar/Ocultar el boton de creaciñon de eventos, en caso de que el usuario sea un alumno
                if (tipoUsuario != "profesor") {
                    hacerEvento.setVisibility(GONE)
                }
            }

        hacerEvento.setOnClickListener {
            navigateHacerEvento()
        }

        contenedorEventos.layoutManager = LinearLayoutManager(this)
        contenedorEventos.adapter = adapter

        cargarEventos()
    }

    private fun initComponents() {
        contenedorEventos = findViewById(R.id.listaEventos)
        listaEventos = mutableListOf()
        adapter = EventoAdapter2(
            listaEventos,
            auth.uid,
            onUnirseClick = { evento ->
                val uid = auth.uid ?: return@EventoAdapter2
                val nombreUsuario = myName

                evento.id?.let { idEvento ->

                    val participantesRef = database.child(idEvento).child("participantes")

                    participantesRef.child(uid).get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            // Si ya existe, nos desapuntamos (funcionalidad SALIRSE)
                            participantesRef.child(uid).removeValue().addOnSuccessListener {
                                updateParticipantesCount(idEvento)
                                Toast.makeText(this, "Has salido del evento", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Añadimos al usuario al mapa de participantes
                            participantesRef.child(uid).setValue(nombreUsuario)
                                .addOnSuccessListener {
                                    updateParticipantesCount(idEvento)
                                    Toast.makeText(this, "¡Te has unido al evento!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error al unirse: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            },
            onBorrarClick = { evento ->
                if (evento.creador == myName) {
                    evento.id?.let { idSeguro ->
                        database.child(idSeguro).removeValue()
                    }
                }
                else
                {
                    Toast.makeText(this, "No eres el creador de este evento", Toast.LENGTH_SHORT)
                        .show(); return@EventoAdapter2
                }
            },
            onVerParticipantesClick = { evento ->
                val intent = Intent(this, ParticipantesActivity::class.java)
                intent.putExtra("eventoId", evento.id)
                startActivity(intent)
            }
        )
        database = FirebaseDatabase.getInstance().reference.child("eventos")
        hacerEvento = findViewById(R.id.hacerEvento)
    }

    private fun updateParticipantesCount(idEvento: String) {
        val eventoRef = database.child(idEvento)
        val participantesRef = eventoRef.child("participantes")
        participantesRef.get().addOnSuccessListener { participantesSnapshot ->
            val nuevoNumero = participantesSnapshot.childrenCount.toInt()
            eventoRef.child("numeroparticipantes").setValue(nuevoNumero)
        }
    }

    private fun cargarEventos() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listaEventos.clear()

                for (eventoSnapshot in snapshot.children) {
                    val evento = eventoSnapshot.getValue(Evento::class.java)
                    if (evento != null) {
                        listaEventos.add(evento)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error: ${error.message}")
            }
        })
    }

    private fun navigateJuegos()
    {
        val intent = Intent(this, ListaJuegosActivity::class.java)
        startActivity(intent)
    }

    private fun navigateInicio()
    {
        val intent = Intent(this, InicioActivity::class.java)
        startActivity(intent)
    }

    private fun navigateHacerEvento()
    {
        val intent = Intent(this, CrearEventoActivity::class.java)
        startActivity(intent)
    }
}