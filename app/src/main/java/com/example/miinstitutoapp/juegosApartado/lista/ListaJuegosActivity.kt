package com.example.miinstitutoapp.juegosApartado.lista

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.miinstitutoapp.R
import com.example.miinstitutoapp.actividadesApartado.ActividadesActivity
import com.example.miinstitutoapp.inicioApartado.InicioActivity
import com.example.miinstitutoapp.juegosApartado.juegos.memorizarColores.JuegoMemoria
import com.example.miinstitutoapp.juegosApartado.juegos.resolverPuzzles.InicioJuegoPuzlesActivity
import com.example.miinstitutoapp.juegosApartado.juegos.sudoku.PlaySudokuActivity

class ListaJuegosActivity : AppCompatActivity() {

    private lateinit var juegoPuzle: LinearLayoutCompat
    private lateinit var juegoMemoria: LinearLayoutCompat

    private lateinit var juegoSudoku: LinearLayoutCompat

    private lateinit var inicioButton: LinearLayout
    private lateinit var actividadesButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_juegos)
        initComponents()

        juegoPuzle.setOnClickListener { navigateJuegoPuzle() }

        juegoMemoria.setOnClickListener { navigateJuegoMemoria() }

        juegoSudoku.setOnClickListener { navigateJuegoSudoku() }

        inicioButton.setOnClickListener { navigateInicio() }

        actividadesButton.setOnClickListener { navigateActividades() }
    }

    private fun initComponents() {
        juegoPuzle = findViewById(R.id.juegoPuzle)
        juegoMemoria = findViewById(R.id.juegoMemoria)
        juegoSudoku = findViewById(R.id.juegoSudoku)
        inicioButton = findViewById(R.id.inicioButton)
        actividadesButton = findViewById(R.id.actividadesButton)
    }

    private fun navigateJuegoPuzle()
    {
        val intent = Intent(this, InicioJuegoPuzlesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateJuegoMemoria()
    {
        val intent = Intent(this, JuegoMemoria::class.java)
        startActivity(intent)
    }

    private fun navigateJuegoSudoku()
    {
        val intent = Intent(this, PlaySudokuActivity::class.java)
        startActivity(intent)
    }

    private fun navigateInicio()
    {
        val intent = Intent(this, InicioActivity::class.java)
        startActivity(intent)
    }

    private fun navigateActividades()
    {
        val intent = Intent(this, ActividadesActivity::class.java)
        startActivity(intent)
    }
}