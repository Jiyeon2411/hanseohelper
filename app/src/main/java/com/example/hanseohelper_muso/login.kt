package com.example.hanseohelper_muso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*  // 자동으로 추가 됨
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class login : AppCompatActivity() {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    lateinit var auth : FirebaseAuth
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.edit_email_login)
            val emailsub = emailEditText.text.toString()
            val domain = "@office.hanseo.ac.kr"
            val email = emailsub + domain

            val passwordEditText = findViewById<EditText>(R.id.edit_pw_login)
            val password = passwordEditText.text.toString()

            signIn(email, password)
        }

    }

    fun signIn(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && user!!.isEmailVerified) {

                        databaseReference.addListenerForSingleValueEvent(object :
                            ValueEventListener { // 딱 한번만 갱신

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val reportnumber = snapshot.child("Account")
                                    .child((findViewById<EditText>(R.id.edit_email_login)).text.toString())
                                    .child("Report").getValue().toString().toInt()

                                if (reportnumber >= 3) {
                                    Toast.makeText(
                                        this@login,
                                        "3회 이상 신고당해 정지되었습니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(this@login, "로그인에 성공하였습니다.", Toast.LENGTH_LONG).show()
                                    moveMainPage(task.result?.user)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }

                    else if (task.isSuccessful) {
                        Toast.makeText(this, "이메일을 인증해주세요.", Toast.LENGTH_LONG).show()
                    }

                    else {
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {


            val intent = Intent(this, main::class.java)
            intent.putExtra(
                "ID",
                (findViewById<EditText>(R.id.edit_email_login)).text.toString()
            )
            startActivity(intent)
        }
    }
}
