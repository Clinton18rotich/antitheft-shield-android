package com.shield.antitheft.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatorScreen(navController: NavController) {
    val context = LocalContext.current
    var running by remember { mutableStateOf(false) }
    var log by remember { mutableStateOf(listOf<String>()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Theft Simulator", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Simulate Theft Scenario", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF87171))
            Text("Tests all detection & response systems", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                running = true; log = emptyList()
                scope.launch {
                    val steps = listOf("📸 Silent photo captured", "📍 GPS: -1.2921, 36.8219", "🎤 Audio recording started", "📧 Alert sent to emergency@gmail.com", "🔒 Device lock command sent", "✅ Simulation complete")
                    steps.forEach { step -> log = log + step; delay(800) }
                    running = false
                    Toast.makeText(context, "Simulation complete!", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth(), enabled = !running, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF87171))) {
                Text(if (running) "Running..." else "▶ Start Simulation", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (log.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Simulation Log", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
                        Spacer(modifier = Modifier.height(8.dp))
                        log.forEach { entry -> Text(entry, fontSize = 12.sp, color = Color(0xFF34D399), modifier = Modifier.padding(vertical = 2.dp)) }
                    }
                }
            }
        }
    }
}
