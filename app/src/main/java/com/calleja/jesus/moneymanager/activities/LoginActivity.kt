package com.calleja.jesus.moneymanager.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.calleja.jesus.moneymanager.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val mAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient()}
    private val RQ_GOOGLE_SIGN_IN = 85

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        textViewShowLicenses.setOnClickListener{
            startActivity(Intent(this, ShowLicensesActivity::class.java))
        }

        loginButton.setOnClickListener{
            val email: String = editTextEmailLogin.text.toString()
            val pass: String = editTextPasswordLogin.text.toString()
            if(validateEmail(email) && validatePass(pass)) {
                loginWithEmail(email,pass)
            }
            else{
                Toast.makeText(this,
                    "Faltan campos de datos por rellenar o las contraseñas no coinciden",Toast.LENGTH_SHORT).show()
            }
        }

        textViewForgotPassword.setOnClickListener{
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }

        buttonSignUp.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        buttonLoginGoogle.setOnClickListener{
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RQ_GOOGLE_SIGN_IN)

        }

        editTextEmailLogin.validate {
            editTextEmailLogin.error = if (validateEmail(it)) null else
                "El email no es válido"
        }

        editTextPasswordLogin.validate {
            editTextPasswordLogin.error = if (validatePass(it)) null else
                "La contraseña debe contener al menos 6 carácteres con al menos una minúscula, una mayúscula y un número"
        }
    }

    private fun getGoogleApiClient(): GoogleApiClient {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()
    }

    private fun loginWithGoogle(googleAccount: GoogleSignInAccount){
        val credentials = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credentials).addOnCompleteListener(this){
            if(mGoogleApiClient.isConnected){
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            }
            goToActivity<MainActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }

    }

    private fun loginWithEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                if (mAuth.currentUser!!.isEmailVerified) {
                    //Toast.makeText(this, "El usuario ha iniciado sesión", Toast.LENGTH_SHORT).show()
                    goToActivity<MainActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                } else {
                    Toast.makeText(this, "El usuario aún no ha verificado su email", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RQ_GOOGLE_SIGN_IN){
            val requestResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (requestResult.isSuccess){
                val account = requestResult.signInAccount
                loginWithGoogle(account!!)
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("Ha fallado la conexión")
    }
}