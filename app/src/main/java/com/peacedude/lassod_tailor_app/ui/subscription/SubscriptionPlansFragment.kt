package com.peacedude.lassod_tailor_app.ui.subscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.helpers.onFlowResponse
import com.peacedude.lassod_tailor_app.helpers.show
import com.peacedude.lassod_tailor_app.model.response.Photo
import com.peacedude.lassod_tailor_app.model.response.PhotoList
import com.peacedude.lassod_tailor_app.model.response.SubscriptionData
import com.peacedude.lassod_tailor_app.model.response.SubscriptionResponse
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_subscription_plans.*
import kotlinx.android.synthetic.main.subscription_plans_description_item.view.*
import kotlinx.android.synthetic.main.subscription_plans_list_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SubscriptionPlansFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubscriptionPlansFragment : DaggerFragment() {
    val title: String by lazy {
        getName()
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> {
        viewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_plans, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscription_plans_fragment_yearly_btn.setOnClickListener {
            subscription_plans_fragment_vf.showNext()
            yearlySubscriptions()
            val defaultBackGround = subscription_plans_fragment_yearly_btn.background
            val chosenBackGround = subscription_plans_fragment_monthly_btn.background
            subscription_plans_fragment_monthly_btn.apply {
                background = defaultBackGround
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            subscription_plans_fragment_yearly_btn.apply {
                background = chosenBackGround
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            }
        }
        subscription_plans_fragment_monthly_btn.setOnClickListener {
            subscription_plans_fragment_vf.showPrevious()
            monthlySubscriptions()
            val defaultBackGround = subscription_plans_fragment_monthly_btn.background
            val chosenBackGround = subscription_plans_fragment_yearly_btn.background
            subscription_plans_fragment_yearly_btn.apply {
                background = defaultBackGround
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            subscription_plans_fragment_monthly_btn.apply {
                background = chosenBackGround
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            }

        }

        val monthlyRv = subscription_plans_fragment_vf[0]
        val yearlyRv = subscription_plans_fragment_vf[1]
        if(monthlyRv.isShown){
            i(title, "MonthlyRv")
            monthlySubscriptions()
        }









    }

    @ExperimentalCoroutinesApi
    private fun monthlySubscriptions() {
        i(title, "Monthly")
        CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                val getAllPlans = async {
                    authViewModel.getAllPlans()
                }

                getAllPlans.await()
                    .catch {
                        i(title, "Error All Photo on flow ${it.message}")
                    }
                    .collect {
                        onFlowResponse<SubscriptionResponse>(response = it) {

                            val monthlySubs = it?.data?.groupBy {
                                it.interval
                            }?.get("monthly")

                            i(title, "$it")


                            subscription_plans_monthly_rv.setupAdapter<SubscriptionData>(R.layout.subscription_plans_list_item) { adapter, context, list ->

                                bind { itemView, position, item ->
                                    itemView.subscription_plans_title_tv.text = item?.name
                                    itemView.subscription_plans_amt_tv.text = "${item?.amount}"
                                    itemView.subscription_plans_duration_tv.text =
                                        "/${item?.interval?.subSequence(0, 2).toString()}"

                                    val listOfDescriptions = item?.description?.split(",")?.filter {
                                        it != ","
                                    }

                                    itemView.subscription_plans_item_rv.setupAdapter<String>(R.layout.subscription_plans_description_item) { subadapter, context, sublist ->

                                        bind { subitemView, subposition, subitem ->
                                            subitemView.subscription_plans_description_tv.text =
                                                subitem
                                        }
                                        setLayoutManager(
                                            LinearLayoutManager(
                                                requireContext(),
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                        )
                                        submitList(listOfDescriptions)
                                    }
                                }
                                setLayoutManager(
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                )
                                submitList(monthlySubs)
                            }
                        }
                    }
            }
        }
    }
    private fun yearlySubscriptions() {
        i(title, "Yearly")
        CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                val getAllPlans = async {
                    authViewModel.getAllPlans()
                }

                getAllPlans.await()
                    .catch {
                        i(title, "Error All Photo on flow ${it.message}")
                    }
                    .collect {
                        onFlowResponse<SubscriptionResponse>(response = it) {

                            val monthlySubs = it?.data?.groupBy {
                                it.interval
                            }?.get("annually")

                            i(title, "$it")


                            subscription_plans_yearly_rv.setupAdapter<SubscriptionData>(R.layout.subscription_plans_list_item) { adapter, context, list ->

                                bind { itemView, position, item ->
                                    itemView.subscription_plans_title_tv.text = item?.name
                                    itemView.subscription_plans_amt_tv.text = "${item?.amount}"
                                    itemView.subscription_plans_duration_tv.text =
                                        "/${item?.interval?.subSequence(0, 2).toString()}"

                                    val listOfDescriptions = item?.description?.split(",")?.filter {
                                        it != ","
                                    }

                                    itemView.subscription_plans_item_rv.setupAdapter<String>(R.layout.subscription_plans_description_item) { subadapter, context, sublist ->

                                        bind { subitemView, subposition, subitem ->
                                            subitemView.subscription_plans_description_tv.text =
                                                subitem
                                        }
                                        setLayoutManager(
                                            LinearLayoutManager(
                                                requireContext(),
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                        )
                                        submitList(listOfDescriptions)
                                    }
                                }
                                setLayoutManager(
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                )
                                submitList(monthlySubs)
                            }
                        }
                    }
            }
        }
    }


}