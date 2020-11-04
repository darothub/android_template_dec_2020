package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
    var genderFocusValue = ""
    var visitUsForMeasurementValue:Boolean?= false
    var acceptSelfMeasurementValue:Boolean?= false
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

                Log.i(title, " visitus\n${user?.visitUsMeasurement} accept ${user?.acceptSelfMeasurement}  ")

                setUpSpecialityRv(user)

                val genderList = setupGenderFocusRecyclerView(user)
                val measurementList = setupMeasurementOptionRv(user)

                //List of fields to be filled with names and others
                val qaList = arrayListOf<UserNameClass>(
                    UserNameClass(getString(R.string.obioma_trained_str), user?.obiomaCert.toString()),
                    UserNameClass(getString(R.string.delivery_lead_time_str), user?.deliveryTime.toString())
                )
                specialty_fragment_qa_rv.setupAdapter<UserNameClass>(R.layout.user_profile_name_item) { adapter, context, list ->
                    bind { itemView, position, item ->
                        CoroutineScope(Main).launch {
                            delay(2000)
                            itemView.user_profile_rv_item_name_title_tv.text = item?.title
                            itemView.user_profile_rv_item_name_value_tv.text = item?.value
                            delay(2000)
                            itemView.user_profile_name_shimmerLayout.stopShimmer()
                            itemView.user_profile_name_shimmerLayout.setShimmer(null)
                            itemView.user_profile_name_item_container.background = null
                        }
                        itemView.user_profile_rv_item_name_value_tv.setOnClickListener {

                            //If gender is clicked
                            if (item?.title == getString(R.string.obioma_trained_str)) {
                                itemView.user_profile_rv_item_name_value_tv.setOnClickListener {
                                    //Provide gender layout
                                    val closeOptionRadioGroup =
                                        dialog.findViewById<RadioGroup>(R.id.multipurpose_gender_dialog_gender_rg)
                                    val yesRadioBtn =
                                        dialog.findViewById<RadioButton>(R.id.multipurpose_gender_dialog_female_rbtn)
                                    yesRadioBtn.text = getString(R.string.yes_str)
                                    val noRadioBtn =
                                        dialog.findViewById<RadioButton>(R.id.multipurpose_gender_dialog_male_rbtn)
                                    noRadioBtn.text = getString(R.string.no_str)
                                    val dialogTitle =
                                        dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
                                    dialogTitle.text = item.title
                                    //When
                                    when (item.value) {
                                        //female radio is ticked
                                        getString(R.string.yes_str) -> {
                                            //Display female radio ticked
                                            yesRadioBtn.isChecked = true

                                        }
                                        //male radio is ticked
                                        getString(R.string.no_str) -> {
                                            //Display female radio ticked
                                            noRadioBtn.isChecked = true
                                        }
                                        else -> {
                                            noRadioBtn.isChecked = false
                                            yesRadioBtn.isChecked = false
                                        }

                                    }
                                    closeOptionLayout.show()
                                    dialog.show()

                                    dialogOkTv.setOnClickListener {
                                        //When
                                        when (closeOptionRadioGroup.checkedRadioButtonId) {
                                            //female radio is ticked
                                            R.id.multipurpose_gender_dialog_female_rbtn -> {
                                                itemView.user_profile_rv_item_name_value_tv.text =
                                                    getString(R.string.female)
                                                //Update new user data
//                                                newUserData.gender = yesRadioBtn.text.toString()
                                            }
                                            //male radio is ticked
                                            R.id.multipurpose_gender_dialog_male_rbtn -> {
                                                itemView.user_profile_rv_item_name_value_tv.text =
                                                    getString(R.string.male)
                                                //Update new user data
//                                                newUserData.gender = noRadioBtn.text.toString()
                                            }
                                        }
                                        dialog.dismiss()
                                    }
                                    dialog.setOnDismissListener {
                                        closeOptionLayout.hide()
                                    }
                                }
                            } else {

                                //If not close option value clicked
                                itemView.user_profile_rv_item_name_value_tv.setOnClickListener {
                                    val dialogTitle =
                                        dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
                                    val dialogEditText =
                                        dialog.findViewById<TextInputEditText>(R.id.multipurpose_name_dialog_et)
                                    val dialogInput =
                                        dialog.findViewById<TextInputLayout>(R.id.multipurpose_name_dialog_input)
                                    dialogEditText.setText(item?.value)
                                    dialogInput.hint = item?.title
                                    dialogTitle.text = item?.title
                                    dialogNameAndOtherLayout.show()
                                    dialog.show()
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

                saveBtn.setOnClickListener {
                    if (user != null) {
                        Log.i("checkbox", "genderfocus $genderFocusValue")
                        updateUserData(user)
                    }
                }
            }
        })
    }

    private fun setUpSpecialityRv(user: User?) {
        val specialtyList = resources.getStringArray(R.array.specialty_list).toList().map { str ->
            i(title, "Str ${user?.specialty?.toList()}")
            if (user?.specialty != null && user.specialty?.contains(str)!!) {
                i(title, "Str1 $str")
                RecyclerItemForCheckBox(text = str, selected = true)
            }
            else{
                RecyclerItemForCheckBox(text = str)
            }

        }
        specialty_rv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Main).launch {
                    delay(2000)
                    itemView.specialty_item_checkbox.show()
                    itemView.specialty_item_checkbox.text = item?.text
                    Log.i(title, "specialty ${item?.text}")
                    itemView.specialty_item_checkbox.isChecked = item?.selected!!
                    delay(2000)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }
                itemView.specialty_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    when (isChecked) {
                        true -> {
                            user?.specialty?.add(item?.text.toString())
                            item?.selected == true
                        }
                        false -> item?.selected == false
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            setLayoutManager(GridLayoutManager(context, 2))
            submitList(specialtyList)
        }
    }

    private fun setupGenderFocusRecyclerView(user: User?):List<RecyclerItemForCheckBox> {
        var genderList = resources.getStringArray(R.array.gender_list).toList().map { str ->
            if (str.toLowerCase() == user?.genderFocus) {
                RecyclerItemForCheckBox(text = str, selected = true)
            }
            RecyclerItemForCheckBox(text = str)

        }
        val checkboxes = arrayListOf<CheckBox>()

        gender_rv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Main).launch {
                    delay(2000)
                    itemView.specialty_item_checkbox.show()
                    itemView.specialty_item_checkbox.text = item?.text
                    itemView.specialty_item_checkbox.isChecked = item?.selected!!
                    delay(2000)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }

                checkboxes.add(itemView.specialty_item_checkbox)
                itemView.specialty_item_checkbox.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        compoundButton.isChecked = true
                        val otherCheckboxes =
                            checkboxes.filter { checkBox -> checkBox.text != compoundButton.text }
                        otherCheckboxes.forEach { checkbox ->
                            if (checkbox.isChecked) {
                                checkbox.isChecked = !checkbox.isChecked
                            }
                        }
                        val sz = otherCheckboxes.size
                        genderFocusValue = compoundButton.text.toString()
                        Log.i("checkbox", "hello ${compoundButton.text} othersSize $sz")
                    }
                }

            }
            setLayoutManager(GridLayoutManager(context, 4))
            submitList(genderList)
        }
        return genderList
    }

    private fun setupMeasurementOptionRv(user: User?):List<RecyclerItemForCheckBox> {

        val measurementOptionsList =
            resources.getStringArray(R.array.measurement_options_list).toList().map { str ->
                if (user?.visitUsMeasurement == true && str == "Visit us for your measurement") {
                    RecyclerItemForCheckBox(text = str, selected = true)
                }
                else if(user?.acceptSelfMeasurement == true && str == "Will accept self-measurement"){
                    RecyclerItemForCheckBox(text = str, selected = true)
                }
                else{
                    RecyclerItemForCheckBox(text = str)
                }
            }
        val checkboxes = arrayListOf<CheckBox>()
        measurement_options_rv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->

                CoroutineScope(Main).launch {
                    delay(2000)
                    itemView.specialty_item_checkbox.show()
                    itemView.specialty_item_checkbox.text = item?.text
                    itemView.specialty_item_checkbox.isChecked = item?.selected!!
                    delay(2000)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }

                checkboxes.add(itemView.measurement_checkbox)
                itemView.measurement_checkbox.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        compoundButton.isChecked = true
                        val otherCheckboxes =
                            checkboxes.filter { checkBox -> checkBox.text != compoundButton.text }
                        otherCheckboxes.forEach { checkbox ->
                            if (checkbox.isChecked) {
                                checkbox.isChecked = !checkbox.isChecked
                            }
                        }
                        val sz = otherCheckboxes.size
                        if (compoundButton.text == "Visit us for your measurement"){
                            visitUsForMeasurementValue = true
                        }
                        else if(compoundButton.text == "Will accept self-measurement"){
                            acceptSelfMeasurementValue = true
                        }

                        Log.i("checkbox", "visit us ${visitUsForMeasurementValue} othersSize $sz")
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
            submitList(measurementOptionsList)
        }
        return measurementOptionsList
    }

    private fun updateUserData(user: User) {
//        val newUserData = User(firstName, lastName, otherName,category,phone)
        user.isVerified = true
        user.genderFocus = genderFocusValue

        user.visitUsMeasurement = visitUsForMeasurementValue
        user.acceptSelfMeasurement = acceptSelfMeasurementValue
        Log.i(title, "onRequest $visitUsForMeasurementValue $acceptSelfMeasurementValue")
        val request = authViewModel.updateUserData(header, user)
        val response = requireActivity().observeRequest(request, progressBar, saveBtn)
        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask<User>(bool, result) {
                val response = result as? UserResponse<User>
                val msg = response?.message
                val profileData = response?.data
                authViewModel.profileData = profileData
            }
        })
    }

}
data class RecyclerItemForCheckBox(val text: String, val selected: Boolean = false)