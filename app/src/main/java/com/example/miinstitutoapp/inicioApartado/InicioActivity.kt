package com.example.miinstitutoapp.inicioApartado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miinstitutoapp.R
import com.example.miinstitutoapp.actividadesApartado.ActividadesActivity
import com.example.miinstitutoapp.actividadesApartado.Evento2
import com.example.miinstitutoapp.actividadesApartado.EventoAdapter
import com.example.miinstitutoapp.ajustesApartado.AjustesActivity
import com.example.miinstitutoapp.juegosApartado.lista.ListaJuegosActivity
import com.example.miinstitutoapp.mensajesApartado.MensajesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InicioActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var nombreUsuario: TextView
    private var myName = ""
    private lateinit var ajustesButton: ImageView
    private lateinit var mensajesButton: ImageView
    private lateinit var actividadesButton: LinearLayout
    private lateinit var jugarButton: LinearLayout

    private lateinit var cvTelefono1: TextView
    private lateinit var cvTelefono2: TextView
    private lateinit var cvTelefono3: TextView
    private lateinit var cvTelefono4: TextView

    private lateinit var cvPaginaweb: CardView

    private lateinit var contenedorEventos: RecyclerView
    private lateinit var listaEventos: MutableList<Evento2>
    private lateinit var adapter: EventoAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inicio)

        initComponents()

        val uid = auth.uid!! // usuario actual

        FirebaseDatabase.getInstance().getReference("users")
            .child(uid)
            .child("name")
            .get()
            .addOnSuccessListener {
                myName = it.value.toString()
                nombreUsuario.text = myName // Agregar nombre del otro usuario a la TextView
            }

        jugarButton.setOnClickListener { navigateJuegos() }

        actividadesButton.setOnClickListener { navigateActividades() }

        mensajesButton.setOnClickListener { navigateMensajes() }

        ajustesButton.setOnClickListener { navigateAjustes() }

        cvPaginaweb.setOnClickListener {
            val url = "https://github.com/SOYUNTAL-JXTX17"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        cvTelefono1.setOnClickListener {
            val telefono = "111 111 111"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefono")
            startActivity(intent)
        }

        cvTelefono2.setOnClickListener {
            val telefono = "222 22 22 22"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefono")
            startActivity(intent)
        }

        cvTelefono3.setOnClickListener {
            val telefono = "333 33 33 33"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefono")
            startActivity(intent)
        }

        cvTelefono4.setOnClickListener {
            val telefono = "444 44 44 44"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefono")
            startActivity(intent)
        }

        contenedorEventos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        contenedorEventos.adapter = adapter
        cargarEventos()
    }


    private fun initComponents()
    {
        ajustesButton = findViewById(R.id.ajustesButton)
        mensajesButton = findViewById(R.id.mensajesButton)
        actividadesButton = findViewById(R.id.actividadesButton)
        jugarButton = findViewById(R.id.jugarButton)

        nombreUsuario = findViewById(R.id.userName)

        cvTelefono1 = findViewById(R.id.cvTelefono1)
        cvTelefono2 = findViewById(R.id.cvTelefono2)
        cvTelefono3 = findViewById(R.id.cvTelefono3)
        cvTelefono4 = findViewById(R.id.cvTelefono4)

        cvPaginaweb = findViewById(R.id.cvPaginaweb)

        contenedorEventos = findViewById(R.id.contenedorEventos)
        listaEventos = mutableListOf()
        adapter = EventoAdapter(
            listaEventos,
            auth.uid,
            onUnirseClick = { evento ->
                val uid = auth.uid ?: return@EventoAdapter
                val nombreUsuario = myName

                evento.id?.let { idEvento ->

                    val participantesRef = database.child(idEvento).child("participantes")
                    val eventoRef = database.child(idEvento) // referencia al evento completo

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
            }
            )
        database = FirebaseDatabase.getInstance().reference.child("eventos")
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
                    val evento = eventoSnapshot.getValue(Evento2::class.java)
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

    private fun navigateActividades()
    {
        val intent = Intent(this, ActividadesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateMensajes()
    {
        val intent = Intent(this, MensajesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateAjustes()
    {
        val intent = Intent(this, AjustesActivity::class.java)
        startActivity(intent)
    }

}