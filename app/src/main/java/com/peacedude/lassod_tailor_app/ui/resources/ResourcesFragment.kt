package com.peacedude.lassod_tailor_app.ui.resources

import android.net.Uri
import android.os.Bundle
import android.system.Os.bind
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.resource_video_item.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import kotlinx.android.synthetic.main.resources_item_sublayout_layout.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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


        lifecycleScope.launchWhenStarted {
            authViewModel.videosStateflow.collect()

        }
        observeGetVideosRequest()

        lifecycleScope.launchWhenStarted {
            authViewModel.articlesStateFlow.collect()

        }
        observeGetArticlesRequest()


        resource_fragment_view_all_video_tv.setOnClickListener {
            goto(R.id.allVideoFragment)
        }
        resource_fragment_view_all_publication_tv.setOnClickListener { goto(R.id.allArticlesFragment) }

    }


    override fun onResume() {
        super.onResume()


    }

    private  fun observeGetArticlesRequest() {

//        observeRequest<ArticleList>(authViewModel.articles, null, null, true, {
//            val data = it?.data?.article
//            i(title, "Photo flow $data")
//            if (data?.isNotEmpty() == true) {
//                setUpRecyclerViewForArticles(data)
//            }
//            else{
//                resources_fragment_article_recycler_vf.displayedChild = 1
//            }
//
//        }, { err ->
//                i(
//                    title,
//                    "ArticlesList $err"
//                )
//                if (err != null) {
//                    requireActivity().gdToast(
//                        err,
//                        Gravity.BOTTOM
//                    )
//                }
//        })
        observeResponseState<ArticleList>(
            authViewModel.articleState,
            null,
            null,
            {
                val data = it?.data?.article
                i(title, "ArticlesList flow $data")
                if (data?.isNotEmpty() == true) {
                    val newList = data.takeIf { it?.size!! > 5 }.let { it?.take(5) }
                    val lm =   LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    setUpRecyclerViewForArticles(resource_fragment_article_publications_rv, newList, lm)

                } else {
                    resources_fragment_article_recycler_vf.displayedChild = 1
                }
            },
            { err ->
                i(
                    title,
                    "ArticlesList $err"
                )
                if (err != null) {
                    requireActivity().gdToast(
                        err,
                        Gravity.BOTTOM
                    )
                }

            })
    }




    private fun observeGetVideosRequest() {
        observeResponseState<VideoList>(
            authViewModel.videoState,
            null,
            null,
            {
                val data = it?.data?.video
                i(title, "VideoList flow $data")
                if (data?.isNotEmpty() == true) {
                    val newList = data.takeIf { it?.size!! > 5 }.let { it?.take(5) }
                    val lm =   LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    setUpRecyclerViewForVideo(resource_fragment_video_rv, newList, lm)
                } else {
                    resources_fragment_video_recycler_vf.displayedChild = 1
                }
            },
            { err ->
                i(
                    title,
                    "VideoList $err"
                )
                if (err != null) {
                    requireActivity().gdToast(
                        err,
                        Gravity.BOTTOM
                    )
                }

            })
    }



}

fun Fragment.setUpRecyclerViewForArticles(rcv:RecyclerView, listOfArticles: List<Article>?, layoutManager:RecyclerView.LayoutManager) {

    rcv.setupAdapter<Article>(
        R.layout.article_publication_item_layout
    ) { adapter, _, list ->
        bind { itemView, position, item ->

            if (item?.articleImage != null) {
                Picasso.get().load(item.articleImage).fit()
                    .into(itemView.resource_article_publications_iv)
                itemView.resource_article_publications_iv.load(item.articleImage) {
                    crossfade(true)
                    placeholder(R.drawable.add)
                    transformations(RoundedCornersTransformation(15F, 0F, 0F, 15F))
                }
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

            itemView.resource_article_publications_iv.clipToOutline = true
            itemView.setOnClickListener {
                GlobalVariables.globalArticle = item
                goto(R.id.singleArticleFragment)
            }

        }
        setLayoutManager(
            layoutManager
        )
        submitList(listOfArticles)
    }
}

fun Fragment.setUpRecyclerViewForVideo(rcv:RecyclerView, listOfVideos: List<VideoResource>?, layoutManager:RecyclerView.LayoutManager) {

    rcv.setupAdapter<VideoResource>(R.layout.resource_video_item) { _, _, _ ->
        bind { itemView, _, item ->
            lifecycle.addObserver(itemView.resource_video_item_ytpv)
            val mediaController = MediaController(requireContext())
            mediaController.hide()
            mediaController.setAnchorView(itemView.resource_video_item_vv)

            itemView.resource_video_item_title_tv.text = item?.title
            itemView.resource_video_item_time_tv.text = item?.duration
            if (item?.videoURL != null && item.videoURL.endsWith(".mp4")) {
                itemView.resource_video_item_vv.show()
                itemView.resource_video_item_ytpv.hide()
                val uri = Uri.parse(item.videoURL)
                itemView.resource_video_item_vv.setMediaController(
                    mediaController
                )
                itemView.resource_video_item_vv.setVideoURI(uri)
                itemView.resource_video_item_fl.clipToOutline = true
                itemView.resource_video_item_vv.seekTo(1)
            } else {
                itemView.resource_video_item_vv.hide()
                itemView.resource_video_item_ytpv.show()
                itemView.resource_video_item_ytpv.clipToOutline = true
                val ext = item?.videoURL?.split("=")?.get(1).toString()
                itemView.resource_video_item_ytpv.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                        youTubePlayer.loadVideo(ext, 0F)
                        youTubePlayer.pause()
                    }

                })

            }


            rcv.addOnScrollListener(object :
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
            itemView.setOnClickListener {
                GlobalVariables.globalVideo = item
                goto(R.id.singleVideoFragment)
            }
//            itemView.setOnClickListener {
//                actionToDisplaySingleVideo(item)
//            }

//            resources_fragment_nsv.viewTreeObserver.addOnScrollChangedListener {
//                mediaController.hide()
//            }
        }
        setLayoutManager(
          layoutManager
        )
        submitList(listOfVideos)
    }

}
private fun Fragment.actionToDisplaySingleVideo(item: VideoResource?) {
    val action = AllVideoFragmentDirections.actionAllVideoFragmentToSingleVideoFragment()
    action.video = item
    goto(action)
}

data class ResourceMediaRecyclerItem(
    var title: String,
    var mediaList: List<CommonMediaClass>?,
    val noDataImage: Int,
    val noDataText: String,
    var footer: String
)



