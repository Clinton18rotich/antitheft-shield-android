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
    var outputText by remember { mutableStateOf("") }
    var history by remember { mutableStateOf(listOf<String>()) }

    val commands = listOf(
        Cmd("PHOTO", "Silent front camera capture", "📸", Color(0xFF22D3EE), "📸 Camera", "val cm = getSystemService(CAMERA_SERVICE) as CameraManager\nval id = cm.cameraIdList.first()\nval reader = ImageReader.newInstance(1920,1080,JPEG,5)\nreader.setOnImageAvailableListener({r->\n    val img = r.acquireLatestImage()\n    val bytes = ByteArray(img.planes[0].buffer.remaining())\n    img.planes[0].buffer.get(bytes)\n    FileOutputStream(File(cacheDir,\"thief.jpg\")).use{it.write(bytes)}\n    img.close()\n},null)\ncm.openCamera(id,callback,null)"),
        Cmd("BURST_PHOTO", "Capture 10 rapid photos", "📷", Color(0xFF06B6D4), "📸 Camera", "repeat(10){i->Thread.sleep(200);captureSilentPhoto()?.let{emailQueue.add(it)}}\nsendBurstEmail()"),
        Cmd("REAR_PHOTO", "Silent rear camera capture", "📹", Color(0xFF0891B2), "📸 Camera", "val cm = getSystemService(CAMERA_SERVICE) as CameraManager\nval rearId = cm.cameraIdList.find{cm.getCameraCharacteristics(it).get(LENS_FACING)==LENS_FACING_BACK}\n// Capture using rear camera for environment context"),
        Cmd("VIDEO_RECORD", "Record 30s silent video", "🎥", Color(0xFF0E7490), "📸 Camera", "val r = MediaRecorder()\nr.setVideoSource(CAMERA);r.setOutputFormat(MPEG_4)\nr.setVideoEncoder(H264);r.setOutputFile(file.absolutePath)\nr.setMaxDuration(30000);r.prepare();r.start()"),
        Cmd("NIGHT_PHOTO", "Night mode photo capture", "🌙", Color(0xFF6366F1), "📸 Camera", "val req = camera.createCaptureRequest(STILL_CAPTURE)\nreq.set(CaptureRequest.CONTROL_MODE,CONTROL_MODE_NIGHT)\nreq.set(CaptureRequest.SENSOR_SENSITIVITY,3200)\nsession.capture(req.build(),null,null)"),
        Cmd("FLASH_PHOTO", "Photo with flash as torch", "⚡", Color(0xFFFBBF24), "📸 Camera", "val req = camera.createCaptureRequest(STILL_CAPTURE)\nreq.set(CaptureRequest.FLASH_MODE,FLASH_MODE_TORCH)\nsession.capture(req.build(),null,null)"),
        Cmd("AUDIO", "Record 5 min ambient audio", "🎤", Color(0xFFFBBF24), "🎤 Audio", "val r = MediaRecorder()\nr.setAudioSource(MIC);r.setOutputFormat(MPEG_4)\nr.setAudioEncoder(AAC);r.setOutputFile(file.absolutePath)\nr.setMaxDuration(300000);r.prepare();r.start()"),
        Cmd("AUDIO_LIVE", "Stream live audio chunks", "🎙️", Color(0xFFF59E0B), "🎤 Audio", "val buf = AudioRecord.getMinBufferSize(44100,CHANNEL_IN_MONO,ENCODING_PCM_16BIT)\nval rec = AudioRecord(MIC,44100,CHANNEL_IN_MONO,ENCODING_PCM_16BIT,buf)\nrec.startRecording()\nwhile(streaming){val b=ShortArray(buf);rec.read(b,0,buf);socket.outputStream.write(b.toByteArray())}"),
        Cmd("VOICE_ANALYZE", "Voice stress & emotion analysis", "📊", Color(0xFFEAB308), "🎤 Audio", "val model = TFLiteModel.load(\"voice_stress.tflite\")\nval features = extractMFCC(audioBuffer)\nval result = model.run(features)\nif(result[0][0]>0.7)sendAlert(\"High stress detected\")"),
        Cmd("LOCATION", "Get GPS coordinates", "📍", Color(0xFF34D399), "📍 Tracking", "val lm = getSystemService(LOCATION_SERVICE) as LocationManager\nlm.requestSingleUpdate(GPS,{loc->\n    val link=\"https://maps.google.com/?q=\"+loc.latitude+\",\"+loc.longitude\n    sendEmail(\"Location\",link)\n},null)"),
        Cmd("TRACK_START", "Continuous GPS every 5 min", "🛰️", Color(0xFF059669), "📍 Tracking", "val n = NotificationCompat.Builder(this,CH).setContentTitle(\"Navigation\").setOngoing(true).build()\nstartForeground(1001,n)\nlm.requestLocationUpdates(GPS,300000L,0f,listener)"),
        Cmd("GEOFENCE", "Alert on area entry/exit", "🎯", Color(0xFF10B981), "📍 Tracking", "val gf = Geofence.Builder().setRequestId(\"zone\").setCircularRegion(-1.2921,36.8219,100f).setExpirationDuration(NEVER_EXPIRE).setTransitionTypes(ENTER or EXIT).build()\nGeofencingClient.addGeofences(listOf(gf),pendingIntent)"),
        Cmd("WIFI_SCAN", "Log nearby WiFi networks", "📶", Color(0xFFA78BFA), "🌐 Network", "val wifi = getSystemService(WIFI_SERVICE) as WifiManager\nwifi.scanResults.forEach{ap->log(\"SSID: \"+ap.SSID+\" BSSID: \"+ap.BSSID+\" dBm: \"+ap.level)}\nemailLog(\"WiFi Scan\")"),
        Cmd("BLUETOOTH_SCAN", "Scan Bluetooth devices", "🔵", Color(0xFF8B5CF6), "🌐 Network", "val bt = BluetoothAdapter.getDefaultAdapter()\nbt.startDiscovery()\n// Register BroadcastReceiver for ACTION_FOUND\n// Log each discovered device with RSSI"),
        Cmd("CELL_TOWER", "Cell tower triangulation", "📻", Color(0xFF7C3AED), "🌐 Network", "val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager\ntm.allCellInfo.forEach{cell->when(cell){is CellInfoLte->log(\"LTE: \"+cell.cellIdentity.tac+\"/\"+cell.cellIdentity.ci);is CellInfoGsm->log(\"GSM: \"+cell.cellIdentity.lac+\"/\"+cell.cellIdentity.cid)}}"),
        Cmd("LOCK", "Lock device instantly", "🔒", Color(0xFFF87171), "🔒 Control", "val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager\nif(dpm.isAdminActive(component)){dpm.lockNow();dpm.setCameraDisabled(component,true)}"),
        Cmd("ALARM", "Max volume siren (130dB)", "🚨", Color(0xFFDC2626), "🔒 Control", "val tone = RingtoneManager.getDefaultUri(TYPE_ALARM)\nRingtoneManager.getRingtone(context,tone).play()\nval cm = getSystemService(CAMERA_SERVICE) as CameraManager\ncm.setTorchMode(cameraId,true)\n// Flash strobe + max volume alarm"),
        Cmd("FAKE_SHUTDOWN", "Screen off, tracking on", "👁️‍🗨️", Color(0xFFEF4444), "🔒 Control", "val wm = getSystemService(WINDOW_SERVICE) as WindowManager\nval view = View(this).apply{setBackgroundColor(Color.BLACK)}\nwm.addView(view,WindowManager.LayoutParams().apply{alpha=0f;flags=FLAG_NOT_TOUCHABLE or FLAG_FULLSCREEN})\n// Screen appears off but all tracking continues"),
        Cmd("SOS_FLASH", "Flash SOS in Morse code", "💡", Color(0xFFB91C1C), "🔒 Control", "val sos=\"...---...\"\nval cm = getSystemService(CAMERA_SERVICE) as CameraManager\nsos.forEach{c->cm.setTorchMode(cameraId,true);Thread.sleep(if(c=='.')200 else 600);cm.setTorchMode(cameraId,false);Thread.sleep(200)}"),
        Cmd("DATA_WIPE", "Secure wipe evidence folder", "🗑️", Color(0xFF991B1B), "🔒 Control", "val dir = File(getExternalFilesDir(null),\"evidence\")\ndir.listFiles()?.forEach{f->repeat(3){RandomAccessFile(f,\"rw\").use{raf->val r=ByteArray(f.length().toInt());Random().nextBytes(r);raf.write(r)}};f.delete()}"),
        Cmd("SCREAM_DETECT", "Auto-alarm on scream", "📢", Color(0xFFEF4444), "🎤 Audio", "val rec = AudioRecord(MIC,44100,CHANNEL_IN_MONO,ENCODING_PCM_16BIT,buf)\nrec.startRecording()\nwhile(true){rec.read(buf,0,buf);val amp=calculateAmplitude(buf);if(amp>SCREAM_THRESHOLD)triggerAlarm()}"),
        Cmd("VIBRATE_PATTERN", "SOS vibration pattern", "📳", Color(0xFFDC2626), "🔒 Control", "val v = getSystemService(VIBRATOR_SERVICE) as Vibrator\nval pattern = longArrayOf(0,200,200,200,200,600,600,200,600,200,600,200)\nv.vibrate(VibrationEffect.createWaveform(pattern,0))"),
        Cmd("VOLUME_MAX", "Set all volumes to maximum", "🔊", Color(0xFFB91C1C), "🔒 Control", "val am = getSystemService(AUDIO_SERVICE) as AudioManager\nam.setStreamVolume(STREAM_ALARM,am.getStreamMaxVolume(STREAM_ALARM),0)\nam.setStreamVolume(STREAM_RING,am.getStreamMaxVolume(STREAM_RING),0)\nam.setStreamVolume(STREAM_MUSIC,am.getStreamMaxVolume(STREAM_MUSIC),0)"),
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
                    item { Text(category, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE), modifier = Modifier.padding(vertical = 8.dp)) }
                    items(cmds) { cmd ->
                        var expanded by remember { mutableStateOf(false) }
                        var showCode by remember { mutableStateOf(false) }
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).clickable { expanded = !expanded },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(cmd.icon, fontSize = 22.sp)
                                    Spacer(Modifier.width(10.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(cmd.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(cmd.desc, fontSize = 10.sp, color = Color.Gray)
                                    }
                                    TextButton(onClick = {
                                        outputText = "⚡ ${cmd.name}...\n✅ ${cmd.desc}\n📍 Success\n🕐 ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())}"
                                        history = listOf(cmd.name) + history.take(9)
                                        Toast.makeText(context, "⚡ ${cmd.name}", Toast.LENGTH_SHORT).show()
                                    }) { Icon(Icons.Default.PlayArrow, null, tint = cmd.color, modifier = Modifier.size(16.dp)) }
                                    Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                                }
                                AnimatedVisibility(visible = expanded) {
                                    Surface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clickable { showCode = !showCode },
                                        color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Code, null, tint = cmd.color, modifier = Modifier.size(14.dp))
                                                Spacer(Modifier.width(6.dp))
                                                Text("Implementation", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                                Text(if (showCode) "Hide ▲" else "View ▼", fontSize = 9.sp, color = cmd.color)
                                            }
                                            AnimatedVisibility(visible = showCode) {
                                                Text(cmd.code, fontSize = 7.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(top = 6.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (history.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        Text("Recent Commands", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        history.forEach { cmd -> Text("⚡ $cmd", fontSize = 10.sp, color = Color(0xFF34D399), modifier = Modifier.padding(vertical = 1.dp)) }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
