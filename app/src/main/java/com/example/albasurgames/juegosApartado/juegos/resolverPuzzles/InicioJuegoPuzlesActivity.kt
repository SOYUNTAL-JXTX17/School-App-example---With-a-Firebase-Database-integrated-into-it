package com.example.albasurgames.juegosApartado.juegos.resolverPuzzles

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.albasurgames.R

class InicioJuegoPuzlesActivity: AppCompatActivity() {
    // VARIABLES
    // 1. Variables globales
    var tipoPuzle: String = ""

    // 2. Componentes de la UI
    private lateinit var backButton: LinearLayout

    private lateinit var botonFacil: CardView

    private lateinit var botonNormal: CardView

    private lateinit var botonDificil: CardView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContentView(R.layout.activity_inicio_juego_puzles)

        initComponents()

        botonFacil.setOnClickListener {

            tipoPuzle = "Facil"

            cambiarActivityPuzle()

        }

        botonNormal.setOnClickListener {

            tipoPuzle = "Normal"

            cambiarActivityPuzle()

        }

        botonDificil.setOnClickListener {

            tipoPuzle = "Dificil"

            cambiarActivityPuzle()

        }

        backButton.setOnClickListener { finish() }
    }

    // METODOS
    private fun initComponents()
    {
        backButton = findViewById(R.id.backButton)
        botonFacil = findViewById(R.id.botonFacil)
        botonNormal = findViewById(R.id.botonNormal)
        botonDificil = findViewById(R.id.botonDificil)
    }

    private fun cambiarActivityPuzle()
    {
        val intent = Intent(this, JuegoPuzlesActivity::class.java)
        intent.putExtra("TIPO_PUZLE", tipoPuzle)
        startActivity(intent)
    }
}