package com.calleja.jesus.moneymanager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.calleja.jesus.moneymanager.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth

//Flujo del Login y el MainActivity
class MainFlow : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mAuth.currentUser == null) {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        } else {
            goToActivity<MainActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }

        finish()
    }
    }
