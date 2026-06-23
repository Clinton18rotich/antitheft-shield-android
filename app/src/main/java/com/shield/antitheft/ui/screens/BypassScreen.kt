package com.shield.antitheft.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BypassScreen(navController: NavController) {
    val bypasses = listOf(
        "SMS → Email (SMTP)" to "Replace SMS with JavaMail",
        "Camera → Foreground Service" to "Disguised system notification",
        "GPS → Navigation Service" to "Fake navigation for unlimited GPS",
        "WiFi Triangulation" to "Location without GPS permission",
        "Cell Tower Location" to "Coarse location from towers",
        "Sensor Dead Reckoning" to "Track via accelerometer+gyro",
        "Notification Listener" to "Read thief's notifications",
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Android 15 Bypass", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(bypasses) { (title, desc) ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFBBF24))
                        Text(desc, fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
