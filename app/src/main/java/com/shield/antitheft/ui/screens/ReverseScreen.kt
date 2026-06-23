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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReverseScreen(navController: NavController) {
    val context = LocalContext.current
    var ip by remember { mutableStateOf("192.168.1.") }
    var port by remember { mutableStateOf("8080") }
    var connected by remember { mutableStateOf(false) }
    var log by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Reverse Connection", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Connect to Thief's Device", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = ip, onValueChange = { ip = it }, label = { Text("Device IP Address") }, modifier = Modifier.fillMaxWidth(), enabled = !connected)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = port, onValueChange = { port = it }, label = { Text("Port") }, modifier = Modifier.fillMaxWidth(), enabled = !connected)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                connected = !connected
                if (connected) {
                    log = listOf("🔗 Connecting to $ip:$port...", "✅ Connected", "📸 Camera stream available", "📍 GPS tracking active", "📁 File browser ready")
                    Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show()
                } else {
                    log = emptyList()
                    Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = if (connected) Color(0xFFF87171) else Color(0xFF22D3EE))) {
                Text(if (connected) "Disconnect" else "Connect", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (log.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        log.forEach { entry -> Text(entry, fontSize = 11.sp, color = Color(0xFF34D399), modifier = Modifier.padding(vertical = 1.dp)) }
                    }
                }
            }
        }
    }
}
