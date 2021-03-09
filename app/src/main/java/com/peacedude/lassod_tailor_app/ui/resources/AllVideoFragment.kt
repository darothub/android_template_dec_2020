package com.peacedude.lassod_tailor_app.ui.resources

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.gdtoast.gdToast
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
import kotlinx.coroutines.flow.collect
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
    private val authViewModel: AuthViewModel by viewModels {
        viewModelProviderFactory
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

        lifecycleScope.launchWhenStarted {
            authViewModel.videosStateflow.collect()

        }

    }

    override fun onResume() {
        super.onResume()
        observeResponseState<VideoList>(
            authViewModel.videoState,
            null,
            null,
            {
                val data = it?.data?.video
                i(title, "VideoList flow $data")
                if (data?.isNotEmpty() == true) {
                    val lm = GridLayoutManager(requireContext(), 2)
                    setUpRecyclerViewForVideo(all_video_fragment_rv, data, lm)
                } else {
                    all_video_fragment_recycler_vf.displayedChild = 1
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