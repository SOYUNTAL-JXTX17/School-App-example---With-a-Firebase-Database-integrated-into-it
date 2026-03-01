package com.example.miinstitutoapp.juegosApartado.juegos.resolverPuzzles

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.miinstitutoapp.R

class JuegoPuzlesActivity : AppCompatActivity() {
    // VARIABLES
    private val typedValue = TypedValue()

    private lateinit var tipoPuzle: String

    private lateinit var salirPartida: LinearLayout

    private lateinit var actualizarPuzle: CardView

    private lateinit var layoutPuzle: LinearLayout

    private lateinit var celdasPuzle: Array<CardView>

    private lateinit var textosPuzle: Array<TextView>

    // METODOS
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContentView(R.layout.activity_juego_puzles)

        // INICIALIZACION DE VARIABLES
        initComponents()

        when (tipoPuzle)
        {
            "Facil" -> cargarPuzle("Facil")
            "Normal" -> cargarPuzle("Normal")
            "Dificil" -> cargarPuzle("Dificil")
        }

        actualizarPuzle.setOnClickListener {
            eliminarPuzle()
            cargarPuzle(tipoPuzle)
        }

        salirPartida.setOnClickListener { volverActivityInicio() }
    }

    private fun initComponents()
    {
        salirPartida = findViewById(R.id.salirPartida)
        layoutPuzle = findViewById(R.id.layoutPuzle)
        actualizarPuzle = findViewById(R.id.actualizarPuzle)
        tipoPuzle = intent.getStringExtra("TIPO_PUZLE").toString()
        celdasPuzle = emptyArray()
        textosPuzle = emptyArray()
    }

    private fun volverActivityInicio()
    {
        Log.d("VOLVERACTIVITYINICIO", "VOLVIENDO AL INICIO...")
        finish()
    }

    private fun eliminarPuzle()
    {
        celdasPuzle = emptyArray()
        textosPuzle = emptyArray()
        Log.d("ELIMINARPUZLE", "ELIMINANDO PUZLE...")
        layoutPuzle.removeAllViews()
    }

    fun cargarPuzle(dificultadPuzle: String)
    {
        Log.d("CARGARPUZLE", "CARGANDO PUZLE...")

        // VARIABLES
        var cNormalId: Int = 0
        val cvaciaId: Int = 7777
        var numeroFilas: Int = 0
        var numeroColumnas: Int = 0
        var numeroCeldasMenosUno: Int = 0
        var radioCardView: Float = 0f
        var tamanioTexto: Float = 0f
        var tamanioCardView: Int = 0

        // INICIALIZACION DE VARIABLES
        when (dificultadPuzle)
        {
            "Facil" ->
            {
                numeroFilas = 3
                numeroColumnas = 3
                numeroCeldasMenosUno = 8
                radioCardView = 16f
                tamanioTexto = 24f
                tamanioCardView = 180
            }
            "Normal" ->
            {
                numeroFilas = 4
                numeroColumnas = 4
                numeroCeldasMenosUno = 15
                radioCardView = 12f
                tamanioTexto = 20f
                tamanioCardView = 135
            }
            "Dificil" ->
            {
                numeroFilas = 6
                numeroColumnas = 6
                numeroCeldasMenosUno = 35
                radioCardView = 8f
                tamanioTexto = 16f
                tamanioCardView = 90
            }
        }

        // PROCESO
        // 1. Crear GridLayout
        val grid = GridLayout(this).apply {
            rowCount = numeroFilas
            columnCount = numeroColumnas
            alignmentMode = GridLayout.ALIGN_MARGINS
            Gravity.CENTER
            useDefaultMargins = true
        }

        // 2. Crear lista de valores
        val valores = (1..numeroCeldasMenosUno).map { it.toString() }.toMutableList()
        valores.add("") // espacio vacío
        valores.shuffle()

        // 3. Crear las celdas y añadirlas al GridLayout
        valores.forEach { valor ->
            if (valor.isNotEmpty())
            {
                cNormalId++

                Log.d("CARGARPUZLE", "AÑADIENDO CELDA: $cNormalId")

                val card = CardView(this).apply {
                    layoutParams = ViewGroup.LayoutParams(tamanioCardView, tamanioCardView)
                    radius = radioCardView
                    context.theme.resolveAttribute(R.attr.fondoBotones, typedValue, true)
                    setCardBackgroundColor(typedValue.data)
                    id = cNormalId
                }

                val texto = TextView(this).apply {
                    text = valor
                    textSize = tamanioTexto
                    context.theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                    setTextColor(typedValue.data)
                    gravity = Gravity.CENTER
                    setTypeface(null, Typeface.BOLD)
                }

                card.addView(texto)
                grid.addView(card)

                celdasPuzle += card
                textosPuzle += texto
            }
            else
            {
                Log.d("CARGARPUZLE", "AÑADIENDO CELDA VACIA")

                val card = CardView(this).apply {
                    layoutParams = ViewGroup.LayoutParams(tamanioCardView, tamanioCardView)
                    context.theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                    setCardBackgroundColor(typedValue.data)
                    id = cvaciaId
                }

                val texto = TextView(this).apply {
                    text = ""
                    textSize = tamanioTexto
                    context.theme.resolveAttribute(R.attr.fondoBotones, typedValue, true)
                    setTextColor(typedValue.data)
                    gravity = Gravity.CENTER
                    setTypeface(null, Typeface.BOLD)
                }

                card.addView(texto)
                grid.addView(card)

                celdasPuzle += card
                textosPuzle += texto
            }
        }

        // SALIDA
        // 4. Añadir el puzle al layout
        layoutPuzle.addView(grid)

        // 5. Implementacion de sistema de movimiento a cada cardView
        celdasPuzle.forEachIndexed { index, celda ->

            val esUltimaDeFila = (index + 1) % numeroColumnas == 0
            val esPrimeraDeFila = index % numeroColumnas == 0

            celda.setOnClickListener {
                Log.d("CARGARPUZLE", "CELDA ${celda.id} PULSADA")
                if (celda.id != cvaciaId)
                {
                    Log.d("CARGARPUZLE", "CELDA NORMAL")

                    // Izquierda
                    if (index - 1 >= 0 &&
                        celdasPuzle[index - 1].id == cvaciaId &&
                        !esPrimeraDeFila)
                    {

                        val textoActual = textosPuzle[index].text

                        // mover texto a la izquierda
                        textosPuzle[index - 1].text = textoActual
                        theme.resolveAttribute(R.attr.fondoBotones, typedValue, true)
                        celdasPuzle[index - 1].setCardBackgroundColor(typedValue.data)

                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        textosPuzle[index - 1].setTextColor(typedValue.data)

                        // vaciar celda actual
                        textosPuzle[index].text = ""
                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        celdasPuzle[index].setCardBackgroundColor(typedValue.data)

                        // actualizar ids
                        celdasPuzle[index - 1].id = celdasPuzle[index].id
                        celdasPuzle[index].id = cvaciaId
                    }

                    // Derecha
                    if (index + 1 < celdasPuzle.size &&
                        celdasPuzle[index + 1].id == cvaciaId &&
                        !esUltimaDeFila)
                    {

                        val textoActual = textosPuzle[index].text

                        // mover texto a la derecha
                        textosPuzle[index + 1].text = textoActual
                        theme.resolveAttribute(R.attr.fondoBotones, typedValue, true)
                        celdasPuzle[index + 1].setCardBackgroundColor(typedValue.data)

                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        textosPuzle[index + 1].setTextColor(typedValue.data)

                        // vaciar celda actual
                        textosPuzle[index].text = ""
                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        celdasPuzle[index].setCardBackgroundColor(typedValue.data)

                        // actualizar ids
                        celdasPuzle[index + 1].id = celdasPuzle[index].id
                        celdasPuzle[index].id = cvaciaId
                    }

                    // Arriba
                    if (index - numeroColumnas >= 0 &&
                        celdasPuzle[index - numeroColumnas].id == cvaciaId) {

                        val textoActual = textosPuzle[index].text

                        // mover texto hacia arriba
                        textosPuzle[index - numeroColumnas].text = textoActual
                        theme.resolveAttribute(R.attr.fondoBotones, typedValue, true)
                        celdasPuzle[index - numeroColumnas].setCardBackgroundColor(typedValue.data)
                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        textosPuzle[index - numeroColumnas].setTextColor(typedValue.data)
                        // vaciar celda actual
                        textosPuzle[index].text = ""
                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        celdasPuzle[index].setCardBackgroundColor(typedValue.data)

                        // actualizar ids
                        celdasPuzle[index - numeroColumnas].id = celdasPuzle[index].id
                        celdasPuzle[index].id = cvaciaId
                    }

                    // Abajo
                    if (index + numeroColumnas < celdasPuzle.size &&
                        celdasPuzle[index + numeroColumnas].id == cvaciaId) {

                        val textoActual = textosPuzle[index].text

                        // mover texto hacia abajo
                        textosPuzle[index + numeroColumnas].text = textoActual
                        theme.resolveAttribute(R.attr.fondoBotones, typedValue, true)
                        celdasPuzle[index + numeroColumnas].setCardBackgroundColor(typedValue.data)

                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        textosPuzle[index + numeroColumnas].setTextColor(typedValue.data)

                        // vaciar celda actual
                        textosPuzle[index].text = ""
                        theme.resolveAttribute(R.attr.textoBotones, typedValue, true)
                        celdasPuzle[index].setCardBackgroundColor(typedValue.data)

                        // actualizar ids
                        celdasPuzle[index + numeroColumnas].id = celdasPuzle[index].id
                        celdasPuzle[index].id = cvaciaId
                    }
                }

                if (puzleResuelto()) {
                    mostrarVictoria()
                }
            }
        }
    }

    private fun mostrarVictoria() {
        volverActivityInicio()
        Toast.makeText(this, "¡PUZLE COMPLETADO!", Toast.LENGTH_SHORT).show()
    }


    private fun puzleResuelto(): Boolean {

        for (i in 0 until textosPuzle.size - 1) {
            if (textosPuzle[i].text.toString() != (i + 1).toString()) {
                return false
            }
        }

        // última celda vacía
        return textosPuzle.last().text.isEmpty()
    }
}