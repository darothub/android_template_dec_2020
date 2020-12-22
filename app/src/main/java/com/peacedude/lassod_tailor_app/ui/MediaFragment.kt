package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.Photo
import com.peacedude.lassod_tailor_app.model.response.PhotoList
import com.skydoves.progressview.ProgressView
import com.squareup.picasso.Picasso
import com.utsman.recycling.adapter.RecyclingAdapter
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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
    private val singleImageDialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.single_image_dialog)
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
    private val singleImageIv by lazy {
        (singleImageDialog.findViewById(R.id.single_media_photo_iv) as ImageView)
    }

    private val singleSendView by lazy {
        (singleImageDialog.findViewById(R.id.single_media_share_btn) as View)
    }
    private val singleEditView by lazy {
        (singleImageDialog.findViewById(R.id.single_media_edit_btn) as View)
    }
    private val singleDeleteView by lazy {
        (singleImageDialog.findViewById(R.id.single_media_delete_btn) as View)
    }
    private val singleUpdateView by lazy {
        (singleImageDialog.findViewById(R.id.single_media_update_btn) as View)
    }
    private val singleUpdateDialogEt by lazy {
        (singleImageDialog.findViewById(R.id.single_media_dialog_et) as TextInputEditText)
    }
    private val singleMediaDialogFlipper by lazy {
        (singleImageDialog.findViewById(R.id.single_media_dialog_vf) as ViewFlipper)
    }

    val header by lazy {
        authViewModel.header
    }
    val photoListLiveData: LiveData<PhotoList> by lazy {
        MutableLiveData<PhotoList>()
    }

    lateinit var singleDeleteBtn: Button
    lateinit var singleSendBtn: Button
    lateinit var singleEditBtn: Button
    lateinit var singleUpdateBtn: Button

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> {
        viewModelProviderFactory
    }

    lateinit var observer: StartActivityForResults
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        observer = StartActivityForResults(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonTransactions(
            {
                singleDeleteBtn = singleDeleteView.findViewById(R.id.btn)
                singleSendBtn = singleSendView.findViewById(R.id.btn)
                singleEditBtn = singleEditView.findViewById(R.id.btn)
                singleUpdateBtn = singleUpdateView.findViewById(R.id.btn)
                singleDeleteBtn.apply {
                    background?.colorFilter = PorterDuffColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.colorRed),
                        PorterDuff.Mode.SRC_IN
                    )
                    text = getString(R.string.delete)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                }
                singleEditBtn.apply {
                    background?.colorFilter = PorterDuffColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.colorGrayDark),
                        PorterDuff.Mode.SRC_IN
                    )
                    text = getString(R.string.edit)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                }
                singleSendBtn.apply {
                    background?.colorFilter = PorterDuffColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                        PorterDuff.Mode.SRC_IN
                    )
                    text = getString(R.string.share)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                }
                singleUpdateBtn.apply {
                    background?.colorFilter = PorterDuffColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                        PorterDuff.Mode.SRC_IN
                    )
                    text = getString(R.string.update_str)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                }
            }, {

            }
        )

        noDataFirstIcon.hide()
        noDataSecondIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.media_icon
            )
        )
        noDataText.text = getString(R.string.you_have_no_photo_str)



        mediaTransaction()



        add_photo_iv.setOnClickListener {
            dialog.show {
                cornerRadius(15F)
            }
        }


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

    @ExperimentalCoroutinesApi
    private fun mediaTransaction() {

        CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                val getAllPhotos = async {
                    authViewModel.getAllPhoto()
                }

                getAllPhotos.await()
                    .catch {
                        i(title, "Error All Photo on flow ${it.message}")
                    }
                    .collect {
                        onFlowResponse<PhotoList>(response = it) { it ->
                            val listOfPhoto = it?.photo?.map { photo ->
                                photo
                            }?.reversed()
                            i(title, "${listOfPhoto?.isEmpty()}")
                            if (listOfPhoto?.isEmpty()!! || media_fragment_rv == null) {


                            } else {

                                media_fragment_rv?.setupAdapter<Photo>(R.layout.media_recycler_item) { adapter, context, list ->

                                    bind { itemView, position, item ->
                                        itemView.media_item_picture_title_tv.text = item?.info

                                        Picasso.get().load(item?.photo)
                                            .into(itemView.media_item_picture_iv)

                                        itemView.setOnClickListener {
                                            singleMediaDialogFlipper.showPrevious()
                                            GlobalVariables.globalId = item?.id.toString()
                                            GlobalVariables.globalPhoto = item
                                            GlobalVariables.globalPosition = position
                                            i(title, "pos ${GlobalVariables.globalPosition}")
                                            Picasso.get().load(item?.photo).into(singleImageIv)

                                            singleSendBtn.setOnClickListener {
                                                shareImage(itemView)
                                            }
                                            singleDeleteBtn.setOnClickListener {
                                                deleteMediaRequest(list, item, adapter)
                                            }

                                            singleEditBtn.setOnClickListener {
                                                singleMediaDialogFlipper.showNext()
                                                singleUpdateDialogEt.setText(item?.info)
                                                singleUpdateBtn.setOnClickListener {
                                                    val newInfo =
                                                        singleUpdateDialogEt.text.toString().trim()
                                                    val checkEmpty =
                                                        IsEmptyCheck(singleUpdateDialogEt)
                                                    when {
                                                        checkEmpty != null -> {
                                                            requireActivity().gdToast(
                                                                "${singleUpdateDialogEt.tag} cannot be empty",
                                                                Gravity.BOTTOM
                                                            )
                                                        }
                                                        else -> {
                                                            singleMediaDialogFlipper.showPrevious()
                                                            val updateReq =
                                                                authViewModel.editPhotoInfo(
                                                                    item?.id.toString(),
                                                                    newInfo
                                                                )
                                                            //Observer for get request
                                                            observeRequest<NothingExpected>(
                                                                updateReq,
                                                                null,
                                                                null,
                                                                true,
                                                                {
                                                                    val res = it
                                                                    item?.info = newInfo
                                                                    adapter.notifyDataSetChanged()

                                                                    i(title, "$res list $list")
                                                                    requireActivity().gdToast(
                                                                        "${res.message}",
                                                                        Gravity.BOTTOM
                                                                    )
                                                                    singleImageDialog.dismiss()

                                                                },
                                                                { err ->
                                                                    i(
                                                                        title,
                                                                        "DeletePhotoReqError $err"
                                                                    )
                                                                    requireActivity().gdToast(
                                                                        err,
                                                                        Gravity.BOTTOM
                                                                    )
                                                                    item?.info = newInfo
                                                                    adapter.notifyDataSetChanged()
                                                                    singleImageDialog.dismiss()
                                                                })
                                                        }
                                                    }
                                                }
                                            }

                                            singleImageDialog.setOnDismissListener {
                                                singleMediaDialogFlipper.showPrevious()
                                            }

                                            singleImageDialog.show {
                                                cornerRadius(10F)
                                            }
                                        }


                                    }
                                    setLayoutManager(GridLayoutManager(requireContext(), 2))
                                    submitList(listOfPhoto)
                                    media_fragment_vf.showNext()
                                }

                            }

                        }
                    }
            }
        }

    }

    private fun shareImage(itemView: View) {
        val b =
            getBitmapFromImageView(itemView.media_item_picture_iv)
        val file = saveBitmap(b)
        val mimeType = "image/*"
        if (file != null) {
            val uri =
                FileProvider.getUriForFile(
                    requireActivity(),
                    "com.peacedude.lassod_tailor_app.fileprovider",
                    file
                )
            val share = ShareCompat.IntentBuilder
                .from(requireActivity())
                .setType(mimeType)
                .setChooserTitle("Share with ")
                .setStream(uri)
                .startChooser()
        } else {
            requireActivity().gdErrorToast("Invalid file", Gravity.BOTTOM)
        }
    }

    private fun deleteMediaRequest(
        list: MutableList<Photo?>?,
        item: Photo?,
        adapter: RecyclingAdapter<Photo>
    ) {
        singleImageDialog.dismiss()
        list?.remove(item)
        adapter.notifyDataSetChanged()
        if (list?.isEmpty()!!) {
            media_fragment_vf.showPrevious()
        }
        val deleteReq =
            authViewModel.deleteMedia(header, item?.id)
        //Observer for get request
        observeRequest<NothingExpected>(
            deleteReq,
            null,
            null,
            true,
            {
                val res = it
                i(title, "$res list $list")
                requireActivity().gdToast(
                    "${res.message}",
                    Gravity.BOTTOM
                )
//                findNavController().navigate(R.id.mediaFragment)

            },
            { err ->
                list?.add(item)
                adapter.notifyDataSetChanged()
                i(title, "DeletePhotoReqError $err")
                requireActivity().gdToast(err, Gravity.BOTTOM)
            })
    }


    @ExperimentalCoroutinesApi
    private fun getPhotoData() {
        addPhotoLoaderLayout.hide()
        progressFill.progress = 0.0F
        addPhotoCancelIcon.hide()
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val chooser = Intent.createChooser(galleryIntent, "Photo options")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent))
        val result = observer.launchImageIntent(chooser)
        result.observe(viewLifecycleOwner, Observer { results ->
            if (results.resultCode == Activity.RESULT_OK) {
                var imageBitmap: Bitmap? = null
                var arrayOfImageFile = listOf<File?>()
                var imageFile: File?
                var reqBody: RequestBody?
                var itemUri: Uri?
                val requestBodyBuilder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                val imageParts: ArrayList<MultipartBody.Part?> = arrayListOf()

                val data = results.data
                i(title, "Result $result")

                i(title, "ImageData $data")
                if (data != null) {
                    val clipData = data.clipData
                    when (clipData?.itemCount) {
                        null -> {
                            imageBitmap = data.data?.let { uriToBitmap(it) }
                                ?: data.extras?.get("data") as Bitmap?
                            imageFile = saveBitmap(imageBitmap)
                            reqBody = imageFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                            requestBodyBuilder.addFormDataPart("photo", imageFile.name, reqBody)
                            imageParts.add(
                                MultipartBody.Part.createFormData(
                                    "photo",
                                    imageFile.name,
                                    reqBody
                                )
                            )
                        }
                        in 1..5 -> {

                            arrayOfImageFile = (0 until clipData.itemCount).map {
                                val itemUriTwo = data.clipData!!.getItemAt(it).uri
                                val imageBitmapTwo = uriToBitmap(itemUriTwo)
                                val imageFileTwo = saveBitmap(imageBitmapTwo)
                                imageFileTwo
                            }



                            i(title, "imageFiles $arrayOfImageFile")
                        }
                        else -> {
                            i(title, "Too many images selected")
                        }

                    }
                    if (arrayOfImageFile.isNotEmpty()) {
                        arrayOfImageFile.forEach { imageFile ->
                            reqBody = imageFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            imageParts.add(
                                MultipartBody.Part.createFormData(
                                    "photo", imageFile.name,
                                    reqBody!!
                                )
                            )
                        }
                    }
                    i(title, "imagePart $imageParts")

//                    val requestBody: RequestBody = requestBodyBuilder.build()
                    val req = authViewModel.addPhoto(imageParts as? List<MultipartBody.Part>)
                    observeRequest<NothingExpected>(req, null, null, true, { result ->
                        val response = result
                        requireActivity().gdToast(response.message.toString(), Gravity.BOTTOM)
                        findNavController().navigate(R.id.mediaFragment)
                        i(title, "URL ${response.message}")
                    }, { err ->
                        requireActivity().gdToast(err, Gravity.BOTTOM)
                        i(title, "AddPhotoReqError $err")
                    })

                }



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


}

