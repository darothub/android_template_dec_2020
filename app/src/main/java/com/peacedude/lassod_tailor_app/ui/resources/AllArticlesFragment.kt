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
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.goto
import com.peacedude.lassod_tailor_app.model.request.ResourcesArticlePublication
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_all_articles.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_all_video.all_video_fragment_rv
import kotlinx.android.synthetic.main.resource_video_item.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [AllArticlesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllArticlesFragment : Fragment() {
    private val title by lazy {
        getName()
    }
    private val toolbar by lazy {
        (all_articles_fragment_tb as Toolbar)
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
        return inflater.inflate(R.layout.fragment_all_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(toolbar, navController)
        val articleResourcesList = arrayListOf<ResourcesArticlePublication>(
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str)),
            ResourcesArticlePublication(getString(R.string.sample_image_str), getString(R.string.sample_str), getString(R.string.by_author_str))
        )

        all_articles_fragment_rv.setupAdapter<ResourcesArticlePublication>(R.layout.article_publication_item_layout) { adapter, context, list ->
            bind { itemView, position, item ->
                itemView.resource_article_publication_item_title_tv.text = item?.articleTitle
                itemView.resource_article_publication_item_author_tv.text = item?.articleAuthor
                Picasso.get().load(item?.articleImageUri).into(itemView.resource_article_publications_iv)

                itemView.resource_article_publications_iv.clipToOutline = true

            }
            setLayoutManager(GridLayoutManager(requireContext(), 2))
            submitList(articleResourcesList)
        }
    }


}