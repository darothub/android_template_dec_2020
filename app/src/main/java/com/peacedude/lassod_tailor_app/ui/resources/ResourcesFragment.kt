package com.peacedude.lassod_tailor_app.ui.resources

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.goto
import com.peacedude.lassod_tailor_app.model.request.Measurement
import com.peacedude.lassod_tailor_app.model.request.ResourcesArticlePublication
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_native_measurement.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.measurement_item.view.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import kotlinx.android.synthetic.main.resource_video_item.view.resource_video_item_time_tv
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [ResourcesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResourcesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val imageLoader by lazy {
        ImageLoader(requireContext())
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
        val videoResourcesList = arrayListOf<ResourcesVideo>(
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str)),
            ResourcesVideo(getString(R.string.sample_video_str), getString(R.string.sample_str), getString(R.string.sample_min_str))
        )

        val articleResourcesList = arrayListOf<ResourcesArticlePublication>(
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str))
        )

        resource_fragment_video_rv.setupAdapter<ResourcesVideo>(R.layout.resource_video_item) { adapter, context, list ->
            bind { itemView, position, item ->
                val mediaController = MediaController(requireContext())
                mediaController.setAnchorView(itemView.resource_video_item_vv)
                val uri = Uri.parse(item?.videoUri)
                itemView.resource_video_item_vv.setMediaController(mediaController)
                itemView.resource_video_item_vv.setVideoURI(uri)
                itemView.resource_video_item_fl.clipToOutline = true
                itemView.resource_video_item_vv.seekTo(1)
                itemView.resource_video_item_title_tv.text = item?.videoTitle
                itemView.resource_video_item_time_tv.text = item?.videoMins

                resource_fragment_video_rv.addOnScrollListener(object: RecyclerView.OnScrollListener(){

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        mediaController.hide()
                    }

                })
                resources_fragment_nsv.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                    mediaController.hide()
                }
            }
            setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
            submitList(videoResourcesList)
        }
        resource_fragment_article_publications_rv.setupAdapter<ResourcesArticlePublication>(R.layout.article_publication_item_layout) { adapter, context, list ->
            bind { itemView, position, item ->

                itemView.resource_article_publication_item_title_tv.text = item?.articleTitle
                itemView.resource_article_publication_item_author_tv.text = item?.articleAuthor
                Picasso.get().load(item?.articleImageUri).into(itemView.resource_article_publications_iv)

//                val request = ImageRequest.Builder(context)
//                    .data(item?.articleImageUri)
//                    .build()
//                lifecycleScope.launch {
//                    val drawable = imageLoader.execute(request).drawable
//                    itemView.resource_article_publications_iv.setImageDrawable(drawable)
//                }

                itemView.resource_article_publications_iv.clipToOutline = true

            }
            setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
            submitList(articleResourcesList)
        }

        resource_fragment_view_all_video_tv.setOnClickListener {
            goto(R.id.allVideoFragment)
        }
    }
}