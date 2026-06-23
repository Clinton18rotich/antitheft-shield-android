package com.shield.antitheft.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Command(val name: String, val desc: String, val icon: ImageVector, val color: Color, val category: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandScreen(navController: NavController) {
    val commands = listOf(
        // Camera
        Command("PHOTO", "Silent front camera capture", Icons.Default.CameraAlt, Color(0xFF22D3EE), "📸 Camera"),
        Command("BURST_PHOTO", "Capture 10 rapid photos", Icons.Default.BurstMode, Color(0xFF06B6D4), "📸 Camera"),
        Command("REAR_PHOTO", "Silent rear camera capture", Icons.Default.Camera, Color(0xFF0891B2), "📸 Camera"),
        Command("VIDEO_RECORD", "Record 30s silent video", Icons.Default.Videocam, Color(0xFF0E7490), "📸 Camera"),
        // Audio
        Command("AUDIO", "Record 5 min ambient audio", Icons.Default.Mic, Color(0xFFFBBF24), "🎤 Audio"),
        Command("AUDIO_LIVE", "Stream live audio chunks", Icons.Default.SettingsVoice, Color(0xFFF59E0B), "🎤 Audio"),
        Command("NOISE_LEVEL", "Measure ambient noise dB", Icons.Default.GraphicEq, Color(0xFFCA8A04), "🎤 Audio"),
        // Tracking
        Command("LOCATION", "Get GPS coordinates", Icons.Default.LocationOn, Color(0xFF34D399), "📍 Tracking"),
        Command("TRACK_START", "Begin continuous tracking", Icons.Default.MyLocation, Color(0xFF10B981), "📍 Tracking"),
        Command("WIFI_SCAN", "Scan nearby WiFi networks", Icons.Default.Wifi, Color(0xFF059669), "📍 Tracking"),
        // Security
        Command("LOCK", "Lock device immediately", Icons.Default.Lock, Color(0xFFF87171), "🔒 Security"),
        Command("WIPE", "Factory reset device", Icons.Default.DeleteForever, Color(0xFFEF4444), "🔒 Security"),
        Command("ALARM", "Sound loud alarm", Icons.Default.Campaign, Color(0xFFDC2626), "🔒 Security"),
        Command("SOS_FLASH", "Flash SOS in Morse code", Icons.Default.FlashlightOn, Color(0xFFB91C1C), "🔒 Security"),
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Command Center", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A)))
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
            val grouped = commands.groupBy { it.category }
            grouped.forEach { (category, cmds) ->
                item {
                    Text(category, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE), modifier = Modifier.padding(vertical = 8.dp))
                }
                items(cmds) { cmd ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).clickable { },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(cmd.icon, null, tint = cmd.color, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(cmd.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(cmd.desc, fontSize = 11.sp, color = Color.Gray)
                            }
                            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
