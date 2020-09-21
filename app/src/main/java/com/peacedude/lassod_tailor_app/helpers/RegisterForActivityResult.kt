package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.Gravity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.ui.SignupChoicesFragmentArgs
import com.peacedude.lassod_tailor_app.ui.SignupChoicesFragmentDirections

class RegisterForActivityResult(
    private val registry: ActivityResultRegistry,
    activity: Activity
) : LifecycleObserver {

    val title by lazy {
        getName()
    }
    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.client_id))
            .requestEmail()
            .build()
    }
    private val mGoogleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            activity,
            gso
        )
    }

    lateinit var getContent: ActivityResultLauncher<Intent>



    companion object {
        @Volatile private var instance: RegisterForActivityResult? = null
        fun getInstance(registry: ActivityResultRegistry, activity: Activity): RegisterForActivityResult {
            val checkInstance = instance
            if (checkInstance != null) {
                return checkInstance
            }

            return synchronized(this) {
                val checkInstanceAgain = instance
                if (checkInstanceAgain != null) {
                    checkInstanceAgain
                } else {
                    val created = RegisterForActivityResult(registry, activity)
                    instance = created
                    created
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner):LiveData<GoogleSignInAccount?> {
        val responseLiveData = MutableLiveData<GoogleSignInAccount?>()
        getContent = registry.register(
            "Hello",
            owner,
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    task.addOnCompleteListener {
                        if (it.isSuccessful) {
                            val account: GoogleSignInAccount? =
                                it.getResult(ApiException::class.java)
                            responseLiveData.value = account

//                            val idToken = it.result?.idToken
//                            val email = account?.email
//                            val lastName = account?.familyName
//                            val firstName = account?.givenName
//                            val otherName = account?.displayName
//                            val imageUrl = account?.photoUrl
//                            val category = arg?.category
//                            val newUser = User(firstName, lastName, otherName, category, null)
//                            newUser.email = email
//                            newUser.token = idToken
//                            (owner as? Activity)?.gdToast(
//                                "Authentication successful",
//                                Gravity.BOTTOM
//                            )
//                            val action =
//                                SignupChoicesFragmentDirections.actionSignupChoicesFragmentToEmailSignupFragment()
//                            action.newUser = newUser
//                            (owner as? Fragment)?.goto(action)
//
//                            Log.i(title, "token $firstName")
//                           requireActivity().startActivity(Intent(requireActivity(), ProfileActivity::class.java))
                        } else {
                            (owner as? Activity)?.gdToast(
                                "Authentication Unsuccessful",
                                Gravity.BOTTOM
                            )
                            Log.i(title, "Task not successful")
                        }
                    }
                } else {
                    Log.i(title, "OKCODE ${Activity.RESULT_OK} RESULTCODE ${result.resultCode}")
                }
            })
        signInWithGoogle()
        return responseLiveData
    }

    fun signInWithGoogle() {
        val intent = mGoogleSignInClient.signInIntent
        getContent.launch(intent)
    }
}