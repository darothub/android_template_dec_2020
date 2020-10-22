package com.peacedude.lassod_tailor_app.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
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
    private val addPhotoFab by lazy {
        (dialog.findViewById(R.id.add_photo_dialog_fab) as FloatingActionButton)
    }
    private val addPhotoLoaderLayout by lazy {
        (dialog.findViewById(R.id.add_photo_loading_ll) as LinearLayout)
    }

    lateinit var observer : StartActivityForResults
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

        add_photo_iv.setOnClickListener {
            dialog.show{
                cornerRadius(15F)
            }
        }

        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)

        addPhotoFab.setOnClickListener {
            addPhotoFab.show()
            checkCameraPermission()
        }
    }

    private fun checkCameraPermission() {
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
            }
        } else {
            i(title, "first else Here we aee")
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val chooser = Intent.createChooser(galleryIntent, "Photo options")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent)).also { takePictureIntent->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    i(title, "file $photoFile")
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            getString(R.string.provider_authority_str),
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        val result = observer.launchImageIntent(takePictureIntent, viewLifecycleOwner)
                        result.observe(viewLifecycleOwner, Observer { result->
                            if (result.resultCode == Activity.RESULT_OK) {
                                val data = result.data
                                val imageUri = data?.data ?: getImageUriFromBitmap(
                                    requireContext(),
                                    data?.extras?.get("data") as Bitmap
                                )
                                i(title, "Uri $imageUri")
                                addPhotoLoaderLayout.show()
                                addPhotoFab.hide()
//                    Picasso.get().load(imageUri).into()
                                requireActivity().gdToast("Picture opened", Gravity.BOTTOM)
                            } else {
                                i(title, "OKCODE ${Activity.RESULT_OK} RESULTCODE ${result.resultCode}")
                            }
                        })

//                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }

//            requireActivity().gdToast(getString(R.string.permission_granted_str), Gravity.BOTTOM)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requireActivity().gdToast(getString(R.string.permission_granted_str), Gravity.BOTTOM)
            }
            else{
                requireActivity().gdToast(getString(R.string.permission_not_granted_str), Gravity.BOTTOM)
            }
        }
        else{
            requireActivity().gdToast("error code", Gravity.BOTTOM)
        }
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}