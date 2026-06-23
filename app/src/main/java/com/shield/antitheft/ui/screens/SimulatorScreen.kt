package com.shield.antitheft.ui.screens

import androidx.compose.foundation.layout.*
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
fun SimulatorScreen(navController: NavController) {
    var running by remember { mutableStateOf(false) }
    var log by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Theft Simulator", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A)))
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Simulate Theft Scenario", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF87171))
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                running = true
                log = listOf("📸 Silent photo captured", "📍 GPS: -1.2921, 36.8219", "🎤 Audio recording started", "📧 Alert sent to email", "🔒 Device lock command sent", "✅ Simulation complete")
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF87171))) {
                Text(if (running) "Running..." else "▶ Start Simulation")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (log.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        log.forEach { entry ->
                            Text(entry, fontSize = 12.sp, color = Color(0xFF34D399), modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            }
        }
    }
}
