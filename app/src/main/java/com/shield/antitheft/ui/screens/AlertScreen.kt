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
fun AlertScreen(navController: NavController) {
    var emailAlerts by remember { mutableStateOf(true) }
    var smsAlerts by remember { mutableStateOf(false) }
    var telegramAlerts by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("emergency@gmail.com") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Alert Config", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Alert Channels", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFBBF24))
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AlertToggle("Email Alerts", "Send evidence to email", emailAlerts) { emailAlerts = it }
                    AlertToggle("SMS Alerts", "Send SMS to emergency contacts", smsAlerts) { smsAlerts = it }
                    AlertToggle("Telegram Alerts", "Send via Telegram bot", telegramAlerts) { telegramAlerts = it }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Emergency Email") }, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun AlertToggle(title: String, desc: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) { Text(title, fontSize = 14.sp, color = Color.White); Text(desc, fontSize = 11.sp, color = Color.Gray) }
        Switch(checked = checked, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFFFBBF24)))
    }
}
