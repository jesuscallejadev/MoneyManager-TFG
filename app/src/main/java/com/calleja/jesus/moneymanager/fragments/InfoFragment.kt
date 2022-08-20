package com.calleja.jesus.moneymanager.fragments

import android.os.Bundle
import android.support.v4.app.Fragment

import com.calleja.jesus.moneymanager.R
import com.calleja.jesus.moneymanager.dialogs.ExpensesDialog
import com.calleja.jesus.moneymanager.toast
import com.calleja.jesus.moneymanager.utils.CircleTransform
import com.calleja.jesus.moneymanager.utils.IbanGenerator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info.view.*
import android.app.Activity
import android.content.Intent
import android.view.*
import com.calleja.jesus.moneymanager.models.Expense
import com.google.firebase.firestore.SetOptions


class InfoFragment : Fragment(){

    private lateinit var _view: View
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var balanceDBRef: CollectionReference
    private lateinit var ibanDBRef: CollectionReference
    private lateinit var paymentDBRef: CollectionReference
    private lateinit var totalExpensesDBRef : CollectionReference
    private lateinit var expenseDBRef: CollectionReference
    private object RequestCode {
        const val REQ_CODE_SECOND_FRAGMENT = 90
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _view =  inflater.inflate(R.layout.fragment_info, container, false)
        setUpCurrentUser()
        setUpCurrentUserInfoUI()
        setUpBalanceDB()
        setUpIbanDB()
        setUpPaymentDB()
        setUpExpenseDB()
        setUpTotalExpensesDB()
        linkIban()
        setUpAddExpenseButton()
        return _view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCode.REQ_CODE_SECOND_FRAGMENT) {
                var secondFragmentAmount = data!!.getStringExtra("INTENT_KEY_SECOND_FRAGMENT_DATA_AMOUNT")
                var secondFragmentMessage = data!!.getStringExtra("INTENT_KEY_SECOND_FRAGMENT_DATA_MESSAGE")
                var secondFragmentCategory = data!!.getStringExtra("INTENT_KEY_SECOND_FRAGMENT_DATA_CATEGORY")
                if (!hasEnoughBalance(secondFragmentAmount)) {
                    activity!!.toast("No puede registrar un gasto superior al saldo disponible")
                }
                decreaseBalance(secondFragmentAmount)
                saveExpense(secondFragmentAmount, secondFragmentMessage, secondFragmentCategory)
            }
        }
    }


    private fun linkIban() {
        var flagNotInitializedIban = false
        ibanDBRef.document("ibanDocument").get().addOnSuccessListener {
            if(it.data != null) {
                try {
                    var iban = it.data!!.getValue(currentUser.uid).toString()
                    _view.userIban.text = iban
                } catch (e:NoSuchElementException) {
                    flagNotInitializedIban = true
                }
            } else {
                flagNotInitializedIban = true
            }
        } .addOnCompleteListener {
            if(flagNotInitializedIban) {
                initializeIban()
            } else {
                linkTotalExpenses()
                updateBalance()
            }
        }
    }

    private fun linkTotalExpenses() {
        var flagNotInitializedTotalExpenses = false
        totalExpensesDBRef.document("totalExpensesDocument").get().addOnSuccessListener {
            if(it.data != null) {
                try {
                    var totalExpense = it.data!!.getValue(_view.userIban.text.toString())
                    _view.userExpenses.text = totalExpense.toString()
                } catch (e:NoSuchElementException) {
                    flagNotInitializedTotalExpenses = true
                }
            } else {
                flagNotInitializedTotalExpenses = true
            }
        } .addOnCompleteListener {
            if(flagNotInitializedTotalExpenses) {
                initializeTotalExpenses()
            }
        }
    }

    private fun updateBalance() {
        var increasedBalance = 0.0
        paymentDBRef.document(_view.userIban.text.toString()).get().addOnSuccessListener {
            if(it.data!=null){
                var amount = it.data!!.getValue("amount").toString()
                var senderName = it.data!!.getValue("senderName").toString()
                var message = it.data!!.getValue("message").toString()
                activity!!.toast("Ha recibido un pago de: $senderName con importe: $amount y concepto: $message")
                increasedBalance = amount.toDouble()
                paymentDBRef.document(_view.userIban.text.toString()).delete()
            }
        }
            .addOnCompleteListener {

                balanceDBRef.document(_view.userIban.text.toString()).get().addOnSuccessListener {
                    if (it.data != null) {
                        try {
                            var currentBalance = it.data!!.getValue(_view.userIban.text.toString())
                            currentBalance = currentBalance.toString().toDouble()
                            if(increasedBalance != 0.0) {
                                currentBalance += increasedBalance
                                saveBalance(currentBalance.toString())
                            }
                            if (currentBalance == 0.0) {
                                _view.userBalance.text = "0 EUR"
                            } else {
                                _view.userBalance.text = "$currentBalance EUR"
                            }
                        } catch (e: NoSuchElementException) {
                            _view.userBalance.text = "0 EUR"
                            saveBalance("0")
                        }
                    } else {
                        _view.userBalance.text = "0 EUR"
                        saveBalance("0")
                    }
                }
            }
    }

    private fun hasEnoughBalance(amount: String): Boolean {
        val expense = amount.toDouble()
        val currentBalance = _view.userBalance.text.toString().removeSuffix(" EUR").toDouble()
        if(expense <= currentBalance){
            return true
        }
        return false
    }

    private fun decreaseBalance(amount: String) {
        val expense = amount.toDouble()
        val currentBalance = _view.userBalance.text.toString().removeSuffix(" EUR").toDouble()
        var decreasedBalance = currentBalance - expense
        saveBalance(decreasedBalance.toString())
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpBalanceDB(){
        balanceDBRef = store.collection("balances")
    }

    private fun setUpIbanDB(){
        ibanDBRef = store.collection("ibans")
    }

    private fun setUpPaymentDB(){
        paymentDBRef = store.collection("payments")
    }

    private fun setUpExpenseDB(){
        expenseDBRef = store.collection("expenses")
    }

    private fun setUpTotalExpensesDB(){
        totalExpensesDBRef = store.collection("totalExpenses")
    }

    private fun setUpAddExpenseButton() {
        _view.editBalanceButton.setOnClickListener {
            val dialog = ExpensesDialog()
            dialog.setTargetFragment(this, RequestCode.REQ_CODE_SECOND_FRAGMENT)
            dialog.show(fragmentManager, "")
        }
    }

    private fun saveBalance(userBalance: String) {
        val newBalance = HashMap<String, String>()
        newBalance[_view.userIban.text.toString()] = userBalance
                        balanceDBRef.document(_view.userIban.text.toString()).set(newBalance)
                            .addOnSuccessListener {
                                activity!!.toast("Saldo actualizado correctamente")
                            }
                            .addOnFailureListener {
                                activity!!.toast("Error al actualizar el saldo")
                            }
                            .addOnCompleteListener {
                                updateBalance()
                            }
                }

    private fun increaseTotalExpenses() {
        val newTotalExpenses = HashMap<String, Int>()
        newTotalExpenses[_view.userIban.text.toString()] = _view.userExpenses.text.toString().toInt() + 1
        totalExpensesDBRef.document("totalExpensesDocument").set(newTotalExpenses)
            .addOnCompleteListener {
               _view.userExpenses.text = (_view.userExpenses.text.toString().toInt() + 1).toString()
            }
    }

    private fun saveExpense(amount: String, message: String, category: String) {
        val expense = Expense(amount, message, category)
        val newExpense = HashMap<String, Expense>()
        newExpense[_view.userIban.text.toString()] = expense
        expenseDBRef.add(newExpense)
            .addOnSuccessListener {
                activity!!.toast("Se ha registrado el gasto correctamente")
            }
            .addOnFailureListener {
                activity!!.toast("Error al registrar el gasto")
            }
            .addOnCompleteListener {
                increaseTotalExpenses()
                //updateBalance()
            }
    }

    private fun initializeIban() {
        val newIban = IbanGenerator.generateIban("ES", "IBAN")
        activity!!.toast("Su IBAN ES: $newIban")
        val iban = HashMap<String, String>()
        iban[mAuth.currentUser!!.uid] = newIban
        ibanDBRef.document("ibanDocument").set(iban, SetOptions.merge())
            .addOnSuccessListener {
                _view.userIban.text = newIban
            }
            .addOnFailureListener {
              //  activity!!.toast("Error al guardar el iban")
            }
            .addOnCompleteListener {
                updateBalance()
            }
    }

    private fun initializeTotalExpenses() {
        _view.userExpenses.text = "0"
        val totalExpenses = HashMap<String, Int>()
        totalExpenses[_view.userIban.text.toString()] = 0
        totalExpensesDBRef.document("totalExpensesDocument").set(totalExpenses)
    }

    private fun setUpCurrentUserInfoUI() {
        _view.textViewInfoEmail.text = currentUser.email
        _view.textViewInfoName.text = currentUser.displayName?.let {currentUser.displayName}
            ?: run {getString(R.string.info_no_name)}

        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).resize(300,300)
                .centerCrop().transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        } ?: run {
            Picasso.get().load(R.drawable.ic_person).resize(300,300)
                .centerCrop().transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        }
    }
}
