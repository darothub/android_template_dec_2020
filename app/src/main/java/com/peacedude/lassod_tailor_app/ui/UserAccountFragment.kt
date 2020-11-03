package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.fragment_user_account.*
import kotlinx.android.synthetic.main.fragment_user_account.account_save_changes_btn
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import kotlinx.android.synthetic.main.user_profile_name_item.*
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
                    UserNameClass(getString(R.string.other_name), user?.otherName.toString())
                )
                user_profile_name_rv.setupAdapter<UserNameClass>(R.layout.user_profile_name_item) { adapter, context, list ->
                    bind { itemView, position, item ->
                        itemView.user_profile_rv_item_name_title_tv.text = item?.title
                        itemView.user_profile_rv_item_name_value_tv.text = item?.value

                        itemView.user_profile_rv_item_name_value_tv.setOnClickListener {
                            val nameLayout =
                                dialog.findViewById<ViewGroup>(R.id.multipurpose_name_dialog)
                            val dialogTitle =
                                dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
                            val dialogEditText =
                                dialog.findViewById<TextInputEditText>(R.id.multipurpose_name_dialog_et)
                            val dialogInput =
                                dialog.findViewById<TextInputLayout>(R.id.multipurpose_name_dialog_input)
                            dialogEditText.setText(item?.value)
                            dialogInput.hint = item?.title
                            dialogTitle.text = item?.title
                            nameLayout.show()
                            dialog.show()
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
//                first_name_value_tv.setText(user?.firstName)
//                last_name_value_tv.setText(user?.lastName)
//                other_name_et.setText(user?.otherName)
//                gender_name_et.setText(user?.gender?: "")
//                workshop_address_et.setText(user?.workshopAddress?: "")
//                showroom_address_et.setText(user?.showroomAddress?: "")
//                no_employee_et.setText(user?.no_Employee?: "")
//                legal_status_et.setText(user?.legalStatus?: "")
//                name_of_union_et.setText(user?.name_union?: "")
//                ward_et.setText(user?.ward?: "")
//                account_lga_et.setText(user?.lga?: "")
//                account_state_et.setText(user?.state?: "Lagos")
//                i(title, "UserToken ${currentUser?.token} loggedIn\n${user?.firstName}")

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
