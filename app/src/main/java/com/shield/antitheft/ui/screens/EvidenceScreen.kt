package com.shield.antitheft.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvidenceScreen(navController: NavController) {
    val steps = listOf(
        Triple("📸", "Capture", "Silent photo + video of thief"),
        Triple("🎤", "Record", "Ambient audio for voice analysis"),
        Triple("📍", "Locate", "GPS + WiFi coordinates"),
        Triple("📧", "Send", "Email to emergency contacts"),
        Triple("☁️", "Upload", "Cloud backup to secure server"),
        Triple("📋", "Report", "Generate evidence report PDF"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Evidence Flow", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(steps.size) { i ->
                val (icon, title, desc) = steps[i]
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFF1E293B), shape = MaterialTheme.shapes.small) { Text(icon, fontSize = 28.sp, modifier = Modifier.padding(12.dp)) }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Step ${i+1}: $title", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
                        Text(desc, fontSize = 11.sp, color = Color.Gray)
                    }
                }
                if (i < steps.size - 1) {
                    Box(modifier = Modifier.fillMaxWidth().padding(start = 24.dp)) { Icon(Icons.Default.ArrowDownward, null, tint = Color(0xFF22D3EE), modifier = Modifier.size(20.dp)) }
                }
            }
        }
    }
}
