package com.peacedude.lassod_tailor_app.ui.resources

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.system.Os.bind
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.Article
import com.peacedude.lassod_tailor_app.model.response.ArticleList
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*

import kotlinx.android.synthetic.main.fragment_all_articles.*
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
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
    private val authViewModel: AuthViewModel by viewModels {
        viewModelProviderFactory
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

        lifecycleScope.launchWhenStarted {
            authViewModel.articlesStateFlow.collect()

        }

    }

    override fun onResume() {
        super.onResume()
        observeResponseState<ArticleList>(
            authViewModel.articleState,
            null,
            null,
            {
                val data = it?.data?.article
                i(title, "ArticlesList flow $data")
                if (data?.isNotEmpty() == true) {
                    val lm = GridLayoutManager(requireContext(), 2)
                    setUpRecyclerViewForArticles(all_articles_fragment_rv, data, lm)

                } else {
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


}

//private val broadcastReceiver = object : BroadcastReceiver() {
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onReceive(context: Context, intent: Intent) {
//        if (true) {
//
//        } else {
//
//        }
//    }
//}

//class FoodOrder private constructor(
//    val bread: String?,
//    val condiments: String?,
//    val meat: String?,
//    val fish: String?) {
//
//    data class Builder(
//        var bread: String? = null,
//        var condiments: String? = null,
//        var meat: String? = null,
//        var fish: String? = null) {
//
//        fun bread(bread: String) = apply { this.bread = bread }
//        fun condiments(condiments: String) = apply { this.condiments = condiments }
//        fun meat(meat: String) = apply { this.meat = meat }
//        fun fish(fish: String) = apply { this.fish = fish }
//        fun build() = FoodOrder(bread, condiments, meat, fish)
//    }
//}