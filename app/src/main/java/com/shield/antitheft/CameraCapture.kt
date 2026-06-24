package com.shield.antitheft

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class CameraCapture(private val context: Context) {
    private var cameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private var backgroundThread: HandlerThread
    private var backgroundHandler: Handler
    
    init {
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread.looper)
    }
    
    fun captureSilentPhoto(callback: (File?) -> Unit) {
        try {
            val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager.getCameraCharacteristics(id)
                    .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
            } ?: cameraManager.cameraIdList.firstOrNull() ?: return
            
            val file = File(context.externalCacheDir, "shield_${System.currentTimeMillis()}.jpg")
            val reader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
            
            reader.setOnImageAvailableListener({ r ->
                val image = r.acquireLatestImage()
                if (image != null) {
                    val buffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    FileOutputStream(file).use { it.write(bytes) }
                    image.close()
                    callback(file)
                } else {
                    callback(null)
                }
            }, backgroundHandler)
            
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    camera.createCaptureSession(listOf(reader.surface),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                val request = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE).apply {
                                    addTarget(reader.surface)
                                    set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF)
                                    set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
                                }
                                session.capture(request.build(), null, backgroundHandler)
                            }
                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                camera.close()
                                callback(null)
                            }
                        }, backgroundHandler)
                }
                override fun onDisconnected(camera: CameraDevice) { camera.close() }
                override fun onError(camera: CameraDevice, error: Int) { camera.close(); callback(null) }
            }, backgroundHandler)
        } catch (e: Exception) {
            Log.e("Shield", "Camera capture failed", e)
            callback(null)
        }
    }
    
    fun cleanup() {
        cameraDevice?.close()
        backgroundThread.quitSafely()
    }
}
