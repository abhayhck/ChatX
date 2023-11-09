package com.abhayhck.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    lateinit var messageRecyclerView: RecyclerView
    lateinit var messageBox:EditText
    lateinit var sendBtn:ImageView
    lateinit var toolbar:Toolbar
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<MessageStructure>
    var senderRoom:String? = null
    var receiverRoom:String? = null
    var roomId:String? = null
    lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name = intent.getStringExtra("name")
        supportActionBar?.title = name

        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().reference
        //senderRoom = receiverUid + senderUid
        //receiverRoom = senderUid + receiverUid
        roomId = generateRoomId(senderUid!!, receiverUid!!)


        messageList = ArrayList()
        messageRecyclerView = findViewById(R.id.recyclerChatView)
        messageBox = findViewById(R.id.et_messageBox)
        sendBtn = findViewById(R.id.sendIv)
        messageAdapter = MessageAdapter(this, messageList)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter
        (messageRecyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
        addDataToRecyclerView(mDbRef)


      /*  mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children)
                    {
                        val message = postSnapshot.getValue(MessageStructure::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })*/
        sendBtn.setOnClickListener{
            val message = messageBox.text.toString()
            addMessageToDatabase(mDbRef, message, senderUid, roomId!!)
        }
        messageAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                messageRecyclerView?.smoothScrollToPosition(messageAdapter?.itemCount ?: 0)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateRoomId(user_id1: String, user_id2: String): String {
        val sortedUserIds = mutableListOf(user_id1, user_id2).sorted()

        return sortedUserIds[0] + sortedUserIds[1]
    }

    private fun addDataToRecyclerView(mDbRef:DatabaseReference)
    {
        mDbRef.child("chats").child(roomId!!).child("messages")
            .addValueEventListener(object: ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children)
                    {
                        val message = postSnapshot.getValue(MessageStructure::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun addMessageToDatabase(mDbRef: DatabaseReference, message:String,
                                     senderUid:String, roomId:String)
    {
        if(message.isNotEmpty()) {
            val messageObject = MessageStructure(message, senderUid)
            mDbRef.child("chats").child(roomId!!).child("messages").push()
                .setValue(messageObject)

            /*
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }*/
            messageBox.text.clear()
            addDataToRecyclerView(mDbRef)
        }
    }
}