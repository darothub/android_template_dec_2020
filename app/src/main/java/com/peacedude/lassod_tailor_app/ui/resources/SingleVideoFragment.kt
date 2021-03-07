package com.peacedude.lassod_tailor_app.ui.resources

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.response.VideoList
import com.peacedude.lassod_tailor_app.model.response.VideoResource
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_single_video.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SingleVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleVideoFragment : DaggerFragment() {

    private val title by lazy {
        getName()
    }
    val header by lazy {
        authViewModel.header
    }

    private val toolbar by lazy {
        (single_video_fragment_tb as Toolbar)
    }
    val singleVideoArg by navArgs<SingleVideoFragmentArgs>()

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

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
        return inflater.inflate(R.layout.fragment_single_video, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Nav controller set on toolbar
        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(toolbar, navController)

        val single = singleVideoArg.video
        val singleMediaController = MediaController(requireContext())

        if(single != null){
            single_video_fragment_video_title_tv.text = single.title
            single_video_fragment_video_time_tv.text = single.description
            if (single.videoURL != null && single.videoURL.endsWith(".mp4")) {
                singleMediaController.setAnchorView(single_video_fragment_vv)
                single_video_fragment_vf.showNext()
                val uri = Uri.parse(single.videoURL)
                single_video_fragment_vv.setMediaController(singleMediaController)
                single_video_fragment_vv.setVideoURI(uri)
                single_video_fragment_vv.seekTo(1)
            } else {
                val ext = single.videoURL?.split("=")?.get(1).toString()
                single_video_fragment_ytpv.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                    override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                        youTubePlayer.loadVideo(ext, 0F)
                    }

                })

            }
        }
        else{
            requireActivity().gdToast("Invalid video resource", Gravity.BOTTOM)
        }

//        CoroutineScope(Dispatchers.Main).launch {
//            supervisorScope {
//                val videoListCall = async { authViewModel.getVideos(header) }
//                try {
//                    videoListCall.await()
//                        .collect {
//                            onFlowResponse<VideoList>(response = it) { videos ->
//                                i(title, "article data flow $it")
//                                val listOfVideos = videos?.video?.map { v ->
//                                    VideoResource(
//                                        v.id,
//                                        v.tailorID,
//                                        v.title,
//                                        v.videoURL,
//                                        v.description,
//                                        v.createdAt,
//                                        v.updatedAt
//                                    ).apply {
//                                        duration = v.duration
//                                    }
//                                }?.takeIf {
//                                    it.size > 5
//                                }.let {
//                                    it?.take(5)
//                                }
//
//                                if(!listOfVideos.isNullOrEmpty()){
//                                    single_video_fragment_rv.setupAdapter<VideoResource>(R.layout.resource_video_item) { adapter, context, list ->
//                                        bind { itemView, position, item ->
//                                            val mediaController = MediaController(requireContext())
//                                            mediaController.hide()
//                                            mediaController.setAnchorView(itemView.resource_video_item_vv)
//                                            itemView.resource_video_item_title_tv.text = item?.title
//                                            itemView.resource_video_item_time_tv.text = item?.duration
//                                            if (item?.videoURL != null && item.videoURL.endsWith(".mp4")) {
//                                                itemView.resource_video_item_vv.show()
//                                                itemView.resource_video_item_ytpv.hide()
//                                                val uri = Uri.parse(item.videoURL)
//                                                itemView.resource_video_item_vv.setMediaController(
//                                                    mediaController
//                                                )
//                                                itemView.resource_video_item_vv.setVideoURI(uri)
//                                                itemView.resource_video_item_fl.clipToOutline = true
//                                                itemView.resource_video_item_vv.seekTo(1)
//                                            } else {
//                                                itemView.resource_video_item_vv.hide()
//                                                itemView.resource_video_item_ytpv.show()
//                                                itemView.resource_video_item_ytpv.clipToOutline = true
//                                                val ext = item?.videoURL?.split("=")?.get(1).toString()
//                                                itemView.resource_video_item_ytpv.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
//                                                    override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
//                                                        youTubePlayer.loadVideo(ext, 0F)
//                                                        youTubePlayer.pause()
//                                                    }
//
//                                                })
//
//                                            }
//
//                                            single_video_fragment_rv.addOnScrollListener(object:RecyclerView.OnScrollListener(){
//
//                                                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                                                    super.onScrollStateChanged(recyclerView, newState)
//                                                    mediaController.hide()
//                                                    singleMediaController.hide()
//                                                }
//
//                                            })
//
//                                        }
//                                        setLayoutManager(GridLayoutManager(requireContext(), 2))
//                                        submitList(listOfVideos)
//                                    }
//                                }
//                                else{
//
//                                }
//                            }
//                        }
//                }catch (e:Exception){
//                    i(title, "Single video error data flow ${e.message}")
//                }
//            }
//        }


        i(title, "Single $single")






    }

}