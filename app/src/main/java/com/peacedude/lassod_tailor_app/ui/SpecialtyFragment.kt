package com.peacedude.lassod_tailor_app.ui

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.show
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.fragment_specialty.*
import kotlinx.android.synthetic.main.fragment_user_account.*
import kotlinx.android.synthetic.main.specialty_layout_item.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [SpecialtyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpecialtyFragment : Fragment() {

    private lateinit var saveBtn: Button
    private lateinit var progressBar: ProgressBar
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
        return inflater.inflate(R.layout.fragment_specialty, container, false)
    }

    override fun onResume() {
        super.onResume()

        val specialtyList = resources.getStringArray(R.array.specialty_list).toList()
        specialty_rv.setupAdapter<String>(R.layout.specialty_layout_item){adapter, context, list ->
            bind { itemView, position, item ->
                itemView.specialty_item_checkbox.show()
                itemView.specialty_item_checkbox.text = item
            }
            setLayoutManager(GridLayoutManager(context, 2))
            submitList(specialtyList)
        }

        val genderList = resources.getStringArray(R.array.gender_list).toList()
        gender_rv.setupAdapter<String>(R.layout.specialty_layout_item){adapter, context, list ->
            bind { itemView, position, item ->
                itemView.specialty_item_checkbox.show()
                itemView.specialty_item_checkbox.text = item
            }
            setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true))
            submitList(genderList)
        }
        val measurementOptionsList = resources.getStringArray(R.array.measurement_options_list).toList()
        measurement_options_rv.setupAdapter<String>(R.layout.specialty_layout_item){adapter, context, list ->
            bind { itemView, position, item ->
                itemView.measurement_vg.show()
                itemView.measurement_options_tv.text = item
            }
            setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false))
            submitList(measurementOptionsList)
        }

        val saveBtnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        saveBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor( requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
        buttonTransactions({
            saveBtn = specialty_save_changes_btn.findViewById(R.id.btn)
            progressBar = specialty_save_changes_btn.findViewById(R.id.progress_bar)
        },{
            saveBtn.text = getString(R.string.save_changes)
            saveBtn.background = saveBtnBackground
            saveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        })
    }

}