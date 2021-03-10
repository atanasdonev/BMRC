package com.example.caloriecalc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.caloriecalc.R
import com.example.caloriecalc.firestore.FirestoreClass
import com.example.caloriecalc.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mFireStore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.extras?.getString("user_id")
        val emailId = intent.extras?.getString("email_id")

        Log.e("MainActivity", "userid - $userId")

        val aUser = User()

        if (userId != null) {

            mFireStore.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d("MainActivity", "DocumentSnapshot data ${document.data}")

                            Log.d("MainActivity", "${document.data?.getValue("age")}")

                            aUser.gender = document.data?.getValue("gender") as String
                            aUser.age = document.data?.getValue("age").toString().toInt()
                            aUser.weight = document.data?.getValue("weight").toString().toInt()
                            aUser.height = document.data?.getValue("height").toString().toInt()
                            Log.d("MainActivity", "done - ${aUser.gender}")
                            calculateBMR(aUser)

                        } else {
                            Log.d("does not exist", "There is a document")
                        }
                    }
                    .addOnFailureListener { exception -> Log.d("error", "Failed with", exception) }
        }

      btn_logout.setOnClickListener {
            //Logout from app
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun calculateBMR(aUser: User){
        var userBMR = 0.0

        userBMR = if(aUser.gender.toLowerCase() == "male") {
            ((10.0 * aUser.weight) + (6.25 * aUser.height) - (5.0 * aUser.age) + 5)
        }else {
            ((10.0 * aUser.weight) + (6.25 * aUser.height) - (5.0 * aUser.age) - 161)
        }
        tv_BMR.text = userBMR.toInt().toString()
        tv_BMR_row1.text = (userBMR + (userBMR/5)).toInt().toString()
        tv_BMR_row2.text = (userBMR + ((37.5078/100)*userBMR)).toInt().toString()
        tv_BMR_row3.text = (userBMR + ((46.4798/100)*userBMR)).toInt().toString()
        tv_BMR_row4.text = (userBMR + ((55.0156/100)*userBMR)).toInt().toString()
        tv_BMR_row5.text = (userBMR + ((72.5234/100)*userBMR)).toInt().toString()
        tv_BMR_row6.text = (userBMR + ((90.0312/100)*userBMR)).toInt().toString()
    }

}