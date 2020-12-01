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
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.internal.LinkedTreeMap
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.ui.UserNameClass
import com.utsman.recycling.adapter.RecyclingAdapter
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.fragment_measurement.*
import kotlinx.android.synthetic.main.fragment_security.*
import kotlinx.android.synthetic.main.measurement_item.view.*
import kotlinx.android.synthetic.main.measurement_items.view.*
import kotlinx.android.synthetic.main.measurement_sub_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
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

    private val editdialog by lazy {
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
    private val dialogEditIncludeBtnLayout by lazy {
        (editdialog.findViewById(R.id.add_measurement_layout_dialog_btn) as View)

    }
    private val dialogNameEditText by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_et) as TextInputEditText)

    }

    private val dialogEditNameEditText by lazy {
        (editdialog.findViewById(R.id.add_measurement_layout_dialog_et) as TextInputEditText)

    }

    private val dialogSpinner by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_spinner) as Spinner)
    }

    private val dialogEditSpinner by lazy {
        (editdialog.findViewById(R.id.add_measurement_layout_dialog_spinner) as Spinner)
    }

    private val dialogAppBar by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_appbar) as AppBarLayout)
    }
    private val dialogEditAppBar by lazy {
        (editdialog.findViewById(R.id.add_measurement_layout_dialog_appbar) as AppBarLayout)
    }
    private val dialogTitleTv by lazy {
        (dialogAppBar.findViewById(R.id.reusable_appbar_title_tv) as TextView)
    }
    private val dialogEditTitleTv by lazy {
        (dialogEditAppBar.findViewById(R.id.reusable_appbar_title_tv) as TextView)
    }
    private val dialogToolbar by lazy {
        (dialogAppBar.findViewById(R.id.reusable_appbar_toolbar) as Toolbar)
    }
    private val dialogEditToolbar by lazy {
        (dialogEditAppBar.findViewById(R.id.reusable_appbar_toolbar) as Toolbar)
    }
    private val dialogRv by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_rv) as RecyclerView)
    }
    private val dialogEditRv by lazy {
        (editdialog.findViewById(R.id.add_measurement_layout_dialog_rv) as RecyclerView)
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
    val measurementMap by lazy {
        HashMap<Long, String>()
    }
    val measurementListMap by lazy {
        HashMap<String, List<Form>>()
    }
    val measurementOptionList by lazy {
        ArrayList<MeasurementLabelValue>()
    }
    lateinit var addMeasurementBtn: Button
    lateinit var addMeasurementProgressBar: ProgressBar
    lateinit var dialogAddMeasurementBtn: Button
    lateinit var dialogAddMeasurementProgressBar: ProgressBar
    lateinit var dialogEditMeasurementBtn: Button
    lateinit var dialogEditMeasurementProgressBar: ProgressBar
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
//        val activityTitle = dialog.findViewById<TextView>(R.id.reusable_appbar_title_tv)
//        activityTitle.text = getString(R.string.add_measurement)

        addMeasurementBtn = measurement_fragment_add_measurement_btn.findViewById(R.id.btn)
        addMeasurementProgressBar =
            measurement_fragment_add_measurement_btn.findViewById(R.id.progress_bar)
        val btnBackground = addMeasurementBtn.background
        btnBackground?.changeBackgroundColor(requireContext(), R.color.colorPrimary)


        dialogToolbar.setNavigationOnClickListener {
            dialog.dismiss()
        }
        dialogEditToolbar.setNavigationOnClickListener {
            editdialog.dismiss()
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

            if (clientToBeEdited == null) {
                i(title, "No client found")
                btnBackground?.changeBackgroundColor(requireContext(), R.color.colorGray)
                addMeasurementBtn.background = btnBackground
                addMeasurementBtn.isClickable = false

            }
        }, {


        })

        CoroutineScope(Main).launch {
            supervisorScope {

                val getAllMeasurements = async {
                    authViewModel.getAllMeasurements(
                        header,
                        clientToBeEdited?.id.toString()
                    )
                }
                //Get and set existing measurements
                getAllMeasurement(getAllMeasurements, dialogTitleTv)

            }
        }


    }

    private fun setListOnMeasurementChange(
        parentAdapter: RecyclingAdapter<MeasurementValues>,
        parentList: MutableList<MeasurementValues?>?
    ) {

        dialogSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemSelected = dialogSpinner.selectedItem as String
                measurementListMap[itemSelected]?.mapTo(measurementOptionList) {
                    i(title, "Label ${it.label}")
                    MeasurementLabelValue(
                        it.key,
                        it.label,
                        ""
                    )
                }
                dialogRv.setupAdapter<MeasurementLabelValue>(R.layout.measurement_items) { subAdapter, context, list ->

                    bind { itemView, position, item ->
                        i(title, "Longer ${list?.size}")
                        itemView.add_measurement_layout_dialog_input.hint = item?.label
                        itemView.add_measurement_layout_dialog_et.doAfterTextChanged { editable ->
                            item?.value = itemView.add_measurement_layout_dialog_et.text?.toString()

                        }
                        dialogAddMeasurementBtn.setOnClickListener {
                            i(title, "Longer ${measurementOptionList?.size}")
                            //Collect filled measurement and add
                            addMeasurementsRequest(parentList, parentAdapter, list, subAdapter)
                        }
                    }
                    setLayoutManager(GridLayoutManager(requireContext(), 2))
                    submitList(measurementOptionList)
                }
                i(title, "Position $measurementListMap $measurementOptionList")
            }
        }
    }

    private fun addMeasurementsRequest(
        parentList: MutableList<MeasurementValues?>?,
        parentAdapter: RecyclingAdapter<MeasurementValues>,
        list: MutableList<MeasurementLabelValue?>?,
        subAdapter: RecyclingAdapter<MeasurementLabelValue>
    ) {
        val filtered = list?.filter {
            !it?.value.isNullOrEmpty() && it?.value != ""
        }
        filtered?.associateByTo(measurementValues, {
            it?.key.toString()
        }, {
            it?.value.toString()
        })


        val name = dialogNameEditText.text.toString()
        val valuess = Valuess(measurementValues)
        val type = dialogSpinner.selectedItem as String


        val clientMeasurement = MeasurementValues(
            name,
            type,
            clientToBeEdited?.id.toString(),
            valuess.map as Map<String, String>
        )
        clientMeasurement.gender = clientToBeEdited?.gender
        i(title, "client $clientMeasurement")
        i(
            title,
            "Map ${valuess.map} \nvaluess $valuess list $list"
        )
        val navHostFragment =
            requireActivity().supportFragmentManager.fragments[0] as NavHostFragment
        val parentFragment =
            navHostFragment.childFragmentManager.primaryNavigationFragment as ClientFragment


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

                parentList?.add(clientMeasurement)
                dialog.dismiss()
                parentAdapter.notifyDataSetChanged()

            }
        }
    }

    private suspend fun CoroutineScope.getMeasurementTypes() {

    }

    private suspend fun getAllMeasurement(
        getAllMeasurements: Deferred<Flow<ServicesResponseWrapper<ParentData>>>,
        activityTitle: TextView
    ) {
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
                        ).apply {
                            this.id = m.id
                        }
                    }
                    if (measurementValueList?.isEmpty()!!) {
                        measurement_fragment_recycler_vf.showNext()
                    } else {
                        resource_fragment_measurement_rv.setupAdapter<MeasurementValues>(
                            R.layout.measurement_sub_item
                        ) { parentAdapter, context, parentList ->

                            bind { itemView, position, parentItem ->
                                itemView.measurement_sub_item_type_tv.text = parentItem?.name

                                //On click to edit
                                setOnMeasurementEdit(
                                    parentList,
                                    parentAdapter,
                                    itemView,
                                    parentItem,
                                    activityTitle
                                )

                                val v = parentItem?.values as? Map<String, String>

                                val valueList = v?.entries?.map { vl ->
                                    UserNameClass(
                                        vl.key,
                                        vl.value
                                    )
                                }?.filter { itv ->
                                    !itv.value.isNullOrEmpty() && itv.value != "null"
                                }

                                itemView.measurement_sub_item_rv.setupAdapter<UserNameClass>(
                                    R.layout.measurement_item
                                ) { adapter, context, list ->
                                    bind { subItemView, position, item ->
                                        subItemView.measurement_name.text = item?.title
                                        subItemView.measurement_value.text = item?.value

                                    }

                                    setLayoutManager(
                                        GridLayoutManager(
                                            requireContext(),
                                            3
                                        )
                                    )
                                    submitList(valueList)
                                }

                                addMeasurementBtn.setOnClickListener {
                                    dialog.show()
                                    dialogAddMeasurementBtn =
                                        dialogIncludeBtnLayout.findViewById<Button>(R.id.btn)
                                    dialogAddMeasurementProgressBar =
                                        dialogIncludeBtnLayout.findViewById<ProgressBar>(R.id.progress_bar)

                                    val dialogBtnBackgound = dialogAddMeasurementBtn.background
                                    dialogBtnBackgound?.changeBackgroundColor(
                                        requireContext(),
                                        R.color.colorPrimary
                                    )

                                    dialogTitleTv.text = getString(R.string.add_measurement)
                                    dialogAddMeasurementBtn.background = dialogBtnBackgound
                                    dialogAddMeasurementBtn.text =
                                        getString(R.string.add_measurement)
                                    dialogAddMeasurementBtn.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.colorAccent
                                        )
                                    )


                                    CoroutineScope(Main).launch {
                                        val getMeasurementTypes =
                                            async { authViewModel.getMeasurementTypes(header) }
                                        getMeasurementTypes.await()
                                            .catch {
                                                i(title, "Error on flow ${it.message}")
                                            }
                                            .collect {
                                                onFlowResponse<MeasurementTypeList>(response = it) {
                                                    i(title, "Measure ${it?.measurementTypes}")
                                                    it?.measurementTypes?.associateByTo(
                                                        measurementMap,
                                                        { k ->
                                                            k.id
                                                        },
                                                        { v ->
                                                            v.name
                                                        })
                                                    it?.measurementTypes?.associateByTo(
                                                        measurementListMap,
                                                        { k ->
                                                            k.name
                                                        },
                                                        { v ->
                                                            v.form
                                                        })
                                                    val measurementTypes =
                                                        it?.measurementTypes?.map {
                                                            it.name
                                                        }
                                                    setUpSpinnerWithList(
                                                        getString(R.string.select_str),
                                                        dialogSpinner,
                                                        measurementTypes as ArrayList<String>
                                                    )
                                                }
                                            }
                                        setListOnMeasurementChange(parentAdapter, parentList)

                                    }
                                }


                            }
                            setLayoutManager(
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            )
                            parentAdapter.delete(parentList)
                                .attachToRecyclerView(resource_fragment_measurement_rv)

                            submitList(measurementValueList)
                        }
                    }
                }
            }
    }

    private fun setOnMeasurementEdit(
        parentList: MutableList<MeasurementValues?>?,
        parentAdapter: RecyclingAdapter<MeasurementValues>,
        itemView: View,
        parentItem: MeasurementValues?,
        activityTitle: TextView
    ) {
        itemView.setOnClickListener {
            dialogEditTitleTv.text = getString(R.string.edit_measurement_str)
            dialogEditMeasurementBtn = dialogEditIncludeBtnLayout.findViewById<Button>(R.id.btn)
            dialogEditMeasurementProgressBar =
                dialogEditIncludeBtnLayout.findViewById<ProgressBar>(R.id.progress_bar)
            val dialogEditBtnBackgound = dialogEditMeasurementBtn.background
            dialogEditBtnBackgound?.changeBackgroundColor(requireContext(), R.color.colorPrimary)

            dialogEditMeasurementBtn.background = dialogEditBtnBackgound
            dialogEditMeasurementBtn.text = getString(R.string.update_measurement)
            dialogEditMeasurementBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            GlobalVariables.globalMeasuremenValues = parentItem
            editdialog.show()

            if (GlobalVariables.globalMeasuremenValues != null) {
                i(title, "Item ${GlobalVariables.globalMeasuremenValues}")

                activityTitle.text = getString(R.string.edit_measurement_str)
                dialogEditNameEditText.setText(parentItem?.name.toString())
                val dialogSpinnerList = ArrayList<String>()
                dialogSpinnerList.add(parentItem?.type.toString())
                setUpSpinnerWithList(
                    null,
                    dialogEditSpinner,
                    dialogSpinnerList
                )
                val valuesToBeEdited =
                    (parentItem?.values as Map<String, String>).entries.map { vtbe ->
                        UserNameClass(
                            vtbe.key,
                            vtbe.value ?: ""
                        )
                    }
                dialogEditRv.setupAdapter<UserNameClass>(R.layout.measurement_items) { subAdapter, context, list ->
                    bind { editItemView, position, editItem ->
                        editItemView.add_measurement_layout_dialog_input.hint = editItem?.title
                        editItemView.add_measurement_layout_dialog_et.setText(editItem?.value)
                        editItemView.add_measurement_layout_dialog_et.doAfterTextChanged { editable ->
                            editItem?.value =
                                editItemView.add_measurement_layout_dialog_et.text?.toString()

                        }

                        dialogEditMeasurementBtn.setOnClickListener {


                            val filtered = list?.filter {
                                !it?.value.isNullOrEmpty() && it?.value != ""
                            }
                            filtered?.associateByTo(measurementValues, {
                                it?.title.toString()
                            }, {
                                it?.value.toString()
                            })

                            val valuess = Valuess(measurementValues)

                            val name = dialogEditNameEditText.text.toString()
                            val type = dialogEditSpinner.selectedItem as String
                            val clientMeasurement = MeasurementValues(
                                name,
                                type,
                                clientToBeEdited?.id.toString(),
                                valuess.map
                            )
                            clientMeasurement.gender = clientToBeEdited?.gender
                            clientMeasurement.id = parentItem.id
                            i(title, "Edit measurement ${clientMeasurement.id}")
                            CoroutineScope(Main).launch {
                                supervisorScope {
                                    val updateMeasurementCall = async {
                                        authViewModel.editMeasurement(
                                            header,
                                            clientMeasurement
                                        )
                                    }
                                    updateMeasurementCall.await()
                                        .catch {
                                            i(title, "RecyclingAdapter delete ${it.message}")
                                        }
                                        .collect {
                                            onFlowResponse<MeasurementValues>(response = it) {
                                                GlobalVariables.globalMeasuremenValues = null
                                                parentAdapter.notifyDataSetChanged()
                                                editdialog.dismiss()
                                                requireActivity().gdToast(
                                                    "$name is updated successfully",
                                                    Gravity.BOTTOM
                                                )
                                            }
                                        }
                                }
                            }
                        }
                    }
                    setLayoutManager(GridLayoutManager(requireContext(), 2))
                    submitList(valuesToBeEdited)
                    editdialog.setOnDismissListener {
                        GlobalVariables.globalMeasuremenValues = null
                    }
                }


            } else {

            }

        }
    }


    private fun <T> RecyclingAdapter<T>.delete(list: MutableList<T?>?): ItemTouchHelper {

        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val item = list?.get(pos) as MeasurementValues

                CoroutineScope(Main).launch {
                    supervisorScope {
                        val deleteMeasurementCall =
                            async { authViewModel.deleteMeasurements(header, item.id.toString()) }
                        deleteMeasurementCall.await()
                            .catch {
                                i(title, "RecyclingAdapter delete ${it.message}")
                            }
                            .collect {
                                onFlowResponse<UserResponse<NothingExpected>>(response = it) {
                                    list.removeAt(pos)
                                    this@delete.notifyDataSetChanged()
                                    if (list.isEmpty()) {
                                        measurement_fragment_recycler_vf.showNext()
                                    }
                                    requireActivity().gdToast(
                                        it?.message.toString(),
                                        Gravity.BOTTOM
                                    )
                                }
                            }
                    }
                }

                i("OnRemoved", " pos $pos itemId ${item.id}")


            }

        })

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

