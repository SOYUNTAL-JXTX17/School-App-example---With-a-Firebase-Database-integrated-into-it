package com.example.miinstitutoapp.sesionApartado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import com.example.miinstitutoapp.R
import com.example.miinstitutoapp.inicioApartado.InicioActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class UserLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var accessButton: AppCompatButton

    private lateinit var correoInput: EditText
    private lateinit var contrasenaInput: EditText

    private lateinit var githubLink: TextView

    private lateinit var registrarse: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("ajustes", MODE_PRIVATE)

        val modoAnadido = sharedPreferences.getBoolean("modoOscuro", false)

        if (modoAnadido)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }

        auth = FirebaseAuth.getInstance()

        val sharedPreferencesSesion = getSharedPreferences("sesion", MODE_PRIVATE)
        val emailGuardado: String = sharedPreferencesSesion.getString("email", "").toString()
        val passwordGuardado: String = sharedPreferencesSesion.getString("password", "").toString()
        Log.d("LoginActivity", emailGuardado)
        Log.d("LoginActivity", passwordGuardado)

        if (emailGuardado != "" || passwordGuardado != "")
        {
            auth.signInWithEmailAndPassword(emailGuardado, passwordGuardado)

            val intent = Intent(this, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        else
        {
            setContentView(R.layout.activity_user_login)

            initComponents()

            githubLink.setOnClickListener {
                val url = "https://github.com/SOYUNTAL-JXTX17"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            accessButton.setOnClickListener {
                val email = correoInput.text.toString().trim()
                val password = contrasenaInput.text.toString().trim()

                // Validar campos vacíos
                if (email.isEmpty()) {
                    correoInput.error = "Introduce tu email"
                    correoInput.requestFocus()
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    contrasenaInput.error = "Introduce tu contraseña"
                    contrasenaInput.requestFocus()
                    return@setOnClickListener
                }

                // Iniciar sesión en Firebase
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login exitoso
                            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, InicioActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()

                            // Guardar inicio de sesion localmente
                            val sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE)
                            sharedPreferences.edit().putString("email", email).apply()
                            sharedPreferences.edit().putString("password", password).apply()

                        } else {
                            // Manejo de errores específicos
                            val exception = task.exception
                            val msg = when (exception) {
                                is FirebaseAuthInvalidUserException -> "Usuario no registrado"
                                is FirebaseAuthInvalidCredentialsException -> "Email o contraseña incorrectos"
                                else -> exception?.message ?: "Error desconocido"
                            }
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                        }
                    }
            }

            registrarse.setOnClickListener {
                val intent = Intent(this, UserRegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initComponents()
    {
        accessButton = findViewById(R.id.accessButton)
        githubLink = findViewById(R.id.githubLink)
        correoInput = findViewById(R.id.correoInput)
        contrasenaInput = findViewById(R.id.contrasenaInput)
        registrarse = findViewById(R.id.registrarse)
    }

}