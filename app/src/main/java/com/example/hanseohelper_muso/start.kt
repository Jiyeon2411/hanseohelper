package com.example.hanseohelper_muso

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_start.*

class start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val anim = AnimationUtils.loadAnimation(this, R.anim.animation)
        logostart.startAnimation(anim)    // 로고에 애니메이션 효과 추가

        logostart.setOnClickListener {
            showSettingPopup()
        }
    }

    private fun showSettingPopup() {
        val intent = Intent(this, login::class.java)
        AlertDialog.Builder(this).run {
            setTitle("주의 사항")
            setIcon(android.R.drawable.ic_dialog_alert)
            setMessage("본 어플은 어플 사용 시 발생하는 문제에 대해 어떠한 법적 책임도 지지 않습니다." +
                    "\n\n이에 동의하십니까?")
            setPositiveButton("동의") { dialog, which ->
                startActivity(intent)
            }
            setNeutralButton("비동의", null)
            show()
        }
    }

}



