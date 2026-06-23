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
fun InstallScreen(navController: NavController) {
    val apps = listOf("Shield Companion", "Hidden Tracker", "Evidence Sync", "SMS Forwarder")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Remote Install", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Install Companion Apps", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA78BFA))
            Spacer(modifier = Modifier.height(16.dp))
            apps.forEach { app ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) { Text(app, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White); Text("v1.0 · 2.3 MB", fontSize = 11.sp, color = Color.Gray) }
                        Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA78BFA))) { Text("Install") }
                    }
                }
            }
        }
    }
}
