package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.internal.LinkedTreeMap
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.Form
import com.peacedude.lassod_tailor_app.model.response.MeasurementTypeList
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.UserNameClass
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.fragment_measurement.*
import kotlinx.android.synthetic.main.fragment_security.*
import kotlinx.android.synthetic.main.measurement_item.view.*
import kotlinx.android.synthetic.main.measurement_items.view.*
import kotlinx.android.synthetic.main.measurement_sub_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 * Use the [MeasurementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeasurementFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }


    val header by lazy {
        authViewModel.header
    }

    private val dialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.add_measurement_layout_dialog)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(this.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val window = this.window
            window?.attributes = lp
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
        }
    }

    private val dialogIncludeBtnLayout by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_btn) as View)

    }

    private val dialogSpinner by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_spinner) as Spinner)
    }

    private val dialogAppBar by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_appbar) as AppBarLayout)
    }
    private val dialogToolbar by lazy {
        (dialog.findViewById(R.id.reusable_appbar_toolbar) as Toolbar)
    }
    private val dialogRv by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_rv) as RecyclerView)
    }
    private val clientToBeEdited by lazy {
        GlobalVariables.globalClient
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    val measurementValues: MutableMap<String?, String?> by lazy {
        mutableMapOf<String?, String?>()
    }
    lateinit var addMeasurementBtn: Button
    lateinit var addMeasurementProgressBar: ProgressBar
    lateinit var dialogAddMeasurementBtn:Button
    lateinit var dialogAddMeasurementProgressBar: ProgressBar
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
        return inflater.inflate(R.layout.fragment_measurement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)
        val activityTitle = dialogAppBar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.add_measurement)

        addMeasurementBtn = measurement_fragment_add_measurement_btn.findViewById(R.id.btn)
        addMeasurementProgressBar =
            measurement_fragment_add_measurement_btn.findViewById(R.id.progress_bar)
        val btnBackground = addMeasurementBtn.background
        btnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
        dialogAddMeasurementBtn = dialogIncludeBtnLayout.findViewById<Button>(R.id.btn)
        dialogAddMeasurementProgressBar = dialogIncludeBtnLayout.findViewById<ProgressBar>(R.id.progress_bar)
        val dialogBtnBackgound = dialogAddMeasurementBtn.background
        dialogBtnBackgound?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        dialogToolbar.setNavigationOnClickListener {
            dialog.dismiss()
        }



        buttonTransactions({
            addMeasurementBtn.background = btnBackground
            addMeasurementBtn.text = getString(R.string.add_measurement)
            addMeasurementBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            dialogAddMeasurementBtn.background = dialogBtnBackgound
            dialogAddMeasurementBtn.text = getString(R.string.add_measurement)
            dialogAddMeasurementBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        }, {

            addMeasurementBtn.setOnClickListener {
                dialog.show()
            }

        })

        if (clientToBeEdited == null) {
            i(title, "No client found")
            btnBackground?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorGray),
                PorterDuff.Mode.SRC_IN
            )
            addMeasurementBtn.background = btnBackground
            addMeasurementBtn.isClickable = false

        }

        CoroutineScope(Main).launch {
            supervisorScope {
                val getMeasurementTypes = async { authViewModel.getMeasurementTypes(header) }
                val getAllMeasurements = async {
                    authViewModel.getAllMeasurements(
                        header,
                        clientToBeEdited?.id.toString()
                    )
                }
                val measurementMap = HashMap<Long, String>()
                val measurementListMap = HashMap<String, List<Form>>()
                try {
                    getAllMeasurements.await()
                        .catch {
                            i(title, "Error All measurement on flow ${it.message}")
                        }
                        .collect {
                            onFlowResponse<ListOfMeasurement>(response = it) {
                                i(title, "Measure ${it?.measurement}")

                                val measurementValueList = it?.measurement?.map { m ->
                                    MeasurementValues(
                                        m.name,
                                        m.type,
                                        m.clientID,
                                        m.values
                                    )
                                }
                                if (measurementValueList?.isEmpty()!!) {
                                    measurement_fragment_recycler_vf.showNext()
                                } else {
                                    resource_fragment_measurement_rv.setupAdapter<MeasurementValues>(
                                        R.layout.measurement_sub_item
                                    ) { adapter, context, list ->

                                        bind { itemView, position, item ->
                                            itemView.measurement_sub_item_type_tv.text = item?.type

                                            i(title, "$item")
                                            itemView.setOnClickListener {

                                            }

                                            val v = item?.values as LinkedTreeMap<String, String>
                                            val ValueList = arrayListOf<UserNameClass>(
                                                UserNameClass(
                                                    "Ankle",
                                                    v.getOrElse("ankle"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Back width",
                                                    v.getOrElse("backWidth"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Bottom",
                                                    v.getOrElse("bottom"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Cap",
                                                    v.getOrElse("cap"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Full length",
                                                    v.getOrElse("fullLength"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "High length",
                                                    v.getOrElse("highLength"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Lap",
                                                    v.getOrElse("lap"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Neck width",
                                                    v.getOrElse("neckWidth"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Round sleeve",
                                                    v.getOrElse("roundSleeve"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Armpit",
                                                    v.getOrElse("armpit"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Band",
                                                    v.getOrElse("band"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Chest",
                                                    v.getOrElse("chest"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Half length",
                                                    v.getOrElse("halfLength"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Hip",
                                                    v.getOrElse("hip"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Long sleeve",
                                                    v.getOrElse("longSleeve"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Short sleeve",
                                                    v.getOrElse("shortSleeve"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Thigh",
                                                    v.getOrElse("thigh"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Shoulder to waist",
                                                    v.getOrElse("shoulderToWaist"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Dart",
                                                    v.getOrElse("dart"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Hand fit",
                                                    v.getOrElse("handFit"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Knee",
                                                    v.getOrElse("knee"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Nape to waist",
                                                    v.getOrElse("napeToWaist"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Round body",
                                                    v.getOrElse("roundBody"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Shoulder",
                                                    v.getOrElse("shoulder"){"0"} as String
                                                ),
                                                UserNameClass(
                                                    "Waist",
                                                    v.getOrElse("waist"){"0"} as String
                                                )

                                            ).filter {itv->
                                                !itv.value.isNullOrEmpty() && itv.value != "null"
                                            }
                                            itemView.measurement_sub_item_rv.setupAdapter<UserNameClass>(
                                                R.layout.measurement_item
                                            ) { adapter, context, list ->
                                                bind { subItemView, position, item ->
                                                    subItemView.measurement_name.text = item?.title
                                                    subItemView.measurement_value.text = item?.value


//                                                    subItemView.setOnClickListener {
//                                                        i(title, "$item")
//                                                    }
                                                }

                                                setLayoutManager(
                                                    GridLayoutManager(
                                                        requireContext(),
                                                        3
                                                    )
                                                )
                                                submitList(ValueList)
                                            }

                                        }
                                        setLayoutManager(
                                            LinearLayoutManager(
                                                requireContext(),
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                        )
                                        submitList(measurementValueList)
                                    }
                                }
                            }
                        }

                    getMeasurementTypes.await()
                        .catch {
                            i(title, "Error on flow ${it.message}")
                        }
                        .collect {
                            onFlowResponse<MeasurementTypeList>(response = it) {
                                i(title, "Measure ${it?.measurementTypes}")
                                it?.measurementTypes?.associateByTo(measurementMap, { k ->
                                    k.id
                                }, { v ->
                                    v.name
                                })
                                it?.measurementTypes?.associateByTo(measurementListMap, { k ->
                                    k.name
                                }, { v ->
                                    v.form
                                })
                                val measurementTypes = it?.measurementTypes?.map {
                                    it.name
                                }
                                setUpSpinnerWithList(
                                    getString(R.string.select_str), dialogSpinner,
                                    measurementTypes as ArrayList<String>
                                )
                            }
                        }

                } catch (e: Exception) {
                    val u = User()
                    val k = u.javaClass.fields
                    k.map {

                    }
                    i(title, "MeasurementType error on flow ${e.message}")
                }

                dialogSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val itemSelected = dialogSpinner.selectedItem as String
                        val measurementOptionList = measurementListMap[itemSelected]?.map {
                            i(title, "Label ${it.label}")
                            MeasurementLabelValue(
                                it.key,
                                it.label,
                                ""
                            )
                        }
                        i(title, "Position $measurementListMap $measurementOptionList")
                        dialogRv.setupAdapter<MeasurementLabelValue>(R.layout.measurement_items) { adapter, context, list ->

                            bind { itemView, position, item ->
                                itemView.add_measurement_layout_dialog_input.hint = item?.label
                                var ankle = ""
                                itemView.add_measurement_layout_dialog_et.doOnTextChanged { text, start, count, after ->
                                    if (text != null) {
                                        item?.value = text.toString()
                                    } else {
                                    }
                                    return@doOnTextChanged
                                }


                                dialogAddMeasurementBtn.setOnClickListener {
                                    val filtered = list?.filter {
                                        !it?.value.isNullOrEmpty()
                                    }
                                    filtered?.associateByTo(measurementValues, {
                                        it?.key.toString()
                                    }, {
                                        it?.value.toString()
                                    })

                                    val valuess = Valuess(measurementValues)
                                    val values = Values(
                                        measurementValues["ankle"].toString(),
                                        measurementValues["backWidth"].toString(),
                                        measurementValues["bottom"].toString(),
                                        measurementValues["cap"].toString(),
                                        measurementValues["fullLength"].toString(),
                                        measurementValues["highLength"].toString(),
                                        measurementValues["lap"].toString(),
                                        measurementValues["neckWidth"].toString(),
                                        measurementValues["roundSleeve"].toString(),
                                        measurementValues["armpit"].toString(),
                                        measurementValues["band"].toString(),
                                        measurementValues["chest"].toString(),
                                        measurementValues["halfLength"].toString(),
                                        measurementValues["hip"].toString(),
                                        measurementValues["longSleeve"].toString(),
                                        measurementValues["shortSleeve"].toString(),
                                        measurementValues["thigh"].toString(),
                                        measurementValues["shoulderToWaist"].toString(),
                                        measurementValues["dart"].toString(),
                                        measurementValues["hand fit"].toString(),
                                        measurementValues["knee"].toString(),
                                        measurementValues["napeToWaist"].toString(),
                                        measurementValues["roundBody"].toString(),
                                        measurementValues["shoulder"].toString(),
                                        measurementValues["waist"].toString()
                                    )
                                    val type = dialogSpinner.selectedItem as String
                                    val clientMeasurement = MeasurementValues(
                                        clientToBeEdited?.name.toString(),
                                        type,
                                        clientToBeEdited?.id.toString(),
                                        valuess
                                    )
                                    clientMeasurement.gender = clientToBeEdited?.gender
                                    i(
                                        title,
                                        "measure $measurementValues \nvalues $values list $list"
                                    )
                                    val addMeasurementReq =
                                        authViewModel.addMeasurement(header, clientMeasurement)
                                    requestObserver(
                                        dialogAddMeasurementProgressBar,
                                        dialogAddMeasurementBtn,
                                        addMeasurementReq,
                                        false
                                    ) { bool, result ->
                                        onRequestResponseTask<ClientMeasurement>(bool, result) {
                                            val res = result as UserResponse<ClientMeasurement>
                                            requireActivity().gdToast(
                                                res.message.toString(),
                                                Gravity.BOTTOM
                                            )
                                            dialog.dismiss()
                                            adapter.notifyDataSetChanged()
//
                                        }
                                    }
                                }

                            }
                            setLayoutManager(GridLayoutManager(requireContext(), 2))
                            submitList(measurementOptionList)
                        }
                    }

                }

            }

        }


    }



}

data class MeasurementTypes(
    val id: Long,
    val name: String
)

data class MeasurementLabelValue(
    var key: String? = "",
    val label: String,
    var value: String?
)

