package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.show
import kotlinx.android.synthetic.main.fragment_english_measurement.*
import kotlinx.android.synthetic.main.fragment_native_measurement.*

/**
 * A simple [Fragment] subclass.
 * Use the [EnglishMeasurementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnglishMeasurementFragment : Fragment() {
    private lateinit var saveBtn: Button
    private lateinit var progressBar: ProgressBar
    private val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.add_measurement_dialog_layout)
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
    private val dialogAddNameField by lazy{
        dialog.findViewById<TextInputEditText>(R.id.add_measurement_et)
    }
    private val dialogEditNameField by lazy{
        dialog.findViewById<TextInputEditText>(R.id.edit_measurement_et)
    }
    private val addTextCountTv by lazy{
        dialog.findViewById<TextView>(R.id.add_measurement_text_count_tv)
    }
    private val editTextCountTv by lazy{
        dialog.findViewById<TextView>(R.id.edit_measurement_text_count_tv)
    }
    lateinit var addMeasurementLayout:View
    lateinit var dialogTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_english_measurement, container, false)
    }

    override fun onResume() {
        super.onResume()
        addMeasurementLayout = dialog.findViewById(R.id.add_measurement_ll)
        dialogTitle = dialog.findViewById(R.id.dialog_title)
        val saveBtnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        saveBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor( requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        buttonTransactions({
            saveBtn = english_measurement_save_btn.findViewById(R.id.btn)
            progressBar = english_measurement_save_btn.findViewById(R.id.progress_bar)
        },{
            saveBtn.text = getString(R.string.save)
            saveBtn.background = saveBtnBackground
            saveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            addMeasurementButton.apply {
                text = getString(R.string.add)
                background = saveBtnBackground
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            EditMeasurementButton.apply{
                text = getString(R.string.save)
                background = saveBtnBackground
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
        })
        dialogAddNameField.textCountListener(addTextCountTv)
        dialogEditNameField.textCountListener(editTextCountTv)

        english_measurement_fab.setOnClickListener {
            dialogTitle.text = getString(R.string.add_measurement)
            addMeasurementLayout.show()
            dialog.show()
        }
    }
    fun EditText.textCountListener(tv:TextView){
        this.doOnTextChanged { text, start, count, after ->
            if (text != null) {
                tv.text = "${text.length}/50"
            }
            return@doOnTextChanged
        }
    }
}