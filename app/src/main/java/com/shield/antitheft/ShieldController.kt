package com.shield.antitheft

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.File

class ShieldController(private val context: Context) {
    private val camera = CameraCapture(context)
    private val gps = GPSTracker(context)
    private val email = EmailSender(context)
    private val alarm = AlarmManager(context)
    private val intruder = IntruderCapture(context)
    private val shutdown = FakeShutdown(context)
    private val pdfReport = PDFReport(context)
    private val handler = Handler(Looper.getMainLooper())
    private val capturedPhotos = mutableListOf<File>()
    
    private val prefs = context.getSharedPreferences("shield_prefs", Context.MODE_PRIVATE)
    private val emergencyEmail: String get() = prefs.getString("emergency_email", "emergency@gmail.com") ?: "emergency@gmail.com"
    private val emergencyPhone: String get() = prefs.getString("emergency_phone", "") ?: ""

    fun executeCommand(command: String) {
        Toast.makeText(context, "⚡ Executing: $command", Toast.LENGTH_SHORT).show()
        
        when (command) {
            "PHOTO", "BURST_PHOTO", "REAR_PHOTO", "NIGHT_PHOTO", "FLASH_PHOTO" -> {
                camera.captureSilentPhoto { file ->
                    if (file != null) {
                        capturedPhotos.add(file)
                        gps.getCurrentLocation { location ->
                            val mapsLink = if (location != null) gps.getMapsLink(location) else "Unknown"
                            val pdf = pdfReport.generateTheftReport(capturedPhotos, location, command, android.os.Build.MODEL)
                            
                            // Send PDF via WhatsApp
                            pdfReport.shareToWhatsApp(pdf, "🚨 Shield Alert: $command\nLocation: $mapsLink\nDevice: ${android.os.Build.MODEL}")
                            
                            // Also send via Email
                            email.sendAlert(emergencyEmail, "📸 Shield Photo - $command", "Location: $mapsLink", file)
                            
                            Toast.makeText(context, "📸 Photo + PDF sent to WhatsApp & Email", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            
            "LOCATION", "TRACK_START" -> {
                gps.getCurrentLocation { location ->
                    if (location != null) {
                        val mapsLink = gps.getMapsLink(location)
                        val pdf = pdfReport.generateTheftReport(capturedPhotos, location, "Location Request", android.os.Build.MODEL)
                        
                        pdfReport.shareToWhatsApp(pdf, "📍 Shield Location\n$mapsLink")
                        email.sendAlert(emergencyEmail, "📍 Shield Location", "Maps: $mapsLink")
                        
                        if (emergencyPhone.isNotEmpty()) {
                            sendSMS(emergencyPhone, "📍 Shield: $mapsLink")
                        }
                        Toast.makeText(context, "📍 Location sent", Toast.LENGTH_LONG).show()
                    }
                }
            }
            
            "LOCK" -> {
                val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? android.app.admin.DevicePolicyManager
                dpm?.lockNow()
                Toast.makeText(context, "🔒 Device locked", Toast.LENGTH_SHORT).show()
            }
            
            "ALARM" -> {
                alarm.soundAlarm()
                Toast.makeText(context, "🚨 Alarm activated!", Toast.LENGTH_LONG).show()
            }
            
            "SOS_FLASH" -> {
                alarm.soundAlarm()
                alarm.vibrateSOS()
                Toast.makeText(context, "💡 SOS signal activated", Toast.LENGTH_SHORT).show()
            }
            
            "INTRUDER_PHOTO" -> {
                intruder.captureIntruder { file ->
                    if (file != null) {
                        capturedPhotos.add(file)
                        gps.getCurrentLocation { location ->
                            val pdf = pdfReport.generateTheftReport(capturedPhotos, location, "Intruder Alert", android.os.Build.MODEL)
                            pdfReport.shareToWhatsApp(pdf, "🤳 Intruder detected! Photos attached.")
                            email.sendEmergencyAlert("Intruder detected at ${location?.let { gps.getMapsLink(it) } ?: "Unknown"}", file)
                        }
                    }
                }
            }
            
            "FAKE_SHUTDOWN" -> shutdown.show()
            "CALCULATOR_MODE" -> {
                context.startActivity(Intent(context, CalculatorActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
            }
            "DATA_WIPE" -> {
                capturedPhotos.forEach { it.delete() }
                capturedPhotos.clear()
                File(context.externalCacheDir, "shield_evidence").listFiles()?.forEach { it.delete() }
                Toast.makeText(context, "🗑️ Evidence wiped", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    fun onTheftDetected(reason: String) {
        camera.captureSilentPhoto { photo ->
            if (photo != null) capturedPhotos.add(photo)
            gps.getCurrentLocation { location ->
                val mapsLink = if (location != null) gps.getMapsLink(location) else "Unknown"
                val pdf = pdfReport.generateTheftReport(capturedPhotos, location, reason, android.os.Build.MODEL)
                
                pdfReport.shareToWhatsApp(pdf, "🚨 THEFT DETECTED: $reason\n$mapsLink")
                email.sendAlert(emergencyEmail, "🚨 THEFT: $reason", "Location: $mapsLink\nDevice: ${android.os.Build.MODEL}", photo)
                
                if (emergencyPhone.isNotEmpty()) sendSMS(emergencyPhone, "🚨 THEFT: $reason - $mapsLink")
                alarm.soundAlarm()
            }
        }
    }
    
    fun onSIMChanged() {
        gps.getCurrentLocation { location ->
            val mapsLink = if (location != null) gps.getMapsLink(location) else "Unknown"
            val pdf = pdfReport.generateTheftReport(capturedPhotos, location, "SIM Card Changed", android.os.Build.MODEL)
            pdfReport.shareToWhatsApp(pdf, "⚠️ SIM CARD CHANGED!\n$mapsLink")
            email.sendAlert(emergencyEmail, "⚠️ SIM Changed!", "Location: $mapsLink")
        }
    }
    
    fun onWrongPassword() {
        intruder.captureIntruder { file ->
            if (file != null) capturedPhotos.add(file)
            gps.getCurrentLocation { location ->
                val pdf = pdfReport.generateTheftReport(capturedPhotos, location, "Wrong Password", android.os.Build.MODEL)
                pdfReport.shareToWhatsApp(pdf, "🤳 Someone tried to unlock your device!")
            }
        }
    }
    
    private fun sendSMS(phone: String, message: String) {
        try { android.telephony.SmsManager.getDefault().sendTextMessage(phone, null, message, null, null) }
        catch (e: Exception) { e.printStackTrace() }
    }
}
