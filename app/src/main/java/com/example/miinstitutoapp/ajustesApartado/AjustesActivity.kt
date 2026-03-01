package com.example.miinstitutoapp.ajustesApartado

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.miinstitutoapp.R
import com.example.miinstitutoapp.sesionApartado.UserLoginActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class AjustesActivity : AppCompatActivity() {

    private lateinit var swModoOscuro: SwitchMaterial

    private lateinit var backButton: ImageView

    private lateinit var cerrarSesionButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)
        initComponents()

        val sharedPreferences = getSharedPreferences("ajustes", MODE_PRIVATE)

        val modoAnadido = sharedPreferences.getBoolean("modoOscuro", false)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            || (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            || modoAnadido)
        {
            swModoOscuro.isChecked = true
        }
        else swModoOscuro.isChecked = false


        swModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Modo oscuro activado
                activarModoOscuro()
            } else {
                // Modo oscuro desactivado
                desactivarModoOscuro()
            }
        }

        cerrarSesionButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE)
            sharedPreferences.edit().remove("email").apply()
            sharedPreferences.edit().remove("password").apply()

            val intent = Intent(this, UserLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        backButton.setOnClickListener { finish() }
    }

    private fun initComponents() {
        swModoOscuro = findViewById(R.id.swModoOscuro)
        backButton = findViewById(R.id.backButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)
    }

    private fun activarModoOscuro()
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()

        val sharedPreferences = getSharedPreferences("ajustes", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("modoOscuro", true).apply()
    }

    private fun desactivarModoOscuro()
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()

        val sharedPreferences = getSharedPreferences("ajustes", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("modoOscuro", false).apply()
    }
}
