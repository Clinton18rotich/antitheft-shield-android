package com.shield.antitheft

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

class SIMChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.SIM_STATE_CHANGED") {
            
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simSerial = tm.simSerialNumber
            val subscriberId = tm.subscriberId
            
            // Check against stored values
            val prefs = context.getSharedPreferences("shield_prefs", Context.MODE_PRIVATE)
            val storedSim = prefs.getString("sim_serial", null)
            val storedSub = prefs.getString("subscriber_id", null)
            
            if (storedSim == null) {
                // First run - store current SIM
                prefs.edit().putString("sim_serial", simSerial).putString("subscriber_id", subscriberId).apply()
            } else if (storedSim != simSerial) {
                // SIM CHANGED! Thief swapped SIM
                Toast.makeText(context, "⚠️ SIM CARD CHANGED! Alerting owner...", Toast.LENGTH_LONG).show()
                // Trigger alert: take photo, send location, email owner
            }
        }
    }
}
