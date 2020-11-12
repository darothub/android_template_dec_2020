package com.peacedude.lassod_tailor_app.ui.resources

import android.R.attr.radius
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.ResourcesArticlePublication
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.response.VideoList
import com.peacedude.lassod_tailor_app.model.response.VideoResource
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ResourcesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResourcesFragment : DaggerFragment() {
    val imageLoader by lazy {
        ImageLoader(requireContext())
    }
    private val title by lazy {
        getName()
    }
    val header by lazy {
        authViewModel.header
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val articleResourcesList = arrayListOf<ResourcesArticlePublication>(
            ResourcesArticlePublication(
                getString(R.string.sample_image_str), getString(R.string.sample_str), getString(
                    R.string.by_author_str
                )
            ),
            ResourcesArticlePublication(
                getString(R.string.sample_image_str), getString(R.string.sample_str), getString(
                    R.string.by_author_str
                )
            ),
            ResourcesArticlePublication(
                getString(R.string.sample_image_str), getString(R.string.sample_str), getString(
                    R.string.by_author_str
                )
            ),
            ResourcesArticlePublication(
                getString(R.string.sample_image_str), getString(R.string.sample_str), getString(
                    R.string.by_author_str
                )
            ),
            ResourcesArticlePublication(
                getString(R.string.sample_image_str), getString(R.string.sample_str), getString(
                    R.string.by_author_str
                )
            )
        )


        resource_fragment_article_publications_rv.setupAdapter<ResourcesArticlePublication>(R.layout.article_publication_item_layout) { adapter, context, list ->
            bind { itemView, position, item ->

                itemView.resource_article_publication_item_title_tv.text = item?.articleTitle
                itemView.resource_article_publication_item_author_tv.text = item?.articleAuthor
                itemView.article_pub_item_image_ll.clipToOutline = true
                Picasso.get().load(item?.articleImageUri).fit().into(itemView.resource_article_publications_iv)
                itemView.resource_article_publications_iv.clipToOutline = true

            }
            setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            submitList(articleResourcesList)
        }

        val request = authViewModel.getAllVideos(header)

        //Observer for get request
        requestObserver(null, null, request, true) { bool, result ->
            //Task to be done on successful
            onRequestResponseTask<VideoList>(bool, result) {
                val results = it
                i("ResourceScreen", "sizeResource ${it?.data?.video?.size}")
                var listOfVideo:List<VideoResource>? = null

                if (results?.data?.video?.size == 0){
                    listOfVideo = arrayListOf<VideoResource>(
                        VideoResource(
                            "0",
                            "0",
                            "Hello World",
                            "video.videoURL",
                            "video.description",
                            "video.createdAt",
                            "video.updatedAt",
                            R.drawable.ic_baseline_slow_motion_video_24
                        ),
                        VideoResource(
                            "0",
                            "0",
                            "Hello World",
                            "",
                            "video.description",
                            "video.createdAt",
                            "video.updatedAt",
                            R.drawable.ic_baseline_slow_motion_video_24
                        ),
                        VideoResource(
                            "0",
                            "0",
                            "Hello World",
                            "video.videoURL",
                            "video.description",
                            "video.createdAt",
                            "video.updatedAt",
                            R.drawable.ic_baseline_slow_motion_video_24
                        )

                    )
                }
                else{
                    listOfVideo = results?.data?.video?.map { video ->
                        VideoResource(
                            video.id,
                            video.tailorID,
                            video.title,
                            video.videoURL,
                            video.description,
                            video.createdAt,
                            video.updatedAt
                        )

                    }?.subList(0, 12)!!
                }

                i("ResourceScreen2", "sizeResource2 ${listOfVideo.size}")
                resource_fragment_video_rv.setupAdapter<VideoResource>(R.layout.resource_video_item) { adapter, context, list ->
                    bind { itemView, position, item ->
                        val mediaController = MediaController(requireContext())
                        mediaController.hide()
                        if (list?.size == 3){
                            itemView.resource_video_item_vv.hide()
                            itemView.resource_video_item_iv.show()
                            itemView.resource_video_item_iv.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    item?.placeHolder!!
                                )
                            )

                        }else{

                            mediaController.setAnchorView(itemView.resource_video_item_vv)
                            val uri = Uri.parse(item?.videoURL)
                            itemView.resource_video_item_vv.show()
                            itemView.resource_video_item_iv.hide()
                            itemView.resource_video_item_vv.setMediaController(mediaController)
                            itemView.resource_video_item_vv.setVideoURI(uri)
                            itemView.resource_video_item_fl.clipToOutline = true
                            itemView.resource_video_item_vv.seekTo(1)
                            itemView.resource_video_item_title_tv.text = item?.title
                            itemView.resource_video_item_time_tv.text = item?.title
                        }

                        itemView.setOnClickListener {
                            mediaController.show()
                        }
                        resource_fragment_video_rv.addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {

                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                mediaController.hide()
                            }

                        })

                        resources_fragment_nsv.viewTreeObserver.addOnScrollChangedListener {
                            mediaController.hide()
                        }
                    }
                    setLayoutManager(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    )
                    submitList(listOfVideo)
                }
//                if (listOfVideo?.isNotEmpty()!!) {
////                    all_video_fragment_no_data_included_layout.hide()
////                    all_video_fragment_rv.show()
//
//
//
//
//
//                } else {
//
//                }
            }
        }

        resource_fragment_view_all_video_tv.setOnClickListener {
            goto(R.id.allVideoFragment)
        }
        resource_fragment_view_all_publication_tv.setOnClickListener {
            goto(R.id.allArticlesFragment)
        }
    }
}


