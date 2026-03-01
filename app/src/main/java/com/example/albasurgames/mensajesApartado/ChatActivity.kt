package com.example.albasurgames.mensajesApartado

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albasurgames.R
import com.example.albasurgames.mensajesApartado.Message
import com.example.albasurgames.mensajesApartado.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var db: DatabaseReference
    private val messages = ArrayList<Message>()
    private lateinit var adapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var backButton: ImageView

    private lateinit var usuarioNombreChat: TextView
    private var myName = ""

    private var otherUserName = ""
    private var chatId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val otherUserId = intent.getStringExtra("otherUserId") ?: return

        val uid1 = auth.uid!!      // usuario actual
        val uid2 = otherUserId     // otro usuario

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        // Obtener nombre del otro usuario
        usuarioNombreChat = findViewById(R.id.usuarioNombreChat)

        FirebaseDatabase.getInstance().getReference("users")
            .child(uid2)
            .child("name")
            .get()
            .addOnSuccessListener {
                otherUserName = it.value.toString()
                usuarioNombreChat.text = otherUserName // Agregar nombre del otro usuario a la TextView
            }


        recyclerView = findViewById(R.id.recyclerMessages)
        val input = findViewById<EditText>(R.id.messageInput)
        val sendBtn = findViewById<CardView>(R.id.sendBtn)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(messages)
        recyclerView.adapter = adapter

        chatId = if (uid1 < uid2) "${uid1}_${uid2}" else "${uid2}_${uid1}"

        db = FirebaseDatabase.getInstance().getReference("private_chats")
            .child(chatId)
            .child("messages")

        // Obtener mi nombre
        FirebaseDatabase.getInstance().getReference("users")
            .child(uid1)
            .child("name")
            .get()
            .addOnSuccessListener { myName = it.value.toString() }

        sendBtn.setOnClickListener {
            val text = input.text.toString()
            if (text.isNotEmpty()) {
                val msg = Message(text, uid1, myName, System.currentTimeMillis())
                db.push().setValue(msg)
                input.setText("")
            }
        }

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (s in snapshot.children) {
                    val msg = s.getValue(Message::class.java)
                    if (msg != null) messages.add(msg)
                }
                adapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messages.size - 1)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}