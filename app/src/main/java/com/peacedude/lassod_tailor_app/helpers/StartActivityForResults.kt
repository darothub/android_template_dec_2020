package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.peacedude.lassod_tailor_app.model.request.User
import javax.inject.Inject

class StartActivityForResults @Inject constructor(private val registry: ActivityResultRegistry) :
    DefaultLifecycleObserver {
    private val title by lazy {
        getName()
    }
    lateinit var getIntentResult: ActivityResultLauncher<Intent>
    lateinit var getUserLiveData: MutableLiveData<User>
    lateinit var getResultLiveData: MutableLiveData<ActivityResult>
    override fun onCreate(owner: LifecycleOwner) {
        getUserLiveData = MutableLiveData()
        getResultLiveData = MutableLiveData()
        getIntentResult = registry.register(
            "key",
            owner,
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    getResultLiveData.value = result

                } else {
                    i(title, "OKCODE ${Activity.RESULT_OK} RESULTCODE ${result.resultCode}")
                }
            })
    }

    fun launchIntentToSignIn(intent: Intent, owner: LifecycleOwner, action:(User)->Unit):LiveData<User> {
        getIntentResult.launch(intent)
        getResultLiveData.observe(owner, Observer {result->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val account: GoogleSignInAccount? =
                        it.getResult(ApiException::class.java)

                    val email = account?.email
                    val lastName = account?.familyName
                    val firstName = account?.givenName
                    val otherName = account?.displayName
                    val imageUrl = account?.photoUrl
                    val idToken = account?.idToken
                    val newUser = User()
                    newUser.firstName = firstName
                    newUser.lastName = lastName
                    newUser.otherName = otherName
                    newUser.imageUrl = imageUrl.toString()
                    newUser.email = email
                    newUser.token = idToken

                    i(title, "Task is successful $email")
                    getUserLiveData.value = newUser
                    action(newUser)


                } else {
                    i(title, "Task not successful")
                }
            }
        })

        return getUserLiveData
    }

    fun launchImageIntent(intent: Intent):LiveData<ActivityResult>{
        getIntentResult.launch(intent)
        return getResultLiveData
    }
}
