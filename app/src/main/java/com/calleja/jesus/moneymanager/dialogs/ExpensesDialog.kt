package com.calleja.jesus.moneymanager.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.os.Bundle
import com.calleja.jesus.moneymanager.R
import kotlinx.android.synthetic.main.dialog_expenses.view.*
import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Spinner

class ExpensesDialog : DialogFragment() {

    private lateinit var mSpinner: Spinner

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_expenses
            , null)

        mSpinner = view.spinner_list
        val categories: ArrayList<String> = ArrayList()
        categories.add("Ocio")
        categories.add("Transporte")
        categories.add("Alimentación")
        categories.add("Ropa y complementos")
        categories.add("Deudas y préstamos")
        categories.add("Salud")
        categories.add("Otros")

        val adp: ArrayAdapter<String>
        adp = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, categories)
        mSpinner.adapter = adp

        return AlertDialog.Builder(context!!)
            .setTitle( getString(R.string.add_expense_button))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
             //   activity!!.toast("Se ha pulsado Ok")
                val expenseAmount = view.editTextExpenseAmount.text.toString()
                val expenseMessage = view.editTextExpenseMessage.text.toString()
                val expenseCategory = view.spinner_list.selectedItem.toString()
                if (expenseAmount.isNotEmpty() && expenseMessage.isNotEmpty()) {
                    val intentInfo = Intent()
                    intentInfo.putExtra("INTENT_KEY_SECOND_FRAGMENT_DATA_AMOUNT", expenseAmount)
                    intentInfo.putExtra("INTENT_KEY_SECOND_FRAGMENT_DATA_MESSAGE", expenseMessage)
                    intentInfo.putExtra("INTENT_KEY_SECOND_FRAGMENT_DATA_CATEGORY", expenseCategory)
                    val fragment = targetFragment
                    fragment!!.onActivityResult(90, Activity.RESULT_OK, intentInfo)


                }
            }
            .setNegativeButton( getString(R.string.dialog_cancel)) { _, _ ->
               // activity!!.toast("Se ha pulsado Cancelar")
            }
            .create()
    }

}
