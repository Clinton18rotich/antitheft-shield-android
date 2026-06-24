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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

data class Cmd(val name: String, val desc: String, val icon: String, val color: Color, val category: String, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandScreen(navController: NavController) {
    val context = LocalContext.current
    var lastCmd by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    val commands = listOf(
        Cmd("PHOTO", "Silent front camera capture", "📸", Color(0xFF22D3EE), "📸 Camera",
            "val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager\nval cameraId = cameraManager.cameraIdList.first()\nval reader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 5)\nreader.setOnImageAvailableListener({ r ->\n    val image = r.acquireLatestImage()\n    val bytes = ByteArray(image.planes[0].buffer.remaining())\n    image.planes[0].buffer.get(bytes)\n    FileOutputStream(File(cacheDir, \"thief.jpg\")).use { it.write(bytes) }\n    image.close()\n}, null)\ncameraManager.openCamera(cameraId, callback, null)"),
        Cmd("BURST_PHOTO", "Capture 10 rapid photos", "📷", Color(0xFF06B6D4), "📸 Camera",
            "repeat(10) { i ->\n    Thread.sleep(200)\n    captureSilentPhoto()?.let { file ->\n        emailQueue.add(file)\n    }\n}\nsendBurstEmail()"),
        Cmd("VIDEO_RECORD", "Record 30s silent video", "🎥", Color(0xFF0E7490), "📸 Camera",
            "val recorder = MediaRecorder()\nrecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA)\nrecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)\nrecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)\nrecorder.setOutputFile(videoFile.absolutePath)\nrecorder.setMaxDuration(30000)\nrecorder.prepare()\nrecorder.start()"),
        Cmd("AUDIO", "Record 5 min ambient audio", "🎤", Color(0xFFFBBF24), "🎤 Audio",
            "val recorder = MediaRecorder()\nrecorder.setAudioSource(MediaRecorder.AudioSource.MIC)\nrecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)\nrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)\nrecorder.setOutputFile(audioFile.absolutePath)\nrecorder.setMaxDuration(300000)\nrecorder.prepare()\nrecorder.start()"),
        Cmd("AUDIO_LIVE", "Stream live audio chunks", "🎙️", Color(0xFFF59E0B), "🎤 Audio",
            "val bufferSize = AudioRecord.getMinBufferSize(44100, CHANNEL_IN_MONO, ENCODING_PCM_16BIT)\nval recorder = AudioRecord(MediaRecorder.AudioSource.MIC, 44100, CHANNEL_IN_MONO, ENCODING_PCM_16BIT, bufferSize)\nrecorder.startRecording()\nwhile (streaming) {\n    val buffer = ShortArray(bufferSize)\n    recorder.read(buffer, 0, bufferSize)\n    socket.outputStream.write(buffer.toByteArray())\n}"),
        Cmd("LOCATION", "Get GPS coordinates", "📍", Color(0xFF34D399), "📍 Tracking",
            "val lm = getSystemService(LOCATION_SERVICE) as LocationManager\nlm.requestSingleUpdate(LocationManager.GPS_PROVIDER, object : LocationListener {\n    override fun onLocationChanged(loc: Location) {\n        val link = \"https://maps.google.com/?q=LATITUDE,LONGITUDE\"\n        sendEmail(\"Location Update\", link)\n    }\n}, null)"),
        Cmd("TRACK_START", "Continuous GPS every 5 min", "🛰️", Color(0xFF059669), "📍 Tracking",
            "val notification = NotificationCompat.Builder(this, CH)\n    .setContentTitle(\"Navigation\")\n    .setOngoing(true).build()\nstartForeground(1001, notification)\nlm.requestLocationUpdates(GPS, 300000L, 0f, listener)"),
        Cmd("WIFI_SCAN", "Log nearby WiFi networks", "📶", Color(0xFFA78BFA), "🌐 Network",
            "val wifi = getSystemService(WIFI_SERVICE) as WifiManager\nval results = wifi.scanResults\nresults.forEach { ap ->\n    log(\"SSID | BSSID | LEVELdBm | FREQMHz\")\n}\nemailLog(\"WiFi Scan Results\")"),
        Cmd("LOCK", "Lock device instantly", "🔒", Color(0xFFF87171), "🔒 Control",
            "val admin = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager\nif (admin.isAdminActive(component)) {\n    admin.lockNow()\n    admin.setCameraDisabled(component, true)\n}"),
        Cmd("ALARM", "Sound loud alarm (130dB)", "🚨", Color(0xFFDC2626), "🔒 Control",
            "val tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)\nval r = RingtoneManager.getRingtone(context, tone)\nr.play()\n// Also flash torch\nval cam = getSystemService(CAMERA_SERVICE) as CameraManager\ncam.setTorchMode(cameraId, true)"),
        Cmd("FAKE_SHUTDOWN", "Screen off, tracking on", "👁️‍🗨️", Color(0xFFEF4444), "🔒 Control",
            "val params = WindowManager.LayoutParams()\nparams.alpha = 0f // Black screen\nparams.flags = FLAG_NOT_TOUCHABLE or FLAG_FULLSCREEN\nwindowManager.addView(blackView, params)\n// Tracking continues in background"),
        Cmd("SOS_FLASH", "Flash SOS in Morse code", "💡", Color(0xFFB91C1C), "🔒 Control",
            "val sos = \"...---...\"\nval cam = getSystemService(CAMERA_SERVICE) as CameraManager\nsos.forEach { c ->\n    cam.setTorchMode(cameraId, true)\n    Thread.sleep(if (c == '.') 200 else 600)\n    cam.setTorchMode(cameraId, false)\n    Thread.sleep(200)\n}"),
        Cmd("CELL_TOWER", "Cell tower triangulation", "📻", Color(0xFF7C3AED), "🌐 Network",
            "val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager\nval cells = tm.allCellInfo\ncells.forEach { cell ->\n    when (cell) {\n        is CellInfoLte -> log(\"LTE Cell: TAC/CI\")\n        is CellInfoGsm -> log(\"GSM Cell: LAC/CID\")\n    }\n}"),
        Cmd("GEOFENCE", "Alert on area entry/exit", "🎯", Color(0xFF10B981), "📍 Tracking",
            "val geofence = Geofence.Builder()\n    .setRequestId(\"safe_zone\")\n    .setCircularRegion(-1.2921, 36.8219, 100f)\n    .setExpirationDuration(Geofence.NEVER_EXPIRE)\n    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)\n    .build()"),
        Cmd("VOICE_ANALYZE", "Voice stress & emotion analysis", "📊", Color(0xFFEAB308), "🎤 Audio",
            "val tensorFlow = TFLiteModel.load(\"voice_stress_model.tflite\")\nval features = extractMFCC(audioBuffer)\nval result = tensorFlow.run(features)\nval stressLevel = result[0][0]\nif (stressLevel > 0.7) sendAlert(\"High stress detected in voice\")"),
        Cmd("DEAD_RECKON", "Sensor-based positioning", "🧭", Color(0xFF047857), "📍 Tracking",
            "val sensor = getSystemService(SENSOR_SERVICE) as SensorManager\nvar heading = 0f; var distance = 0f\nsensor.registerListener(object : SensorEventListener {\n    override fun onSensorChanged(event: SensorEvent) {\n        heading += event.values[2] * (event.timestamp - lastTime) / 1e9f\n        val steps = detectSteps(event.values)\n        distance += steps * 0.75f\n    }\n}, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)"),
        Cmd("DATA_WIPE", "Secure wipe evidence folder", "🗑️", Color(0xFF991B1B), "🔒 Control",
            "val evidenceDir = File(getExternalFilesDir(null), \"evidence\")\nevidenceDir.listFiles()?.forEach { file ->\n    // Overwrite 3 times with random data before delete\n    repeat(3) { RandomAccessFile(file, \"rw\").use { raf ->\n        val random = ByteArray(file.length().toInt())\n        Random().nextBytes(random)\n        raf.write(random)\n    } }\n    file.delete()\n}")
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Command Center", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (outputText.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth().padding(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF000000))) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("root@shield:~$", fontSize = 11.sp, color = Color(0xFF34D399), fontFamily = FontFamily.Monospace)
                            TextButton(onClick = { outputText = "" }) { Text("clear", fontSize = 10.sp, color = Color(0xFFF87171)) }
                        }
                        Text(outputText, fontSize = 10.sp, color = Color(0xFF22D3EE), fontFamily = FontFamily.Monospace)
                    }
                }
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                val grouped = commands.groupBy { it.category }
                grouped.forEach { (category, cmds) ->
                    item {
                        Text(category, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE), modifier = Modifier.padding(vertical = 8.dp))
                    }
                    items(cmds) { cmd ->
                        var expanded by remember { mutableStateOf(false) }
                        var showCode by remember { mutableStateOf(false) }
                        
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            .clickable { expanded = !expanded },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(cmd.icon, fontSize = 22.sp)
                                    Spacer(Modifier.width(10.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(cmd.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(cmd.desc, fontSize = 10.sp, color = Color.Gray)
                                    }
                                    Surface(color = cmd.color.copy(alpha = 0.2f), shape = MaterialTheme.shapes.small) {
                                        TextButton(onClick = {
                                            lastCmd = cmd.name
                                            outputText = "⚡ Executing: ${cmd.name}...\n✅ ${cmd.desc}\n📍 Status: Success\n🕐 ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())}"
                                            Toast.makeText(context, "⚡ ${cmd.name} executed", Toast.LENGTH_SHORT).show()
                                        }) {
                                            Icon(Icons.Default.PlayArrow, null, tint = cmd.color, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                    Spacer(Modifier.width(4.dp))
                                    Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                                }
                                AnimatedVisibility(visible = expanded) {
                                    Column(modifier = Modifier.padding(top = 8.dp)) {
                                        Surface(modifier = Modifier.fillMaxWidth().clickable { showCode = !showCode },
                                            color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Code, null, tint = cmd.color, modifier = Modifier.size(14.dp))
                                                    Spacer(Modifier.width(6.dp))
                                                    Text("Implementation", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                                    Text(if (showCode) "Hide Code ▲" else "View Code ▼", fontSize = 9.sp, color = cmd.color)
                                                }
                                                AnimatedVisibility(visible = showCode) {
                                                    Text(cmd.code, fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(top = 6.dp))
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
}
