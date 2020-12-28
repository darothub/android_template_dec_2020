package com.peacedude.lassod_tailor_app.ui.resources

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.response.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.fragment_single_video.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [AllVideo.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllVideoFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }
    val header by lazy {
        authViewModel.header
    }

    private val toolbar by lazy {
        (all_video_fragment_tb.findViewById(R.id.reusable_appbar_toolbar) as Toolbar)
    }
    private val noDataFirstIcon by lazy {
        all_video_fragment_no_data_included_layout.findViewById<ImageView>(R.id.no_data_first_icon_iv)
    }
    private val noDataSecondIcon by lazy {
        all_video_fragment_no_data_included_layout.findViewById<ImageView>(R.id.no_data_second_icon_iv)
    }
    private val noDataText by lazy {
        all_video_fragment_no_data_included_layout.findViewById<TextView>(R.id.no_data_text_tv)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        i(title, "OnViewcreated")
        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(toolbar, navController)

        noDataFirstIcon.hide()
        noDataSecondIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_slow_motion_video_24
            )
        )
        noDataText.text = getString(R.string.no_video_available)
        val request = authViewModel.getAllVideos(header)

        //Observer for get request
        observeRequest<VideoList>(request, null, null, true, {
            val results = it

            val listOfVideo = results.data?.video?.map { video->
                VideoResource(
                    video.id,
                    video.tailorID,
                    video.title,
                    video.videoURL,
                    video.description,
                    video.createdAt,
                    video.updatedAt
                ).apply{
                    duration = video.duration
                }

            }
            if (listOfVideo?.isNotEmpty()!!) {
                all_video_fragment_no_data_included_layout.hide()
                all_video_fragment_rv.show()

                all_video_fragment_rv.setupAdapter<VideoResource>(R.layout.resource_video_item) { adapter, context, list ->
                    bind { itemView, position, item ->
                        val mediaController = MediaController(requireContext())
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
                            itemView.resource_video_item_ytpv.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                                override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                                    youTubePlayer.loadVideo(ext, 0F)
                                    youTubePlayer.pause()

                                }

                            })

                        }


                        itemView.setOnClickListener {
                            actionToDisplaySingleVideo(item)
                        }

                        all_video_fragment_rv.addOnScrollListener(object: RecyclerView.OnScrollListener(){

                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                mediaController.hide()
                            }

                        })
                    }
                    setLayoutManager(GridLayoutManager(requireContext(), 2))
                    submitList(listOfVideo)
                }

            } else {
                no_data_included_layout.show()
            }
        },{err ->
            //On error
            i(title, "VideoListError $err")
        })



    }

    private fun actionToDisplaySingleVideo(item: VideoResource?) {
        val action = AllVideoFragmentDirections.actionAllVideoFragmentToSingleVideoFragment()
        action.video = item
        goto(action)
    }

}