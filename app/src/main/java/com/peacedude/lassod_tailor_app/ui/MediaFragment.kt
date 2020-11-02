package com.peacedude.lassod_tailor_app.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.skydoves.progressview.ProgressView
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val REQUEST_CODE = 1
const val GALLERY_PHOTO_CODE = 0

/**
 * A simple [Fragment] subclass.
 * Use the [MediaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }
    private val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.add_photo_dialog_layout)
        }
    }
    lateinit var currentPhotoPath: String
    private val addPhotoDialogFab by lazy {
        (dialog.findViewById(R.id.add_photo_dialog_fab) as FloatingActionButton)
    }
    private val addPhotoLoaderLayout by lazy {
        (dialog.findViewById(R.id.add_photo_loading_ll) as LinearLayout)
    }

    private val progressFill by lazy {
        (dialog.findViewById(R.id.add_photo_fill_pl) as ProgressView)
    }
    private val addPhotoCancelIcon by lazy {
        (dialog.findViewById(R.id.add_photo_dialog_cancel_iv) as ImageView)
    }

    private val noDataFirstIcon by lazy {
        no_data_included_layout.findViewById<ImageView>(R.id.no_data_first_icon_iv)
    }
    private val noDataSecondIcon by lazy {
        no_data_included_layout.findViewById<ImageView>(R.id.no_data_second_icon_iv)
    }
    private val noDataText by lazy {
        no_data_included_layout.findViewById<TextView>(R.id.no_data_text_tv)
    }

    val header by lazy {
        authViewModel.header
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    lateinit var observer: StartActivityForResults
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        observer = StartActivityForResults(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer as StartActivityForResults)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noDataFirstIcon.hide()
        noDataSecondIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.media_icon
            )
        )
        noDataText.text = getString(R.string.you_have_no_photo_str)

        add_photo_iv.setOnClickListener {
            dialog.show {
                cornerRadius(15F)
            }
        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CODE
        )

        addPhotoDialogFab.setOnClickListener {
            val res = checkCameraPermission()
            if (res) {
                getPhotoData()
            } else {
                i(title, getString(R.string.no_permission))
                requireActivity().gdToast(getString(R.string.no_permission), Gravity.BOTTOM)
            }


        }
    }

    private fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            i(title, "first if here we aee")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.CAMERA
                )
            ) {
                i(title, "second if here we are")
                val alertBuilder = AlertDialog.Builder(requireActivity())
                alertBuilder.setTitle(R.string.allow_camera)
                alertBuilder.setMessage(R.string.camera_str)
                alertBuilder.setPositiveButton(getString(R.string.ok_str)) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.CAMERA),
                        REQUEST_CODE
                    )
                    return@setPositiveButton
                }
                alertBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                    requireActivity().gdToast(
                        "Permission is needed for photo upload",
                        Gravity.BOTTOM
                    )
                    val settingIntent = Intent()
                    settingIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    settingIntent.data = uri
                    startActivity(settingIntent)
                    return@setNegativeButton
                }
                val alertDialog = alertBuilder.create()
                alertDialog.setOnShowListener {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                }
                alertDialog.show()
            } else {
                i(title, "second else Here we are")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CODE
                )
                return true
            }
        } else {
            i(title, "first else Here we aee")
            return true
        }
        return false
    }

    private fun getPhotoData() {
        addPhotoLoaderLayout.hide()
        progressFill.progress = 0.0F
        addPhotoCancelIcon.hide()
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
                val imageFile = saveBitmap(imageBitmap)
                if (imageFile != null) {
                    Picasso.get().load(imageFile).into(noDataSecondIcon)
                }
                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("photo", imageFile.toString())
                    .build()
//                val requestBody = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
//                val filePart: MultipartBody.Part? = requestBody.let {
//                    MultipartBody.Part.createFormData(
//                        "photo",
//                        imageFile?.name,
//                        it
//                    )
//                }
                val req = authViewModel.addPhoto(header, requestBody)
                requestObserver(null, null, req) { bool, result ->
                    onRequestResponseTask<User>(bool, result) {
                        val response = result as UserResponse<NothingExpected>
//                        val responseData = response.data
                        i(title, "URL ${response.message}")
                    }
                }
                i(title, "File $imageFile")
                dialog.dismiss()
                requireActivity().gdToast("Picture opened", Gravity.BOTTOM)
            } else {
                i(
                    title,
                    "OKCODE ${Activity.RESULT_OK} RESULTCODE ${results.resultCode}"
                )
            }
        })


        addPhotoCancelIcon.setOnClickListener {
            addPhotoLoaderLayout.hide()
            addPhotoDialogFab.show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requireActivity().gdToast(
                    getString(R.string.permission_granted_str),
                    Gravity.BOTTOM
                )
            } else {
                requireActivity().gdToast(
                    getString(R.string.permission_not_granted_str),
                    Gravity.BOTTOM
                )
            }
        } else {
            requireActivity().gdToast("error code", Gravity.BOTTOM)
        }
    }

    private fun saveBitmap(bmp: Bitmap): File? {
        val extStorageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var outStream: OutputStream? = null
        var file:File?=null
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val child = "JPEG_${timeStamp}_.jpg"
        // String temp = null;
        if (extStorageDirectory != null){
            file = File(extStorageDirectory, child)
            if (file.exists()) {
                file.delete()
                file = File(extStorageDirectory, child)
            }
            try {
                outStream = FileOutputStream(file)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.flush()
                outStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        return file
    }

    private fun uriToBitmap(uriImage: Uri): Bitmap? {
        var mBitmap: Bitmap? = null
        if(Build.VERSION.SDK_INT < 28) {
            mBitmap = MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                uriImage
            )
        } else {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uriImage)
            mBitmap = ImageDecoder.decodeBitmap(source)
        }
        return mBitmap
    }
    override fun onPause() {
        super.onPause()
        i(title, "Onpause")
    }

}