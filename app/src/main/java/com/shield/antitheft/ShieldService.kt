package com.shield.antitheft

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ShieldService : Service() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(9999, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Background tasks:
        // - GPS tracking every 5 min
        // - Check for SIM changes
        // - Monitor battery level
        // - Heartbeat to server
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "shield_service",
                "Shield Protection",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "AntiTheft Shield is protecting your device"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "shield_service")
            .setContentTitle("System Services")
            .setContentText("Android system running")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
    }
}
