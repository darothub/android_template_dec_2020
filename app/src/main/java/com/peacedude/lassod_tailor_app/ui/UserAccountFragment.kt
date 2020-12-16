package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.UserAddress
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user_account.account_save_changes_btn
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.user_profile_address_item.view.*
import kotlinx.android.synthetic.main.user_profile_name_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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
    private val dialogOkTv by lazy{
        dialog.findViewById<TextView>(R.id.multipurpose_dialog_ok_tv)
    }
    private val dialogCancelTv by lazy{
        dialog.findViewById<TextView>(R.id.multipurpose_dialog_cancel_tv)
    }
    lateinit var observer: StartActivityForResults
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = StartActivityForResults(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer as StartActivityForResults)
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
        networkMonitor().observe(viewLifecycleOwner, Observer {
            if (it) {
                getUserData()
            }
        })


        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CODE
        )

        tap_to_change_pic_tv.setOnClickListener {
            photoRequest()
        }
        user_account_profile_image.setOnClickListener {
            photoRequest()
        }
    }

    private fun photoRequest() {
        val res = checkCameraPermission()
        if (res) {
            getPhotoData()
        } else {
            i(title, getString(R.string.no_permission))
            requireActivity().gdToast(getString(R.string.no_permission), Gravity.BOTTOM)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData(){
        //Get user data request
        val request = authViewModel.getUserData(header.toString())
        //Get User data response
        observeRequest<User>(request, null, null, true, {resp->
            val user = resp?.data

            //Initialize new user for data update
            val newUserData = User()
            val nameList = setUserNamesAndOtherFields(user, newUserData)
            val addressList = setUserAddress(user)
            val unionList = setUnionData(user)

            saveBtn.setOnClickListener {
                newUserData.firstName = nameList[0].value
                newUserData.lastName = nameList[1].value
                newUserData.otherName = nameList[2].value
                if(nameList[4].value != "null"){
                    newUserData.noOfEmployees = nameList[4].value?.toInt()
                }
                else{
                    newUserData.noOfEmployees = 0
                }

                newUserData.workshopUserAddress = addressList[0].value
                newUserData.showroomUserAddress = addressList[1].value
                newUserData.showroomUserAddress = addressList[1].value
                newUserData.unionName = unionList[0].value
                newUserData.unionWard = unionList[1].value
                newUserData.unionLga = unionList[2].value
                newUserData.unionState = unionList[3].value
                i(title, "new user ${newUserData.workshopUserAddress}")
                updateUserData(newUserData)
            }
        },{err->
            i(title, "UserAccountError $err")
        })
        //Observe response
//        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            val (bool, result) = it
//            //Do on response
//            onRequestResponseTask<User>(bool, result) {
//                val resp = result as? UserResponse<User>
//                val user = resp?.data
//
//                //Initialize new user for data update
//                val newUserData = User()
//                val nameList = setUserNamesAndOtherFields(user, newUserData)
//                val addressList = setUserAddress(user)
//                val unionList = setUnionData(user)
//
//                saveBtn.setOnClickListener {
//                    newUserData.firstName = nameList[0].value
//                    newUserData.lastName = nameList[1].value
//                    newUserData.otherName = nameList[2].value
//                    if(nameList[4].value != "null"){
//                        newUserData.noOfEmployees = nameList[4].value?.toInt()
//                    }
//                    else{
//                        newUserData.noOfEmployees = 0
//                    }
//
//                    newUserData.workshopUserAddress = addressList[0].value
//                    newUserData.showroomUserAddress = addressList[1].value
//                    newUserData.showroomUserAddress = addressList[1].value
//                    newUserData.unionName = unionList[0].value
//                    newUserData.unionWard = unionList[1].value
//                    newUserData.unionLga = unionList[2].value
//                    newUserData.unionState = unionList[3].value
//                    i(title, "new user ${newUserData.workshopUserAddress}")
//                    updateUserData(newUserData)
//                }
//            }
//        })
    }

    private fun setUnionData(user: User?): ArrayList<UserNameClass> {
        val unionList = arrayListOf<UserNameClass>(
            UserNameClass(getString(R.string.name_of_union), user?.unionName.toString()),
            UserNameClass(getString(R.string.ward), user?.unionWard.toString()),
            UserNameClass(getString(R.string.lga), user?.unionLga.toString()),
            UserNameClass(getString(R.string.state), user?.unionState.toString())
        )
        user_profile_name_union_rv.setupAdapter<UserNameClass>(R.layout.user_profile_name_item) { adapter, context, list ->
            bind { itemView, position, item ->


                CoroutineScope(Dispatchers.Main).launch {
                    itemView.user_profile_rv_item_name_title_tv.text = item?.title
                    itemView.user_profile_rv_item_name_value_tv.text = item?.value
                    delay(1000)
                    union_membership.show()
                    union_membership_line.show()
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
                    dialogEditText.setText(item?.value)
                    dialogInput.hint = item?.title
                    dialogTitle.text = item?.title
                    dialogNameAndOtherLayout.show()
                    dialog.show()
                    dialogOkTv.setOnClickListener {
                        item?.value = dialogEditText.text.toString()
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
            if(currentUser?.category == getString(R.string.fashionista)){
                submitList(unionList.apply{
                    clear()
                })
                union_membership.hide()
                union_membership_line.hide()
            }
            else{
                submitList(unionList)
            }

        }
        return unionList
    }

    @SuppressLint("SetTextI18n")
    private fun setUserAddress(user: User?): ArrayList<UserAddressClass> {
        val workshopStreet = user?.workshopUserAddress?.street
        val workshopCity = user?.workshopUserAddress?.city
        val workshopState = user?.workshopUserAddress?.state
        val workshopAddress = UserAddress(workshopStreet, workshopCity, workshopState)
        val showroomStreet = user?.showroomUserAddress?.street
        val showroomCity = user?.showroomUserAddress?.city
        val showroomState = user?.showroomUserAddress?.state
        val showroomAddress = UserAddress(showroomStreet, showroomCity, showroomState)
        val deliveryStreet = user?.deliveryAddress?.street
        val deliveryCity = user?.deliveryAddress?.city
        val deliveryState = user?.deliveryAddress?.state
        val deliveryAddress = UserAddress(deliveryStreet, deliveryCity, deliveryState)

        val addressList = arrayListOf<UserAddressClass>(
            UserAddressClass(
                getString(R.string.delivery_address_str),
                deliveryAddress
            ),
            UserAddressClass(
                getString(R.string.workshop_address),
                workshopAddress
            ),
            UserAddressClass(getString(R.string.showroom_address), showroomAddress)
        )

        user_profile_address_rv.setupAdapter<UserAddressClass>(R.layout.user_profile_address_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Dispatchers.Main).launch {
                    itemView.user_profile_rv_item_address_title_tv.text = item?.title
                    itemView.user_profile_rv_item_address_value_tv.text = "${item?.value?.street}, ${item?.value?.city}, ${item?.value?.state}"
                    delay(1000)
                    itemView.user_profile_address_shimmerLayout.stopShimmer()
                    itemView.user_profile_address_shimmerLayout.setShimmer(null)
                    itemView.user_profile_address_item_container.background = null
                }

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
                    dialogOkTv.setOnClickListener {
                        val street = dialogStreetEditText.text.toString()
                        val city = dialogCityEditText.text.toString()
                        val state = dialogStateEditText.text.toString()
                        item?.value = UserAddress(street, city, state)
                        adapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }
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
            if(currentUser?.category == getString(R.string.fashionista)){
                submitList(addressList.take(1))
            }
            else{
                submitList(addressList.takeLast(2))
            }

        }
        return addressList
    }

    private fun setUserNamesAndOtherFields(
        user: User?,
        newUserData: User
    ): ArrayList<UserNameClass> {
        //List of fields to be filled with names and others
        val nameList = arrayListOf<UserNameClass>(
            UserNameClass(getString(R.string.first_name), user?.firstName.toString()),
            UserNameClass(getString(R.string.last_name), user?.lastName.toString()),
            UserNameClass(getString(R.string.other_name), user?.otherName.toString()),
            UserNameClass(getString(R.string.gender), user?.gender.toString()),
            UserNameClass(getString(R.string.no_of_emplyee), user?.noOfEmployees.toString()),
            UserNameClass(getString(R.string.legal_status), user?.legalStatus.toString())
        )
        //Recycler view to display names and others
        user_profile_name_rv.setupAdapter<UserNameClass>(R.layout.user_profile_name_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Dispatchers.Main).launch {
                    itemView.user_profile_rv_item_name_title_tv.text = item?.title
                    itemView.user_profile_rv_item_name_value_tv.text = item?.value
                    delay(1000)
                    itemView.user_profile_name_shimmerLayout.stopShimmer()
                    itemView.user_profile_name_shimmerLayout.setShimmer(null)
                    itemView.user_profile_name_item_container.background = null
                }
                dialogCancelTv.setOnClickListener {
                    dialog.dismiss()
                }
                //If gender is clicked
                if (item?.title == getString(R.string.gender)) {
                    itemView.user_profile_rv_item_name_value_tv.setOnClickListener {
                        //Provide gender layout
                        val genderRadioGroup =
                            dialog.findViewById<RadioGroup>(R.id.multipurpose_gender_dialog_gender_rg)
                        val femaleRadioBtn =
                            dialog.findViewById<RadioButton>(R.id.multipurpose_gender_dialog_female_rbtn)
                        val maleRadioBtn =
                            dialog.findViewById<RadioButton>(R.id.multipurpose_gender_dialog_male_rbtn)
                        val dialogTitle =
                            dialog.findViewById<TextView>(R.id.multipurpose_title_name_tv)
                        dialogTitle.text = item.title
                        //When
                        when (item.value) {
                            //female radio is ticked
                            getString(R.string.female) -> {
                                //Display female radio ticked
                                femaleRadioBtn.isChecked = true

                            }
                            //male radio is ticked
                            getString(R.string.male) -> {
                                //Display female radio ticked
                                maleRadioBtn.isChecked = true
                            }
                            else -> {
                                maleRadioBtn.isChecked = false
                                femaleRadioBtn.isChecked = false
                            }

                        }

                        genderLayout.show()
                        dialog.show()

                        dialogOkTv.setOnClickListener {
                            //When
                            when (genderRadioGroup.checkedRadioButtonId) {
                                //female radio is ticked
                                R.id.multipurpose_gender_dialog_female_rbtn -> {
                                    itemView.user_profile_rv_item_name_value_tv.text =
                                        getString(R.string.female)
                                    //Update new user data
                                    newUserData.gender = femaleRadioBtn.text.toString()
                                }
                                //male radio is ticked
                                R.id.multipurpose_gender_dialog_male_rbtn -> {
                                    itemView.user_profile_rv_item_name_value_tv.text =
                                        getString(R.string.male)
                                    //Update new user data
                                    newUserData.gender = maleRadioBtn.text.toString()
                                }
                            }
                            dialog.dismiss()
                        }
                        dialog.setOnDismissListener {
                            genderLayout.hide()
                        }
                    }
                } else {

                    //If not gender value clicked
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
                            //Update itemin the list
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
            setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )
            if(currentUser?.category == getString(R.string.fashionista)){
                submitList(nameList.subList(0, 4))
            }
            else{
                submitList(nameList)
            }

        }
        return nameList
    }

    private fun updateUserData(user: User){
        val request = authViewModel.updateUserData(user)
        observeRequest<User>(request, progressBar, saveBtn, false, {response->
            val msg = response?.message
            val profileData = response?.data
            authViewModel.profileData = profileData
            requireActivity().gdToast(msg.toString(), Gravity.BOTTOM)
            i(title, "msg $msg ${profileData?.firstName}")
        },{err ->
            i(title, "UserAccountError $err")
        })
//        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            val (bool, result) = it
//            onRequestResponseTask<User>(bool, result){
//                val response = result as? UserResponse<User>
//                val msg = response?.message
//                val profileData = response?.data
//                authViewModel.profileData = profileData
//                requireActivity().gdToast(msg.toString(), Gravity.BOTTOM)
//                i(title, "msg $msg ${profileData?.firstName}")
//            }
//        })
    }

    private fun getPhotoData() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val chooser = Intent.createChooser(galleryIntent, "Photo options")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent))
        val result = observer.launchImageIntent(chooser)
        result.observe(viewLifecycleOwner, Observer { results ->
            if (results.resultCode == Activity.RESULT_OK) {
                val data = results.data
                val imageBitmap =
                    data?.data?.let { uriToBitmap(it) } ?: data?.extras?.get("data") as Bitmap
                val file = saveBitmap(imageBitmap)
                if (file != null) {
//                    Picasso.get().load(file).into(user_account_profile_image)
                    user_account_profile_image.load(file) {
                        crossfade(true)
                        placeholder(R.drawable.profile_image)
                        transformations(CircleCropTransformation())
                    }
                    val reqBody = file.asRequestBody("image".toMediaTypeOrNull())
                    val profileImagePart = MultipartBody.Part.createFormData(
                        "avatar",
                        file.name, reqBody
                    )
                    i(title, "Imagefile $file name ${file.name} path ${file.absolutePath}")
                    val requestBody: RequestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("avatar", file.toString())
                        .build()

                    val req = authViewModel.uploadProfilePicture(requestBody)
                    observeRequest<User>(req, null, null, true, {result->
                        val responseData = result.data
                        i(title, "URL ${responseData?.avatar}")
                    }, { err ->
                        i(title, "ResetPasswordError $err")
                    })

                    i(title, "File $file")
//                    requireActivity().gdToast("Picture opened", Gravity.BOTTOM)
                }

            } else {
                i(
                    title,
                    "OKCODE ${Activity.RESULT_OK} RESULTCODE ${results.resultCode}"
                )
            }
        })

    }
}

data class UserNameClass(var title:String, var value:String?)
data class UserAddressClass(var title:String, var value:UserAddress)


