package com.peacedude.lassod_tailor_app.ui.subscription

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.Data
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.SubscribedDataDetails
import com.peacedude.lassod_tailor_app.model.response.SubscriptionResponse
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.model.typealiases.SubscriptionList
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_subscription_home.*
import kotlinx.android.synthetic.main.profile_header.*
import kotlinx.android.synthetic.main.subscription_arangement_items.view.*
import kotlinx.android.synthetic.main.subscription_list_item.view.*
import kotlinx.android.synthetic.main.subscription_plans_description_item.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [SubscriptionHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubscriptionHomeFragment : DaggerFragment() {
    val title: String by lazy {
        getName()
    }

    lateinit var noDataFirstIcon:ImageView
    lateinit var noDataSecondIcon:ImageView
    lateinit var noDataText:TextView
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> {
        viewModelProviderFactory
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
        return inflater.inflate(R.layout.fragment_subscription_home, container, false)
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        noDataFirstIcon = subscription_home_fragment_no_data_included_layout.findViewById(R.id.no_data_first_icon_iv)
        noDataSecondIcon = subscription_home_fragment_no_data_included_layout.findViewById(R.id.no_data_second_icon_iv)
        noDataText = subscription_home_fragment_no_data_included_layout.findViewById(R.id.no_data_text_tv)

        noDataFirstIcon.hide()
        noDataSecondIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.shopping_cart_24px))
        noDataText.text = getString(R.string.you_have_no_sub)


        //launch scope for coroutine jobs
        CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                val getUserSubscriptions = async {
                    authViewModel.getUserAllSubscriptions()
                }
                //Get user subscriptions
                getUserSubscriptions.await()
                    .handleResponse({
                        onFlowResponse<Data>(response = it, error = { err ->
                            i(title, "error on subscriptionlist $err")
                            requireActivity().gdToast(err, Gravity.BOTTOM)
                        }) { it1 ->

                            val listOfSubscriptions = arrayListOf<SubscriptionsInfoByExpiryDate>()
                            //Mapping and grouping subscription by expiry date.
                            (it1?.data as? SubscriptionList)?.map {
                                SubscriptionsInfo(it.plan.name, it.nextPaymentDate)
                            }?.groupBy {
                                it.expiryDate
                            }?.onEachIndexed { index, entry ->
                                if(index == 0){
                                    listOfSubscriptions.add(SubscriptionsInfoByExpiryDate("latest", entry.value))
                                }
                                listOfSubscriptions.add(SubscriptionsInfoByExpiryDate("Old", entry.value))
                            }
                            if(listOfSubscriptions.isEmpty()){

                            }
                            else{
                                subscription_home_fragment_vf.showNext()
                                val listOfString = arrayListOf("Subscription", "Expiry date")
                                subscription_home_fragment_rv.setupAdapter<SubscriptionsInfoByExpiryDate>(R.layout.subscription_arangement_items) { adapter, context, list ->

                                    bind { itemView, position, item ->

                                        itemView.subscription_arrangementfragment_sub_rv.setupAdapter<SubscriptionsInfo>(R.layout.subscription_list_item) { subadapter, context, sublist ->

                                            bind { subitemView, subposition, subitem ->
                                                val expDate = subitem?.expiryDate.toString().replace("[TZ]".toRegex(), " ")
                                                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                                                val dateString = simpleDateFormat.parse(expDate)?.toString()
                                                val dayOfWeek = dateString?.subSequence(0, 11)
                                                val year = dateString?.takeLast(4)
                                                subitemView.subscription_type_tv.text = subitem?.planName
                                                subitemView.subscription_exp_tv.text = "$dayOfWeek $year"

                                            }

                                            setLayoutManager(
                                                LinearLayoutManager(
                                                    requireContext(),
                                                    LinearLayoutManager.VERTICAL,
                                                    false
                                                )
                                            )
                                            submitList(item?.list)
                                        }
                                    }
                                    setLayoutManager(
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    )
                                    submitList(listOfSubscriptions)
                                }
                            }
                        }
                    }, {err ->
                        i(title, "error2 on subscriptionlist $err")
                        requireActivity().gdToast(err, Gravity.BOTTOM)
                    })


            }
        }


    }


}

data class SubscriptionsInfo(var planName:String, var expiryDate:String)
data class SubscriptionsInfoByExpiryDate(var name:String, var list:List<SubscriptionsInfo>)