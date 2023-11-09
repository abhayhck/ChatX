package com.abhayhck.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess
import java.util.regex.Pattern

class Signup : AppCompatActivity() {
    private lateinit var nameEt:EditText
    private lateinit var emailEt:EditText
    private lateinit var passwordEt:EditText
    private lateinit var rePasswordEt:EditText
    private lateinit var btnSignup:AppCompatButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEt = findViewById(R.id.nameEt)
        emailEt = findViewById(R.id.emailEt)
        passwordEt = findViewById(R.id.passwordEt)
        rePasswordEt = findViewById(R.id.repasswordEt)
        btnSignup = findViewById(R.id.signupbtn)
        mAuth = FirebaseAuth.getInstance()

        btnSignup.setOnClickListener{
            val name = nameEt.text.toString()
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            val rePassword = rePasswordEt.text.toString()

            if(password != rePassword)
            {
                Toast.makeText(this, "Password do not match!", Toast.LENGTH_SHORT).show()
            }
            else if(!isEmailValid(email))
            {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            }
            else if(name.isEmpty())
                Toast.makeText(this, "Enter a Name", Toast.LENGTH_SHORT).show()
            else
            {
                signup(name, email, password)
            }
        }

    }

    private fun signup(name:String, email:String, password:String)
    {
        //logic of creating user

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this@Signup, "Some error Occurred", Toast.LENGTH_SHORT).show()
                }
            }

    }


    private fun addUserToDatabase(name:String, email:String, uid:String)
    {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("User").child(uid).setValue(UserStructure(name, email, uid))
    }


    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}