package com.abhayhck.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserContactAdapter(var context: Context, val userlist:ArrayList<UserStructure>) :
    RecyclerView.Adapter<UserContactAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = userlist[position]

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }

        holder.txtname.text = currentUser.name

    }


    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var txtname:TextView = itemView.findViewById(R.id.txt_name)
    }
}