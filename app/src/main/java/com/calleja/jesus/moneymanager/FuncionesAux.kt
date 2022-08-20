package com.calleja.jesus.moneymanager

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Pattern

fun Activity.toast(mensaje: CharSequence, duracion: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, mensaje, duracion).show()
//fun Activity.toast(resourceId: Int, duracion: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, resourceId, duracion).show()

fun ViewGroup.inflate(layoutId:Int) = LayoutInflater.from(context).inflate(layoutId, this, false)


inline fun <reified T : Activity> Activity.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent)
}

fun EditText.validate(funcion: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            funcion(editable.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })

}

    fun Activity.validateEmail(email: String): Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    //La pass debe cumplir: 1 num / 1 Minuscula / 1 Mayuscula / 1 Special / Min 6 Caracteres
    // val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$"
    // La pass debe cumplir: 1 num / 1 Minuscula / 1 Mayuscula / Min 6 Caracteres
    fun Activity.validatePass(pass: String): Boolean{
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(pass).matches()
    }

    fun Activity.validateConfirmPass(pass: String, confirmPass: String): Boolean{
        return (pass.compareTo(confirmPass) == 0)
    }

