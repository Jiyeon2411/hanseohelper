package com.example.hanseohelper_muso

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class list : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_list)
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.list_fragment, Fraglike())
        transaction.commit()
    }
}