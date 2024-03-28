package com.example.hanseohelper_muso

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_report_myservice.*

class report_myservice : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_myservice)

        ArrayAdapter.createFromResource(
            this,
            R.array.report_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter
        }
        spinner2.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val spinner2 = findViewById<View>(R.id.spinner2) as Spinner
        val text = spinner2.selectedItem.toString()
        extra2.setVisibility(View.INVISIBLE)
        extra2.setClickable(false)
        extra2.setFocusable(false)

        if(text == "기타") {
            extra2.setVisibility(View.VISIBLE)
            extra2.setFocusableInTouchMode (true)
            extra2.setFocusable(true)
        }

        btn_report2.setOnClickListener {

            if(text=="신고사유를 선택해주세요")
            {
                Toast.makeText(this, "신고사유를 선택해주세요", Toast.LENGTH_LONG).show()
            }

            else {

                val intent_id_report: String? = intent.extras?.getString("ID")

                if (intent_id_report != null) {

                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener { // 딱 한번만 갱신
                        override fun onDataChange(dataSnapshot: DataSnapshot) {


                            val requestname =
                                intent_id_report?.let {
                                    dataSnapshot.child("Account").child(it).child("Request")
                                        .getValue()
                                        .toString()
                                }


                            val reportid1 = requestname.let {
                                dataSnapshot.child("ConnectedRequest").child(it).child("accWho")
                                    .getValue().toString()
                            }
                            val reportid2 = requestname.let {
                                dataSnapshot.child("ConnectedRequest").child(it).child("reqWho")
                                    .getValue().toString()
                            }

                            if (reportid1 == intent_id_report) {
                                val reportnumber =
                                    reportid2.let {
                                        dataSnapshot.child("Account").child(it).child("Report")
                                            .getValue().toString().toInt()
                                    } + 1
                                databaseReference.child("Account").child(reportid2)
                                    .child("Report").setValue(reportnumber)
                            }

                            if (reportid2 == intent_id_report) {
                                val reportnumber =
                                    reportid1.let {
                                        dataSnapshot.child("Account").child(it).child("Report")
                                            .getValue().toString().toInt()
                                    } + 1
                                databaseReference.child("Account").child(reportid1)
                                    .child("Report").setValue(reportnumber)
                            }

                            if (text == "기타") {
                                if (reportid1 == intent_id_report) {
                                    databaseReference.child("Report")
                                        .child((findViewById<EditText>(R.id.extra2)).text.toString())
                                        .setValue(reportid2)
                                }
                                if (reportid2 == intent_id_report) {
                                    databaseReference.child("Report")
                                        .child((findViewById<EditText>(R.id.extra2)).text.toString())
                                        .setValue(reportid1)
                                }

                                Toast.makeText(this@report_myservice, "신고가 접수되었습니다.", Toast.LENGTH_LONG)
                                    .show()
                                finish()
                            } else {
                                if (reportid1 == intent_id_report) {
                                    databaseReference.child("Report").child(text)
                                        .setValue(reportid2)
                                    Toast.makeText(
                                        this@report_myservice,
                                        "신고가 접수되었습니다.",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    finish()
                                }

                                if (reportid2 == intent_id_report) {
                                    databaseReference.child("Report").child(text)
                                        .setValue(reportid1)
                                    Toast.makeText(
                                        this@report_myservice,
                                        "신고가 접수되었습니다.",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                    intent.putExtra("ID",intent_id_report)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }


                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })

                }
            }
        }

        btn_back2.setOnClickListener {

            finish()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}