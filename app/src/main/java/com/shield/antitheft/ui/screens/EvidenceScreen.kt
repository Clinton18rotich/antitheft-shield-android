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

data class EvidenceStep(val title: String, val icon: String, val color: Color, val details: List<String>, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvidenceScreen(navController: NavController) {
    val context = LocalContext.current
    var activeStep by remember { mutableStateOf(0) }

    val steps = listOf(
        EvidenceStep("Stolen Device Detection", "🔍", Color(0xFFF87171),
            listOf("Motion sensor triggers on sudden movement", "Proximity sensor detects removal from pocket/bag", "Accelerometer detects running/walking pattern", "Gyroscope detects orientation change", "Light sensor detects change in ambient light", "Pressure sensor detects grip release"),
            "sensorManager.registerListener(object : SensorEventListener {\n    override fun onSensorChanged(event: SensorEvent) {\n        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {\n            val acceleration = sqrt(event.values[0].pow(2) + event.values[1].pow(2) + event.values[2].pow(2))\n            if (acceleration > THRESHOLD) triggerTheftProtocol()\n        }\n    }\n}, accelerometer, SensorManager.SENSOR_DELAY_UI)"),
        EvidenceStep("Capture Evidence", "📸", Color(0xFF22D3EE),
            listOf("Silent front camera photo every 5 seconds", "Rear camera photo for environment context", "30-second ambient audio recording", "GPS coordinates with accuracy radius", "WiFi network scan for location fingerprint", "Bluetooth device scan for nearby devices", "Cell tower IDs for triangulation"),
            "fun captureEvidence() {\n    val photo = captureSilentPhoto(CAMERA_FRONT)\n    val audio = recordAmbientAudio(30_000)\n    val location = getLastKnownLocation()\n    val wifi = scanWiFiNetworks()\n    val cell = getCellTowerInfo()\n    return Evidence(photo, audio, location, wifi, cell)\n}"),
        EvidenceStep("Package Data", "📦", Color(0xFFFBBF24),
            listOf("Bundle all evidence into single package", "Add timestamp watermark to photos", "Embed GPS coordinates in EXIF data", "Generate SHA-256 hash for chain of custody", "Compress files to reduce size", "Encrypt package with AES-256", "Create evidence manifest document"),
            "fun packageEvidence(evidence: Evidence): File {\n    val zip = ZipOutputStream(FileOutputStream(packageFile))\n    zip.putNextEntry(ZipEntry(\"photo.jpg\")); zip.write(evidence.photo)\n    zip.putNextEntry(ZipEntry(\"audio.aac\")); zip.write(evidence.audio)\n    zip.putNextEntry(ZipEntry(\"manifest.json\")); zip.write(evidence.manifest)\n    zip.close()\n    return encryptFile(packageFile)\n}"),
        EvidenceStep("Secure Transmission", "🔒", Color(0xFFA78BFA),
            listOf("Establish TLS 1.3 connection to server", "Upload encrypted package via HTTPS", "Fallback to SMTP email if upload fails", "Try multiple endpoints (primary, backup)", "Queue for retry if offline", "Split large files into chunks", "Verify successful delivery with checksum"),
            "fun transmitEvidence(file: File) {\n    try {\n        val client = OkHttpClient()\n        val body = MultipartBody.Builder().addFormDataPart(\"file\", file.name, file.asRequestBody())\n        val request = Request.Builder().url(SERVER_URL).post(body.build()).build()\n        client.newCall(request).execute()\n    } catch (e: Exception) {\n        sendViaEmail(file) // Fallback\n    }\n}"),
        EvidenceStep("Gmail Delivery", "📧", Color(0xFF34D399),
            listOf("Primary: SMTP to emergency Gmail", "CC: Secondary backup email", "BCC: Police/security email if configured", "Subject: [URGENT] Device Theft Alert", "Body: GPS link, timestamp, device info", "Attachments: Photos, audio, location log", "Read receipt requested for confirmation"),
            "fun sendGmailAlert(evidence: File) {\n    val session = Session.getInstance(props, object : Authenticator() {\n        override fun getPasswordAuthentication() = PasswordAuthentication(EMAIL, PASSWORD)\n    })\n    val msg = MimeMessage(session).apply {\n        setFrom(InternetAddress(EMAIL))\n        addRecipient(TO, InternetAddress(\"emergency@gmail.com\"))\n        subject = \"[URGENT] Device Theft Alert\"\n        setText(\"Location: MAPS_LINK\\nDevice: DEVICE_INFO\")\n    }\n    Transport.send(msg)\n}"),
        EvidenceStep("Reverse SSH Tunnel", "🔄", Color(0xFF22D3EE),
            listOf("Phone creates SSH tunnel to your server", "You connect back through the tunnel", "Full shell access to thief's device", "Browse files remotely via SFTP", "Stream camera feed in real-time", "Execute commands remotely", "No port forwarding needed on phone"),
            "val session = JSch().getSession(\"user\", \"your-server.com\", 22)\nsession.setPassword(\"password\")\nsession.setPortForwardingR(8080, \"localhost\", 8080)\nsession.connect()\n// Now connect to localhost:8080 to access thief's device"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Evidence Flow", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF34D399).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Visibility, null, tint = Color(0xFF34D399))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("6-Step Evidence Pipeline", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399))
                            Text("${activeStep}/6 steps completed • Tap steps to activate", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
            items(steps.size) { i ->
                val step = steps[i]
                var expanded by remember { mutableStateOf(false) }
                var showCode by remember { mutableStateOf(false) }
                val isActive = i < activeStep
                val isCurrent = i == activeStep
                
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    .clickable {
                        if (i <= activeStep) activeStep = i + 1
                        Toast.makeText(context, "Step ${i+1}: ${step.title}", Toast.LENGTH_SHORT).show()
                    }.clickable { expanded = !expanded },
                    colors = CardDefaults.cardColors(containerColor = if (isActive) Color(0xFF0F766E) else Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(step.icon, fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Step ${i+1}: ${step.title}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = step.color)
                                if (isActive) Text("✅ Completed", fontSize = 10.sp, color = Color(0xFF34D399))
                            }
                            if (isActive) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF34D399))
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                        }
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                step.details.forEach { detail ->
                                    Text("• $detail", fontSize = 10.sp, color = Color(0xFFA0A0B8), modifier = Modifier.padding(vertical = 1.dp))
                                }
                                Spacer(Modifier.height(8.dp))
                                Surface(modifier = Modifier.fillMaxWidth().clickable { showCode = !showCode },
                                    color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Code, null, tint = step.color, modifier = Modifier.size(14.dp))
                                            Spacer(Modifier.width(6.dp))
                                            Text("Code", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                            Text(if (showCode) "Hide" else "View", fontSize = 9.sp, color = step.color)
                                        }
                                        AnimatedVisibility(visible = showCode) {
                                            Text(step.code, fontSize = 7.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(top = 6.dp))
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
