package com.peacedude.lassod_tailor_app.ui.clientmanagement

import IsEmptyCheck
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_client_account.*
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
    val header by lazy {
        authViewModel.header
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        changeStatusBarColor(R.color.colorTransparentWhite)
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

        val nextBtnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_primary)
//        nextBtnBackground?.colorFilter = PorterDuffColorFilter(
//            ContextCompat.getColor( requireContext(), R.color.colorPrimary),
//            PorterDuff.Mode.SRC_IN
//        )

        val navHostFragment = requireActivity().supportFragmentManager.fragments[0] as NavHostFragment
        val parent = navHostFragment.childFragmentManager.primaryNavigationFragment as ClientFragment

        setupCategorySpinner(
            requireContext(),
            client_account_gender_spinner,
            R.array.gender_normal_list
        )

        setUpCountrySpinner(getString(R.string.select_your_country_str), client_account_country_spinner)

        buttonTransactions({
            nextBtn = client_account_next_btn.findViewById(R.id.btn)
            progressBar = client_account_next_btn.findViewById(R.id.progress_bar)

            nextBtn.text = getString(R.string.add_client)
            nextBtn.background = nextBtnBackground
            nextBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }) {
            nextBtn.setOnClickListener {
                Log.i(title, "here2")
                val clientName = client_account_name_et.text.toString().trim()
                var clientPhoneNumber = client_account_phone_number_et.text.toString().trim()
                val clientShippingAddress = client_account_shipping_address_et.text.toString().trim()
                val clientEmail = client_account_email_et.text.toString().trim()
                val clientCountry = client_account_country_spinner.selectedItem.toString()
                var phonePattern = Regex("""\d{10,17}""")
                val checkPhoneStandard = phonePattern.matches(clientPhoneNumber)
                val validation = IsEmptyCheck.fieldsValidation(clientEmail, null)
                val checkForEmpty =
                    IsEmptyCheck(client_account_name_et,client_account_phone_number_et,client_account_email_et,client_account_shipping_address_et )
                when {
                    checkForEmpty != null -> {
                       requireActivity().gdErrorToast(checkForEmpty.tag.toString(), Gravity.BOTTOM)
                    }
                    validation != null -> requireActivity().gdErrorToast("$validation is invalid", Gravity.BOTTOM)
                    clientCountry == getString(R.string.select_your_country_str) ->{
                        requireActivity().gdErrorToast("Please ${getString(R.string.select_your_country_str)}", Gravity.BOTTOM)
                    }
                    else->{
                        Log.i(title, " header $header")
                        val client = Client(clientName, clientPhoneNumber, clientEmail, clientShippingAddress)
                        val gender = client_account_gender_spinner.selectedItem as String
                        client.gender = gender
                        client.country = clientCountry

                        val req = authViewModel.addClient(header, client)
                        val observer =
                            requireActivity().observeRequest(req, progressBar, nextBtn)
                        observer.observe(viewLifecycleOwner, Observer {
                            val (bool, result) = it
                            onRequestResponseTask<Client>(bool, result) { res ->
                                val newClient  = res?.data
                                requireActivity().gdToast(
                                    "Client added successfully",
                                    Gravity.BOTTOM
                                )
                                authViewModel.newClient = newClient
                                i(title, "client $newClient Id ${newClient?.id}")
//                                parent.setItem(1)
//
//                                i(title, getString(R.string.check_email))
                            }
                        })
//                        val action = "android-app://obioma/nativemeasurement/$client".toUri()
//
                    }
                }
            }


        }
    }



}