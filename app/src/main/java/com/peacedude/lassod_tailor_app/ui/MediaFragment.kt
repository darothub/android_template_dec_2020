package com.peacedude.lassod_tailor_app.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
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
import com.peacedude.lassod_tailor_app.utils.BASE_URL_STAGING
import com.peacedude.lassod_tailor_app.utils.bearer
import com.peacedude.lassod_tailor_app.utils.http.MultipartUtility
import com.skydoves.progressview.ProgressView
import com.squareup.picasso.Picasso
import com.utsman.recycling.adapter.RecyclingAdapter
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
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

    private val singleSendTv by lazy {
        (singleImageDialog.findViewById(R.id.single_media_send_tv) as TextView)
    }
    private val singleDeleteTv by lazy {
        (singleImageDialog.findViewById(R.id.single_media_delete_tv) as TextView)
    }
    val header by lazy {
        authViewModel.header
    }
    val photoListLiveData: LiveData<PhotoList> by lazy {
        MutableLiveData<PhotoList>()
    }


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
                            }
                            i(title, "${listOfPhoto?.isEmpty()}")
                            if (listOfPhoto?.isEmpty()!!) {


                            } else {

                                media_fragment_rv.setupAdapter<Photo>(R.layout.media_recycler_item) { adapter, context, list ->

                                    bind { itemView, position, item ->
                                        itemView.media_item_picture_title_tv.text =
                                            getString(R.string.elegant_str)
                                        Picasso.get().load(item?.photo)
                                            .into(itemView.media_item_picture_iv)

                                        itemView.setOnClickListener {
                                            GlobalVariables.globalId = item?.id.toString()
                                            GlobalVariables.globalPhoto = item
                                            GlobalVariables.globalPosition = position
                                            i(title, "pos ${GlobalVariables.globalPosition}")
                                            Picasso.get().load(item?.photo).into(singleImageIv)

                                            singleSendTv.setOnClickListener {
                                                shareImage(itemView)

                                            }
                                            singleDeleteTv.setOnClickListener {
                                                deleteMediaRequest(list, item, adapter)
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
                findNavController().navigate(R.id.mediaFragment)

            },
            { err ->
                list?.add(item)
                adapter.notifyDataSetChanged()
                i(title, "DeletePhotoReqError $err")
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
                i(title, "imageFile $imageFile")

                val reqBody = imageFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())


                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("photo", imageFile?.name, reqBody)
                    .build()


                val req = authViewModel.addPhoto(requestBody)
                observeRequest<NothingExpected>(req, null, null, true, { result ->
                    val response = result
                    requireActivity().gdToast(response.message.toString(), Gravity.BOTTOM)
                    findNavController().navigate(R.id.mediaFragment)
                    i(title, "URL ${response.message}")
                }, { err ->
                    i(title, "AddPhotoReqError $err")
                })


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

