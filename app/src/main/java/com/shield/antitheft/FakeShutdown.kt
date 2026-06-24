package com.shield.antitheft

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

class FakeShutdown(private val context: Context) {
    private var overlay: View? = null
    
    fun show() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        val blackView = View(context).apply {
            setBackgroundColor(Color.BLACK)
            setOnClickListener {
                // Long press to reveal
                var count = 0
                setOnLongClickListener {
                    count++
                    if (count >= 3) {
                        hide()
                    }
                    true
                }
            }
        }
        
        val params = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            type = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            format = PixelFormat.TRANSLUCENT
        }
        
        wm.addView(blackView, params)
        overlay = blackView
        
        // Remove FLAG_NOT_TOUCHABLE after 2 seconds (looks like shutdown)
        android.os.Handler().postDelayed({
            params.flags = params.flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
            wm.updateViewLayout(blackView, params)
        }, 2000)
    }
    
    fun hide() {
        overlay?.let {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.removeView(it)
            overlay = null
        }
    }
}
