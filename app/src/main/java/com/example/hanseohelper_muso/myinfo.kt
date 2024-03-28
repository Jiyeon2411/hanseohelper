package com.example.hanseohelper_muso

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_myinfo.*
import kotlinx.android.synthetic.main.activity_request.*
import kotlinx.android.synthetic.main.activity_start.*

class myinfo : AppCompatActivity() {

    private val REQUEST_CODE = 0

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //팝업액티비티의 제목을 제거한다.

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val layoutParams = WindowManager.LayoutParams()
        WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.5f
        }

        window.attributes = layoutParams

        setContentView(R.layout.activity_myinfo)

        val profileNum: String? = intent.extras?.getString("ID")
        if (profileNum != null) {
            fetchProfile(profileNum.toInt())
        }

        val intent_id_myinfo: String? = intent.extras?.getString("ID")

        profile.setOnClickListener {
            val popup = PopupMenu(this, it)
            menuInflater.inflate(com.example.hanseohelper_muso.R.menu.profile_popup, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.profile_change -> {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                    R.id.profile_delete -> {
                        profile.setImageResource(R.drawable.ic_default_profile)
                        if (profileNum != null) {
                            deleteProfile(profileNum.toInt())
                        }
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popup.show()
        }


        btn_pw_change.setOnClickListener {
            var editTextNewPassword = EditText(this)
            editTextNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("패스워드 변경")
            alertDialog.setMessage("변경하고 싶은 패스워드를 입력하세요")
            alertDialog.setView(editTextNewPassword)
            alertDialog.setPositiveButton(
                "변경",
                { dialogInterface, i -> changePassword(editTextNewPassword.text.toString())
                    if (intent_id_myinfo != null) {
                        databaseReference.child("Account").child(intent_id_myinfo).child("Password").setValue(editTextNewPassword.text.toString())
                    }})
            alertDialog.setNegativeButton("취소", { dialogInterface, i -> dialogInterface.dismiss() })
            alertDialog.show()

        }

        btn_close.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                //데이터 전달하고 액티비티 닫기
                finish()
            }
        })

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // 프로필 사진
                val profile =
                    profileNum?.let {
                        dataSnapshot.child("Account").child(it).child("Profile").getValue()
                            .toString()
                    }

                // 본인 이름
                val name =
                    intent_id_myinfo?.let {
                        dataSnapshot.child("Account").child(it).child("Name").getValue().toString()
                    }
                name_myinfo.setText(name)

                // 본인 학번
                val studentnumber = intent_id_myinfo
                studentnumber_myinfo.setText(studentnumber)

                // 본인 이메일
                val email = intent_id_myinfo + "@office.hanseo.ac.kr"
                email_myinfo.setText(email)

                // 본인 번호
                val phone =
                    intent_id_myinfo?.let {
                        dataSnapshot.child("Account").child(it).child("Phone").getValue().toString()
                    }
                phone_myinfo.setText(phone)

                // 본인 평점
                val score =
                    intent_id_myinfo?.let {
                        dataSnapshot.child("Account").child(it).child("Score").getValue().toString()
                    }
                score_myinfo.setText(score)


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    val uri: Uri? = data?.data
                    Glide.with(applicationContext)
                        .load(uri)
                        .into(profile)

                    if (uri != null) {
                        updateProfile(uri)
                    }
                } catch (e: Exception) {
                }
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    companion object {
        const val VALUE_ONE = "1234"
    }

    private fun fetchProfile(profileNum: Int) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        storageRef.child("profile_img/profile$profileNum.jpg")
            .downloadUrl
            .addOnSuccessListener {
                Glide.with(applicationContext)
                    .load(it)
                    .into(profile)
            }
            .addOnFailureListener {
                profile.setImageResource(R.drawable.ic_default_profile)
            }
    }

    private fun updateProfile(profileUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val profileNum: String? = intent.extras?.getString("ID")
        val filename = "profile$profileNum.jpg"

        val file: Uri = profileUri

        val riversRef = storageRef.child("profile_img/$filename")
        val uploadTask = riversRef.putFile(file)

        uploadTask
            .addOnFailureListener {
                showToast("프로필 업로드에 실패했습니다.")
            }
            .addOnSuccessListener {
                showToast("프로필 이미지가 변경이 완료되었습니다!")
            }
    }

    private fun deleteProfile(profileNum: Int) {
        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.reference

        val desertRef = storageRef.child("profile_img/profile$profileNum.jpg") //삭제할 프로필이미지 명

        desertRef.delete()
            .addOnSuccessListener {
                showToast("프로필 삭제를 완료하였습니다!")
            }
            .addOnFailureListener {
                showToast("프로필 삭제에 실패하였습니다.")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


    fun changePassword(password: String) {
        FirebaseAuth.getInstance().currentUser!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()

                }
            }
    }

}