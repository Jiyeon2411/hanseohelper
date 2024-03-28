package com.example.hanseohelper_muso

import android.content.Intent
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_score.*

class score : AppCompatActivity() {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        score_score.setText("0점")

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            score_score.text = "${fl} 점"
        }

        val intent_id_score: String? = intent.extras?.getString("ID")
        val intent_id2_score: String? = intent.extras?.getString("ID2")


        Toast.makeText(this@score, intent_id2_score, Toast.LENGTH_LONG).show()

        btn_evaluate.setOnClickListener {

            if (intent_id2_score != null) {

                val scoredata = ratingBar.getRating().toString().toDouble()

                databaseReference.addListenerForSingleValueEvent(object :
                    ValueEventListener { // 딱 한번만 갱신
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val scoreoriginal =
                            intent_id2_score.let {
                                dataSnapshot.child("Account").child(it).child("Score").getValue()
                                    .toString().toDouble()
                            }

                        val scorenumber =
                            intent_id2_score.let {
                                dataSnapshot.child("Account").child(it).child("Scorenumber")
                                    .getValue()
                                    .toString().toInt()
                            } + 1
                        databaseReference.child("Account").child(intent_id2_score).child("Scorenumber").setValue(scorenumber)

                        val scorefinal = (scoreoriginal*(scorenumber-1)+scoredata) / scorenumber
                        databaseReference.child("Account").child(intent_id2_score).child("Scorenumber")
                            .setValue(scorenumber)
                        databaseReference.child("Account").child(intent_id2_score).child("Score")
                            .setValue(scorefinal)

                        val intent = Intent(this@score, main::class.java)
                        intent.putExtra("ID",intent_id_score)
                        startActivity(intent)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
            }
        }
    }
}