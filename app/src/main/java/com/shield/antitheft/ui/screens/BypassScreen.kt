package com.shield.antitheft.ui.screens
import android.os.Build

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class BypassWorkaround(val title: String, val desc: String, val code: String)
data class BypassTechnique(val restriction: String, val problem: String, val icon: String, val color: Color, val workarounds: List<BypassWorkaround>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BypassScreen(navController: NavController) {
    val techniques = listOf(
        BypassTechnique(
            "SMS Sending/Receiving Blocked",
            "Android 14+ blocks background SMS for all apps",
            "💬", Color(0xFFF87171),
            listOf(
                BypassWorkaround("Email (SMTP) as Primary Channel", "Replace ALL SMS with JavaMail SMTP. Works on every version.",
                    "val session = Session.getInstance(props, Authenticator())\nval msg = MimeMessage(session)\nmsg.setFrom(InternetAddress(\"app@gmail.com\"))\nmsg.addRecipient(TO, InternetAddress(\"emergency@gmail.com\"))\nmsg.subject = \"ALERT\"\nmsg.setText(\"Location: \" + mapsLink)\nTransport.send(msg)"),
                BypassWorkaround("Firebase Cloud Messaging (FCM)", "Push notifications replace SMS. Free, instant, works background.",
                    "val message = RemoteMessage.Builder(\"token\")\n    .setData(mapOf(\"alert\" to \"theft\", \"lat\" to loc.lat))\n    .build()\nFirebaseMessaging.getInstance().send(message)"),
                BypassWorkaround("WebSocket Persistent Connection", "Bidirectional real-time commands without SMS.",
                    "val ws = OkHttpClient().newWebSocket(\n    Request.Builder().url(\"wss://server.com/ws\").build(),\n    object : WebSocketListener() {\n        override fun onMessage(ws: WebSocket, text: String) {\n            executeCommand(text)\n        }\n    })"),
                BypassWorkaround("Notification Listener", "Read thief WhatsApp notifications to gather intel.",
                    "class NotifListener : NotificationListenerService() {\n    override fun onNotificationPosted(srn: StatusBarNotification) {\n        val text = srn.notification.extras.getString(\"android.text\")\n        sendEmail(\"Notification: \" + text)\n    }\n}"),
                BypassWorkaround("MQTT Lightweight Protocol", "IoT protocol for battery-efficient communication.",
                    "val mqtt = MqttAndroidClient(ctx, \"tcp://broker:1883\", deviceId)\nmqtt.connect()\nmqtt.subscribe(\"cmd/\" + deviceId, 1) { topic, msg ->\n    executeCommand(String(msg.payload))\n}")
            )
        ),
        BypassTechnique(
            "Background Camera Blocked",
            "Camera requires foreground service with notification",
            "📸", Color(0xFF22D3EE),
            listOf(
                BypassWorkaround("Disguised Foreground Notification", "Show 'System Services' notification while capturing.",
                    "val notification = NotificationCompat.Builder(this, CH)\n    .setContentTitle(\"System Services\")\n    .setContentText(\"Android system running\")\n    .setSmallIcon(R.drawable.ic_system)\n    .setOngoing(true)\n    .build()\nstartForeground(9999, notification)\n// Camera works with disguised notification"),
                BypassWorkaround("Screen Capture via MediaProjection", "Capture what thief is doing on screen.",
                    "val mp = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager\nstartActivityForResult(mp.createScreenCaptureIntent(), REQ)\n// Captures: passwords, messages, apps used"),
                BypassWorkaround("WorkManager Periodic Photo", "Schedule photos every 15min when device is charging.",
                    "val constraints = Constraints.Builder()\n    .setRequiresCharging(true)\n    .build()\nval work = PeriodicWorkRequestBuilder<PhotoWorker>(15, TimeUnit.MINUTES)\n    .setConstraints(constraints)\n    .build()\nWorkManager.getInstance(ctx).enqueue(work)"),
                BypassWorkaround("Quick Settings Tile Camera", "Add camera trigger to Quick Settings panel.",
                    "class CameraTileService : TileService() {\n    override fun onClick() {\n        unlockAndRun { captureSilentPhoto() }\n    }\n}")
            )
        ),
        BypassTechnique(
            "Continuous Background Location",
            "GPS throttled to 30-min intervals in background",
            "📍", Color(0xFF34D399),
            listOf(
                BypassWorkaround("Foreground Navigation Service", "Disguise as navigation app for unlimited GPS.",
                    "val notification = NotificationCompat.Builder(this, CH)\n    .setContentTitle(\"Navigation\")\n    .setContentText(\"Calculating route...\")\n    .setSmallIcon(R.drawable.ic_nav)\n    .setOngoing(true)\n    .build()\nstartForeground(1001, notification)\nlm.requestLocationUpdates(GPS, 5000L, 0f, listener)"),
                BypassWorkaround("Sensor Dead Reckoning", "Track movement using accelerometer + gyroscope.",
                    "heading += gyro[2] * dt\nsteps = detectSteps(accel)\ndistance = steps * 0.75f\nlat += distance * cos(heading) / 111320.0\nlng += distance * sin(heading) / (111320 * cos(lat))"),
                BypassWorkaround("WiFi RSSI Triangulation", "Get location from WiFi signals without GPS permission.",
                    "val results = wifiManager.scanResults\nresults.forEach { ap ->\n    val dist = 10.0.pow((27.55 - 20*log10(ap.frequency) + abs(ap.level)) / 20)\n    // Trilaterate from 3+ APs\n}"),
                BypassWorkaround("Cell Tower Triangulation", "Use cell tower signals for coarse location.",
                    "val cells = telephonyManager.allCellInfo\ncells.forEach { cell ->\n    if (cell is CellInfoLte) {\n        val tac = cell.cellIdentity.tac\n        val ci = cell.cellIdentity.ci\n        lookupTowerLocation(tac, ci)\n    }\n}"),
                BypassWorkaround("Bluetooth Beacon Positioning", "Use BLE beacons for indoor location.",
                    "val scanner = btAdapter.bluetoothLeScanner\nscanner.startScan { result, _ ->\n    val rssi = result.rssi\n    val dist = 10.0.pow((-69 - rssi) / 20.0)\n}")
            )
        ),
        BypassTechnique(
            "IMEI Access Blocked",
            "Android 10+ blocks IMEI access for apps",
            "📱", Color(0xFFA78BFA),
            listOf(
                BypassWorkaround("Settings.Global Device ID", "Get device identifier from Settings.",
                    "val deviceId = Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME)\nval androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)"),
                BypassWorkaround("Advertising ID", "Use Google Advertising ID as device identifier.",
                    "val advId = AdvertisingIdClient.getAdvertisingIdInfo(context).id"),
                BypassWorkaround("SpoofCheck Fingerprint", "Generate unique fingerprint from device properties.",
                    "val fingerprint = \"${Build.MODEL}-${Build.SERIAL}-${Build.HARDWARE}\".hashCode().toString()")
            )
        ),
        BypassTechnique(
            "Background Audio Recording",
            "Background audio capture limited to 5 minutes",
            "🎤", Color(0xFFFBBF24),
            listOf(
                BypassWorkaround("Foreground Audio Service", "Use media playback service to disguise recording.",
                    "val notification = NotificationCompat.Builder(this, CH)\n    .setContentTitle(\"Media Player\")\n    .setContentText(\"Playing...\")\n    .setSmallIcon(R.drawable.ic_music)\n    .setOngoing(true).build()\nstartForeground(2001, notification)\nrecorder.start()"),
                BypassWorkaround("Chunked Recording", "Record in 4.5-min chunks, restart automatically.",
                    "fun startChunkedRecording() {\n    recorder.setMaxDuration(270000) // 4.5 min\n    recorder.setOnInfoListener { _, what, _ ->\n        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {\n            saveAndRestart()\n        }\n    }\n}")
            )
        ),
        BypassTechnique(
            "Storage Access (Scoped)",
            "Android 11+ limits file system access",
            "💾", Color(0xFF8B5CF6),
            listOf(
                BypassWorkaround("MANAGE_EXTERNAL_STORAGE Permission", "Request full storage access.",
                    "if (Build.VERSION.SDK_INT >= 30) {\n    if (!Environment.isExternalStorageManager()) {\n        startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))\n    }\n}"),
                BypassWorkaround("App-Specific Directory", "Use app's own directory for evidence storage.",
                    "val evidenceDir = File(getExternalFilesDir(null), \"evidence\")\nif (!evidenceDir.exists()) evidenceDir.mkdirs()\nval photoFile = File(evidenceDir, \"thief_${System.currentTimeMillis()}.jpg\")")
            )
        ),
        BypassTechnique(
            "Background Execution Limits",
            "Android 8+ limits background services",
            "⚡", Color(0xFFFB923C),
            listOf(
                BypassWorkaround("WorkManager for Guaranteed Execution", "Use WorkManager for reliable background work.",
                    "val work = OneTimeWorkRequestBuilder<TheftCheckWorker>()\n    .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())\n    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)\n    .build()\nWorkManager.getInstance(ctx).enqueue(work)"),
                BypassWorkaround("Foreground Service with Notification", "Keep service alive with visible notification.",
                    "class KeepAliveService : Service() {\n    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {\n        startForeground(3001, buildNotification())\n        return START_STICKY\n    }\n}")
            )
        ),
        BypassTechnique(
            "Call Log & Contacts Blocked",
            "Android restricts access to call logs and contacts",
            "📞", Color(0xFFF87171),
            listOf(
                BypassWorkaround("Default Dialer Role", "Request default dialer for call log access.",
                    "val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)\n    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)\nstartActivity(intent)"),
                BypassWorkaround("READ_CALL_LOG Permission", "Request call log permission explicitly.",
                    "if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PERMISSION_GRANTED) {\n    requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG), REQ_CALL_LOG)\n}")
            )
        )
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Android 15 Bypass Engine", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFBBF24).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFFBBF24))
                        Spacer(Modifier.width(8.dp))
                        Text("${techniques.size} restriction bypasses with working code", fontSize = 12.sp, color = Color(0xFFFBBF24))
                    }
                }
            }
            items(techniques) { tech ->
                var expanded by remember { mutableStateOf(false) }
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { expanded = !expanded }, colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tech.icon, fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(tech.restriction, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = tech.color)
                                Text(tech.problem, fontSize = 11.sp, color = Color.Gray)
                            }
                            Text("${tech.workarounds.size} bypasses", fontSize = 10.sp, color = Color.Gray)
                            Spacer(Modifier.width(4.dp))
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                        }
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                tech.workarounds.forEachIndexed { i, wa ->
                                    var showCode by remember { mutableStateOf(false) }
                                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Surface(color = tech.color, shape = MaterialTheme.shapes.small) { Text("${i+1}", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, color = Color.White) }
                                                Spacer(Modifier.width(8.dp))
                                                Column(Modifier.weight(1f)) {
                                                    Text(wa.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                    Text(wa.desc, fontSize = 10.sp, color = Color.Gray)
                                                }
                                                TextButton(onClick = { showCode = !showCode }) { Text(if (showCode) "Hide" else "Code", fontSize = 10.sp, color = tech.color) }
                                            }
                                            AnimatedVisibility(visible = showCode) {
                                                Surface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), color = Color(0xFF000000), shape = MaterialTheme.shapes.small) {
                                                    Text(wa.code, modifier = Modifier.padding(12.dp), fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399))
                                                }
                                            }
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
