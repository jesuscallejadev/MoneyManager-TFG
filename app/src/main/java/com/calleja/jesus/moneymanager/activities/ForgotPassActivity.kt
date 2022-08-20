package com.calleja.jesus.moneymanager.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.calleja.jesus.moneymanager.R
import com.calleja.jesus.moneymanager.toast
import com.calleja.jesus.moneymanager.validate
import com.calleja.jesus.moneymanager.validateEmail
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_pass.*

class ForgotPassActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        editTextEmailForgotPass.validate {
            editTextEmailForgotPass.error = if (validateEmail(it)) null else
                "El email no es válido"
        }


        buttonGoBackLoginForgotPass.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        buttonForgotPass.setOnClickListener{
            val email = editTextEmailForgotPass.text.toString()
            if(validateEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) {
                  toast("Se ha enviado el email para restablecer su contraseña")
                    startActivity(Intent(this, LoginActivity::class.java))
                }
        }
            else{
                toast("Comprueba que la dirección de correo es correcta")
            }
        }
    }
}
