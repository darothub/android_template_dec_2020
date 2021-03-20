package com.peacedude.lassod_tailor_app.ui.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_card.*
import kotlinx.android.synthetic.main.fragment_subscription_home.*
import kotlinx.android.synthetic.main.fragment_subscription_payment.*
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(toolbar, navController)

//        CoroutineScope(Dispatchers.Main).launch {
//            supervisorScope {
//                val email = currentUser?.email
//                i(title, "User $email")
//
//                authViewModel.addCard(header, email, "1000")
//                    .catch {
//                        i(title, "Error on flow ${it.message}")
//                    }
//                    .collect {
//                        onFlowResponse<AddCardWrapper<AddCardRes>>(response = it) {
//                            GlobalVariables.globalString = it?.data?.reference.toString()
//
//                            val reference = it?.data?.reference
//                            val settings: WebSettings = webviewone.settings
//                            settings.setJavaScriptEnabled(true)
//                            settings.allowContentAccess = true
//                            settings.domStorageEnabled = true
//                            webviewone.loadUrl("${it?.data?.authorizationURL}")
//                            webviewone.webViewClient = CustomWebViewClient {
//                                i(title, "We are here")
//                                CoroutineScope(Main).launch {
//                                    GlobalVariables.globalString = it?.data?.reference.toString()
//                                    GlobalVariables.globalAddCardRes = it?.data
//                                    val action = SubscriptionPaymentFragmentDirections.gotoAddCard()
//                                    action.addCardData = it?.data
//                                    goto(action)
//                                }
//
//                            }
//
//
//                        }
//                    }
//                i(title, "Address data flow ${GlobalVariables.globalString}")
//            }
//
//        }
    }
}
