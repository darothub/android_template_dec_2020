package com.peacedude.lassod_tailor_app.ui.subscription

import IsEmptyCheck
import android.app.Dialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.paystack.android.Paystack.TransactionCallback
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.AddCardRes
import com.peacedude.lassod_tailor_app.model.response.AddCardWrapper
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_subscription.*
import kotlinx.android.synthetic.main.fragment_add_card.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [AddCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCardFragment : DaggerFragment() {

    val title by lazy {
        getName()
    }

    val header by lazy {
        authViewModel.header
    }

    val currentUser by lazy {
        authViewModel.currentUser
    }

    val cardDetailIncludedBtnLayout by lazy {
        add_card_fragment_card_detail_layout.findViewById<View>(R.id.card_details_include_btn)
    }

    val cardDetailIncludedCardNumberEt by lazy {
        add_card_fragment_card_detail_layout.findViewById<TextInputEditText>(R.id.card_details_card_number_et)
    }
    val cardDetailIncludedCVVEt by lazy {
        add_card_fragment_card_detail_layout.findViewById<EditText>(R.id.card_details_card_cvv_et)
    }

    val cardDetailIncludedMonthEt by lazy {
        add_card_fragment_card_detail_layout.findViewById<EditText>(R.id.card_details_card_month_et)
    }
    val customerNameTv by lazy{
        add_card_fragment_card_detail_layout.findViewById<TextView>(R.id.card_details_customer_name_tv)
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    private val dialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.success_dialog_layout)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val payBtnBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        payBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorGreen),
            PorterDuff.Mode.SRC_IN
        )

//        cardDetailIncludedMonthEt.doOnTextChanged { text, start, before, count ->
//            val expiryMonthYearPattern = Regex("""^(?!.*1[3-9])[0-1][0-9]/2[0-9]$""")
//            when{
//
//                text?.length == 2 && text?.length < 3->{
//                    val t = "$text/"
//                    cardDetailIncludedMonthEt.setText(t)
//                    cardDetailIncludedMonthEt.setSelection(cardDetailIncludedMonthEt.text.length )
//
//                }
//                !expiryMonthYearPattern.matches(text.toString()) -> {
//                    cardDetailIncludedMonthEt.error = getString(R.string.invalid_expiry_date_str)
//                }
//            }
//        }

//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.setData(Uri.parse("https://www.checkout.paystack.com/7zcxkg2ob9kyhvv"))
//        intent.setPackage("com.android.chrome") // package of SafeBrowser App
//
//        startActivity(intent)

