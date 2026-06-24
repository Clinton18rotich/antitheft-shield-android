package com.shield.antitheft.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Restriction(val feature: String, val status: String, val icon: String, val color: Color, val desc: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestrictionsScreen(navController: NavController) {
    val stats = listOf("23" to "Blocked", "15" to "Limited", "6" to "Alternatives", "100%" to "Email OK")

    val restrictions = listOf(
        Restriction("SMS Sending/Reading", "Blocked", "💬", Color(0xFFF87171), "Android 14+ blocks background SMS for all apps. Use Email/FCM/MQTT instead."),
        Restriction("Background Camera", "Limited", "📸", Color(0xFFFB923C), "Camera requires foreground service with persistent notification. Disguise as system service."),
        Restriction("Continuous GPS", "Limited", "📍", Color(0xFFFBBF24), "GPS throttled to 30-min intervals in background. Use navigation disguise or sensor dead reckoning."),
        Restriction("IMEI Access", "Blocked", "📱", Color(0xFFF87171), "Android 10+ blocks IMEI. Use Settings.Global ANDROID_ID or Advertising ID instead."),
        Restriction("Background Audio", "Limited", "🎤", Color(0xFFFB923C), "Background audio limited to 5 minutes. Use chunked recording with auto-restart."),
        Restriction("Storage Access", "Limited", "💾", Color(0xFFFBBF24), "Scoped storage on Android 11+. Request MANAGE_EXTERNAL_STORAGE or use app-specific directory."),
        Restriction("Background Execution", "Limited", "⚡", Color(0xFFFB923C), "Android 8+ limits background services. Use WorkManager or foreground service."),
        Restriction("Call Log & Contacts", "Blocked", "📞", Color(0xFFF87171), "Restricted access. Request default dialer role or READ_CALL_LOG permission explicitly."),
        Restriction("App Hiding", "Limited", "👻", Color(0xFFFB923C), "Android 15 restricts hidden apps. Use component disable trick or root for full hiding."),
        Restriction("Auto-start on Boot", "Limited", "🔌", Color(0xFFFBBF24), "Restricted on newer devices. Use BOOT_COMPLETED receiver + foreground service workaround."),
        Restriction("Clipboard Access", "Limited", "📋", Color(0xFFFB923C), "Android 10+ limits background clipboard. Only foreground apps can read clipboard."),
        Restriction("Notification Listener", "Limited", "🔔", Color(0xFFFBBF24), "Requires user to manually enable in Settings. Guide them or use Accessibility Service."),
    )

    val reasons = listOf(
        Triple(Icons.Default.Shield, "Privacy Protection", "Prevent apps from secretly accessing user data"),
        Triple(Icons.Default.Lock, "Anti-Stalkerware", "Block spyware that tracks without knowledge"),
        Triple(Icons.Default.PhoneAndroid, "Battery Life", "Stop background apps draining battery"),
        Triple(Icons.Default.Info, "User Transparency", "Users must know when sensors are active"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Android 15 Restrictions", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Android 15", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Surface(color = Color(0xFFF87171).copy(alpha = 0.2f), shape = MaterialTheme.shapes.small) { Text("API 35", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = Color(0xFFF87171), fontWeight = FontWeight.Bold) }
                }
                Text("How Google strengthened privacy and affected anti-theft apps", fontSize = 11.sp, color = Color.Gray)
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    stats.forEach { (val_, label) ->
                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(val_, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF87171))
                                Text(label, fontSize = 9.sp, color = Color.Gray)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Why Google Restricted Features", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    reasons.forEach { (icon, title, desc) ->
                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(icon, null, tint = Color(0xFF22D3EE), modifier = Modifier.size(20.dp))
                                Text(title, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(desc, fontSize = 7.sp, color = Color.Gray)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("12 Key Restrictions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(8.dp))
            }
            items(restrictions) { r ->
                var expanded by remember { mutableStateOf(false) }
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).clickable { expanded = !expanded },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(r.icon, fontSize = 22.sp)
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(r.feature, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Row { Surface(color = r.color.copy(alpha = 0.2f), shape = MaterialTheme.shapes.small) { Text(r.status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 9.sp, color = r.color) } }
                            }
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                        }
                        AnimatedVisibility(visible = expanded) {
                            Text(r.desc, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
