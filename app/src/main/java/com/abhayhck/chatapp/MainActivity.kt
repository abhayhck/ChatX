package com.abhayhck.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView:RecyclerView
    private var userlist:ArrayList<UserStructure> = ArrayList()
    private var personalUserList:ArrayList<UserStructure> = ArrayList()
    private lateinit var mAuth:FirebaseAuth
    private lateinit var toolbar:Toolbar
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance() //getting instance of firebase
        mDbRef = FirebaseDatabase.getInstance().reference

        toolbar = findViewById(R.id.toolbar) //remember that toolbar id is to be provided in toolbar.xml
        setSupportActionBar(toolbar)   //step1
        //by default toolbar has a back button, we just have to apply it
        //step2,applying default back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //compiler doesnt know if we have set the toolbar or not(which we did in step1)
        //setTitle
        supportActionBar?.title = "ChatX"

        recyclerView = findViewById(R.id.recyclerContactView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = UserContactAdapter(this, userlist)
        recyclerView.adapter = adapter

        mDbRef.child("User").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                userlist.clear()
                for(postSnapshot in snapshot.children)
                {
                    val currentUser = postSnapshot.getValue(UserStructure::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid )
                        userlist.add(currentUser!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
//step3
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                mAuth.signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
                return true
            }
            android.R.id.home ->{//back button which we set by default
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }





}