package com.shield.antitheft.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun EvidenceScreen(navController: NavController) {
    val context = LocalContext.current
    var activeStep by remember { mutableStateOf(0) }
    
    val steps = listOf(
        listOf("📸", "Capture", "Silent photo + video of thief", "Execute"),
        listOf("🎤", "Record", "Ambient audio for voice analysis", "Execute"),
        listOf("📍", "Locate", "GPS + WiFi coordinates", "Execute"),
        listOf("📧", "Send", "Email to emergency contacts", "Execute"),
        listOf("☁️", "Upload", "Cloud backup to secure server", "Execute"),
        listOf("📋", "Report", "Generate evidence report PDF", "Execute"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Evidence Flow", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(steps.size) { i ->
                val (icon, title, desc, action) = steps[i]
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
                    activeStep = i + 1
                    Toast.makeText(context, "✅ $title: Executed", Toast.LENGTH_SHORT).show()
                }, colors = CardDefaults.cardColors(containerColor = if (i < activeStep) Color(0xFF0F766E) else Color(0xFF1E293B))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(icon, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Step ${i+1}: $title", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
                            Text(desc, fontSize = 11.sp, color = Color.Gray)
                        }
                        if (i < activeStep) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF34D399))
                        else Text(action, fontSize = 10.sp, color = Color(0xFF22D3EE))
                    }
                }
                if (i < steps.size - 1) {
                    Box(modifier = Modifier.fillMaxWidth().padding(start = 24.dp)) {
                        Icon(Icons.Default.ArrowDownward, null, tint = if (i < activeStep) Color(0xFF34D399) else Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
