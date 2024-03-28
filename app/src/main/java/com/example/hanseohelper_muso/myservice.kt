package com.example.hanseohelper_muso

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_myclient.*
import kotlinx.android.synthetic.main.activity_myservice.*
import kotlinx.android.synthetic.main.activity_myservice.score_myclient
import kotlinx.android.synthetic.main.activity_myservice.textView12
import kotlinx.android.synthetic.main.activity_myservice.textView18

class myservice : AppCompatActivity() {


    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myservice)

        val intent_id_myservice : String? = intent.extras?.getString("ID")

        bt_report_myservice.setOnClickListener {
            val intent = Intent(this, report_myservice::class.java)
            intent.putExtra("ID",intent_id_myservice)
            startActivity(intent)
        }

        bt_chat_myservice.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("ID", intent_id_myservice)
            startActivity(intent)
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // 의뢰 이름
                val requestnumber =
                    intent_id_myservice?.let {
                        dataSnapshot.child("Account").child(it).child("Requestnumber").getValue()
                            .toString().toInt()
                    }

                val connectednumber =
                    intent_id_myservice?.let {
                        dataSnapshot.child("Account").child(it).child("Connectednumber").getValue()
                            .toString().toInt()
                    }

                if (requestnumber == 0 || connectednumber == 0) {
                    request_message_myservice.setText("")
                    name_myservice.setText("")
                    studentnumber_myservice.setText("")
                    title_myservice.setText("")
                    service_myservice.setText("")
                    pay_bill_myservice.setText("")
                    pay_method_myservice.setText("")
                    score_myclient.setText("")
                    textView11.setText("")
                    textView12.setText("")
                    textView18.setText("")
                    btn_complete.setVisibility(View.INVISIBLE)
                } else {
                    textView10.setText("")

                    val requestname =
                        intent_id_myservice?.let {
                            dataSnapshot.child("Account").child(it).child("Request").getValue()
                                .toString()
                        }
                    title_myservice.setText(requestname)

                    val id =
                        requestname?.let {
                            dataSnapshot.child("ConnectedRequest").child(it).child("accWho")
                                .getValue().toString()
                        }


                    val studentnumber =
                        id?.let {
                            dataSnapshot.child("Account").child(it).child("StudentNumber")
                                .getValue().toString()
                        }
                    studentnumber_myservice.setText(studentnumber)

                    val name =
                        id?.let {
                            dataSnapshot.child("Account").child(it).child("Name").getValue()
                                .toString()
                        }
                    name_myservice.setText(name)

                    val score =
                        id?.let {
                            dataSnapshot.child("Account").child(it).child("Score").getValue()
                                .toString()
                        }
                    score_myclient.setText(score)

                    val paymethod =
                        requestname?.let {
                            dataSnapshot.child("ConnectedRequest").child(it).child("reqPayMethod")
                                .getValue().toString()
                        }
                    pay_method_myservice.setText(paymethod)

                    val pay =
                        requestname?.let {
                            dataSnapshot.child("ConnectedRequest").child(it).child("reqPay")
                                .getValue().toString()
                        }
                    pay_bill_myservice.setText(pay)

                    val message =
                        requestname?.let {
                            dataSnapshot.child("ConnectedRequest").child(it).child("reqMessage")
                                .getValue().toString()
                        }
                    request_message_myservice.setText(message)

                    val category =
                        requestname?.let {
                            dataSnapshot.child("ConnectedRequest").child(it).child("reqCategory")
                                .getValue().toString()
                        }
                    service_myservice.setText(category)

                    btn_complete.setOnClickListener {
                        AlertDialog.Builder(this@myservice).run {
                            setTitle("완료 확인")
                            setIcon(android.R.drawable.ic_dialog_alert)
                            setMessage("상대방이 의뢰를 완료하였나요?")
                            setPositiveButton("네") { dialog, which ->

                                if (id != null) {
                                    databaseReference.child("Account").child(id).child("Acceptrequest").removeValue()
                                    databaseReference.child("Account").child(id).child("Connectednumber").setValue(0)
                                }

                                if (intent_id_myservice != null) {
                                    databaseReference.child("Account").child(intent_id_myservice).child("Request").removeValue()
                                    databaseReference.child("Account").child(intent_id_myservice).child("Connectednumber").setValue(0)
                                    databaseReference.child("Account").child(intent_id_myservice).child("Requestnumber").setValue(0)
                                }

                                if (requestname != null) {
                                    databaseReference.child("ConnectedRequest").child(requestname).removeValue()
                                    databaseReference.child("CompletedRequest").child(requestname).child("accWho").setValue(id)
                                    databaseReference.child("CompletedRequest").child(requestname).child("reqWho").setValue(intent_id_myservice)
                                    databaseReference.child("CompletedRequest").child(requestname).child("reqCategory").setValue(category)
                                    databaseReference.child("CompletedRequest").child(requestname).child("reqPay").setValue(pay)
                                    databaseReference.child("CompletedRequest").child(requestname).child("reqPayMethod").setValue(paymethod)
                                    databaseReference.child("CompletedRequest").child(requestname).child("reqMessage").setValue(message)
                                }

                                Toast.makeText(this@myservice, "의뢰 완료처리되었습니다.", Toast.LENGTH_LONG).show()

                                val intent = Intent(this@myservice, com.example.hanseohelper_muso.score::class.java)
                                intent.putExtra("ID",intent_id_myservice)
                                intent.putExtra("ID2",id)
                                startActivity(intent)

                            }
                            setNeutralButton("아니오", null)
                            show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }

        })

    }

}