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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.ResourcesArticlePublication
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.response.Article
import com.peacedude.lassod_tailor_app.model.response.ArticleList
import com.peacedude.lassod_tailor_app.model.response.VideoList
import com.peacedude.lassod_tailor_app.model.response.VideoResource
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_all_articles.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_all_video.all_video_fragment_rv
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [AllArticlesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllArticlesFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }
    private val toolbar by lazy {
        (all_articles_fragment_tb.findViewById(R.id.reusable_appbar_toolbar) as Toolbar)
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
        CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                val articleListCall = async { authViewModel.getArticles(header) }

                try {
                    articleListCall.await()
                        .collect {
                            onFlowResponse<ArticleList>(response = it) {
                                val listOfArticles = it?.article?.map {a->
                                    Article(
                                        a.id,
                                        a.tailorID,
                                        a.title,
                                        a.description,
                                        a.isBlacklisted,
                                        a.body,
                                        a.articleImage,
                                        a.createdAt,
                                        a.updatedAt
                                    )
                                }
                                i(title, "article data flow $it")
                                if (listOfArticles?.isNotEmpty()!!) {
                                    all_articles_fragment_rv.setupAdapter<Article>(
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

                                            itemView.setOnClickListener {
                                                GlobalVariables.globalArticle = item
                                                goto(R.id.singleArticleFragment)
                                            }

                                        }
                                        setLayoutManager(GridLayoutManager(requireContext(), 2))
                                        submitList(listOfArticles)
                                    }
                                } else {
                                    resources_fragment_article_recycler_vf.showNext()

                                }
                            }
                        }
                }
                catch (e:Exception){
                    i(title, "resource error data flow ${e.message}")
                }
            }

        }

    }


}