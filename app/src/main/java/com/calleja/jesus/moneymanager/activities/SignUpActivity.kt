package com.calleja.jesus.moneymanager.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.calleja.jesus.moneymanager.*
import com.calleja.jesus.moneymanager.utils.IbanGenerator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*



class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var balanceDBRef: CollectionReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        buttonGoBackLoginSignUp.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        buttonRegistro.setOnClickListener{
            val email = editTextEmailSignUp.text.toString()
            val pass = editTextPasswordSignUp.text.toString()
            val confirmPass = editTextConfirmPasswordSignUp.text.toString()

            if(validateEmail(email) && validatePass(pass) && validateConfirmPass(pass,confirmPass)) {
                createUserWithEmail(email,pass)
            }
            else{
                Toast.makeText(this,
                    "Faltan campos de datos por rellenar o las contraseñas no coinciden",Toast.LENGTH_SHORT).show()
            }

        }

        editTextEmailSignUp.validate {
        editTextEmailSignUp.error = if (validateEmail(it)) null else
            "El email no es válido"
        }

        editTextPasswordSignUp.validate {
            editTextPasswordSignUp.error = if (validatePass(it)) null else
                "La contraseña debe contener al menos 6 carácteres con al menos una minúscula, una mayúscula y un número"
        }

        editTextConfirmPasswordSignUp.validate {
            editTextConfirmPasswordSignUp.error =
                if (validateConfirmPass(editTextPasswordSignUp.text.toString(),it)) null else
                    "Las contraseñas no coinciden"
        }
    }


    private fun createUserWithEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this) {
                        Toast.makeText(this, "Se ha enviado email de confirmación",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }

                } else {
                    Toast.makeText(this, "Ha ocurrido un error inesperado",Toast.LENGTH_SHORT).show()
                }
            }

    }
}
