package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.utils.RecyclerItem
import com.peacedude.lassod_tailor_app.utils.bearer
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_specialty.*
import kotlinx.android.synthetic.main.fragment_user_account.*
import kotlinx.android.synthetic.main.specialty_layout_item.view.*
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
        storageRequest.checkUser(loggedInUserKey)
    }
    val token by lazy {
        currentUser?.token
    }
    val header by lazy {
        "$bearer $token"
    }

    @Inject
    lateinit var storageRequest: StorageRequest

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    var genderValue = ""
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

    override fun onResume() {
        super.onResume()

        val specialtyList = resources.getStringArray(R.array.specialty_list).toList()
        specialty_rv.setupAdapter<String>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                itemView.specialty_item_checkbox.show()
                itemView.specialty_item_checkbox.text = item
            }
            setLayoutManager(GridLayoutManager(context, 2))
            submitList(specialtyList)
        }


        val measurementOptionsList =
            resources.getStringArray(R.array.measurement_options_list).toList()
        measurement_options_rv.setupAdapter<String>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                itemView.measurement_vg.show()
                itemView.measurement_options_tv.text = item
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
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val request = authViewModel.getUserData(header)
        val response = requireActivity().observeRequest(request, null, null)
        response.observe(this, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask(bool, result) {
                val resp = result as? UserResponse
                val user = resp?.data

                obioma_trained_value_tv.text = user?.obiomaCert
                delivery_lead_time_value_tv.text = user?.deliveryTime?: ""
                Log.i(title, "UserToken ${currentUser?.token} loggedIn\n${user?.firstName}")

                setupGenderFocusRecyclerView(user)

                saveBtn.setOnClickListener {
                    if (user != null) {
                        updateUserData(user)
                    }
                }
            }
        })
    }

    private fun setupGenderFocusRecyclerView(user: User?) {
        var genderList = resources.getStringArray(R.array.gender_list).toList().map { str ->
            if (str.toLowerCase() == user?.genderFocus) {
                RecyclerItem(text = str, selected = true)
            }
            RecyclerItem(text = str)

        }
        val checkboxes = arrayListOf<CheckBox>()

        gender_rv.setupAdapter<RecyclerItem>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                itemView.specialty_item_checkbox.show()
                itemView.specialty_item_checkbox.text = item?.text
                itemView.specialty_item_checkbox.isChecked = item?.selected!!

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
                        Log.i("checkbox", "hello ${compoundButton.text} othersSize $sz")
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
    }

    private fun updateUserData(user: User) {
//        val newUserData = User(firstName, lastName, otherName,category,phone)
        user.isVerified = true
        val request = authViewModel.updateUserData(header, user)
        val response = requireActivity().observeRequest(request, progressBar, saveBtn)
        response.observe(this, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask(bool, result) {
                val response = result as? UserResponse
                val msg = response?.message
                val profileData = response?.data
                authViewModel.profileData = profileData
            }
        })
    }

}