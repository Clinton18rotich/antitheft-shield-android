package com.shield.antitheft.ui.screens

import android.widget.Toast
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Scenario(val name: String, val desc: String, val icon: String, val color: Color, val steps: List<String>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatorScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf("") }
    var log by remember { mutableStateOf(listOf<String>()) }
    var currentStep by remember { mutableStateOf(0) }

    val scenarios = listOf(
        Scenario("Phone Snatched from Hand", "Simulate grab-and-run theft scenario", "🏃", Color(0xFFF87171),
            listOf("Motion sensor detects sudden acceleration", "GPS starts tracking at 5s intervals", "Front camera captures burst of 5 photos", "Ambient audio recording begins", "Email alert sent to emergency contacts", "Location mapped on Google Maps link", "Device info logged (IMEI, SIM, WiFi)")),
        Scenario("Phone Stolen from Bag", "Simulate stealth theft from bag/pocket", "🎒", Color(0xFFFB923C),
            listOf("Proximity sensor detects removal", "Silent photo capture (no flash)", "GPS logging begins quietly", "WiFi network scanning for location", "Cell tower triangulation backup", "No audible alarm (stealth mode)", "Evidence packaged and queued for sending")),
        Scenario("SIM Card Swap", "Simulate thief swapping SIM card", "📱", Color(0xFFFBBF24),
            listOf("SIM change detected immediately", "New phone number logged", "IMEI and device ID captured", "Location sent via WiFi/GPS", "Alert sent to backup email", "Lock command queued for execution", "New SIM info forwarded to owner")),
        Scenario("Factory Reset Attempt", "Simulate thief trying to wipe device", "🗑️", Color(0xFFEF4444),
            listOf("Reset intent detected", "Critical evidence uploaded to cloud", "Final GPS location sent", "All photos/videos backed up", "Device info saved to server", "SMS alert sent before wipe", "App survives as system app")),
        Scenario("USB Debugging Attack", "Simulate thief connecting to PC", "💻", Color(0xFF8B5CF6),
            listOf("USB connection detected", "ADB access blocked immediately", "Screen lock enforced", "Camera captures photo of connected environment", "Audio recording starts", "GPS fix taken", "Alert sent with USB connection details")),
        Scenario("Airplane Mode Evasion", "Simulate thief enabling airplane mode", "✈️", Color(0xFF06B6D4),
            listOf("Airplane mode activation detected", "Evidence stored locally with timestamp", "GPS continues via offline chip", "Camera and audio still functional", "WiFi disabled but cellular queued", "All data synced when connection restored", "Offline mode indicator logged")),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Theft Simulator", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF87171).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("6 Theft Scenarios", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF87171))
                        Text("Test how the system responds to each theft type", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
            items(scenarios) { scenario ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(scenario.icon, fontSize = 36.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(scenario.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = scenario.color)
                                Text(scenario.desc, fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = {
                            running = scenario.name; currentStep = 0; log = emptyList()
                            scope.launch {
                                scenario.steps.forEachIndexed { i, step ->
                                    currentStep = i + 1; log = log + "✅ $step"; delay(600)
                                }
                                running = ""
                                Toast.makeText(context, "${scenario.name}: Complete!", Toast.LENGTH_SHORT).show()
                            }
                        }, modifier = Modifier.fillMaxWidth(), enabled = running.isEmpty(), colors = ButtonDefaults.buttonColors(containerColor = scenario.color)) {
                            Text(if (running == scenario.name) "Running... ${currentStep}/${scenario.steps.size}" else "▶ Run Simulation", fontSize = 13.sp)
                        }
                        if (log.isNotEmpty() && running == scenario.name) {
                            Spacer(Modifier.height(8.dp))
                            Surface(color = Color(0xFF000000), shape = MaterialTheme.shapes.small) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    log.forEach { entry -> Text(entry, fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(vertical = 1.dp)) }
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
