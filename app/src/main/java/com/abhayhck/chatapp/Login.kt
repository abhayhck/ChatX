package com.abhayhck.chatapp

import android.content.Intent
import java.util.regex.Pattern
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var btnSignup:AppCompatButton
    private lateinit var btnLogin:AppCompatButton
    private lateinit var emailEt:EditText
    private lateinit var passwordEt:EditText
    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null)
        {
            startActivity(Intent(this@Login, MainActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btnSignup = findViewById(R.id.signupbtn)
        btnLogin = findViewById(R.id.loginbtn)
        emailEt = findViewById(R.id.emailEt)
        passwordEt = findViewById(R.id.passwordEt)


        btnSignup.setOnClickListener{
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{
            val email = emailEt.text.toString()
            var password = passwordEt.text.toString()
            password = password.trim()
            if(isEmailValid(email))
                login(email, password)
            else
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(email:String, password:String)
    {
        //logic for login user

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login, MainActivity::class.java)
                    Log.i("hellow", "ReachedLogin")
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this@Login, "Incorrect Email/Password", Toast.LENGTH_SHORT).show()
                }
            }
    }



    fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}