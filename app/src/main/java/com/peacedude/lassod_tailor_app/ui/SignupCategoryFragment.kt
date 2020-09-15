package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.goto
import com.peacedude.lassod_tailor_app.helpers.setupCategorySpinner
import com.peacedude.lassod_tailor_app.model.request.User
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup_category.*


/**
 * A simple [Fragment] subclass.
 * Use the [SignupCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupCategoryFragment : Fragment() {
    val title = getName()
    private lateinit var continueBtn: Button
    val arg:SignupCategoryFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_category, container, false)
    }

    override fun onResume() {
        super.onResume()
        setupCategorySpinner(requireContext(), signup_category_spinner, R.array.categories_array)
        val backgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_outline)

        buttonTransactions({
            continueBtn = signup_category_cont_btn.findViewById(R.id.btn)
            continueBtn.text = getString(R.string.continue_text)
            continueBtn.background = backgroundDrawable

        }, {

            continueBtn.setOnClickListener {
                val category = signup_category_spinner.selectedItem.toString()
                if(arg != null){

                    val action = SignupCategoryFragmentDirections.actionSignupCategoryFragmentToEmailSignupFragment()
                    action.newUser = arg.newUser
                    Log.i(title, "category $category")
                    goto(action)
                }
                else{
                    val action = SignupCategoryFragmentDirections.actionSignupCategoryFragmentToEmailSignupFragment()
                    action.category = category
                    Log.i(title, "category $category")
                    goto(action)
                }

            }
        })
    }

}