package com.shield.antitheft

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class SIMChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.SIM_STATE_CHANGED") {
            
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simSerial = tm.simSerialNumber ?: return
            val prefs = context.getSharedPreferences("shield_prefs", Context.MODE_PRIVATE)
            val storedSim = prefs.getString("sim_serial", null)
            
            if (storedSim == null) {
                prefs.edit().putString("sim_serial", simSerial).apply()
            } else if (storedSim != simSerial) {
                val controller = ShieldController(context)
                controller.onSIMChanged()
            }
        }
    }
}
