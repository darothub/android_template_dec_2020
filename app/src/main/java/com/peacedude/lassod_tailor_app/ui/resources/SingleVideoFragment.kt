package com.peacedude.lassod_tailor_app.ui.resources

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.goto
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_single_video.*
import kotlinx.android.synthetic.main.resource_video_item.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [SingleVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleVideoFragment : Fragment() {

    private val toolbar by lazy {
        (single_video_fragment_tb as Toolbar)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Nav controller set on toolbar
        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(toolbar, navController)

        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(single_video_fragment_vv)
        val uri = Uri.parse(getString(R.string.sample_video_str))
        single_video_fragment_vv.setMediaController(mediaController)
        single_video_fragment_vv.setVideoURI(uri)

        val videoResourcesList = arrayListOf<ResourcesVideo>(
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str))
        )

        single_video_fragment_rv.setupAdapter<ResourcesVideo>(R.layout.resource_video_item) { adapter, context, list ->
            bind { itemView, position, item ->
                val mediaController = MediaController(requireContext())
                mediaController.setAnchorView(itemView.resource_video_item_vv)
                val uri = Uri.parse(item?.videoUri)
                itemView.resource_video_item_vv.setMediaController(mediaController)
                itemView.resource_video_item_vv.setVideoURI(uri)
                itemView.resource_video_item_fl.clipToOutline = true

                itemView.resource_video_item_title_tv.text = item?.videoTitle
                itemView.resource_video_item_time_tv.text = item?.videoMins

            }
            setLayoutManager(GridLayoutManager(requireContext(), 2))
            submitList(videoResourcesList)
        }

    }

}