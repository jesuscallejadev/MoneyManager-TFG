package com.calleja.jesus.moneymanager.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.calleja.jesus.moneymanager.R
import com.calleja.jesus.moneymanager.models.NewRateEvent
import com.calleja.jesus.moneymanager.models.Rate
import com.calleja.jesus.moneymanager.toast
import com.calleja.jesus.moneymanager.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_expenses.view.*
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*
import kotlin.collections.ArrayList
import com.calleja.jesus.moneymanager.MainActivity as MainActivity1
import com.calleja.jesus.moneymanager.MainActivity as MoneymanagerMainActivity
import com.calleja.jesus.moneymanager.dialogs.RateDialog as RateDialog

class RateDialog : DialogFragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        setUpCurrentUser()
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rate, null)

       return AlertDialog.Builder(context!!)
            .setTitle( getString(R.string.dialog_rate_title))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_rate_ok)) { _, _ ->
                val textRate = view.editTextRateFeedback.text.toString()
                if (textRate.isNotEmpty()) {
                    val imageURL = currentUser.photoUrl?.toString() ?: run { "" }
                    val rate = Rate(currentUser.uid, textRate, view.ratingBarFeedback.rating, Date(), imageURL)
                    RxBus.publish(NewRateEvent(rate))
                }
            }
            .setNegativeButton( getString(R.string.dialog_cancel)) { _, _ ->
               // activity!!.toast("Se ha pulsado Cancelar")
            }
            .create()
    }

}
