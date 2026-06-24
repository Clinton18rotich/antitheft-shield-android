package com.shield.antitheft

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class AlarmManager(private val context: Context) {
    
    fun soundAlarm() {
        try {
            // Max volume
            val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audio.setStreamVolume(AudioManager.STREAM_ALARM, audio.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0)
            audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), 0)
            
            // Play alarm
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val ringtone = RingtoneManager.getRingtone(context, alarmUri)
            ringtone.play()
            
            // Flash torch
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val camManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                val cameraId = camManager.cameraIdList.firstOrNull() ?: return
                camManager.setTorchMode(cameraId, true)
                
                // Turn off after 30 seconds
                android.os.Handler().postDelayed({
                    camManager.setTorchMode(cameraId, false)
                }, 30000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun vibrateSOS() {
        val pattern = longArrayOf(0, 200, 200, 200, 200, 600, 600, 200, 600, 200, 600, 200)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        }
    }
    
    fun stopAlarm() {
        // Stop ringtone
        val ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        ringtone.stop()
        
        // Stop torch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val camManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            camManager.cameraIdList.forEach { id ->
                camManager.setTorchMode(id, false)
            }
        }
    }
}
