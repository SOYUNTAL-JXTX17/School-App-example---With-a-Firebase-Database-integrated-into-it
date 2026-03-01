package com.example.albasurgames.mensajesApartado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.albasurgames.R
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val auth = FirebaseAuth.getInstance()

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == auth.uid) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layout = if (viewType == 1) R.layout.item_message_sent else R.layout.item_message_received
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvHora: TextView = itemView.findViewById(R.id.tvHora)
        fun bind(message: Message) {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = sdf.format(Date(message.timestamp))
            tvMessage.text = message.text
            tvHora.text = time
        }
    }
}