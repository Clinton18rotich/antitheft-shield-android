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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Module(val name: String, val desc: String, val icon: String, val color: Color, val features: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesScreen(navController: NavController) {
    val modules = listOf(
        Module("Biometric Capture", "Face, voice, fingerprint capture", "📸", Color(0xFF22D3EE), 18),
        Module("Location Tracker", "GPS, WiFi, Cell triangulation", "📍", Color(0xFF34D399), 12),
        Module("Stealth Mode", "Hide app, run silently", "👻", Color(0xFF8B5CF6), 10),
        Module("Evidence Flow", "Auto-collect & email evidence", "📦", Color(0xFFFBBF24), 14),
        Module("Remote Install", "Install companion apps", "📲", Color(0xFFA78BFA), 8),
        Module("Reverse Connect", "Connect to thief's device", "🔄", Color(0xFF22D3EE), 9),
        Module("Alert System", "Email, SMS, Telegram alerts", "🚨", Color(0xFFF87171), 11),
        Module("Security Lab", "Test & verify protections", "🔬", Color(0xFF34D399), 15),
        Module("Bypass Engine", "Bypass Android restrictions", "🔓", Color(0xFFFBBF24), 23),
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Modules", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A)))
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
            items(modules) { mod ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(mod.icon, fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(mod.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(mod.desc, fontSize = 11.sp, color = Color.Gray)
                        }
                        Surface(color = mod.color.copy(alpha = 0.2f), shape = MaterialTheme.shapes.small) {
                            Text("${mod.features} features", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, color = mod.color)
                        }
                    }
                }
            }
        }
    }
}
