package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.appbar.AppBarLayout
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.ArticleList
import com.peacedude.lassod_tailor_app.model.response.MeasurementTypeList
import com.skydoves.progressview.ProgressView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.fragment_measurement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [MeasurementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeasurementFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }
    private val toolbar by lazy {
        resources_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
    }

    val header by lazy {
        authViewModel.header
    }

    private val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.add_measurement_layout_dialog)
        }
    }

    private val dialogIncludeBtnLayout by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_btn) as View)

    }

    private val dialogSpinner by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_spinner) as Spinner)
    }


    private val dialogAppBar by lazy {
        (dialog.findViewById(R.id.add_measurement_layout_dialog_appbar) as AppBarLayout)
    }
    private val dialogToolbar by lazy {
        (dialogAppBar.findViewById(R.id.reusable_appbar_toolbar) as Toolbar)
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    lateinit var addMeasurementBtn: Button
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
        return inflater.inflate(R.layout.fragment_measurement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)
        val activityTitle = dialogAppBar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.add_measurement)

        addMeasurementBtn = measurement_fragment_add_measurement_btn.findViewById(R.id.btn)
        val btnBackground = addMeasurementBtn.background
        btnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
        val dialogAddMeasurementBtn = dialogIncludeBtnLayout.findViewById<Button>(R.id.btn)
        val dialogBtnBackgound = dialogAddMeasurementBtn.background
        dialogBtnBackgound?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        buttonTransactions({
            addMeasurementBtn.background = btnBackground
            addMeasurementBtn.text = getString(R.string.add_measurement)
            addMeasurementBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            dialogAddMeasurementBtn.background = dialogBtnBackgound
            dialogAddMeasurementBtn.text = getString(R.string.add_measurement)
            dialogAddMeasurementBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        }, {

            addMeasurementBtn.setOnClickListener {
                dialog.show()
            }

        })

        CoroutineScope(Main).launch {
            authViewModel.getMeasurementTypes(header)
                .catch {
                    i(title, "Error on flow ${it.message}")
                }
                .collect {
                    onFlowResponse<MeasurementTypeList>(it) {
                        i(title, "Measure ${it?.measurementTypes}")

                        val measurementMap = HashMap<Long, String>()
                        it?.measurementTypes?.associateByTo(measurementMap, { k ->
                            k.id
                        }, { v ->
                            v.name
                        })
                        val measurementTypes = it?.measurementTypes?.map {
                            it.name
                        }
                        setUpSpinnerWithList(
                            getString(R.string.select_str), dialogSpinner,
                            measurementTypes as ArrayList<String>
                        )
                    }
                }

            dialogSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    i(title, "Position $position")
                }

            }

        }
    }

}

data class MeasurementTypes(
    val id: Long,
    val name: String
)