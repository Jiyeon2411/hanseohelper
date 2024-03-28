package com.example.hanseohelper_muso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class register : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var sign_up: Button? = null

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        sign_up = findViewById(R.id.btn_register3)
        btn_register3.setOnClickListener(View.OnClickListener { signUp() })
    }

    // When initializing your Activity, check to see if the user is currently signed in.
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
    }

    // 회원가입 클래스
    private fun signUp() {

        // 이름
        val nameEditText = findViewById<EditText>(R.id.edit_name_register)
        val name = nameEditText.text.toString()

        // 이메일 , 학번
        val emailEditText = findViewById<EditText>(R.id.edit_email_register)
        val emailsub = emailEditText.text.toString()
        val studentnumber = emailsub
        val domain = "@office.hanseo.ac.kr"
        val email = emailsub+domain

        // 전화번호
        val phoneEditText = findViewById<EditText>(R.id.edit_phone_register)
        val phone = phoneEditText.text.toString()

        val score : Double =3.0
        val scorenumber : Int =0
        val report : Int =0

        // 비밀번호
        val passwordEditText = findViewById<EditText>(R.id.edit_pw_register)
        val password = passwordEditText.text.toString()

        val passwordreEditText = findViewById<EditText>(R.id.edit_pw_re_register)
        val passwordre = passwordEditText.text.toString()


        // 비밀번호 재입력의 값과 비밀번호의 값이 일치하면 회원가입 진행
        if (password==passwordre) {
            mAuth!!.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this,
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = mAuth!!.currentUser

                            sendEmailVerification()

                            databaseReference.child("Account").child(studentnumber).child("Name").setValue(name)
                            databaseReference.child("Account").child(studentnumber).child("StudentNumber").setValue(studentnumber)
                            databaseReference.child("Account").child(studentnumber).child("Password").setValue(password)
                            databaseReference.child("Account").child(studentnumber).child("Phone").setValue(phone)
                            databaseReference.child("Account").child(studentnumber).child("Score").setValue(score)
                            databaseReference.child("Account").child(studentnumber).child("Scorenumber").setValue(scorenumber)
                            databaseReference.child("Account").child(studentnumber).child("Report").setValue(report)
                            databaseReference.child("Account").child(studentnumber).child("Requestnumber").setValue(0)
                            databaseReference.child("Account").child(studentnumber).child("Connectednumber").setValue(0)

                            val intent = Intent(this@register, login::class.java)
                            startActivity(intent)
                            Toast.makeText(this@register, "등록 완료", Toast.LENGTH_SHORT).show()
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@register, "등록 에러", Toast.LENGTH_SHORT).show()
                            return@OnCompleteListener
                        }
                    })
        }
        else{
            Toast.makeText(this, "비밀번호와 비밀번호 재입력 값이 다릅니다.", Toast.LENGTH_LONG)
            return
        }
    }

    companion object {
        private const val TAG = "SignUpActivity"
    }

    fun sendEmailVerification(){

        if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
            Toast.makeText(this, "이메일 인증이 이미 완료되었습니다", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "확인메일을 보냈습니다", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}