//        webviewone.loadUrl("https://www.checkout.paystack.com/7zcxkg2ob9kyhvv")
//        webviewone.show()


        cardDetailIncludedCardNumberEt.doOnTextChanged { text, start, before, count ->
            i(title, "herefirst ${text?.length}")
            val t = cardDetailIncludedCardNumberEt.text.toString().length
            i(title, "heresecond $t")
            when{
                text?.length == 16 && !text.contains("-")->{
                    val first = text.substring(0..3)
                    val second = text.substring(4..7)
                    val third = text.substring(8..11)
                    val last = text.substring(12..15)
                    cardDetailIncludedCardNumberEt.setText("$first-$second-$third-$last")

                }
            }
        }

        buttonTransactions({
            button = cardDetailIncludedBtnLayout.findViewById(R.id.btn)
            button.background = payBtnBackground
            button.text = getString(R.string.pay)
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )
        }, {
            button.setOnClickListener {
                val card = Card("4682190123315851", 12, 21, "003")
                if(card.isValid){
                    i(title, "Yes valid")
                }else{
                    i(title, "Not valid")
                }
//                val checkforEmptyEt = IsEmptyCheck(
//                    cardDetailIncludedCardNumberEt,
//                    cardDetailIncludedCVVEt,
//                    cardDetailIncludedMonthEt
//                )
//                val expiryMonthYearPattern = Regex("""^(?!.*1[3-9])[0-1][0-9]/2[0-9]$""")
//                val expiryDate = cardDetailIncludedMonthEt.text.toString()
//                val matches = expiryMonthYearPattern.matches(expiryDate)
//                val cvvPattern = Regex("""\d{3}""")
//                val cvv = cardDetailIncludedCVVEt.text.toString()
//                val cvvMatches = cvvPattern.matches(cvv)
//                when {
//                    checkforEmptyEt != null -> {
//                        checkforEmptyEt.error = "${checkforEmptyEt.tag} is empty"
//                        requireActivity().gdToast("${checkforEmptyEt.tag} is empty", Gravity.BOTTOM)
//                    }
//                    matches == false -> {
//                        cardDetailIncludedMonthEt.error =
//                            getString(R.string.invalid_expiry_date_str)
//                        requireActivity().gdToast(
//                            getString(R.string.invalid_expiry_date_str),
//                            Gravity.BOTTOM
//                        )
//                    }
//                    cvvMatches == false -> {
//                        cardDetailIncludedCVVEt.error = getString(R.string.invalid_cvv_str)
//                        requireActivity().gdToast(
//                            getString(R.string.invalid_cvv_str),
//                            Gravity.BOTTOM
//                        )
//                    }
//                    else -> {
//                        val accessCode = GlobalVariables.globalString
//                        i(title, "access code${GlobalVariables.globalString}")
//                        val number = cardDetailIncludedCardNumberEt.text.toString()
//                        val date = cardDetailIncludedMonthEt.text.toString()
//                        val dateSplit = date.split("/")
//                        val month = dateSplit[0].toInt()
//                        val year = dateSplit[1].toInt()
//                        val card = Card("5078 5078 5078 5078 12", 11, 21, "408")
//                        if(card.isValid){
//                            i(title, "Yes valid")
//                        }else{
//                            i(title, "Not valid")
//                        }
//                        val charge = Charge()
////                        charge.card = card
////                        charge.accessCode
//
//
//
//                        PaystackSdk.chargeCard(
//                            requireActivity(),
//                            charge,
//                            object : TransactionCallback {
//                                override fun onSuccess(transaction: Transaction?) {
//                                    // This is called only after transaction is deemed successful.
//                                    // Retrieve the transaction, and send its reference to your server
//                                    // for verification.
//                                    i(title, "OnSuccess $transaction")
//                                }
//
//                                override fun beforeValidate(transaction: Transaction?) {
//                                    // This is called only before requesting OTP.
//                                    // Save reference so you may send to server. If
//                                    // error occurs with OTP, you should still verify on server.
//
//                                    i(title, "BeforeValidate ${transaction?.reference}")
//                                }
//
//                                override fun onError(
//                                    error: Throwable?,
//                                    transaction: Transaction?
//                                ) {
//                                    //handle error here
//                                    i(title, "OnError $error")
//                                }
//                            })
//                    }
//                }
            }
        })
        add_card_fragment_add_card_layout.setOnClickListener {

            customerNameTv.text = currentUser?.firstName
            add_fragment_vf.showNext()
//            CoroutineScope(Main).launch {
//                val email = currentUser?.email
//                i(title, "User $email")
//                authViewModel.addCard(header, email, "1000")
//                    .catch {
//                        i(title, "Error on flow ${it.message}")
//                    }
//                    .collect {
//                        onFlowResponse<AddCardWrapper<AddCardRes>>( response = it) {
//                            GlobalVariables.globalString = it?.data?.accessCode.toString()
//                            val settings: WebSettings = webviewone.settings
//                            settings.setJavaScriptEnabled(true)
//                            settings.setAllowContentAccess(true)
//                            settings.setDomStorageEnabled(true)
//                            webviewone.setWebViewClient(CustomWebViewClient())
//                            webviewone.loadUrl("${it?.data?.authorizationURL}")
//
//                            webviewone.show()
//
//                            i(title, "Address data flow $it")
//                        }
//                    }
//
//            }

        }
        (requireActivity().subscription_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)).setNavigationOnClickListener {
            if (add_card_fragment_add_card_layout.hide()) {
                add_card_fragment_add_card_layout.show()
                add_card_fragment_card_detail_layout.hide()
            } else {
                add_card_fragment_add_card_layout.show()
//                requireActivity().gdToast("Here", Gravity.BOTTOM)
                requireActivity().finish()
            }
        }

        cardDetailIncludedMonthEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val calendar = Calendar.getInstance()
                val defaultYear = calendar.get(Calendar.YEAR)
                val defaultMonth = calendar.get(Calendar.MONTH)
                val dialogFragment =
                    MonthYearPickerDialogFragment.getInstance(defaultMonth, defaultYear)
                dialogFragment.show(requireActivity().supportFragmentManager, null)

                dialogFragment.setOnDateSetListener { year, monthOfYear ->
                    val y = year.toString().substring(2..3)
                    val m = monthOfYear+1
                    cardDetailIncludedMonthEt.setText("$m/$y")
                    cardDetailIncludedMonthEt.clearFocus()
                }
            }

        }

        webviewone.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    val webView: WebView = v as WebView
                    when (keyCode) {
                        KeyEvent.KEYCODE_BACK -> if (webviewone.canGoBack()) {
                            webviewone.goBack()
                            return true
                        }
                    }
                }
                requireActivity().finish()
                return false
            }
        })


    }

    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }



}