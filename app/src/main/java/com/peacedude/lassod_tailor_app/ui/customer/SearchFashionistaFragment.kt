package com.peacedude.lassod_tailor_app.ui.customer

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.request.UserAddress
import com.peacedude.lassod_tailor_app.model.response.Artisan
import com.peacedude.lassod_tailor_app.model.response.ArtisanSearchResponse
import com.peacedude.lassod_tailor_app.model.response.Profile
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.utsman.recycling.extentions.Recycling
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.measurement_items.view.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.android.synthetic.main.search_filter_item.view.*
import kotlinx.android.synthetic.main.search_media_category_item.view.*
import kotlinx.android.synthetic.main.search_result_media_item.view.*
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

    val currentUser by lazy {
        userViewModel.currentUser
    }
    val header by lazy {
        userViewModel.header
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val userViewModel by viewModels<UserViewModel> {
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        i(title, "onViewCreated")
        changeStatusBarColor(R.color.colorWhite)

        val listOfFilterOptions = arrayListOf<SearchFilter>(
            SearchFilter("Artisan", arrayListOf("All", "Tailor", "Weaver")),
            SearchFilter("Style", arrayListOf("All", "Native", "English")),
            SearchFilter("Location", arrayListOf("All", "Lagos", "Ibadan"))
        )

        search_fragment_filter_ib.setOnClickListener {
            if (search_fragment_search_rv.invisible()) {
                search_fragment_search_rv.show()
            } else {
                search_fragment_search_rv.invisible()
            }
        }

        search_fragment_login_ib.setOnClickListener {
            goto(MainActivity::class.java)
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
                    val category = list?.get(0)?.selectedItem as String
                    val specialty = list?.get(1)?.selectedItem as String
                    val location =  list?.get(2)?.selectedItem as String
                    requireActivity().gdToast("$category $specialty $location", Gravity.BOTTOM)
                    val keyword = search_fragment_search_et.text.toString().trim()

                    search_fragment_search_result_rv.setupAdapter<SearchResultTwo>(R.layout.search_media_category_item) { adapter, context, list ->

                        bind { itemView, position, item ->
                            itemView.search_result_search_result_title_tv.text = item?.category
                            itemView.search_result_rv.setupAdapter<Artisan>(R.layout.search_result_media_item) { adapter, context, mediaList ->
                                bind { mediaItemView, position, mediaItem ->
                                    mediaItemView.search_result_picture_title_tv.text =
                                        mediaItem?.firstName
                                    mediaItemView.search_result_picture_location_tv.text =
                                       "${ mediaItem?.profile?.workshopAddress?.city } ${ mediaItem?.profile?.workshopAddress?.state }"
                                    mediaItemView.search_result_picture_iv.load(mediaItem?.profile?.avatar) {
                                        crossfade(true)
                                        placeholder(R.drawable.profile_image)
                                    }
                                    mediaItemView.setOnClickListener {
                                        val action = SearchFragmentDirections.actionSearchFragmentToSingleFashionistaFragment()
                                        action.artisanDetails = mediaItem
                                        goto(action)
                                    }
                                }
                                setLayoutManager(
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                )
                                submitList(item?.list)
                            }

                        }


                        val layoutManager =  LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        setLayoutManager(layoutManager)

                        setupData(
                            this@setupAdapter,
                            keyword,
                            location,
                            specialty,
                            category,
                            1
                        )

//                        onPagingListener(layoutManager) { page, itemCount ->
//
//                            // call function setup data with page +1
//                            setupData(
//                                this@setupAdapter,
//                                keyword,
//                                location,
//                                specialty,
//                                category,
//                                page + 1.toLong()
//                            )
//                        }


                    }

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


    }


    private fun setupData(
        recycling: Recycling<SearchResultTwo>,
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
                        onFlowResponse<ArtisanSearchResponse>(response = it) {
                            val artisanList = arrayListOf<SearchResultTwo>(
                                SearchResultTwo("Tailors",
                                    it?.tailors as List<Artisan>
                                ),
                                SearchResultTwo("Weavers",
                                    it.weavers as List<Artisan>
                                )
                            )
                            i(title, "artisans ${it?.tailors}")
                            recycling.submitList(artisanList)
                        }
                    }

            }
        }

    }

    override fun onResume() {
        super.onResume()

        val user = GlobalVariables.globalUser
        i(title, "LoggedIn ${GlobalVariables.globalUser?.loggedIn} current${currentUser!!.loggedIn}")
        if((user != null && user.loggedIn) || (currentUser != null && currentUser!!.loggedIn)){
            search_fragment_login_ib.text = getString(R.string.goto_dashboard)
        }
        else{
            search_fragment_login_ib.text = getString(R.string.login)
        }
    }

    override fun onPause() {
        super.onPause()
        userViewModel.currentUser = GlobalVariables.globalUser
    }
}

data class SearchFilter(
    var title: String,
    var spinnerList: ArrayList<String>,
    var selectedItem: String? = ""
)

data class SearchResult(var title: String, var location: String, var media: String)
data class SearchResultTwo(var category: String?, var list: List<Artisan>)

//val listSearchResultTwo = arrayListOf<SearchResultTwo>(
//    SearchResultTwo(
//        "Tailor",
//        arrayListOf(SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)),
//            SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)),
//            SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)))
//    ),
//    SearchResultTwo(
//        "Weaver",
//        arrayListOf(SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)),
//            SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)),
//            SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)))
//    ),
//    SearchResultTwo(
//        "Weaver",
//        arrayListOf(SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)),
//            SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)),
//            SearchResult("JJ Fashionista", "Lagos", getString(R.string.test_photo)))
//    )
//)