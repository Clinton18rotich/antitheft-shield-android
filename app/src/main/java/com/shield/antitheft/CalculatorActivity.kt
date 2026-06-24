package com.shield.antitheft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.app.Activity

class CalculatorActivity : Activity() {
    private var input = ""
    private val SECRET_PIN = "7443" // SHIELD on dialpad
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // UI created programmatically // Will use programmatic layout
        createCalculatorUI()
    }
    
    private fun createCalculatorUI() {
        val display = TextView(this).apply {
            text = "0"
            textSize = 48f
            setPadding(32, 64, 32, 32)
        }
        
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(display)
        }
        
        val buttons = arrayOf(
            arrayOf("7","8","9","÷"),
            arrayOf("4","5","6","×"),
            arrayOf("1","2","3","-"),
            arrayOf("0",".","=","+")
        )
        
        for (row in buttons) {
            val rowLayout = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
            }
            for (label in row) {
                val btn = Button(this).apply {
                    text = label
                    textSize = 24f
                    layoutParams = android.widget.LinearLayout.LayoutParams(0, 200, 1f)
                    setOnClickListener {
                        if (label == "=") {
                            input += label
                            if (input.contains(SECRET_PIN)) {
                                // Secret PIN detected! Open real app
                                startActivity(Intent(this@CalculatorActivity, MainActivity::class.java))
                                finish()
                            } else {
                                try {
                                    // Evaluate simple math
                                    val result = evaluate(input.dropLast(1))
                                    display.text = result.toString()
                                    input = result.toString()
                                } catch (e: Exception) {
                                    display.text = "Error"
                                    input = ""
                                }
                            }
                        } else if (label == "C") {
                            input = ""
                            display.text = "0"
                        } else {
                            input += label
                            display.text = input.replace(SECRET_PIN, "****") // Hide PIN
                        }
                    }
                }
                rowLayout.addView(btn)
            }
            layout.addView(rowLayout)
        }
        
        setContentView(layout)
    }
    
    private fun evaluate(expr: String): Double {
        // Simple evaluation - replace operators
        val cleaned = expr.replace("×", "*").replace("÷", "/")
        // Very basic: just parse a+b, a-b, a*b, a/b
        val parts = cleaned.split(Regex("(?<=[+\\-*/])|(?=[+\\-*/])"))
        var result = parts[0].toDouble()
        var i = 1
        while (i < parts.size) {
            val op = parts[i]
            val num = parts[i+1].toDouble()
            result = when (op) {
                "+" -> result + num
                "-" -> result - num
                "*" -> result * num
                "/" -> result / num
                else -> result
            }
            i += 2
        }
        return result
    }
}
