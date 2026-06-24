package com.shield.antitheft

import android.content.Context
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import java.io.File
import java.io.FileOutputStream

class IntruderCapture(private val context: Context) {
    private val handlerThread = HandlerThread("IntruderCam").apply { start() }
    private val handler = Handler(handlerThread.looper)
    
    fun captureIntruder(callback: (File?) -> Unit) {
        try {
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val frontCam = manager.cameraIdList.firstOrNull { id ->
                manager.getCameraCharacteristics(id).get(CameraCharacteristics.LENS_FACING) == 
                    CameraCharacteristics.LENS_FACING_FRONT
            } ?: return
            
            val file = File(context.externalCacheDir, "intruder_${System.currentTimeMillis()}.jpg")
            val reader = ImageReader.newInstance(1080, 1920, android.graphics.ImageFormat.JPEG, 1)
            
            reader.setOnImageAvailableListener({ r ->
                val img = r.acquireLatestImage()
                if (img != null) {
                    val buf = img.planes[0].buffer
                    val bytes = ByteArray(buf.remaining())
                    buf.get(bytes)
                    FileOutputStream(file).use { it.write(bytes) }
                    img.close()
                    callback(file)
                }
            }, handler)
            
            manager.openCamera(frontCam, object : CameraDevice.StateCallback() {
                override fun onOpened(cam: CameraDevice) {
                    cam.createCaptureSession(listOf(reader.surface),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(s: CameraCaptureSession) {
                                val req = cam.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE).apply {
                                    addTarget(reader.surface)
                                    set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF)
                                }
                                s.capture(req.build(), null, handler)
                            }
                            override fun onConfigureFailed(s: CameraCaptureSession) {
                                cam.close()
                                callback(null)
                            }
                        }, handler)
                }
                override fun onDisconnected(cam: CameraDevice) { cam.close() }
                override fun onError(cam: CameraDevice, e: Int) { cam.close(); callback(null) }
            }, handler)
        } catch (e: Exception) {
            callback(null)
        }
    }
}
