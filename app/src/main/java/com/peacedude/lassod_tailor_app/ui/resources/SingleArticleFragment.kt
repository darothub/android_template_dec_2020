package com.peacedude.lassod_tailor_app.ui.resources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import coil.load
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.GlobalVariables
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.article_publication_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_single_article.*
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SingleArticleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleArticleFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }
    val header by lazy {
        authViewModel.header
    }
    val singleArticle by lazy {
        GlobalVariables.globalArticle
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> {
        viewModelProviderFactory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Nav controller set on toolbar
        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(single_articlefragment_tb, navController)


        if(singleArticle != null){
            i(title, "$singleArticle")
            single_article_fragment_article_tv.setHtml(
                singleArticle?.body.toString(),
                HtmlHttpImageGetter(single_article_fragment_article_tv)
            )
            single_article_fragment_iv.load(singleArticle?.articleImage){
                crossfade(true)
                placeholder(R.drawable.obioma_logo_blue)
            }
            single_article_collapsingtbl.title = singleArticle?.title
        }
        else{
            i(title, "Cannot resolve body of null")
        }


    }

}