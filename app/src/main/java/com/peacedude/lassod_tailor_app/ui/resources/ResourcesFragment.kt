package com.peacedude.lassod_tailor_app.ui.resources

import android.R.attr.radius
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.annotations.SerializedName
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.ResourcesArticlePublication
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import kotlinx.android.synthetic.main.resources_item_sublayout_layout.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.io.Serializable
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

    private val noVideoDataFirstIcon by lazy {
        resource_fragment_video_no_data_included_layout.findViewById<ImageView>(R.id.no_data_first_icon_iv)
    }
    private val noVideoDataSecondIcon by lazy {
        resource_fragment_video_no_data_included_layout.findViewById<ImageView>(R.id.no_data_second_icon_iv)
    }
    private val noVideoDataText by lazy {
        resource_fragment_video_no_data_included_layout.findViewById<TextView>(R.id.no_data_text_tv)
    }
    private val noArticleDataFirstIcon by lazy {
        resources_fragment_article_no_data_included_layout.findViewById<ImageView>(R.id.no_data_first_icon_iv)
    }
    private val noArticleDataSecondIcon by lazy {
        resources_fragment_article_no_data_included_layout.findViewById<ImageView>(R.id.no_data_second_icon_iv)
    }
    private val noArticleDataText by lazy {
        resources_fragment_article_no_data_included_layout.findViewById<TextView>(R.id.no_data_text_tv)
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> {
        viewModelProviderFactory
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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noVideoDataFirstIcon.hide()
        noArticleDataFirstIcon.hide()
        noVideoDataSecondIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_slow_motion_video_24
            )
        )
        noVideoDataText.text = getString(R.string.no_video_available)
        noArticleDataSecondIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_note_add_24
            )
        )
        noArticleDataText.text = getString(R.string.no_article_available)

        CoroutineScope(Main).launch {
            supervisorScope {
                val articleListCall = async { authViewModel.getArticles(header) }
                val videoListCall = async { authViewModel.getVideos(header) }

                    videoListCall.await()
                        .collect {
                            onFlowResponse<VideoList>(response = it) {videos->
                                i(title, "video data flow $it")
                                val listOfVideos = videos?.video?.map {v-> v
                                }?.takeIf {
                                    it.size > 5
                                }.let {
                                    it?.take(5)
                                }
                                if(!listOfVideos?.isNullOrEmpty()!!) {
                                    resource_fragment_video_rv.setupAdapter<VideoResource>(R.layout.resource_video_item) { adapter, context, list ->
                                        bind { itemView, position, item ->
                                            val mediaController = MediaController(requireContext())
                                            mediaController.hide()
                                            mediaController.setAnchorView(itemView.resource_video_item_vv)
                                            itemView.resource_video_item_title_tv.text = item?.title
                                            itemView.resource_video_item_time_tv.text = item?.duration
                                            if (item?.videoURL != null && item.videoURL.endsWith(".mp4")) {
                                                val uri = Uri.parse(item.videoURL)
                                                itemView.resource_video_item_vv.setMediaController(
                                                    mediaController
                                                )
                                                itemView.resource_video_item_vv.setVideoURI(uri)
                                                itemView.resource_video_item_fl.clipToOutline = true
                                                itemView.resource_video_item_vv.seekTo(1)
                                            } else {
                                                val uri =
                                                    Uri.parse(getString(R.string.sample_video_str))
                                                itemView.resource_video_item_vv.setMediaController(
                                                    mediaController
                                                )
                                                itemView.resource_video_item_vv.setVideoURI(uri)
                                                itemView.resource_video_item_fl.clipToOutline = true
                                                itemView.resource_video_item_vv.seekTo(1)
                                            }


                                            resource_fragment_video_rv.addOnScrollListener(object :
                                                RecyclerView.OnScrollListener() {

                                                override fun onScrollStateChanged(
                                                    recyclerView: RecyclerView,
                                                    newState: Int
                                                ) {
                                                    super.onScrollStateChanged(
                                                        recyclerView,
                                                        newState
                                                    )
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
                                        submitList(listOfVideos)
                                    }
                                } else {
                                    resources_fragment_video_recycler_vf.showNext()

                                }

                            }
                        }
            }

        }
        CoroutineScope(Main).launch {

            supervisorScope {
                val articleListCall = async { authViewModel.getArticles(header) }
                articleListCall.await()
                    .catch { err ->
                        i(title, "article data flow error $err")
                    }
                    .collect {
                        onFlowResponse<ArticleList>(response = it) {
                            val listOfArticles = it?.article?.map {a->
                                a
                            }?.takeIf {
                                it.size > 5
                            }.let {
                                it?.take(5)
                            }
                            i(title, "article data flow $it")
                            if (listOfArticles?.isNotEmpty()!!) {
                                resource_fragment_article_publications_rv.setupAdapter<Article>(
                                    R.layout.article_publication_item_layout
                                ) { adapter, context, list ->
                                    bind { itemView, position, item ->

                                        if (item?.articleImage != null) {
                                            Picasso.get().load(item.articleImage).fit()
                                                .into(itemView.resource_article_publications_iv)
                                        } else {
                                            itemView.resource_article_publications_iv.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.obioma_logo_blue
                                                )
                                            )
                                        }
                                        itemView.resource_article_publication_item_title_tv.text =
                                            item?.title
                                        itemView.resource_article_publication_item_author_tv.text =
                                            item?.description
                                        itemView.article_pub_item_image_ll.clipToOutline = true

                                        itemView.resource_article_publications_iv.clipToOutline =
                                            true


                                    }
                                    setLayoutManager(
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                    )
                                    submitList(listOfArticles)
                                }
                            } else {
                                resources_fragment_article_recycler_vf.showNext()

                            }
                        }
                    }
            }
        }

        resource_fragment_view_all_video_tv.setOnClickListener {
            goto(R.id.allVideoFragment)
        }
        resource_fragment_view_all_publication_tv.setOnClickListener { goto(R.id.allArticlesFragment) }

    }




}

data class ResourceMediaRecyclerItem(
    var title: String,
    var mediaList: List<CommonMediaClass>?,
    val noDataImage:Int,
    val noDataText:String,
    var footer: String
)



