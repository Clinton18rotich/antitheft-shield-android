package com.shield.antitheft

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras ?: return
            val pdus = bundle["pdus"] as Array<*> ?: return
            
            for (pdu in pdus) {
                val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(pdu as ByteArray, "3gpp")
                } else {
                    SmsMessage.createFromPdu(pdu as ByteArray)
                }
                val body = message.messageBody ?: continue
                
                val controller = ShieldController(context)
                
                when {
                    body.contains("SHIELD:LOCK") -> controller.executeCommand("LOCK")
                    body.contains("SHIELD:LOCATE") -> controller.executeCommand("LOCATION")
                    body.contains("SHIELD:PHOTO") -> controller.executeCommand("PHOTO")
                    body.contains("SHIELD:WIPE") -> controller.executeCommand("DATA_WIPE")
                    body.contains("SHIELD:ALARM") -> controller.executeCommand("ALARM")
                    body.contains("SHIELD:SHUTDOWN") -> controller.executeCommand("FAKE_SHUTDOWN")
                    body.contains("SHIELD:INTRUDER") -> controller.executeCommand("INTRUDER_PHOTO")
                }
            }
        }
    }
}
