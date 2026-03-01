package com.example.albasurgames.mensajesApartado

import android.R.attr.height
import android.R.attr.name
import android.R.attr.width
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albasurgames.R
import com.example.albasurgames.actividadesApartado.ActividadesActivity
import com.example.albasurgames.inicioApartado.InicioActivity
import com.example.albasurgames.juegosApartado.lista.ListaJuegosActivity
import com.example.albasurgames.mensajesApartado.MessageAdapter
import com.example.albasurgames.mensajesApartado.User
import com.example.albasurgames.mensajesApartado.UsersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import kotlin.apply

class MensajesActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("users")
    private val users = ArrayList<User>()
    private lateinit var adapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var jugarButton: LinearLayout
    private lateinit var inicioButton: LinearLayout
    private lateinit var actividadesButton: LinearLayout
    private lateinit var backButton: ImageView

    private lateinit var rlMensajes: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajes)

        initComponents()

        jugarButton.setOnClickListener { navigateJuegos() }

        inicioButton.setOnClickListener { navigateInicio() }

        actividadesButton.setOnClickListener { navigateActividades() }

        backButton.setOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsersAdapter(this, users)
        recyclerView.adapter = adapter

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (s in snapshot.children) {
                    val user = s.getValue(User::class.java)
                    if (user != null && user.uid != auth.uid) {
                        users.add(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initComponents()
    {
        jugarButton = findViewById(R.id.jugarButton)
        inicioButton = findViewById(R.id.inicioButton)
        actividadesButton = findViewById(R.id.actividadesButton)
        backButton = findViewById(R.id.backButton)
        rlMensajes = findViewById(R.id.rlMensajes)
    }

    private fun navigateJuegos()
    {
        val intent = Intent(this, ListaJuegosActivity::class.java)
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