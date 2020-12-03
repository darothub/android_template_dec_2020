package com.peacedude.lassod_tailor_app.ui.customer

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.Photo
import com.peacedude.lassod_tailor_app.ui.clientmanagement.MeasurementLabelValue
import com.squareup.picasso.Picasso
import com.utsman.recycling.extentions.Recycling
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_media.media_fragment_rv
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.measurement_items.view.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.android.synthetic.main.search_filter_item.view.*
import kotlinx.android.synthetic.main.search_media_result_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }


    val header by lazy {
        userViewModel.header
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
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

        search_fragment_filter_ib.setOnClickListener {
            if (search_fragment_search_rv.invisible()) {
                search_fragment_search_rv.show()
            } else {
                search_fragment_search_rv.invisible()
            }
        }


        search_fragment_search_rv.setupAdapter<SearchFilter>(R.layout.search_filter_item) { adapter, context, list ->

            bind { itemView, position, item ->
                itemView.search_filter_title_tv.text = item?.title
                item?.spinnerList?.let {
                    setUpSpinnerWithList(
                        null, itemView.search_filter_spinner,
                        it
                    )
                }

                itemView.search_filter_spinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val itemSelected = itemView.search_filter_spinner.selectedItem as String
                            item?.selectedItem = itemSelected

                        }
                    }
                search_fragment_search_btn.setOnClickListener {
                    val selected = list?.map {
                        it?.selectedItem
                    }

                    val keyword = search_fragment_search_et.text.toString()
                    val keywordTag = search_fragment_search_et.tag
                    when {
                        keyword.isNullOrEmpty() -> requireActivity().gdToast(
                            "$keywordTag cannot be empty",
                            Gravity.BOTTOM
                        )
                        else -> {
                            val location = selected?.get(2)
                            val specialty = selected?.get(1)
                            val category = selected?.get(0)

                            search_fragment_search_result_rv.setupAdapter<SearchResult>(R.layout.search_media_result_item) { adapter, context, list ->

                                bind { itemView, position, item ->
                                    itemView.search_result_picture_title_tv.text = item?.title
                                    itemView.search_result_picture_location_tv.text = item?.location
                                    itemView.search_result_picture_iv.load(item?.media) {
                                        crossfade(true)
                                        placeholder(R.drawable.profile_image)
                                    }
                                }


                                val layoutManager = GridLayoutManager(requireContext(), 2)
                                setLayoutManager(layoutManager)

                                // for grid layout manager, loader by default is ugly, to fix use fixGridSpan
                                fixGridSpan(3)
                                setupData(
                                    this@setupAdapter,
                                    keyword,
                                    location,
                                    specialty,
                                    category,
                                    1
                                )

                                // use paging listener for endless recycler view and loaded data
                                onPagingListener(layoutManager) { page, itemCount ->

                                    // call function setup data with page +1
                                    setupData(
                                        this@setupAdapter,
                                        keyword,
                                        location,
                                        specialty,
                                        category,
                                        page + 1.toLong()
                                    )
                                }
//
//            // for grid layout manager, loader by default is ugly, to fix use fixGridSpan
//            fixGridSpan(3)


                            }
                        }
                    }



                    i(title, "Item $item itemselected $selected ")
                }

            }


            setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )

            submitList(listOfFilterOptions)


        }

        var searchResult = arrayListOf<SearchResult>(
            SearchResult("JJ Fashionista", "Egbeda", getString(R.string.test_photo)),
            SearchResult("JJ Fashionista", "Egbeda", getString(R.string.test_photo)),
            SearchResult("JJ Fashionista", "Egbeda", getString(R.string.test_photo))
        )


    }

    private fun setupData(
        recycling: Recycling<SearchResult>,
        keyword: String?,
        location: String?,
        specialty: String?,
        category: String?,
        page: Long
    ) {
        CoroutineScope(Dispatchers.Main).launch {

            supervisorScope {

                userViewModel.searchArtisan(keyword, location, specialty, category, page, 2)
                    .catch {

                    }
                    .collect {

                    }


            }
        }

    }

}

data class SearchFilter(
    var title: String,
    var spinnerList: ArrayList<String>,
    var selectedItem: String? = ""
)

data class SearchResult(var title: String, var location: String, var media: String)