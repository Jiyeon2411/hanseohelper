package com.example.hanseohelper_muso

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_myclient.*

class myclient : AppCompatActivity() {


    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myclient)

        val studentNum: String? = intent.extras?.getString("ID")

        bt_report_myclient.setOnClickListener {
            val intent = Intent(this, report_myclient::class.java)
            intent.putExtra("ID", studentNum)
            startActivity(intent)
        }

        // 채팅 기능 추가
        bt_chat_myclient.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("ID", studentNum)
            startActivity(intent)
        }

        if (studentNum != null) {
            fetchProfile(studentNum.toInt())
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                // 나의 의뢰인의 의뢰제목
                val acceptrequest =
                    studentNum?.let { dataSnapshot.child("Account").child(it).child("Acceptrequest").getValue().toString() }
                title_myclient.setText(acceptrequest)

                // 의뢰자 이름
                val id = acceptrequest?.let { dataSnapshot.child("ConnectedRequest").child(it).child("reqWho").getValue().toString() }

                val name = id?.let { dataSnapshot.child("Account").child(it).child("Name").getValue().toString() }
                name_myclient.setText(name)

                // 의뢰자 학번
                val studentnumber =
                    id?.let { dataSnapshot.child("Account").child(it).child("StudentNumber").getValue().toString() }
                studentnumber_myclient.setText(studentnumber)

                // 의뢰 서비스 구분
                val category =
                    acceptrequest?.let { dataSnapshot.child("ConnectedRequest").child(it).child("reqCategory").getValue().toString() }
                service_myclient.setText(category)

                // 의뢰비
                val pay =
                    acceptrequest?.let { dataSnapshot.child("ConnectedRequest").child(it).child("reqPay").getValue().toString() }
                val pay_bill2 = pay + " 원 "
                pay_bill_myclient.setText(pay_bill2)

                // 의뢰 지불 방식
                val pay_method =
                    acceptrequest?.let { dataSnapshot.child("ConnectedRequest").child(it).child("reqPayMethod").getValue().toString() }
                val pay_method2 = "(" + pay_method + ")"
                pay_method_myclient.setText(pay_method2)

                // 의뢰 요청사항
                val request_message =
                    acceptrequest?.let { dataSnapshot.child("ConnectedRequest").child(it).child("reqMessage").getValue().toString() }
                val request_message2 = " - " + request_message
                request_message_myclient.setText(request_message2)

                val score = id?.let { dataSnapshot.child("Account").child(it).child("Score").getValue().toString() }
                score_myclient.setText(score)

                val connectednumber =
                    studentNum?.let {
                        dataSnapshot.child("Account").child(it).child("Connectednumber").getValue()
                            .toString().toInt()
                    }

                if (connectednumber == 0) {
                    myclientProfile.setVisibility(View.INVISIBLE)
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.commit()
                    request_message_myclient.setText("")
                    name_myclient.setText("")
                    studentnumber_myclient.setText("")
                    title_myclient.setText("")
                    service_myclient.setText("")
                    pay_bill_myclient.setText("")
                    pay_method_myclient.setText("")
                    score_myclient.setText("")
                    textView12.setText("")
                    textView18.setText("")

                } else {
                    textView16.setText("")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })

    }

    private fun fetchProfile(profileNum: Int) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        storageRef.child("profile_img/profile$profileNum.jpg")
            .downloadUrl
            .addOnSuccessListener {
                Glide.with(applicationContext)
                    .load(it)
                    .into(myclientProfile)
            }
            .addOnFailureListener {
                myclientProfile.setImageResource(R.drawable.ic_default_profile)
            }
    }

    companion object {
        const val VALUE_ONE = "1234"
    }
}