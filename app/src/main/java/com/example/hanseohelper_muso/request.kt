package com.example.hanseohelper_muso

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_request.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class request : AppCompatActivity() {

    private var mEtAddress: EditText? = null
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //팝업액티비티의 제목을 제거한다.
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.5f
        window.attributes = layoutParams
        setContentView(R.layout.activity_request)


        mEtAddress = findViewById(R.id.data_address)
        mEtAddress?.setFocusable(false)
        mEtAddress?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@request, searchaddress::class.java)
            getSearchResult.launch(intent)
        })

        val intent_id_request = intent.extras?.getString("ID")
        val id = intent_id_request

        //확인버튼 이벤트
        val button_ok: Button = findViewById<View>(R.id.btn1) as Button
        button_ok.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val requestnumber = id?.let {
                            snapshot.child("Account")
                                .child(it).child("Requestnumber").getValue().toString().toInt()
                        }
                        if (requestnumber == 1) {

                            Toast.makeText(this@request, "의뢰는 한번씩만 등록할 수 있습니다.", Toast.LENGTH_LONG)
                                .show()
                        }else {
                            // 의뢰제목
                            val theme = data_title.text.toString()
                            databaseReference.child("Request").child(theme).child("reqTheme")
                                .setValue(theme)

                            // 의뢰 서비스
                            if (service1.isChecked) {
                                val service = findViewById<RadioButton>(R.id.service1).text.toString()
                                databaseReference.child("Request").child(theme).child("reqCategory")
                                    .setValue(service)
                                databaseReference.child("Request").child(theme).child("reqImage")
                                    .setValue("https://firebasestorage.googleapis.com/v0/b/hanseo-helper.appspot.com/o/post.png?alt=media&token=5dfb87fe-68bd-4b4d-b719-dd1a213ee207")

                            }
                            if (service2.isChecked) {
                                val service = findViewById<RadioButton>(R.id.service2).text.toString()
                                databaseReference.child("Request").child(theme).child("reqCategory")
                                    .setValue(service)
                                databaseReference.child("Request").child(theme).child("reqImage")
                                    .setValue("https://firebasestorage.googleapis.com/v0/b/hanseo-helper.appspot.com/o/clean.png?alt=media&token=e30c8c50-cd19-4eaf-88d4-0c62bee52eb8")
                            }
                            if (service3.isChecked) {
                                val service = findViewById<RadioButton>(R.id.service3).text.toString()
                                databaseReference.child("Request").child(theme).child("reqCategory")
                                    .setValue(service)
                                databaseReference.child("Request").child(theme).child("reqImage")
                                    .setValue("https://firebasestorage.googleapis.com/v0/b/hanseo-helper.appspot.com/o/bug.png?alt=media&token=21435887-77a6-4743-9787-27a2bc0e9e29")
                            }
                            if (service4.isChecked) {
                                val service = findViewById<RadioButton>(R.id.service4).text.toString()
                                databaseReference.child("Request").child(theme).child("reqCategory")
                                    .setValue(service)
                                databaseReference.child("Request").child(theme).child("reqImage")
                                    .setValue("https://firebasestorage.googleapis.com/v0/b/hanseo-helper.appspot.com/o/study.png?alt=media&token=ca758a5e-aabd-4856-b725-4df14a08bb89")
                            }
                            if (service5.isChecked) {
                                val service = findViewById<RadioButton>(R.id.service5).text.toString()
                                databaseReference.child("Request").child(theme).child("reqCategory")
                                    .setValue(service)
                                databaseReference.child("Request").child(theme).child("reqImage")
                                    .setValue("https://firebasestorage.googleapis.com/v0/b/hanseo-helper.appspot.com/o/pet.png?alt=media&token=ad4e8a59-09fb-49fa-9ea0-87d9ff367f48")
                            }
                            if (service6.isChecked) {
                                val service = findViewById<RadioButton>(R.id.service6).text.toString()
                                databaseReference.child("Request").child(theme).child("reqCategory")
                                    .setValue(service)
                                databaseReference.child("Request").child(theme).child("reqImage")
                                    .setValue("https://firebasestorage.googleapis.com/v0/b/hanseo-helper.appspot.com/o/etcicon.png?alt=media&token=096a6db2-801b-481c-9836-935281013dac")
                            }

                            // 의뢰 결제방법
                            if (pay1.isChecked) {
                                val pay_method = findViewById<RadioButton>(R.id.pay1).text.toString()
                                databaseReference.child("Request").child(theme).child("reqPayMethod")
                                    .setValue(pay_method)
                            }
                            if (pay2.isChecked) {
                                val pay_method = findViewById<RadioButton>(R.id.pay2).text.toString()
                                databaseReference.child("Request").child(theme).child("reqPayMethod")
                                    .setValue(pay_method)
                            }

                            // 의뢰 주소

                            val address = data_address.text.toString()
                            databaseReference.child("Request").child(theme).child("reqAddress")
                                .setValue(address)

                            // 의뢰 원하는 성별
                            if (gender1.isChecked) {
                                val gender = findViewById<RadioButton>(R.id.gender1).text.toString()
                                databaseReference.child("Request").child(theme).child("reqGender")
                                    .setValue(gender)
                            }
                            if (gender2.isChecked) {
                                val gender = findViewById<RadioButton>(R.id.gender2).text.toString()
                                databaseReference.child("Request").child(theme).child("reqGender")
                                    .setValue(gender)
                            }
                            if (gender3.isChecked) {
                                val gender = findViewById<RadioButton>(R.id.gender3).text.toString()
                                databaseReference.child("Request").child(theme).child("reqGender")
                                    .setValue(gender)
                            }

                            // 의뢰비
                            val pay_bill = data_pay_bill.text.toString()
                            databaseReference.child("Request").child(theme).child("reqPay")
                                .setValue(pay_bill)

                            // 의뢰 요청사항
                            val request_message = data_request_message.text.toString()
                            databaseReference.child("Request").child(theme).child("reqMessage")
                                .setValue(request_message)

                            // 의뢰한 사람 ID(학번) 받아오기
                            val request_who = id
                            databaseReference.child("Request").child(theme).child("reqWho")
                                .setValue(request_who)

                            // account에 저장할 의뢰제목
                            if (intent_id_request != null) {
                                databaseReference.child("Account").child(intent_id_request).child("Request")
                                    .setValue(theme)
                                databaseReference.child("Account").child(intent_id_request).child("Requestnumber")
                                    .setValue(1)
                            }

                            //현재 시간 가져오는 함수
                            val now = LocalDateTime.now()
                            val format = DateTimeFormatter.ofPattern("MM월 dd일 HH:mm:ss")
                            val nowTime: String = now.format(format);

                            databaseReference.child("Request").child(theme).child("time").setValue(nowTime)

                            Toast.makeText(this@request, "의뢰 등록이 완료되었습니다.", Toast.LENGTH_LONG).show()

                            val intent = Intent(this@request, main::class.java)
                            intent.putExtra("ID",intent_id_request)
                            startActivity(intent)

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        })
    }

    private val getSearchResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                val data = result.data!!.getStringExtra("data")
                mEtAddress!!.setText(data)
            }
        }
    }

    //바깥영역 클릭 방지와 백 버튼 차단
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (event.action == MotionEvent.ACTION_OUTSIDE) {
            false
        } else true
    } //    @Override
    //    public void onBackPressed() {
    //        return;
    //    }
}