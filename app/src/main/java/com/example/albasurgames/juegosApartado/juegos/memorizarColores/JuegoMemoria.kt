package com.example.albasurgames.juegosApartado.juegos.memorizarColores

import android.content.Context
import android.content.res.ColorStateList
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albasurgames.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class JuegoMemoria : AppCompatActivity() {

    // VARIABLES
    private var hayPartidaComenzada: Boolean = false

    private var mensajePuesto: Boolean = false

    private lateinit var COLORES: Array<Int>
    private var SECUENCIA_COLORES = emptyArray<Int>()

    private var clicksRonda = emptyArray<Int>()
    private var clicksEnTexto: Int = 0

    private var rondasCompletas: Int = 0
    private var rondaComenzada: Boolean = false // Detectar si la ronda ha comenzado para comenzar o no a guardar los colores clickeados.
    private var rondaTerminada: Boolean = false // Detectar si el numero de clicks a los colores que el jugador ha dado en una ronda, es el mismo que el numero de colores que contiene esa ronda.

    private lateinit var pantalla: CardView

    private var mensajeJob: Job? = null
    private lateinit var botones: Array<Int>

    private lateinit var textoRecord: TextView

    private lateinit var textoPantalla: TextView

    private lateinit var clicks: TextView

    private lateinit var clickstexto: TextView

    private lateinit var comenzarPartida: AppCompatButton

    private val mensajes = mutableListOf<Mensaje>()
    private lateinit var rvMensajes: RecyclerView
    private lateinit var mensajesAdapter: MensajesAdapter

    private lateinit var backButton: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContentView(R.layout.activity_juego_memoria)

        COLORES = arrayOf(
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.blue),
            ContextCompat.getColor(this, R.color.yellow),
            ContextCompat.getColor(this, R.color.purple),
            ContextCompat.getColor(this, R.color.Orange),
            ContextCompat.getColor(this, R.color.Pink),
            ContextCompat.getColor(this, R.color.Brown)
        )

        botones = arrayOf(
            R.id.red,
            R.id.blue,
            R.id.yellow,
            R.id.purple,
            R.id.Orange,
            R.id.Pink,
            R.id.Brown
        )

        initComponents()
        initUI()
        cargarRecordRondas()

        comenzarPartida.setOnClickListener {
            if (!hayPartidaComenzada) {
                hayPartidaComenzada = true;

                Log.d("MENSAJEJOB", "$mensajeJob")

                if (mensajeJob != null)
                {
                    mensajeJob?.cancel()

                    if (mensajes.isNotEmpty())
                    {
                        val index = mensajes.size - 1
                        mensajes.removeAt(index)
                        mensajesAdapter.notifyItemRemoved(index)
                    }
                }

                if (!mensajePuesto)
                {
                    mensajePuesto = true
                    mensajes.add(Mensaje("Comenzando nueva partida...", MensajesCategory.Aviso))
                    mensajesAdapter.notifyItemInserted(mensajes.size - 1)
                    rvMensajes.scrollToPosition(mensajes.size - 1)
                }

                if (mensajePuesto)
                {
                    mensajeJob = lifecycleScope.launch {
                        delay(1000)
                        mensajePuesto = false
                        if (mensajes.isNotEmpty()) {
                            val index = mensajes.size - 1
                            mensajes.removeAt(index)
                            mensajesAdapter.notifyItemRemoved(index)
                        }
                    }
                }

                generarRonda();

            } else {
                Log.d("BIEN", "SI HAY PARTIDA COMENZADA")

                Log.d("MENSAJEJOB", "$mensajeJob")
                if (mensajeJob != null)
                {
                    mensajeJob?.cancel()

                    if (mensajes.isNotEmpty())
                    {
                        val index = mensajes.size - 1
                        mensajes.removeAt(index)
                        mensajesAdapter.notifyItemRemoved(index)
                    }
                }

                if (!mensajePuesto)
                {
                    mensajePuesto = true
                    mensajes.add(Mensaje("Si hay partida comenzada", MensajesCategory.Aviso))
                    mensajesAdapter.notifyItemInserted(mensajes.size - 1)
                    rvMensajes.scrollToPosition(mensajes.size - 1)
                }

                if (mensajePuesto)
                {
                    mensajeJob = lifecycleScope.launch {
                        delay(1000)
                        mensajePuesto = false
                        if (mensajes.isNotEmpty()) {
                            val index = mensajes.size - 1
                            mensajes.removeAt(index)
                            mensajesAdapter.notifyItemRemoved(index)
                        }
                    }
                }

            }
        }

        botones.forEach { id ->
            findViewById<CardView>(id).setOnClickListener {
                if (!hayPartidaComenzada)
                {
                    Log.d("ERROR", "NO HAY PARTIDA COMENZADA")

                    Log.d("MENSAJEJOB", "$mensajeJob")

                    if (mensajeJob != null)
                    {
                        mensajeJob?.cancel()

                        if (mensajes.isNotEmpty())
                        {
                            val index = mensajes.size - 1
                            mensajes.removeAt(index)
                            mensajesAdapter.notifyItemRemoved(index)
                        }
                    }

                    if (!mensajePuesto)
                    {
                        mensajePuesto = true
                        mensajes.add(Mensaje("No hay partida comenzada", MensajesCategory.Aviso))
                        mensajesAdapter.notifyItemInserted(mensajes.size - 1)
                        rvMensajes.scrollToPosition(mensajes.size - 1)
                    }

                    if (mensajePuesto)
                    {
                        mensajeJob = lifecycleScope.launch {
                            delay(1000)
                            mensajePuesto = false
                            if (mensajes.isNotEmpty()) {
                                val index = mensajes.size - 1
                                mensajes.removeAt(index)
                                mensajesAdapter.notifyItemRemoved(index)
                            }
                        }
                    }
                    return@setOnClickListener
                }

                if (rondaComenzada)
                {
                    detectarClicksColores(findViewById<CardView>(id).cardBackgroundColor.defaultColor)
                }
                else
                {
                    Log.d("ERROR", "LA RONDA NO HA COMENZADO TODAVIA")

                    Log.d("MENSAJEJOB", "$mensajeJob")

                    if (mensajeJob != null)
                    {
                        mensajeJob?.cancel()

                        if (mensajes.isNotEmpty())
                        {
                            val index = mensajes.size - 1
                            mensajes.removeAt(index)
                            mensajesAdapter.notifyItemRemoved(index)
                        }
                    }
                    if (!mensajePuesto)
                    {
                        mensajePuesto = true
                        mensajes.add(Mensaje("La ronda no ha comenzado todavía", MensajesCategory.Aviso))
                        mensajesAdapter.notifyItemInserted(mensajes.size - 1)
                        rvMensajes.scrollToPosition(mensajes.size - 1)
                    }

                    if (mensajePuesto)
                    {
                        mensajeJob = lifecycleScope.launch {
                            delay(1000)
                            mensajePuesto = false
                            if (mensajes.isNotEmpty()) {
                                val index = mensajes.size - 1
                                mensajes.removeAt(index)
                                mensajesAdapter.notifyItemRemoved(index)
                            }
                        }
                    }
                }
            }
        }

        backButton.setOnClickListener { finish() }
    }

    private fun initComponents()
    {
        pantalla = findViewById(R.id.pantalla)

        textoRecord = findViewById(R.id.record)

        textoPantalla = findViewById(R.id.textoPantalla)

        clicks = findViewById(R.id.clicks)

        clickstexto = findViewById(R.id.clickstexto)

        comenzarPartida = findViewById(R.id.comenzarPartida)

        rvMensajes = findViewById(R.id.rvMensajes)

        mensajesAdapter = MensajesAdapter(mensajes)

        backButton = findViewById(R.id.backButton)
    }

    private fun initUI() {
        rvMensajes.layoutManager = LinearLayoutManager(this)
        rvMensajes.adapter = mensajesAdapter
    }

    private fun guardarRondasCompletadas(nRondasActual: Int) {

        val sharedPreferences = getSharedPreferences("datos_juego", MODE_PRIVATE)

        val recordAnterior = sharedPreferences.getInt("record", 0)

        if (nRondasActual > recordAnterior) {
            sharedPreferences.edit().putInt("record", nRondasActual).apply()
        }
    }


    private fun cargarRecordRondas()
    {
        // VARIABLES
        val sharedPreferences = getSharedPreferences("datos_juego", MODE_PRIVATE)

        val recordRondas = sharedPreferences.getInt("record", 0)

        // PROCESO
        textoRecord.text = "$recordRondas"
    }

    private fun actualizarNumeroClicks()
    {
        clicksEnTexto++
        clicks.text = "$clicksEnTexto"
    }

    private fun restaurarElementosRonda()
    {
        SECUENCIA_COLORES = emptyArray()
        clicksRonda = emptyArray()
        rondaTerminada = false

        clicksEnTexto = 0
        clicks.text = "$clicksEnTexto"
        clickstexto.visibility = TextView.INVISIBLE
        clicks.visibility = TextView.INVISIBLE
    }

    private fun generarSecuenciaColores()
    {
        rondasCompletas++
        COLORES.shuffle()
        for (indice in 0 until rondasCompletas) {
            SECUENCIA_COLORES += COLORES[indice]
        }
    }

    private fun mostrarNombreColor(nombreColor: Int)
    {
        when (nombreColor) {
            ContextCompat.getColor(this, R.color.red) -> textoPantalla.text = "ROJO"
            ContextCompat.getColor(this, R.color.blue) -> textoPantalla.text = "AZUL"
            ContextCompat.getColor(this, R.color.yellow) -> textoPantalla.text = "AMARILLO"
            ContextCompat.getColor(this, R.color.purple) -> textoPantalla.text = "MORADO"
            ContextCompat.getColor(this, R.color.Orange) -> textoPantalla.text = "NARANJA"
            ContextCompat.getColor(this, R.color.Pink) -> textoPantalla.text = "ROSA"
            ContextCompat.getColor(this, R.color.Brown) -> textoPantalla.text = "MARRON"
            ContextCompat.getColor(this, R.color.LightGrey) -> textoPantalla.text = ""
        }
    }

    private fun mostrarColoresPantalla() {
        var contador = 0
        rondaComenzada = false

        lifecycleScope.launch {

            while (contador < SECUENCIA_COLORES.size) {

                val colorNombre = SECUENCIA_COLORES[contador]
                contador++

                // Cambiar color de fondo
                pantalla.setBackgroundColor(colorNombre)

                // Mostrar nombre del color
                mostrarNombreColor(colorNombre)

                delay(1000)
            }

            // Espera extra como el setTimeout
            delay(1000)

            rondaComenzada = true

            pantalla.setBackgroundColor(ContextCompat.getColor(this@JuegoMemoria, R.color.LightGrey))
            mostrarNombreColor(ContextCompat.getColor(this@JuegoMemoria, R.color.LightGrey))

            Log.d("BIEN", "RONDA $rondasCompletas HA COMENZADO")

            Log.d("MENSAJEJOB", "$mensajeJob")
            if (mensajeJob != null)
            {
                mensajeJob?.cancel()

                if (mensajes.isNotEmpty())
                {
                    val index = mensajes.size - 1
                    mensajes.removeAt(index)
                    mensajesAdapter.notifyItemRemoved(index)
                }
            }

            if (!mensajePuesto)
            {
                mensajePuesto = true
                mensajes.add(Mensaje("RONDA $rondasCompletas HA COMENZADO", MensajesCategory.Aviso))
                mensajesAdapter.notifyItemInserted(mensajes.size - 1)
                rvMensajes.scrollToPosition(mensajes.size - 1)
            }

            if (mensajePuesto)
            {
                mensajeJob = lifecycleScope.launch {
                    delay(1000)
                    mensajePuesto = false
                    if (mensajes.isNotEmpty()) {
                        val index = mensajes.size - 1
                        mensajes.removeAt(index)
                        mensajesAdapter.notifyItemRemoved(index)
                    }
                }
            }

        }
    }

    private fun generarRonda()
    {
        generarSecuenciaColores();
        mostrarColoresPantalla();

        clickstexto.visibility = TextView.VISIBLE
        clicks.visibility = TextView.VISIBLE
    }

    private fun detectarClicksColores(colorTocado: Int)
    {
        if (clicksRonda.size == SECUENCIA_COLORES.size - 1)
        {
            actualizarNumeroClicks()
            clicksRonda += colorTocado
            rondaTerminada = true

            val correcto: Boolean = clicksRonda.contentEquals(SECUENCIA_COLORES)

            if (correcto) {
                Log.d("BIEN", "RONDA COMPLETADA")

                mensajePuesto = true
                mensajes.add(Mensaje("Ronda completada", MensajesCategory.Exito))
                mensajesAdapter.notifyItemInserted(mensajes.size - 1)
                rvMensajes.scrollToPosition(mensajes.size - 1)

                mensajeJob = lifecycleScope.launch {
                    delay(1000)
                    restaurarElementosRonda()
                    generarRonda()
                    mensajePuesto = false
                    if (mensajes.isNotEmpty()) {
                        val index = mensajes.size - 1
                        mensajes.removeAt(index)
                        mensajesAdapter.notifyItemRemoved(index)
                    }
                }
                return
            }

            if (!correcto)
            {
                Log.d("ERROR", "RONDA FALLIDA")

                Log.d("MENSAJEJOB", "$mensajeJob")
                if (mensajeJob != null)
                {
                    mensajeJob?.cancel()

                    if (mensajes.isNotEmpty())
                    {
                        val index = mensajes.size - 1
                        mensajes.removeAt(index)
                        mensajesAdapter.notifyItemRemoved(index)
                    }
                }

                mensajePuesto = true
                mensajes.add(Mensaje("Ronda fallida", MensajesCategory.Fracaso))
                mensajesAdapter.notifyItemInserted(mensajes.size - 1)
                rvMensajes.scrollToPosition(mensajes.size - 1)

                guardarRondasCompletadas(rondasCompletas)
                rondasCompletas = 0
                restaurarElementosRonda()
                cargarRecordRondas()

                hayPartidaComenzada = false

                mensajeJob = lifecycleScope.launch {
                    delay(1000)
                    mensajePuesto = false
                    if (mensajes.isNotEmpty()) {
                        val index = mensajes.size - 1
                        mensajes.removeAt(index)
                        mensajesAdapter.notifyItemRemoved(index)
                    }
                }
                return
            }
            return
        }

        if (!rondaTerminada) {
            actualizarNumeroClicks()
            clicksRonda += colorTocado
            return
        }
    }
}