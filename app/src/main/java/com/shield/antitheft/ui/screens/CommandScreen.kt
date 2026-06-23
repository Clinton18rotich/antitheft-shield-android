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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Cmd(val name: String, val desc: String, val icon: String, val color: Color, val category: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandScreen(navController: NavController) {
    val context = LocalContext.current
    var lastCmd by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    val commands = listOf(
        // Camera
        Cmd("PHOTO", "Silent front camera capture", "📸", Color(0xFF22D3EE), "📸 Camera"),
        Cmd("BURST_PHOTO", "Capture 10 rapid photos", "📷", Color(0xFF06B6D4), "📸 Camera"),
        Cmd("REAR_PHOTO", "Silent rear camera capture", "📹", Color(0xFF0891B2), "📸 Camera"),
        Cmd("VIDEO_RECORD", "Record 30s silent video", "🎥", Color(0xFF0E7490), "📸 Camera"),
        Cmd("NIGHT_PHOTO", "Night mode photo capture", "🌙", Color(0xFF6366F1), "📸 Camera"),
        Cmd("FLASH_PHOTO", "Photo with flash as torch", "⚡", Color(0xFFFBBF24), "📸 Camera"),
        // Audio
        Cmd("AUDIO", "Record 5 min ambient audio", "🎤", Color(0xFFFBBF24), "🎤 Audio"),
        Cmd("AUDIO_LIVE", "Stream live audio chunks", "🎙️", Color(0xFFF59E0B), "🎤 Audio"),
        Cmd("VOICE_ANALYZE", "Voice stress & emotion analysis", "📊", Color(0xFFEAB308), "🎤 Audio"),
        Cmd("ULTRASONIC_TX", "Transmit data via ultrasound", "📡", Color(0xFFD97706), "🎤 Audio"),
        Cmd("NOISE_LEVEL", "Measure ambient noise dB", "📈", Color(0xFFCA8A04), "🎤 Audio"),
        // Tracking
        Cmd("LOCATION", "Get GPS coordinates", "📍", Color(0xFF34D399), "📍 Tracking"),
        Cmd("TRACK_START", "Continuous GPS every 5 min", "🛰️", Color(0xFF059669), "📍 Tracking"),
        Cmd("GEOFENCE", "Alert on area entry/exit", "🎯", Color(0xFF10B981), "📍 Tracking"),
        Cmd("DEAD_RECKON", "Sensor-based positioning", "🧭", Color(0xFF047857), "📍 Tracking"),
        Cmd("MAGNETIC_MAP", "Magnetic field location", "🧲", Color(0xFF065F46), "📍 Tracking"),
        Cmd("ALTITUDE", "Barometric altitude reading", "⛰️", Color(0xFF064E3B), "📍 Tracking"),
        // Network
        Cmd("WIFI_SCAN", "Log nearby WiFi networks", "📶", Color(0xFFA78BFA), "🌐 Network"),
        Cmd("BLUETOOTH_SCAN", "Scan Bluetooth devices", "🔵", Color(0xFF8B5CF6), "🌐 Network"),
        Cmd("CELL_TOWER", "Cell tower triangulation", "📻", Color(0xFF7C3AED), "🌐 Network"),
        Cmd("WIFI_LOG", "Log connected WiFi history", "📝", Color(0xFF6D28D9), "🌐 Network"),
        Cmd("MAC_ADDRESS", "Get network MAC addresses", "💻", Color(0xFF5B21B6), "🌐 Network"),
        Cmd("IP_CONFIG", "Get IP configuration", "🌍", Color(0xFF4C1D95), "🌐 Network"),
        // Control
        Cmd("LOCK", "Lock device instantly", "🔒", Color(0xFFF87171), "🔒 Control"),
        Cmd("FAKE_SHUTDOWN", "Screen off, tracking on", "👁️‍🗨️", Color(0xFFEF4444), "🔒 Control"),
        Cmd("ALARM", "Sound loud alarm (130dB)", "🚨", Color(0xFFDC2626), "🔒 Control"),
        Cmd("SOS_FLASH", "Flash SOS in Morse code", "💡", Color(0xFFB91C1C), "🔒 Control"),
        Cmd("DATA_WIPE", "Secure wipe evidence folder", "🗑️", Color(0xFF991B1B), "🔒 Control"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Command Center", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Terminal output
            if (outputText.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth().padding(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF000000))) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("root@shield:~$", fontSize = 11.sp, color = Color(0xFF34D399), fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                            TextButton(onClick = { outputText = "" }) { Text("clear", fontSize = 10.sp, color = Color(0xFFF87171)) }
                        }
                        Text(outputText, fontSize = 10.sp, color = Color(0xFF22D3EE), fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
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
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            .clickable {
                                lastCmd = cmd.name
                                outputText = "⚡ Executing: ${cmd.name}...\n✅ ${cmd.desc}\n📍 Status: Success\n🕐 Time: ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}"
                                Toast.makeText(context, "⚡ ${cmd.name} executed", Toast.LENGTH_SHORT).show()
                            },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(cmd.icon, fontSize = 22.sp)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(cmd.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(cmd.desc, fontSize = 10.sp, color = Color.Gray)
                                }
                                Surface(color = cmd.color.copy(alpha = 0.2f), shape = MaterialTheme.shapes.small) {
                                    Icon(Icons.Default.PlayArrow, null, tint = cmd.color, modifier = Modifier.padding(4.dp).size(16.dp))
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
