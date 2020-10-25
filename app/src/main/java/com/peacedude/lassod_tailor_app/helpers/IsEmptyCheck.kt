import android.view.Gravity
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.hide
import com.peacedude.lassod_tailor_app.helpers.show

class IsEmptyCheck {

    companion object {
        operator fun invoke(vararg edits: EditText): EditText? {
            for (edit in edits) {
                if (edit.text.isEmpty()) {
                    return edit
                }
            }
            return null
        }

        /**
         * A function to validate email and password fields
         * @param email is a type of string
         * @param password is a type of string
         */
        fun fieldsValidation(email: String?=null, password: String?=null, phone_number:String?=null): String? {
            val emailPattern = Regex("""^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*${'$'}""")
            val passwordPattern = Regex("""^[a-zA-Z0-9@$!.%*#?&]{6,}$""")
            val phonePattern = Regex("""\d{13}""")
            var message:String? = ""
            when {
                email != null && password != null && phone_number != null  -> {
                    val matchedEmail = emailPattern.matches(email)
                    val matchedPassword = passwordPattern.matches(password)
                    val matchedPhone = phonePattern.matches(phone_number)
                    message = when {
                        !matchedEmail -> email
                        !matchedPassword -> password
                        !matchedPhone -> phone_number
                        else -> null
                    }
                }
                phone_number != null ->{
                    val matchedPhone = phonePattern.matches(phone_number)
                    message = when {
                        !matchedPhone -> phone_number
                        else -> null
                    }
                }
                email != null ->{
                    val matchedEmail = emailPattern.matches(email.toString())
                    message = when {
                        !matchedEmail -> email
                        else -> null
                    }
                }
                password != null ->{
                    val matchedPassword = passwordPattern.matches(password)
                    message = when {
                        !matchedPassword -> password
                        else -> null
                    }
                }

            }

            return message
        }

    }
}
//fun Fragment.revealEmptyEditText(editText: EditText): Boolean {
//    val emptyEditText = IsEmptyCheck(editText)
//    return when {
//        emptyEditText != null ->{
//            emptyEditText.error = getString(R.string.field_required)
//            val editTextName = emptyEditText.tag
//            requireActivity().gdErrorToast("$editTextName is empty", Gravity.BOTTOM)
//            false
//        }
//        else -> true
//    }
//}
fun validatePasswordAndAdvise(text:CharSequence, view: View) {
    val passwordPattern = Regex("""^[a-zA-Z0-9@$!%*#?&]{6,}$""")
    val matchedPassword = passwordPattern.matches(text)
    if (!matchedPassword) {
        view.show()
    } else {
        view.hide()
    }
}