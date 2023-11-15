package com.abhayhck.chatapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var addBtn:ImageButton
    private var userName:String? = null
    private lateinit var tvUserName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar) //remember that toolbar id is to be provided in toolbar.xml
        setSupportActionBar(toolbar)   //step1
        //by default toolbar has a back button, we just have to apply it
        //step2,applying default back button
        //supportActionBar?.setDisplayHomeAsUpEnabled(true) //compiler doesnt know if we have set the toolbar or not(which we did in step1)
        //setTitle
        supportActionBar?.title = "ChatX"

        tvUserName = findViewById(R.id.tv_user_name)
        mAuth = FirebaseAuth.getInstance() //getting instance of firebase
        recyclerView = findViewById(R.id.recyclerContactView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = UserContactAdapter(this, personalUserList)
        recyclerView.adapter = adapter


        val uid:String = mAuth.currentUser?.uid!!
        addDataToRecyclerView(adapter, uid)

        mDbRef.child("User").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                userlist.clear()
                for(postSnapshot in snapshot.children)
                {
                    val currentUser = postSnapshot.getValue(UserStructure::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid )
                        userlist.add(currentUser!!)
                    else {
                        userName = currentUser?.name            //to set current user name in activity
                        tvUserName.text = userName.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })





        addBtn = findViewById(R.id.addBtn)
        addBtn.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_user_dialog)
            val dialogAddBtn = dialog.findViewById<AppCompatButton>(R.id.dialogAddBtn)
            val window: Window? = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialogAddBtn.setOnClickListener{
                val emailEt = dialog.findViewById<EditText>(R.id.dialogEmailEt)
                val email = emailEt.text.toString()
                addUser(email,uid,userlist, adapter)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun addUser(email: String, uid: String, userlist: ArrayList<UserStructure>,
                        adapter: UserContactAdapter) {
        mDbRef = FirebaseDatabase.getInstance().reference
        var requiredUser:UserStructure? = null
        for(user in userlist)
        {
            if(user.email == email){
                requiredUser = user
                break
            }
        }
        if(requiredUser == null)
            Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()
        else {
            val userName = requiredUser.name
            val userEmail = requiredUser.email
            val userUid = requiredUser.uid
            mDbRef.child("userContacts").child(uid).child(userUid!!).setValue(UserStructure(userName!!, userEmail!!, userUid!!))
            addDataToRecyclerView(adapter, uid)
        }
    }

    private fun addDataToRecyclerView(adapter:UserContactAdapter, uid:String)
    {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("userContacts").child(uid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                personalUserList.clear()
                for(postSnapshot in snapshot.children)
                {
                    val currentUser = postSnapshot.getValue(UserStructure::class.java)
                    personalUserList.add(currentUser!!)
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