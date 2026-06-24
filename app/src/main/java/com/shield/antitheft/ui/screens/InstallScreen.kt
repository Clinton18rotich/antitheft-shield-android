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

data class InstallMethod(val title: String, val desc: String, val icon: String, val color: Color, val steps: List<String>, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallScreen(navController: NavController) {
    val methods = listOf(
        InstallMethod("ADB Wireless Install", "Install via WiFi ADB - no USB cable needed after initial setup", "🔌", Color(0xFF22D3EE),
            listOf("Enable Developer Options on target", "Enable Wireless Debugging", "Pair device with pairing code", "Run: adb connect IP:PORT", "Run: adb install shield.apk", "App installs silently in background"),
            "adb pair IP:PORT PAIRING_CODE\nadb connect IP:PORT\nadb install -r shield_companion.apk\nadb shell pm install-existing com.shield.antitheft"),
        InstallMethod("Web Download + Direct Install", "Host APK on web server, user downloads and installs via browser", "🌐", Color(0xFF34D399),
            listOf("Upload APK to web server", "Send download link via SMS/email", "User clicks link in browser", "Browser downloads APK", "User taps to install", "Grant 'Install Unknown Apps' permission"),
            "val intent = Intent(Intent.ACTION_VIEW)\nintent.setDataAndType(Uri.parse(\"https://server.com/shield.apk\"), \"application/vnd.android.package-archive\")\nintent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION\nstartActivity(intent)"),
        InstallMethod("Firebase App Distribution", "Enterprise distribution via Firebase to specific testers", "🔥", Color(0xFFFBBF24),
            listOf("Upload APK to Firebase Console", "Add tester emails", "Testers receive invite email", "Accept invite on device", "Download Firebase Tester app", "Install shield from Firebase", "Auto-updates enabled"),
            "firebaseAppDistribution {\n    appId = \"1:123456:android:abc123\"\n    serviceCredentialsFile = \"service-account.json\"\n    groups = \"testers\"\n}"),
        InstallMethod("QR Code Install", "Generate QR code linking to APK download", "📱", Color(0xFFA78BFA),
            listOf("Upload APK to accessible URL", "Generate QR code with URL", "Display QR on your screen", "Target device scans QR", "Browser opens download link", "Install from downloaded APK"),
            "val qrBitmap = QRCodeWriter().encode(\"https://server.com/shield.apk\", BarcodeFormat.QR_CODE, 512, 512)\nimageView.setImageBitmap(qrBitmap)\n// User scans QR → downloads → installs"),
        InstallMethod("NFC Beam Install", "Transfer APK via NFC tap between devices", "📡", Color(0xFF8B5CF6),
            listOf("Enable NFC on both devices", "Open APK on source device", "Tap devices back-to-back", "NFC beam transfers APK", "Accept transfer on target", "Install from received file"),
            "val uri = FileProvider.getUriForFile(this, \"PACKAGE_NAME.fileprovider\", apkFile)\nnfcAdapter.setBeamPushUris(arrayOf(uri), this)\n// Tap phones → APK transfers via NFC"),
        InstallMethod("Bluetooth Transfer", "Send APK via Bluetooth to nearby device", "🔵", Color(0xFF06B6D4),
            listOf("Pair devices via Bluetooth", "Select APK file to share", "Choose Bluetooth from share menu", "Accept transfer on target", "Open received file", "Install from Bluetooth folder"),
            "val intent = Intent(Intent.ACTION_SEND)\nintent.type = \"application/vnd.android.package-archive\"\nintent.putExtra(Intent.EXTRA_STREAM, uri)\nintent.setPackage(\"com.android.bluetooth\")\nstartActivity(intent)"),
        InstallMethod("Email Attachment Install", "Send APK as email attachment", "📧", Color(0xFFF87171),
            listOf("Compose email to target", "Attach shield.apk", "Send to target's Gmail", "Target opens email", "Downloads attachment", "Installs from Downloads"),
            "val email = Intent(Intent.ACTION_SEND)\nemail.type = \"message/rfc822\"\nemail.putExtra(Intent.EXTRA_EMAIL, arrayOf(\"target@gmail.com\"))\nemail.putExtra(Intent.EXTRA_SUBJECT, \"Important Update\")\nemail.putExtra(Intent.EXTRA_STREAM, uri)\nstartActivity(email)"),
        InstallMethod("Direct Link SMS", "Send download link via SMS for one-tap install", "💬", Color(0xFFFB923C),
            listOf("Upload APK to server", "Shorten URL", "Send SMS with download link", "Target taps link in SMS", "Browser downloads APK", "User installs directly"),
            "val sms = SmsManager.getDefault()\nsms.sendTextMessage(phoneNumber, null, \"Update: https://bit.ly/shield-apk\", null, null)\n// One tap → download → install"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Remote Install System", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFA78BFA).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("8 Installation Methods", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA78BFA))
                        Text("Deploy companion apps remotely for enhanced surveillance", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
            items(methods) { method ->
                var expanded by remember { mutableStateOf(false) }
                var showCode by remember { mutableStateOf(false) }
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { expanded = !expanded },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(method.icon, fontSize = 32.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(method.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = method.color)
                                Text(method.desc, fontSize = 11.sp, color = Color.Gray)
                            }
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                        }
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Text("Steps:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                method.steps.forEachIndexed { i, step ->
                                    Text("${i+1}. $step", fontSize = 10.sp, color = Color(0xFFA0A0B8), modifier = Modifier.padding(vertical = 1.dp))
                                }
                                Spacer(Modifier.height(8.dp))
                                Surface(modifier = Modifier.fillMaxWidth().clickable { showCode = !showCode },
                                    color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Code, null, tint = method.color, modifier = Modifier.size(14.dp))
                                            Spacer(Modifier.width(6.dp))
                                            Text("Code", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                            Text(if (showCode) "Hide" else "View", fontSize = 9.sp, color = method.color)
                                        }
                                        AnimatedVisibility(visible = showCode) {
                                            Text(method.code, fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(top = 6.dp))
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
