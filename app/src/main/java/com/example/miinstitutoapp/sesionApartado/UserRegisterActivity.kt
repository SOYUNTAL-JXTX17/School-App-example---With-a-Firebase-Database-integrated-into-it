package com.example.miinstitutoapp.sesionApartado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.miinstitutoapp.R
import com.example.miinstitutoapp.inicioApartado.InicioActivity
import com.example.miinstitutoapp.mensajesApartado.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var accessButton: AppCompatButton

    private lateinit var nombreInput: EditText
    private lateinit var correoInput: EditText
    private lateinit var contrasenaInput: EditText

    private lateinit var tipoAlumno: CardView

    private lateinit var tipoProfesor: CardView

    private lateinit var githubLink: TextView

    private lateinit var iniciarSesion: TextView

    var tipoEscogido: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)
        auth = FirebaseAuth.getInstance()
        initComponents()

        tipoAlumno.setOnClickListener {
            elegirTipoUsuario("alumno")
        }

        tipoProfesor.setOnClickListener {
            elegirTipoUsuario("profesor")
        }

        githubLink.setOnClickListener {
            val url = "https://github.com/SOYUNTAL-JXTX17"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        accessButton.setOnClickListener {
            val name = nombreInput.text.toString().trim()
            val email = correoInput.text.toString().trim()
            val password = contrasenaInput.text.toString().trim()

            if (name.isEmpty()) {
                nombreInput.error =
                    "Introduce tu nombre"; nombreInput.requestFocus(); return@setOnClickListener
            }
            if (email.isEmpty()) {
                correoInput.error =
                    "Introduce tu email"; correoInput.requestFocus(); return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6) {
                contrasenaInput.error =
                    "Contraseña mínima 6 caracteres"; contrasenaInput.requestFocus(); return@setOnClickListener
            }
            if (tipoEscogido.isEmpty()) {
                Toast.makeText(this, "Debes de escoger tu tipo de usuario", Toast.LENGTH_SHORT)
                    .show(); return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser!!.uid
                        val user = User(uid, name, email, tipoEscogido)
                        FirebaseDatabase.getInstance().getReference("users").child(uid)
                            .setValue(user)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT)
                                        .show()
                                    val intent = Intent(this, InicioActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                    // Guardar inicio de sesion localmente
                                    val sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE)
                                    sharedPreferences.edit().putString("email", email).apply()
                                    sharedPreferences.edit().putString("password", password).apply()
                                    sharedPreferences.edit().putString("tipo", tipoEscogido).apply()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error DB: ${dbTask.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Error Auth: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        iniciarSesion.setOnClickListener {
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun elegirTipoUsuario(tipo: String)
    {
        // RECURSOS
        var colorEscogido = ContextCompat.getColor(this, R.color.DarkGray)
        var colorPredeterminado = ContextCompat.getColor(this, R.color.Lavender)

        // PROCESO
        tipoEscogido = tipo

        when (tipo)
        {
            "alumno" -> {
                tipoAlumno.setCardBackgroundColor(colorEscogido)
                tipoProfesor.setCardBackgroundColor(colorPredeterminado)
            }
            "profesor" -> {
                tipoProfesor.setCardBackgroundColor(colorEscogido)
                tipoAlumno.setCardBackgroundColor(colorPredeterminado)
            }
        }
    }
    private fun initComponents()
    {
        accessButton = findViewById(R.id.accessButton)
        githubLink = findViewById(R.id.githubLink)
        nombreInput = findViewById(R.id.nombreInput)
        correoInput = findViewById(R.id.correoInput)
        contrasenaInput = findViewById(R.id.contrasenaInput)
        iniciarSesion = findViewById(R.id.iniciarSesion)
        tipoAlumno = findViewById(R.id.tipoAlumno)
        tipoProfesor = findViewById(R.id.tipoProfesor)
    }
}