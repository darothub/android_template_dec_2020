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
import androidx.navigation.fragment.navArgs
import co.paystack.android.Paystack.TransactionCallback
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.AddCardRes
import com.peacedude.lassod_tailor_app.model.response.AddCardResponse
import com.peacedude.lassod_tailor_app.model.response.AddCardWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_subscription.*
import kotlinx.android.synthetic.main.fragment_add_card.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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
    val addCardDataArg by navArgs<AddCardFragmentArgs>()

    val addCardData by lazy {
        addCardDataArg.addCardData
    }
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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val payBtnBackground = button.background
            payBtnBackground?.changeBackgroundColor(requireContext(), R.color.colorGreen)
            button.background = payBtnBackground
            button.text = getString(R.string.pay)
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )
        }, {

        })
        add_card_fragment_add_card_layout.setOnClickListener {
            goto(R.id.subscriptionPaymentFragment)
            customerNameTv.text = currentUser?.firstName
        }

       if(addCardData != null){
//           CoroutineScope(Main).launch {
//               i(title, "hide")
//
//               authViewModel.verifyPayment(header, addCardData?.reference.toString())
//                   .catch {
//                       val exceptionRegex =
//                           Regex("""java.lang.\w+Exception: (\w+\s)+,?\w+""")
//                       val exception =
//                           exceptionRegex.find(it.message.toString())?.value
//                       val exceptionSplit = exception?.split(":")
//                       val errorMessage = exceptionSplit?.get(1).toString()
//                       i(title, "Error on flow $errorMessage")
//                       requireActivity().gdErrorToast(
//                           errorMessage,
//                           Gravity.BOTTOM
//                       )
//                   }
//                   .collect {
//
//                       onFlowResponse<UserResponse<AddCardResponse>>(
//                           response = it,
//                           action = {
//
//                               requireActivity().gdToast(
//                                   it?.message.toString(),
//                                   Gravity.BOTTOM
//                               )
//                           },
//                           error = { message ->
//
//                               i(title, "Error on nested flow  ${message}")
//
//
//                           })
//                   }
//           }
       }
        else{

       }






    }

    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }



}