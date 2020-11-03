package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user_account.account_save_changes_btn
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.user_profile_address_item.view.*
import kotlinx.android.synthetic.main.user_profile_name_item.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [UserAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserAccountFragment : DaggerFragment() {
    val title: String by lazy {
        getName()
    }

    private lateinit var saveBtn: Button
    private lateinit var progressBar: ProgressBar

    //Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }
    val header by lazy {
        authViewModel.header
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
    private val genderLayout by lazy{
        dialog.findViewById<ViewGroup>(R.id.multipurpose_gender_dialog)
    }
    private val addressLayout by lazy{
        dialog.findViewById<ViewGroup>(R.id.multipurpose_address_dialog)
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val saveBtnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        saveBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor( requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        i(title, "header $header token ${currentUser?.token}")
        buttonTransactions({
            saveBtn = account_save_changes_btn.findViewById(R.id.btn)
            progressBar = account_save_changes_btn.findViewById(R.id.progress_bar)
        },{
            saveBtn.text = getString(R.string.save_changes)
            saveBtn.background = saveBtnBackground
            saveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        })

        getUserData()

//        first_name_value_tv.setOnClickListener {
//            val nameLayout = dialog.findViewById<ViewGroup>(R.id.multipurpose_name_dialog)
//            val dialogTitle = dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
//            val dialogEditText = dialog.findViewById<TextInputEditText>(R.id.multipurpose_name_dialog_et)
//            val dialogInput = dialog.findViewById<TextInputLayout>(R.id.multipurpose_name_dialog_input)
//            dialogEditText.setText(first_name_value_tv.text)
//            dialogInput.hint = getString(R.string.first_name)
//            dialogTitle.text = getString(R.string.first_name)
//            nameLayout.show()
//            dialog.show()
//        }

    }

    @SuppressLint("SetTextI18n")
    private fun getUserData(){
        val request = authViewModel.getUserData(header.toString())
        val response = requireActivity().observeRequest(request, null, null, true)
        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask<User>(bool, result) {
                val resp = result as? UserResponse<User>
                val user = resp?.data

                val nameList = arrayListOf<UserNameClass>(
                    UserNameClass(getString(R.string.first_name), user?.firstName.toString()),
                    UserNameClass(getString(R.string.last_name), user?.lastName.toString()),
                    UserNameClass(getString(R.string.other_name), user?.otherName.toString()),
                    UserNameClass(getString(R.string.gender), user?.gender.toString()),
                    UserNameClass(getString(R.string.no_of_emplyee), user?.no_Employee.toString()),
                    UserNameClass(getString(R.string.legal_status), user?.legalStatus.toString())
                )
                user_profile_name_rv.setupAdapter<UserNameClass>(R.layout.user_profile_name_item) { adapter, context, list ->
                    bind { itemView, position, item ->
                        itemView.user_profile_rv_item_name_title_tv.text = item?.title
                        itemView.user_profile_rv_item_name_value_tv.text = item?.value

                        if(item?.title == getString(R.string.gender)){
                            itemView.user_profile_rv_item_name_value_tv.setOnClickListener {

                                val genderRadioGroup = dialog.findViewById<RadioGroup>(R.id.multipurpose_gender_dialog_gender_rg)
                                val femaleRadioBtn =
                                    dialog.findViewById<RadioButton>(R.id.multipurpose_gender_dialog_female_rbtn)
                                val maleRadioBtn =
                                    dialog.findViewById<RadioButton>(R.id.multipurpose_gender_dialog_male_rbtn)
                                val dialogTitle =
                                    dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
                                dialogTitle.text = item.title
                                if(item.value == getString(R.string.female)){
                                    femaleRadioBtn.isSelected = true
                                }
                                else if(item.value == getString(R.string.male)){
                                    maleRadioBtn.isSelected = true
                                }
                                else{
                                    maleRadioBtn.isSelected = false
                                    femaleRadioBtn.isSelected = false
                                }
                                genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                                    when(checkedId){
                                        R.id.multipurpose_gender_dialog_female_rbtn ->{
                                            itemView.user_profile_rv_item_name_value_tv.text = getString(R.string.female)
                                        }
                                        R.id.multipurpose_gender_dialog_male_rbtn ->{
                                            itemView.user_profile_rv_item_name_value_tv.text = getString(R.string.male)
                                        }
                                    }
                                }
                                genderLayout.show()
                                dialog.show()

                                dialog.setOnDismissListener {
                                    genderLayout.hide()
                                }
                            }
                        }
                        else{
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
                                dialog.setOnDismissListener {
                                    dialogNameAndOtherLayout.hide()
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
                    submitList(nameList)
                }

                val addressList = arrayListOf<UserAddressClass>(
                    UserAddressClass(getString(R.string.workshop_address), user?.workshopAddress.toString()),
                    UserAddressClass(getString(R.string.showroom_address), user?.showroomAddress.toString())
                )

                user_profile_address_rv.setupAdapter<UserAddressClass>(R.layout.user_profile_address_item) { adapter, context, list ->
                    bind { itemView, position, item ->
                        itemView.user_profile_rv_item_address_title_tv.text = item?.title
                        itemView.user_profile_rv_item_address_value_tv.text = item?.value

                        itemView.setOnClickListener {

                            val dialogTitle =
                                dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
                            val dialogStreetEditText =
                                dialog.findViewById<TextInputEditText>(R.id.multipurpose_address_dialog_street_et)
                            val dialogCityEditText =
                                dialog.findViewById<TextInputEditText>(R.id.multipurpose_address_dialog_city_et)
                            val dialogStateEditText =
                                dialog.findViewById<TextInputEditText>(R.id.multipurpose_address_dialog_state_et)

                            dialogTitle.text = item?.title
                            addressLayout.show()
                            dialog.show()
                            dialog.setOnDismissListener {
                                addressLayout.hide()
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
                    submitList(addressList)
                }

                val unionList = arrayListOf<UserNameClass>(
                    UserNameClass(getString(R.string.name_of_union), user?.name_union.toString()),
                    UserNameClass(getString(R.string.ward), user?.ward.toString()),
                    UserNameClass(getString(R.string.lga), user?.lga.toString()),
                    UserNameClass(getString(R.string.state), user?.state.toString())
                )
                user_profile_name_union_rv.setupAdapter<UserNameClass>(R.layout.user_profile_name_item) { adapter, context, list ->
                    bind { itemView, position, item ->
                        itemView.user_profile_rv_item_name_title_tv.text = item?.title
                        itemView.user_profile_rv_item_name_value_tv.text = item?.value

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
                    submitList(unionList)
                }

                saveBtn.setOnClickListener {
                    if (user != null) {
//                        updateUserData(user)
                    }
                }
            }
        })
    }
//    private fun updateUserData(user: User){
//        user.firstName = "Darotudeen"
//        user.isVerified = true
//        user.firstName = first_name_value_tv.text.toString().trim()
//        user.lastName = last_name_value_tv.text.toString().trim()
//        user.otherName = other_name_et.text.toString().trim()
//        user.category = user.category
//        user.phone = user.phone
//        user.gender = gender_name_et.text.toString().toLowerCase().trim()
//        user.workshopAddress = workshop_address_et.text.toString().trim()
//        user.showroomAddress = showroom_address_et.text.toString().trim()
//        user.no_Employee = no_employee_et.text.toString().trim()
//        user.legalStatus = legal_status_et.text.toString().trim()
//        user.name_union = name_of_union_et.text.toString().trim()
//        user.ward = ward_et.text.toString().trim()
//        user.lga =account_lga_et.text.toString().trim()
//        user.state = account_state_et.text.toString().trim()
////        val newUserData = User(firstName, lastName, otherName,category,phone)
//        val request = authViewModel.updateUserData(header.toString(), user)
//        val response = requireActivity().observeRequest(request, progressBar, saveBtn)
//        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            val (bool, result) = it
//            onRequestResponseTask<User>(bool, result){
//                val response = result as? UserResponse<User>
//                val msg = response?.message
//                val profileData = response?.data
//                authViewModel.profileData = profileData
//                i(title, "msg $msg ${profileData?.firstName}")
//            }
//        })
//    }

}

data class UserNameClass(var title:String, var value:String)
data class UserAddressClass(var title:String, var value:String)
