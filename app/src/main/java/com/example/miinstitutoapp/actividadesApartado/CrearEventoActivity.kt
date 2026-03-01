package com.example.miinstitutoapp.actividadesApartado

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.miinstitutoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CrearEventoActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private var myName = ""
    private var nombreCreador = ""
    private lateinit var backButton: ImageView

    private lateinit var nombreEvento: EditText
    private lateinit var descripcionEvento: EditText
    private lateinit var fechaEvento: EditText
    private lateinit var maxpersonasEvento: EditText

    private lateinit var imagenEvento: EditText
    private lateinit var subirImagenBoton: Button

    private lateinit var imageView: ImageView

    private lateinit var imageUri: Uri

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            uri.let {
                imageUri = it
                imageView.setImageURI(it)
            }
        }
    }

    private lateinit var crearEventoButton: Button

    val database = FirebaseDatabase.getInstance().getReference("eventos")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)
        initComponents()
        val uid = auth.uid!! // usuario actual


        backButton.setOnClickListener { finish() }

        subirImagenBoton.setOnClickListener {
            launcher.launch("image/*")
        }

        // Crear mapa de participantes
        val participantesEvento = hashMapOf<String, String>()

        crearEventoButton.setOnClickListener {
            val nombreEV = nombreEvento.text.toString().trim()
            val descripcionEV = descripcionEvento.text.toString()
            val fechaEV = fechaEvento.text.toString().trim()
            val numeroparticipantesmaxEV = maxpersonasEvento.text.toString().toInt()
            var portadaEV = imagenEvento.text.toString()

            if (nombreEvento.text.isEmpty() ||
                descripcionEvento.text.isEmpty() ||
                fechaEvento.text.isEmpty() ||
                maxpersonasEvento.text.isEmpty()) {
                Toast.makeText(this, "Introduce todos tus datos", Toast.LENGTH_SHORT)
                    .show(); return@setOnClickListener
            }
            else
            {
                val eventoId = database.push().key
                FirebaseDatabase.getInstance().getReference("users")
                    .child(uid)
                    .child("name")
                    .get()
                    .addOnSuccessListener {
                        myName = it.value.toString()
                        nombreCreador = myName

                        val evento = Evento(
                            descripcion = descripcionEV,
                            fecha = fechaEV,
                            nombre = nombreEV,
                            numeroparticipantes = 0,
                            numeroparticipantesmax = numeroparticipantesmaxEV,
                            portada = portadaEV,
                            participantes = participantesEvento,
                            creador = nombreCreador,
                            id = eventoId.toString()
                        )

                        if (eventoId != null) {
                            database.child(eventoId).setValue(evento)

                            finish()

                            Toast.makeText(this, "Evento creado", Toast.LENGTH_SHORT)
                                .show();
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al obtener nombre: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }

    private fun initComponents()
    {
        backButton = findViewById(R.id.backButton)
        nombreEvento = findViewById(R.id.nombreEvento)
        descripcionEvento = findViewById(R.id.descripcionEvento)
        fechaEvento = findViewById(R.id.fechaEvento)
        maxpersonasEvento = findViewById(R.id.maxpersonasEvento)
        subirImagenBoton = findViewById(R.id.subirImagenBoton)
        imagenEvento = findViewById(R.id.imagenEvento)
        imageView = findViewById(R.id.imageView)
        crearEventoButton = findViewById(R.id.creareventoButton)
    }
}