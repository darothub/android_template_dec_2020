package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientMeasurement
import com.peacedude.lassod_tailor_app.model.request.Measurement
import com.peacedude.lassod_tailor_app.model.request.MeasurementValues
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_native_measurement.*
import kotlinx.android.synthetic.main.measurement_item.view.*
import java.util.HashMap
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [NativeMeasurementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NativeMeasurementFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }
    private lateinit var saveBtn: Button
    private lateinit var progressBar: ProgressBar
    private val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.add_measurement_dialog_layout)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(this.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val window = this.window
            window?.attributes = lp
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
    private val dialogIncludedButtonView by lazy {
        (dialog.findViewById(R.id.add_measurement_save_btn) as View)
    }
    private val addMeasurementButton by lazy {
        dialogIncludedButtonView.findViewById(R.id.btn) as Button
    }
    private val EditMeasurementButton by lazy {
        dialogIncludedButtonView.findViewById(R.id.btn) as Button
    }
    private val dialogAddNameField by lazy {
        dialog.findViewById<TextInputEditText>(R.id.add_measurement_et)
    }
    private val dialogAddValueField by lazy {
        dialog.findViewById<TextInputEditText>(R.id.add_measurement_value_et)
    }
    private val dialogEditNameField by lazy {
        dialog.findViewById<TextInputEditText>(R.id.edit_measurement_et)
    }
    private val dialogEditValueField by lazy {
        dialog.findViewById<TextInputEditText>(R.id.edit_measurement_value_et)
    }
    private val addTextCountTv by lazy {
        dialog.findViewById<TextView>(R.id.add_measurement_text_count_tv)
    }
    private val editTextCountTv by lazy {
        dialog.findViewById<TextView>(R.id.edit_measurement_text_count_tv)
    }
    private val measurementValuesSpinner by lazy {
        dialog.findViewById<Spinner>(R.id.add_measurement_value_spinner)
    }
    private val addMeasurementLayout by lazy {
        dialog.findViewById<ViewGroup>(R.id.add_measurement_ll)
    }
    private val dialogTitle by lazy {
        dialog.findViewById<TextView>(R.id.add_measurement_dialog_title_tv)
    }
    private val header by lazy {
        authViewModel.header
    }

    val measurementValues: HashMap<String, String> by lazy {
        HashMap<String, String> ()
    }

        //    private val arg:NativeMeasurementFragmentArgs by navArgs()
        @Inject
        lateinit var viewModelProviderFactory: ViewModelFactory
        private val authViewModel: AuthViewModel by lazy {
            ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
        }
        var client: Client? = Client("", "", "", "")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_native_measurement, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            arguments?.let {

            }
            val navHostFragment = requireActivity().supportFragmentManager.fragments[0] as NavHostFragment
            val parent = navHostFragment.childFragmentManager.primaryNavigationFragment as ClientFragment
            val measurementValuesList = resources.getStringArray(R.array.measurement_values_list).toList() as ArrayList<String>

            val measurementList = ArrayList<Measurement>()


            val addMeasurementBtnBackground = addMeasurementButton.background
            addMeasurementBtnBackground?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
            )


//        val client:Client? = arg.client ?: emptyClient
//        Log.i(title, "client name ${client?.name}")
            native_measurement_fab.setOnClickListener {
                setUpSpinnerWithList(getString(R.string.select_str), measurementValuesSpinner, measurementValuesList)
                dialogTitle.text = getString(R.string.add_measurement)

                addMeasurementLayout.show()
                dialog.show{
                    cornerRadius(10F)
                }
            }
            addMeasurementButton.apply {
                text = getString(R.string.add)
                    background = addMeasurementBtnBackground
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            buttonTransactions({

                saveBtn = native_measurement_included_btn.findViewById(R.id.btn)
                val saveBtnBackground = saveBtn.background

                saveBtnBackground?.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN
                )
                saveBtn.apply {
                    text = getString(R.string.save)
                    background = saveBtnBackground
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                }

                progressBar = native_measurement_included_btn.findViewById(R.id.progress_bar)

            }, {
                addMeasurementButton.setOnClickListener {
                    val measurementName = measurementValuesSpinner.selectedItem.toString().trim()
                    val measurementValue = dialogAddValueField.text.toString().trim()
                    val measurement = Measurement(measurementName, measurementValue)
                    if(measurementList.contains(measurement)){
                        requireActivity().gdToast("${measurement.name} already exist", Gravity.BOTTOM)
                    }
                    else{
                        measurementList.add(measurement)
                    }


                    native_measurement_rv.setupAdapter<Measurement>(R.layout.measurement_item) { adapter, context, list ->
                        bind { itemView, position, item ->
                            itemView.measurement_name.text = item?.name
                            itemView.measurement_value.text = item?.value
                            itemView.measurement_delete_btn.setOnClickListener {
                                val m = Measurement(item?.name.toString(), item?.value.toString())
                                if(measurementList.contains(m)){
                                    list?.removeAt(position)
                                    adapter.notifyDataSetChanged()
                                }
                                else{
                                    requireActivity().gdToast("Measurement not found", Gravity.BOTTOM)
                                }

                            }
                            itemView.setOnClickListener {
                                dialogTitle.text = getString(R.string.edit_measurement_str)
                                addMeasurementButton.apply {
                                    text = getString(R.string.update_str)
                                    background = addMeasurementBtnBackground
                                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                                }
                                dialog.show{
                                    cornerRadius(10F)
                                }
                                setUpSpinnerWithList(item?.name.toString(), measurementValuesSpinner, measurementValuesList)
                                addMeasurementButton.setOnClickListener {
                                    item?.value = dialogAddValueField.text.toString().trim()
                                    dialog.dismiss()
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                        setLayoutManager(GridLayoutManager(requireContext(), 3))
                        submitList(measurementList)
                    }
                    dialog.dismiss()
                }
                val client =  GlobalVariables.globalClient
                Log.i(title, "newclient $client")
                saveBtn.setOnClickListener {
                    measurementList.associateByTo(measurementValues, {
                        it.name
                    },{
                        it.value
                    })
                    val clientMeasurement = MeasurementValues(
                        client?.name.toString(),
                        "Type",
                        client?.id.toString(),
                        measurementValues
                    )
                    Log.i(title, "values $measurementValues")

                    val addMeasurementReq = authViewModel.addMeasurement(header, clientMeasurement)
                    requestObserver(progressBar, saveBtn, addMeasurementReq, false) { bool, result ->
                        onRequestResponseTask<ClientMeasurement>(bool, result) {
                            val res = result as UserResponse<ClientMeasurement>
                            requireActivity().gdToast("${res.message}", Gravity.BOTTOM)
//                                        listOfClient.toMutableList().removeAt(position)
                        }
                    }

                }
            })



            dialogAddNameField.textCountListener(addTextCountTv)
            dialogEditNameField.textCountListener(editTextCountTv)



        }


        fun EditText.textCountListener(tv: TextView) {
            this.doOnTextChanged { text, start, count, after ->
                if (text != null) {
                    tv.text = "${text.length}/15"
                }
                return@doOnTextChanged
            }
        }
}