package com.peacedude.lassod_tailor_app.ui.customer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.Artisan
import com.peacedude.lassod_tailor_app.model.response.Favourite
import com.peacedude.lassod_tailor_app.model.response.PhotoList
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_single_fashionista.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SingleFashionistaFragment : DaggerFragment() {

    private val title by lazy {
        getName()
    }
    lateinit var chatButton: Button
    lateinit var callButton: Button
    lateinit var galleryButton: Button
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val userViewModel by viewModels<UserViewModel> {
        viewModelProviderFactory
    }
    val artisanDetails by navArgs<SingleFashionistaFragmentArgs>()
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
        return inflater.inflate(R.layout.fragment_single_fashionista, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)


        val artisan = artisanDetails.artisanDetails

        single_fashionista_fragment_name_tv.text = "${artisan?.firstName} ${artisan?.lastName}"
        single_fashionista_fragment_address_tv.text =
            "${artisan?.profile?.workshopAddress?.street} ${artisan?.profile?.workshopAddress?.city} ${artisan?.profile?.workshopAddress?.state}"

        single_fashionista_fragment_info_tv.text = ""
        artisan?.profile?.specialty?.forEach{
            single_fashionista_fragment_info_tv.append(it)
        }
        single_fashionista_fragment_rating_value_tv.text = "${ artisan?.profile?.rating }"
        single_fashionista_fragment_rb.rating = artisan?.profile?.rating.toString().toFloat()
        single_fashionista_fragment_image_iv.load(artisan?.profile?.avatar) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.profile_image)
        }




        buttonTransactions({

            galleryButton = single_fashionista_fragment_gallery_btn.findViewById(R.id.btn)
            galleryButton.apply {
                text = getString(R.string.goto_gallery)
                background.changeBackgroundColor(requireContext(), R.color.colorPrimary)
            }

        },{

        })

        single_fashionista_fragment_favourite_iv.setOnClickListener {
             CoroutineScope(Dispatchers.Main).launch {
                 supervisorScope {
                     val addFavourite = async {
                         userViewModel.addFavourite(artisan?.id.toString())
                     }
                     addFavourite.await()
                     .catch {
                        i(title, "Error All Photo on flow ${it.message}")
                    }
                    .collect {
                        onFlowResponse<UserResponse<Artisan>>(response = it) { it ->
                            requireActivity().gdToast(it?.message.toString(), Gravity.BOTTOM)
                            single_fashionista_fragment_favourite_vf.showNext()
                        }
                    }
                 }
             }


        }
        single_fashionista_fragment_favourite_fill_iv.setOnClickListener {
//            single_fashionista_fragment_favourite_vf.showPrevious()
        }

        single_fashionista_fragment_see_review_tv.setOnClickListener {
            val action = SingleFashionistaFragmentDirections.actionSingleFashionistaFragmentToReviewFragment()
            action.artisanDetails = artisan
            goto(action)
        }
    }


}