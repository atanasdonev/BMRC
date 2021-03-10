package com.example.caloriecalc.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.example.caloriecalc.R
import com.example.caloriecalc.firestore.FirestoreClass
import com.example.caloriecalc.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tv_login.setOnClickListener{
            onBackPressed()
        }

        btn_register.setOnClickListener {
            registerUser()
        }
    }

    private fun validateRegisterDetails(): Boolean{
        return when{

            TextUtils.isEmpty(et_register_email.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_register_password.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_register_age.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_age), true)
                false
            }

            TextUtils.isEmpty(et_register_height.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_height), true)
                false
            }

            TextUtils.isEmpty(et_register_weight.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_weight), true)
                false
            }

            TextUtils.isEmpty(et_register_gender.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_gender), true)
                false
            }
            else -> {
               // showErrorSnackBar("Registry successful.", false)
                true
            }
        }
    }

    private fun registerUser(){
        if(validateRegisterDetails()){

            val email: String = et_register_email.text.toString().trim{ it <= ' '}
            val password: String = et_register_password.text.toString().trim{ it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            OnCompleteListener <AuthResult> { task ->

                                if(task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    val user = User(
                                            firebaseUser.uid,
                                            et_register_age.text.toString().toInt(),
                                            et_register_weight.text.toString().toInt(),
                                            et_register_height.text.toString().toInt(),
                                            et_register_gender.text.toString()
                                    )

                                    FirestoreClass().registerUser(this@RegisterActivity, user)

                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id",firebaseUser.uid)
                                    intent.putExtra("email_id",email)
                                    startActivity(intent)
                                    finish()
                                } else{
                                    showErrorSnackBar(task.exception!!.message.toString(), true)
                                }
                            }
                    )
        }
    }

}