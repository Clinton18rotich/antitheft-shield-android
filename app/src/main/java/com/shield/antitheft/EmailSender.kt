package com.shield.antitheft

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

class EmailSender(private val context: Context) {
    
    fun sendAlert(to: String, subject: String, body: String, attachment: File? = null) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
                
                if (attachment != null && attachment.exists()) {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        attachment
                    )
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }
            
            // Try Gmail first, then any email app
            intent.setPackage("com.google.android.gm")
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                intent.setPackage(null)
                context.startActivity(Intent.createChooser(intent, "Send Alert"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show()
        }
    }
    
    fun sendEmergencyAlert(location: String, photoFile: File?) {
        val body = """
            🚨 ANTI-THEFT ALERT
            ═══════════════
            Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}
            Location: $location
            Device: ${android.os.Build.MODEL}
            
            Evidence attached below.
            ---
            Sent by AntiTheft Shield
        """.trimIndent()
        
        sendAlert("emergency@gmail.com", "🚨 URGENT: Device Theft Alert", body, photoFile)
    }
}
