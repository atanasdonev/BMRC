package com.example.caloriecalc.firestore

import android.app.Activity
import android.util.Log
import com.example.caloriecalc.activity.LoginActivity
import com.example.caloriecalc.activity.MainActivity
import com.example.caloriecalc.activity.RegisterActivity
import com.example.caloriecalc.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.concurrent.timerTask

open class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection("users")
                .document(userInfo.id)
                .set(userInfo, SetOptions.merge())
    }

    private fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

   fun getUserDetails(activity: Activity):User {
       val aUser = User()
        mFireStore.collection("users")
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("exists", "DocumentSnapshot data ${document.data}")

                        Log.d("exists", "${document.data?.getValue("gender")}")

                        aUser.gender = document.data?.getValue("gender") as String
                        aUser.age = document.data?.getValue("age").toString().toInt()
                        aUser.weight = document.data?.getValue("weight").toString().toInt()
                        aUser.height = document.data?.getValue("height").toString().toInt()

                    } else {
                        Log.d("does not exist", "There is a document")
                    }
                }
                .addOnFailureListener { exception -> Log.d("error", "Failed with", exception) }
       return aUser
    }

}

