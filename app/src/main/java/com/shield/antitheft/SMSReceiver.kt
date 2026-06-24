package com.shield.antitheft

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<*>
                for (pdu in pdus) {
                    val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SmsMessage.createFromPdu(pdu as ByteArray, "3gpp")
                    } else {
                        SmsMessage.createFromPdu(pdu as ByteArray)
                    }
                    val sender = message.originatingAddress
                    val body = message.messageBody
                    
                    // Secret SMS commands
                    when {
                        body?.contains("SHIELD:LOCK") == true -> {
                            // Lock device
                            Toast.makeText(context, "🔒 Remote lock command received", Toast.LENGTH_LONG).show()
                        }
                        body?.contains("SHIELD:LOCATE") == true -> {
                            // Send location
                            Toast.makeText(context, "📍 Location request received", Toast.LENGTH_LONG).show()
                        }
                        body?.contains("SHIELD:PHOTO") == true -> {
                            // Capture photo
                            Toast.makeText(context, "📸 Photo capture command received", Toast.LENGTH_LONG).show()
                        }
                        body?.contains("SHIELD:WIPE") == true -> {
                            // Wipe data
                            Toast.makeText(context, "🗑️ Remote wipe command received", Toast.LENGTH_LONG).show()
                        }
                        body?.contains("SHIELD:ALARM") == true -> {
                            // Sound alarm
                            Toast.makeText(context, "🚨 Alarm command received", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
