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
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.SingleClient
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
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
    private lateinit var addClientBtn: Button
    private lateinit var updateClientBtn: Button
    private lateinit var updateProgressBar: ProgressBar
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

        val addClientBtnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        addClientBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor( requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )


        val navHostFragment = requireActivity().supportFragmentManager.fragments[0] as NavHostFragment
        val parent = navHostFragment.childFragmentManager.primaryNavigationFragment as ClientFragment

        val genderList =  resources.getStringArray(R.array.gender_normal_list).toList() as ArrayList<String>
        setUpSpinnerWithList(getString(R.string.gender), client_account_gender_spinner,  genderList)
        setUpCountrySpinner(getString(R.string.select_your_country_str), client_account_country_spinner)

        buttonTransactions({
            addClientBtn = client_account_next_btn.findViewById(R.id.btn)
            updateClientBtn = client_account_update_btn.findViewById(R.id.btn)
            progressBar = client_account_next_btn.findViewById(R.id.progress_bar)
            updateProgressBar = client_account_update_btn.findViewById(R.id.progress_bar)
            addClientBtn.text = getString(R.string.add_client)
            updateClientBtn.text = getString(R.string.update_str)
            addClientBtn.background = addClientBtnBackground
            addClientBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            val updateBtnBackground = updateClientBtn.background
            updateBtnBackground.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor( requireContext(), R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
            )
            updateClientBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            updateClientBtn.background = updateBtnBackground
        }) {

            authViewModel.netWorkLiveData.observe(viewLifecycleOwner, Observer {
                if (it) {
                    addClientBtn.show()

                } else {
                    addClientBtn.invisible()
                }
            })
            addClientBtn.setOnClickListener {
                Log.i(title, "here2")
                validateClientRequest { client, gender, state, country ->
                    addClientRequest(client, gender, state, country)
                }
            }


            val clientToBeEdited = GlobalVariables.globalClient
            Log.i(title, "ClientAccount ${clientToBeEdited}")
            if(clientToBeEdited != null){
                client_account_fragment_vf.showNext()
                client_account_name_et.setText(clientToBeEdited.name)
                client_account_phone_number_et.setText(clientToBeEdited.phone)
                client_account_shipping_address_et.setText(clientToBeEdited.deliveryAddress)
                client_account_email_et.setText(clientToBeEdited.email)
                client_account_state_et.setText(clientToBeEdited.state)
                setUpCountrySpinner(clientToBeEdited.country.toString(), client_account_country_spinner)
                setUpSpinnerWithList(clientToBeEdited.gender.toString(), client_account_gender_spinner,  genderList)

                updateClientBtn.setOnClickListener {
                    validateClientRequest { client, gender, state, country ->
                        Log.i(title, "ClientAccount ${client.tailorId} ${client.id}")
                        client.id = clientToBeEdited.id
                        client.tailorId = clientToBeEdited.tailorId
                        updateClientRequest(client, gender, state, country)
                    }
                }
            }
            else{
                client_account_fragment_vf.showPrevious()
            }


        }
    }

    private fun validateClientRequest(action:(Client, String, String, String)->Unit) {
        val clientName = client_account_name_et.text.toString().trim()
        var clientPhoneNumber = client_account_phone_number_et.text.toString().trim()
        val clientShippingAddress = client_account_shipping_address_et.text.toString().trim()
        val clientEmail = client_account_email_et.text.toString().trim()
        val clientState = client_account_state_et.text.toString().trim()
        val clientCountry = client_account_country_spinner.selectedItem.toString()
        var phonePattern = Regex("""\d{10,17}""")
        val checkPhoneStandard = phonePattern.matches(clientPhoneNumber)
        val gender = client_account_gender_spinner.selectedItem as String
        val validation = IsEmptyCheck.fieldsValidation(clientEmail, null)
        val checkForEmpty =
            IsEmptyCheck(
                client_account_name_et,
                client_account_phone_number_et,
                client_account_email_et,
                client_account_shipping_address_et
            )
        when {
            checkForEmpty != null -> {
                requireActivity().gdErrorToast(checkForEmpty.tag.toString(), Gravity.BOTTOM)
            }
            validation != null -> requireActivity().gdErrorToast(
                "$validation is invalid",
                Gravity.BOTTOM
            )
            clientCountry == getString(R.string.select_your_country_str) -> {
                requireActivity().gdErrorToast(
                    "Please ${getString(R.string.select_your_country_str)}",
                    Gravity.BOTTOM
                )
            }
            gender == getString(R.string.gender) -> {
                requireActivity().gdErrorToast(
                    "Please ${getString(R.string.select_valid_gender_str)}",
                    Gravity.BOTTOM
                )
            }
            else -> {
                val client =
                    Client(clientName, clientPhoneNumber, clientEmail, clientShippingAddress)

                action(client, gender, clientState, clientCountry)
                //                        val action = "android-app://obioma/nativemeasurement/$client".toUri()
    //
            }
        }
    }

    private fun addClientRequest(
        client: Client, gender:String, clientState:String, clientCountry:String
    ) {
        Log.i(title, " header $header")

        client.gender = gender
        client.country = clientCountry
        client.state = clientState

        val req = authViewModel.addClient(header, client)
        val observer =
            requireActivity().observeRequest(req, progressBar, addClientBtn)
        observer.observe(viewLifecycleOwner, Observer {
            val (bool, result) = it
            onRequestResponseTask<Client>(bool, result) { res ->
                val newClient = res?.data
                requireActivity().gdToast(
                    "Client added successfully",
                    Gravity.BOTTOM
                )
                authViewModel.newClient = newClient
                i(title, "client $newClient Id ${newClient?.id}")
                goto(DashboardActivity::class.java)

            }
        })
    }

    private fun updateClientRequest(
        client: Client, gender:String, clientState:String, clientCountry:String
    ) {
        Log.i(title, " header $header")

        client.gender = gender
        client.country = clientCountry
        client.state = clientState

        val editClientReq = authViewModel.editClient(header, client)
        requestObserver(
            updateProgressBar,
            updateClientBtn,
            editClientReq
        ) { bool, result ->
            //Task to be done on successful
            onRequestResponseTask<ClientsList>(bool, result) {
                val results = result as UserResponse<SingleClient>
                val msg = results.message.toString()

                val newClientData = result.data?.client
                requireActivity().gdToast(msg, Gravity.BOTTOM)

            }

        }
    }


}