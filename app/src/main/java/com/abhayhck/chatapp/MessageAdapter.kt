package com.abhayhck.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import kotlin.contracts.contract

class MessageAdapter(val context: Context, val messageList:ArrayList<MessageStructure>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private val ITEM_RECEIVE = 1;
    private val ITEM_SENT = 2;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == ITEM_RECEIVE)
        {
            val view:View = LayoutInflater.from(context).inflate(R.layout.received_message_layout, parent, false)
            return ReceivedViewHolder(view)
        }
        else
        {
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent_message_layout, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java)
        {
            //Holder is from sent view holder
            val viewHolder:ViewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else
        {
            val viewHolder:ViewHolder = holder as ReceivedViewHolder
            holder.receivedMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId))
            return ITEM_SENT
        else
            return ITEM_RECEIVE
    }




    class SentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var sentMessage:TextView = itemView.findViewById(R.id.tvSentMsg)
    }
    class ReceivedViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var receivedMessage:TextView = itemView.findViewById(R.id.tvRecMsg)
    }
}