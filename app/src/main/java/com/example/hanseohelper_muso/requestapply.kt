package com.example.hanseohelper_muso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_requestapply.*
import kotlinx.android.synthetic.main.activity_start.*

class requestapply : AppCompatActivity() {


    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requestapply)

        val acceptwho : String? = intent.extras?.getString("ID")

        val theme : String? = intent.extras?.getString("theme") // 추가
        Theme_requestapply.setText(theme)  // 추가

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                val category =
                    theme?.let { dataSnapshot.child("Request").child(it).child("reqCategory").getValue().toString() }
                Category_requestapply.setText(category)

                val pay =
                    theme?.let { dataSnapshot.child("Request").child(it).child("reqPay").getValue().toString() }
                Pay_requestapply.setText(pay+" 원")

                val paymethod =
                    theme?.let { dataSnapshot.child("Request").child(it).child("reqPayMethod").getValue().toString() }
                PayMethod_requestapply.setText(paymethod)

                val gender =
                    theme?.let { dataSnapshot.child("Request").child(it).child("reqGender").getValue().toString() }

                if(gender=="여성" || gender =="남성")
                {
                    val gender2 = gender +"분"
                    Gender_requestapply.setText(gender2)
                } else {
                    Gender_requestapply.setText(gender)
                    textView28.setText("하며")
                }

                val message =
                    theme?.let { dataSnapshot.child("Request").child(it).child("reqMessage").getValue().toString() }
                Message_requestapply.setText(message)

                val time = theme?.let{dataSnapshot.child("Request").child(it).child("time").getValue().toString()}
                Time_tv.setText(time)

                val reqwho =
                    theme?.let { dataSnapshot.child("Request").child(it).child("reqWho").getValue().toString() }

                if(acceptwho==reqwho)
                {
                    btn_accept.setVisibility(View.INVISIBLE)
                }

                else
                {
                    btn_delete.setVisibility(View.INVISIBLE)
                }

                btn_accept.setOnClickListener {
                    AlertDialog.Builder(this@requestapply).run {
                        setTitle("")
                        setIcon(android.R.drawable.ic_dialog_alert)
                        setMessage("수락하시겠습니까?")
                        setPositiveButton("수락") { dialog, which ->

                            val requestwho = theme?.let { dataSnapshot.child("Request").child(it).child("reqWho").getValue().toString() } // 의뢰를 올린사람
                            val acceptwho : String? = intent.extras?.getString("ID")

                            if (theme != null) {
                                databaseReference.child("ConnectedRequest").child(theme).child("accWho").setValue(acceptwho)
                                databaseReference.child("ConnectedRequest").child(theme).child("reqWho").setValue(requestwho)
                                databaseReference.child("ConnectedRequest").child(theme).child("reqCategory").setValue(category)
                                databaseReference.child("ConnectedRequest").child(theme).child("reqPay").setValue(pay)
                                databaseReference.child("ConnectedRequest").child(theme).child("reqPayMethod").setValue(paymethod)
                                databaseReference.child("ConnectedRequest").child(theme).child("reqGender").setValue(gender)
                                databaseReference.child("ConnectedRequest").child(theme).child("reqMessage").setValue(message)
                            } // ConnectedRequest로 request 이동

                            if (requestwho != null) {
                                databaseReference.child("Account").child(requestwho).child("Connectednumber").setValue(1)
                            }

                            if (acceptwho != null) {
                                databaseReference.child("Account").child(acceptwho).child("Connectednumber").setValue(1)
                                databaseReference.child("Account").child(acceptwho).child("Acceptrequest").setValue(theme)
                            } // Account 데이터베이스 수락한 의뢰 추가

                            if (theme != null) {
                                databaseReference.child("Request").child(theme).removeValue()
                            } // Request 데이터베이스에서 삭제

                            Toast.makeText(this@requestapply, "수락되었습니다.", Toast.LENGTH_LONG).show()

                            val intent = Intent(this@requestapply, main::class.java)
                            intent.putExtra("ID",acceptwho)
                            startActivity(intent)


                        }
                        setNeutralButton("취소", null)
                        show()
                    }
                }

                btn_delete.setOnClickListener {
                    AlertDialog.Builder(this@requestapply).run {
                        setTitle("")
                        setIcon(android.R.drawable.ic_dialog_alert)
                        setMessage("의뢰를 삭제하시겠습니까?")
                        setPositiveButton("삭제") { dialog, which ->

                            val requestwho : String? = intent.extras?.getString("ID")

                            if (requestwho != null) {
                                databaseReference.child("Account").child(requestwho).child("Requestnumber").setValue(0)
                            }

                            if (theme != null) {
                                databaseReference.child("Request").child(theme).removeValue()
                            } // Request 데이터베이스에서 삭제

                            Toast.makeText(this@requestapply, "수락되었습니다.", Toast.LENGTH_LONG).show()

                            val intent = Intent(this@requestapply, main::class.java)
                            intent.putExtra("ID",requestwho)
                            startActivity(intent)

                        }
                        setNeutralButton("취소", null)
                        show()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })

    }
}