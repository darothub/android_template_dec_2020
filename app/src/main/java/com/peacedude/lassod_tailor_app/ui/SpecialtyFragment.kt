package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.utils.bearer
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_specialty.*
import kotlinx.android.synthetic.main.specialty_layout_item.view.*
import kotlinx.android.synthetic.main.user_profile_name_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SpecialtyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpecialtyFragment : DaggerFragment() {

    val title: String by lazy {
        getName()
    }
    private lateinit var saveBtn: Button
    private lateinit var progressBar: ProgressBar
    val currentUser: User? by lazy {
        authViewModel.currentUser
    }
    val token by lazy {
        currentUser?.token
    }
    val header by lazy {
        "$bearer $token"
    }

    private val dialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.multipurpose_dialog_layout)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(this.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val window = this.window
            window?.attributes = lp
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }
    private val dialogNameAndOtherLayout by lazy{
        dialog.findViewById<ViewGroup>(R.id.multipurpose_name_dialog)
    }
    private val dialogOkTv by lazy{
        dialog.findViewById<TextView>(R.id.multipurpose_dialog_ok_tv)
    }
    private val dialogCancelTv by lazy{
        dialog.findViewById<TextView>(R.id.multipurpose_dialog_cancel_tv)
    }
    private val closeOptionLayout by lazy{
        dialog.findViewById<ViewGroup>(R.id.multipurpose_gender_dialog)
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    var genderFocusList = ArrayList<String>()
    var specialtyValueList= ArrayList<String>()
    var visitUsForMeasurementValue:Boolean?= false
    var acceptSelfMeasurementValue:Boolean?= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    val newUserData by lazy{
        User()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specialty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val saveBtnBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        saveBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
        buttonTransactions({
            saveBtn = specialty_save_changes_btn.findViewById(R.id.btn)
            progressBar = specialty_save_changes_btn.findViewById(R.id.progress_bar)
        }, {
            saveBtn.text = getString(R.string.save_changes)
            saveBtn.background = saveBtnBackground
            saveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        })
        getUserData()

        dialogCancelTv.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val request = authViewModel.getUserData(header)
        val response = requireActivity().observeRequest(request, null, null, true)
        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask<User>(bool, result) {
                val resp = result as? UserResponse<User>
                val user = resp?.data

                setUpSpecialityRv(user)
                val genderList = setupGenderFocusRecyclerView(user)
                val measurementList = setupMeasurementOptionRv(user)
                val qaList = setupQaRecylerView(user)

                saveBtn.setOnClickListener {
                    user?.deliveryTimePeriod = qaList[1].value
                    if(qaList[1].value != "null"){
                        user?.deliveryTimeNo = qaList[1].value.toInt()
                    }
                    else{
                        user?.deliveryTimeNo  = 0
                    }

                    user?.visitUsMeasurement = measurementList[0].selected
                    user?.acceptSelfMeasurement = measurementList[1].selected
                    i(title, "checked1 ${measurementList[0].selected }")
                    i(title, "checked2 ${measurementList[1].selected }")
                    if (user != null) {
                        updateUserData(user)
                    }
                }
            }
        })
    }

    private fun setupQaRecylerView(user: User?):ArrayList<UserNameClass> {
        //List of fields to be filled with names and others
        val qaList = arrayListOf<UserNameClass>(
            UserNameClass(getString(R.string.obioma_trained_str), user?.obiomaCert.toString()),
            UserNameClass(getString(R.string.delivery_time_period_str), user?.deliveryTimePeriod.toString()),
            UserNameClass(getString(R.string.delivery_time_no_str), user?.deliveryTimeNo.toString())
        )
        specialty_fragment_qa_rv.setupAdapter<UserNameClass>(R.layout.user_profile_name_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Main).launch {
                    itemView.user_profile_rv_item_name_title_tv.text = item?.title
                    itemView.user_profile_rv_item_name_value_tv.text = item?.value
                    delay(1000)
                    itemView.user_profile_name_shimmerLayout.stopShimmer()
                    itemView.user_profile_name_shimmerLayout.setShimmer(null)
                    itemView.user_profile_name_item_container.background = null
                }
                itemView.user_profile_rv_item_name_value_tv.setOnClickListener {
                    val dialogTitle =
                        dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
                    val dialogEditText =
                        dialog.findViewById<TextInputEditText>(R.id.multipurpose_name_dialog_et)
                    val dialogInput =
                        dialog.findViewById<TextInputLayout>(R.id.multipurpose_name_dialog_input)
                    if(item?.title == getString(R.string.delivery_time_no_str)){
                        if(item.value == null || item.value == "null" ){
                            item.value = 0.toString()
                        }
                        dialogEditText.inputType = InputType.TYPE_CLASS_NUMBER
                    }

                    dialogEditText.setText(item?.value)

                    dialogInput.hint = item?.title
                    dialogTitle.text = item?.title
                    dialogNameAndOtherLayout.show()
                    if(item?.title != getString(R.string.obioma_trained_str)){
                        dialog.show()
                    }
                    else{
                        dialogNameAndOtherLayout.hide()
                        dialog.hide()
                    }

                    dialogOkTv.setOnClickListener {
                        //Update item in the list
                        item?.value = dialogEditText.text.toString()
                        //Update recycler view
                        adapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    dialog.setOnDismissListener {
                        dialogNameAndOtherLayout.hide()
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
            submitList(qaList)
        }
        return qaList
    }

    private fun setUpSpecialityRv(user: User?) {
        val specialtyList = resources.getStringArray(R.array.specialty_list).toList().map { str ->
            if (user?.specialty != null && user.specialty?.contains(str.toLowerCase())!!) {
                RecyclerItemForCheckBox(text = str, selected = true)
            }
            else{
                RecyclerItemForCheckBox(text = str)
            }

        }
        specialty_rv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Main).launch {
                    itemView.specialty_item_checkbox.show()
                    itemView.specialty_item_checkbox.text = item?.text?.toLowerCase()
                    itemView.specialty_item_checkbox.isChecked = item?.selected!!
                    delay(1000)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }
                itemView.specialty_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    when (isChecked) {
                        true -> {
                            specialtyValueList.add(buttonView.text.toString())
                            item?.selected == isChecked
                            i(title, "specialty ${specialtyValueList.toList()}")
                        }
                        false -> item?.selected == isChecked
                    }

                }
            }
            setLayoutManager(GridLayoutManager(context, 2))
            submitList(specialtyList)
        }
    }

    private fun setupGenderFocusRecyclerView(user: User?):List<RecyclerItemForCheckBox> {
        var genderList = resources.getStringArray(R.array.gender_list).toList().map { str ->
            if (user?.genderFocus != null && user.genderFocus?.contains(str.toLowerCase())!!) {
                RecyclerItemForCheckBox(text = str, selected = true)
            }
            else{
                RecyclerItemForCheckBox(text = str)
            }
        }
        val checkboxes = arrayListOf<CheckBox>()

        gender_rv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Main).launch {
                    itemView.specialty_item_checkbox.show()
                    itemView.specialty_item_checkbox.text = item?.text?.toLowerCase()
                    itemView.specialty_item_checkbox.isChecked = item?.selected!!
                    delay(1000)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }

                checkboxes.add(itemView.specialty_item_checkbox)
                itemView.specialty_item_checkbox.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        genderFocusList.add(compoundButton.text.toString())
                        i(title, "genderFocus ${genderFocusList.toList()}")
                    }
                }

            }
            setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            submitList(genderList)
        }
        return genderList
    }

    private fun setupMeasurementOptionRv(user: User?):List<RecyclerItemForCheckBox> {

        val measurementOptionsList = arrayListOf<RecyclerItemForCheckBox>(
            RecyclerItemForCheckBox(getString(R.string.visit_us_for_measurement_str), user?.visitUsMeasurement!!),
            RecyclerItemForCheckBox(getString(R.string.accept_self_measurement_str), user.acceptSelfMeasurement)
        )

        val checkboxes = arrayListOf<CheckBox>()
        measurement_options_rv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->

                CoroutineScope(Main).launch {
                    itemView.measurement_checkbox.show()
                    itemView.measurement_checkbox.text = item?.text
                    itemView.measurement_checkbox.isChecked = item?.selected!!
                    delay(1000)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }

                checkboxes.add(itemView.measurement_checkbox)
                i(title, "checkboxes us ${checkboxes.size}")
                itemView.measurement_checkbox.setOnCheckedChangeListener { compoundButton, b ->
                    item?.selected = compoundButton.isChecked
                    user.visitUsMeasurement = list?.get(0)?.selected!!
                    user.acceptSelfMeasurement = list?.get(1)?.selected!!
                    i(title, "checked1 ${list?.get(0)?.selected }")
                    i(title, "checked2 ${list?.get(1)?.selected }")
                    val otherCheckboxes =
                        checkboxes.filter { checkBox -> checkBox.text != compoundButton.text }
                    otherCheckboxes.forEach { checkbox ->
                        checkbox.isChecked = !compoundButton.isChecked
                    }
                    val sz = otherCheckboxes.size
                    i(title, "others us $sz")
                }
            }
            setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )
            submitList(measurementOptionsList)
        }
        return measurementOptionsList
    }

    private fun updateUserData(user: User) {
//        val newUserData = User(firstName, lastName, otherName,category,phone)
        user.isVerified = true
        user.specialty = specialtyValueList
        user.genderFocus = genderFocusList
        val request = authViewModel.updateUserData(header, user)
        val response = requireActivity().observeRequest(request, progressBar, saveBtn)
        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask<User>(bool, result) {
                val response = result as? UserResponse<User>
                val msg = response?.message
                val profileData = response?.data
                authViewModel.profileData = profileData
                requireActivity().gdToast(msg.toString(), Gravity.BOTTOM)
            }
        })
    }

}
data class RecyclerItemForCheckBox(val text: String, var selected: Boolean = false)