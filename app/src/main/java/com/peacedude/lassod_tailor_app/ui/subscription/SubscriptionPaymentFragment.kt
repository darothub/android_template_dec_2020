package com.peacedude.lassod_tailor_app.ui.subscription

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
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
import kotlinx.android.synthetic.main.fragment_add_card.*
import kotlinx.android.synthetic.main.fragment_add_card.webviewone
import kotlinx.android.synthetic.main.fragment_subscription_home.*
import kotlinx.android.synthetic.main.fragment_subscription_payment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SubscriptionPaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubscriptionPaymentFragment : DaggerFragment() {
    val title by lazy {
        getName()
    }

    val header by lazy {
        authViewModel.header
    }

    val currentUser by lazy {
        authViewModel.currentUser
    }
    private val toolbar by lazy {
        (subscription_payment_fragment_tb as androidx.appcompat.widget.Toolbar)
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_subscription_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(toolbar, navController)

        CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                val email = currentUser?.email
                i(title, "User $email")

                authViewModel.addCard(header, email, "1000")
                    .catch {
                        i(title, "Error on flow ${it.message}")
                    }
                    .collect {
                        onFlowResponse<AddCardWrapper<AddCardRes>>(response = it) {
                            GlobalVariables.globalString = it?.data?.reference.toString()

                            val reference = it?.data?.reference
                            val settings: WebSettings = webviewone.settings
                            settings.setJavaScriptEnabled(true)
                            settings.allowContentAccess = true
                            settings.domStorageEnabled = true
                            webviewone.loadUrl("${it?.data?.authorizationURL}")
                            webviewone.webViewClient = CustomWebViewClient {
                                i(title, "We are here")
                                CoroutineScope(Main).launch {
                                    GlobalVariables.globalString = it?.data?.reference.toString()
                                    GlobalVariables.globalAddCardRes = it?.data
                                    val action = SubscriptionPaymentFragmentDirections.gotoAddCard()
                                    action.addCardData = it?.data
                                    goto(action)
                                }

                            }


                        }
                    }
                i(title, "Address data flow ${GlobalVariables.globalString}")
            }

        }
    }


}