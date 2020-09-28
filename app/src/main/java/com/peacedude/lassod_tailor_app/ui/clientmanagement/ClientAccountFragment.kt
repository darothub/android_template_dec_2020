package com.peacedude.lassod_tailor_app.ui.clientmanagement

import IsEmptyCheck
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.getEditTextName
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.goto
import com.peacedude.lassod_tailor_app.model.request.Client
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_client.*
import kotlinx.android.synthetic.main.fragment_client_account.*
import kotlinx.android.synthetic.main.fragment_phone_signup.*
import kotlinx.android.synthetic.main.fragment_user_account.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ClientAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientAccountFragment : DaggerFragment(){
    private val title by lazy {
        getName()
    }
    private lateinit var nextBtn: Button
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_account, container, false)
    }


    override fun onResume() {
        super.onResume()

        val nextBtnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        nextBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor( requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        val navHostFragment = requireActivity().supportFragmentManager.fragments[0] as NavHostFragment
        val parent = navHostFragment.childFragmentManager.primaryNavigationFragment as ClientFragment

        buttonTransactions({
            nextBtn = client_account_next_btn.findViewById(R.id.btn)
            progressBar = client_account_next_btn.findViewById(R.id.progress_bar)

            nextBtn.text = getString(R.string.next)
            nextBtn.background = nextBtnBackground
            nextBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }) {
            nextBtn.setOnClickListener {
                Log.i(title, "here2")
                val clientName = client_account_name_et.text.toString().trim()
                var clientPhoneNumber = client_account_phone_number_et.text.toString().trim()
                val clientShippingAddress = client_account_shipping_address_et.text.toString().trim()
                val clientEmail = client_account_email_et.text.toString().trim()
                var phonePattern = Regex("""\d{10,13}""")
                val checkPhoneStandard = phonePattern.matches(clientPhoneNumber)
                val validation = IsEmptyCheck.fieldsValidation(clientEmail, null)
                val checkForEmpty =
                    IsEmptyCheck(client_account_name_et,client_account_phone_number_et,client_account_email_et,client_account_shipping_address_et )
                when {
                    checkForEmpty != null -> {
                        val pattern = Regex("""phone_number|name|shipping_address|email""")
                        requireActivity().getEditTextName(checkForEmpty, pattern)
                    }
                    validation != null -> requireActivity().gdErrorToast("$validation is invalid", Gravity.BOTTOM)

                    else->{

                        val client = Client(clientName, clientPhoneNumber, clientEmail, clientShippingAddress)
                        authViewModel.newClient = client
                        Log.i(title, "newclient ${client.name}")
//                        val action = "android-app://obioma/nativemeasurement/$client".toUri()
                        parent.setItem(1)
                    }
                }
            }


        }
    }



}