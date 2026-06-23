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
fun StealthScreen(navController: NavController) {
    var hideIcon by remember { mutableStateOf(true) }
    var disguiseName by remember { mutableStateOf(false) }
    var fakeCalculator by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Stealth Mode", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Configuration", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StealthToggle("Hide App Icon", "App disappears from launcher", hideIcon) { hideIcon = it }
                    StealthToggle("Disguise App Name", "Shows as 'System Services'", disguiseName) { disguiseName = it }
                    StealthToggle("Fake Calculator", "Opens real calculator on wrong code", fakeCalculator) { fakeCalculator = it }
                }
            }
        }
    }
}

@Composable
fun StealthToggle(title: String, desc: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) { Text(title, fontSize = 14.sp, color = Color.White); Text(desc, fontSize = 11.sp, color = Color.Gray) }
        Switch(checked = checked, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF8B5CF6)))
    }
}
