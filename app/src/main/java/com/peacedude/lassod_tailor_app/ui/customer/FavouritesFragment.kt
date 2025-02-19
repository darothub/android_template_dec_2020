package com.peacedude.lassod_tailor_app.ui.customer

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.Data
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.MeasurementValues
import com.peacedude.lassod_tailor_app.model.response.Favourite
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.ReviewResponse
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.resources.SingleVideoFragmentArgs
import com.utsman.recycling.adapter.RecyclingAdapter
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.favourite_list_item.view.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.android.synthetic.main.fragment_measurement.*
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.fragment_single_video.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : DaggerFragment() {


    private val title by lazy {
        getName()
    }

    private val toolbar by lazy {
        (favourite_fragment_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar))
    }
    private val titleTV by lazy {
        (favourite_fragment_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv))
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val userViewModel by viewModels<UserViewModel> {
        viewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTV.text = getString(R.string.favourites)

        toolbar.setNavigationOnClickListener {
            finish()
        }
        onBackDispatcher {
            findNavController().popBackStack()
            finish()
        }

        CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                val getFavouriteReq = async {
                    userViewModel.getFavourites()
                }
                getFavouriteReq.await()
                    .catch { err ->
                        requireActivity().gdToast(err.message.toString(), Gravity.BOTTOM)
                    }
                    .collect { data ->
                        onFlowResponse<Data>(response = data) { it ->
                            val listOfFavourites = it?.data?.map {each ->
                                each
                            }
                            if(listOfFavourites?.isNotEmpty() == true){
                                favourite_fragment_vf.showNext()
                                favorite_fragment_rv.setupAdapter<Favourite>(R.layout.favourite_list_item) { subAdapter, context, list ->

                                    bind { itemView, position, item ->
                                        itemView.favourite_list_name_tv.text = "${item?.user?.firstName} ${item?.user?.lastName}"
                                    }
                                    setLayoutManager(
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    )
                                    subAdapter.delete(list)
                                        .attachToRecyclerView(favorite_fragment_rv)
                                    submitList(listOfFavourites as List<Favourite?>?)
                                }
                            }
                            else{

                            }

                        }
                    }
            }
        }
    }


    @ExperimentalCoroutinesApi
    private fun <T> RecyclingAdapter<T>.delete(list: MutableList<T?>?): ItemTouchHelper {

        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val item = list?.get(pos) as Favourite

                CoroutineScope(Dispatchers.Main).launch {
                    supervisorScope {
                        val artisanId = item.artisanID
                        val removeFavoriteReq =
                            async { userViewModel.removeFavourites(artisanId) }
                        removeFavoriteReq.await()
                            .catch {
                                i(title, "RecyclingAdapter delete ${it.message}")
                            }
                            .collect {
                                onFlowResponse<UserResponse<NothingExpected>>(response = it) {
                                    list.removeAt(pos)
                                    this@delete.notifyDataSetChanged()
                                    if (list.isEmpty()) {

                                    }
                                    requireActivity().gdToast(
                                        it?.message.toString(),
                                        Gravity.BOTTOM
                                    )
                                }
                            }
                    }
                }

                i("OnRemoved", " pos $pos itemId ${item.id}")


            }

        })

    }

}