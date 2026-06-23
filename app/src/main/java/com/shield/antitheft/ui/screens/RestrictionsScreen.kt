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
fun RestrictionsScreen(navController: NavController) {
    val stats = listOf("23" to "Features Blocked", "15" to "Features Limited", "6" to "Alternatives", "100%" to "Email Still Works")
    val restrictions = listOf(
        "SMS Sending/Reading" to "Blocked on Android 14+",
        "Background Camera" to "Requires foreground service",
        "Continuous GPS" to "Throttled to 30-min intervals",
        "Background Audio" to "Limited to 5 minutes",
        "App Hiding" to "Android 15 blocks hidden apps",
        "Auto-start on Boot" to "Restricted on new devices",
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Android 15 Restrictions", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    stats.forEach { (val_, label) ->
                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                Text(val_, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF87171))
                                Text(label, fontSize = 9.sp, color = Color.Gray)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Key Restrictions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(restrictions) { (feature, status) ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) { Text(feature, fontSize = 13.sp, color = Color.White); Text(status, fontSize = 10.sp, color = Color(0xFFF87171)) }
                    }
                }
            }
        }
    }
}
