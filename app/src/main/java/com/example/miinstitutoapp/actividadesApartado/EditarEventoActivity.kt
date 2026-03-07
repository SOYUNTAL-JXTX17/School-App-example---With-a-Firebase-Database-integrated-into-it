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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditarEventoActivity : AppCompatActivity() {
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

    private lateinit var editareventoButton: Button

    private lateinit var eventoId: String
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_evento)
        initComponents()

        database = FirebaseDatabase.getInstance().getReference("eventos").child(eventoId)

        database.child("nombre").get().addOnSuccessListener { nombreEvento.setText(it.value.toString()) }
        database.child("descripcion").get().addOnSuccessListener { descripcionEvento.setText(it.value.toString()) }
        database.child("fecha").get().addOnSuccessListener { fechaEvento.setText(it.value.toString()) }
        database.child("numeroparticipantesmax").get().addOnSuccessListener { maxpersonasEvento.setText(it.value.toString()) }
        database.child("portada").get().addOnSuccessListener { imagenEvento.setText(it.value.toString()) }
        backButton.setOnClickListener { finish() }

        subirImagenBoton.setOnClickListener {
            launcher.launch("image/*")
        }

        editareventoButton.setOnClickListener {
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
                FirebaseDatabase.getInstance().getReference("eventos")
                    .child(eventoId)
                    .get()
                    .addOnSuccessListener {

                        database.child("descripcion").setValue(descripcionEV)
                        database.child("fecha").setValue(fechaEV)
                        database.child("nombre").setValue(nombreEV)
                        database.child("numeroparticipantesmax").setValue(numeroparticipantesmaxEV)
                        database.child("portada").setValue(portadaEV)

                        finish()

                        Toast.makeText(this, "Evento editado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al obtener el evento: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }

    private fun initComponents()
    {
        eventoId = intent.getStringExtra("eventoSeleccionadoId") ?: ""
        backButton = findViewById(R.id.backButton)
        nombreEvento = findViewById(R.id.nombreEvento)
        descripcionEvento = findViewById(R.id.descripcionEvento)
        fechaEvento = findViewById(R.id.fechaEvento)
        maxpersonasEvento = findViewById(R.id.maxpersonasEvento)
        subirImagenBoton = findViewById(R.id.subirImagenBoton)
        imagenEvento = findViewById(R.id.imagenEvento)
        imageView = findViewById(R.id.imageView)
        editareventoButton = findViewById(R.id.editareventoButton)
    }
}