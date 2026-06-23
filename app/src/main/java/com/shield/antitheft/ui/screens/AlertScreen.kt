package com.shield.antitheft.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class AlertConfig(val title: String, val desc: String, val icon: String, val color: Color, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(navController: NavController) {
    val context = LocalContext.current
    
    val configs = listOf(
        AlertConfig("Email Alert Template", "Customizable email template with dynamic fields for theft alerts", "📧", Color(0xFF22D3EE),
            "Subject: [LEVEL] AntiTheft Alert - [EVENT]\n\nTime: [TIMESTAMP]\nDevice: [DEVICE_MODEL]\nLocation: [GPS_LINK]\nEvent: [DETAILS]\n\nEvidence Attached:\n[EVIDENCE_LIST]\n\n---\nSent by AntiTheft Shield"),
        AlertConfig("Escalation Chain", "Try email first, then SMS, then Telegram - never miss an alert", "🔔", Color(0xFFFBBF24),
            "fun sendAlert(message: String, evidence: File) {\n    try { sendEmail(\"emergency@gmail.com\", message, evidence) }\n    catch (e: Exception) {\n        try { sendSMS(\"+254700000000\", message) }\n        catch (e2: Exception) {\n            sendTelegram(\"@emergency_chat\", message)\n        }\n    }\n}"),
        AlertConfig("Scheduled Digest", "Send summary report every 6 hours with all evidence collected", "⏰", Color(0xFF34D399),
            "AlarmManager.setInexactRepeating(\n    ELAPSED_REALTIME,\n    SystemClock.elapsedRealtime(),\n    6 * 3600 * 1000,\n    digestPendingIntent)\n\nfun sendDigest() {\n    val summary = \"6-HOUR DIGEST\\nPhotos: \$photoCount\\nLocations: \$locCount\"\n    sendEmail(\"DIGEST\", summary)\n}"),
        AlertConfig("Multi-Recipient Alerts", "Send alerts to multiple emails and phones simultaneously", "📨", Color(0xFFA78BFA),
            "val recipients = listOf(\n    \"emergency@gmail.com\",\n    \"police@kenya.go.ke\",\n    \"backup@gmail.com\"\n)\n\nrecipients.forEach { email ->\n    sendEmail(email, subject, body, evidence)\n}"),
        AlertConfig("Silent vs Loud Alerts", "Choose between silent tracking or loud alarm based on situation", "🚨", Color(0xFFF87171),
            "enum class AlertMode { SILENT, LOUD }\n\nfun triggerAlert(mode: AlertMode) {\n    when (mode) {\n        SILENT -> { capturePhoto(); sendEmail() }\n        LOUD -> { lockDevice(); soundAlarm(); flashSOS() }\n    }\n}")
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Alert Configuration", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
            items(configs) { config ->
                var expanded by remember { mutableStateOf(false) }
                var codeExpanded by remember { mutableStateOf(false) }
                
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { expanded = !expanded },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(config.icon, fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(config.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = config.color)
                                Text(config.desc, fontSize = 11.sp, color = Color.Gray)
                            }
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                        }
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Surface(modifier = Modifier.fillMaxWidth().clickable { codeExpanded = !codeExpanded },
                                    color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Code, null, tint = config.color, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text("Implementation Code", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                            TextButton(onClick = { codeExpanded = !codeExpanded }) { Text(if (codeExpanded) "Hide" else "View", fontSize = 10.sp, color = config.color) }
                                        }
                                        AnimatedVisibility(visible = codeExpanded) {
                                            Text(config.code, fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(top = 8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
