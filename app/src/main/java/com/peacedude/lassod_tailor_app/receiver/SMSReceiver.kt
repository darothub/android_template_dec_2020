package com.peacedude.lassod_tailor_app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.EditText

class SMSReceiver : BroadcastReceiver() {

    companion object {
        lateinit var editText: EditText
    }

    fun setEditText(editText: EditText) {
        SMSReceiver.editText = editText
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in msgs) {
            val msgBody = sms.messageBody
            val otp = msgBody.split(" ").last()
            editText.setText(otp)
        }
    }
}
