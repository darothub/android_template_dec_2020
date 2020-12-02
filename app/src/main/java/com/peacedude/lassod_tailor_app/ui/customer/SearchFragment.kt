package com.peacedude.lassod_tailor_app.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.Photo
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_media.media_fragment_rv
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.android.synthetic.main.search_filter_item.view.*
import kotlinx.android.synthetic.main.search_media_result_item.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : DaggerFragment() {
    val title: String by lazy {
        getName()
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var listOfFilterOptions = arrayListOf<SearchFilter>(
            SearchFilter("Artisan", arrayListOf("All", "Tailor", "Weaver")),
            SearchFilter("Style", arrayListOf("All", "Tailor", "Weaver")),
            SearchFilter("Location", arrayListOf("All", "Tailor", "Weaver"))

        )


        search_fragment_search_rv.setupAdapter<SearchFilter>(R.layout.search_filter_item) { adapter, context, list ->

            bind { itemView, position, item ->
                itemView.search_filter_title_tv.text = item?.title
                item?.spinnerList?.let {
                    setUpSpinnerWithList(null, itemView.search_filter_spinner,
                        it
                    )
                }
            }


            setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
            submitList(listOfFilterOptions)
        }

        var searchResult = arrayListOf<SearchResult>(
            SearchResult("JJ Fashionista", "Egbeda", getString(R.string.test_photo)),
            SearchResult("JJ Fashionista", "Egbeda", getString(R.string.test_photo)),
            SearchResult("JJ Fashionista", "Egbeda", getString(R.string.test_photo))


        )

        search_fragment_search_result_rv.setupAdapter<SearchResult>(R.layout.search_media_result_item) { adapter, context, list ->

            bind { itemView, position, item ->
                itemView.search_result_picture_title_tv.text = item?.title
                itemView.search_result_picture_location_tv.text = item?.location
                itemView.search_result_picture_iv.load(item?.media){
                    crossfade(true)
                    placeholder(R.drawable.profile_image)

                }
            }


            setLayoutManager(GridLayoutManager(requireContext(), 3))
            submitList(searchResult)
        }

    }

}

data class SearchFilter(var title: String, var spinnerList:ArrayList<String>)
data class SearchResult(var title: String, var location:String, var media:String)