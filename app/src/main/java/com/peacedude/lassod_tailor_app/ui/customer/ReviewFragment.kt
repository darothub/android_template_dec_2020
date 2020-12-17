package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.changeBackgroundColor
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.client_list_item.view.*
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.measurement_items.view.*
import kotlinx.android.synthetic.main.progressbar_review_item.view.*
import kotlinx.android.synthetic.main.review_list_item.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReviewFragment : Fragment() {
    private val title by lazy {
        getName()
    }
    lateinit var reviewPostBtn:Button
    lateinit var reviewProgressBar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonTransactions({
            reviewPostBtn = review_fragment_post_btn.findViewById(R.id.btn)
            reviewProgressBar = review_fragment_post_btn.findViewById(R.id.progress_bar)
            reviewPostBtn.apply {
                background.changeBackgroundColor(requireContext(), R.color.colorPrimary)
                text = getString(R.string.post)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            }

        },{

        })

        val listOfProgressRating = arrayListOf<ProgressBarRating>(
            ProgressBarRating(5),
            ProgressBarRating(4, 20),
            ProgressBarRating(3, 50),
            ProgressBarRating(2),
            ProgressBarRating(1)
        )

        review_fragment_progress_bar_rv.setupAdapter<ProgressBarRating>(R.layout.progressbar_review_item) { subAdapter, context, list ->

            bind { itemView, position, item ->
                itemView.progressbar_review_tv.text = "${item?.rating}"
                itemView.progressbar_review_pb.progress = item?.progress!!
            }
            setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false))
            submitList(listOfProgressRating)
        }
        val listOfProgressRatingStar = arrayListOf<ProgressBarRating>(
            ProgressBarRating(0.5F),
            ProgressBarRating(0F),
            ProgressBarRating(0F),
            ProgressBarRating(0.5F),
            ProgressBarRating(0F)

        )
        review_fragment_rating_bar_rb.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            i("Hello", "Rating bar clicked $rating")
        }

        var listOfReviews = arrayListOf<Reviews>(
            Reviews("Kaza Kaza", 4F, "24-12-2020"),
            Reviews("Kaza Kaza", 4F, "24-12-2020")
        )
        review_fragment_reviews_rv.setupAdapter<Reviews>(R.layout.review_list_item) { subAdapter, context, list ->

            bind { itemView, position, item ->
                itemView.review_list_name_tv.text = item?.name
                itemView.review_list_rb.rating = item?.rating!!
                itemView.review_list_date_tv.text = item.date
                val nameContainsSpace = item.name.contains(" ")
                if(nameContainsSpace!!){
                    val nameSplit = item.name.split(" ")
                    val firstName = nameSplit.get(0)
                    val lastName = nameSplit.get(1)
                    itemView.review_list_name_initials_tv.text = "${firstName.get(0)}${lastName.get(0)}"
                }
                else{
                    val firstName = item.name[0]
                    itemView.review_list_name_initials_tv.text = "$firstName"
                }
                itemView.review_list_more_iv.setOnClickListener {v->
                    val popup = PopupMenu(requireContext(), itemView.review_list_more_iv)
                    popup.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.edit -> {
                                i(title, "Edit")
                                true
                            }
                            R.id.delete ->{
                                i(title, "delete")
                                true
                            }
                            else -> {
                                i(title, "Else")
                                false
                            }
                        }
                    }
                    popup.inflate(R.menu.review_popup_menu)
                    popup.show()
                }
            }
            setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false))
            submitList(listOfReviews)
        }
    }

}

data class ProgressBarRating(var rating:Int=0, var progress:Int=0){
    var ratings = 0F
    constructor(ratings:Float):this(0, 0){
        this.ratings = ratings
    }
}
data class Reviews(var name:String, var rating:Float=0F, var date:String)