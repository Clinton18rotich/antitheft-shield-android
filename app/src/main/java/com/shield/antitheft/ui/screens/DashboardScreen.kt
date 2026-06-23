package com.shield.antitheft.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class QuickAction(val label: String, val icon: ImageVector, val route: String, val color: Color)

@Composable
fun DashboardScreen(navController: NavController) {
    val actions = listOf(
        QuickAction("Simulator", Icons.Default.PlayArrow, "simulator", Color(0xFFF87171)),
        QuickAction("Modules", Icons.Default.Extension, "modules", Color(0xFFA78BFA)),
        QuickAction("Commands", Icons.Default.FlashOn, "command", Color(0xFFFB923C)),
        QuickAction("Evidence", Icons.Default.Visibility, "evidence", Color(0xFF34D399)),
        QuickAction("Reverse", Icons.Default.SwapHoriz, "reverse", Color(0xFF22D3EE)),
        QuickAction("Install", Icons.Default.Download, "install", Color(0xFFA78BFA)),
        QuickAction("Stealth", Icons.Default.VisibilityOff, "stealth", Color(0xFF8B5CF6)),
        QuickAction("Security Lab", Icons.Default.PhoneAndroid, "security", Color(0xFF34D399)),
        QuickAction("Alerts", Icons.Default.Notifications, "alerts", Color(0xFFFBBF24)),
        QuickAction("Bypass", Icons.Default.Lightbulb, "bypass", Color(0xFFFBBF24)),
        QuickAction("Restrictions", Icons.Default.Warning, "restrictions", Color(0xFFF87171)),
    )

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("AntiTheft Shield", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
        Text("System Active • All modules running", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        // Stats row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("Protected", "Active", Color(0xFF22D3EE), modifier = Modifier.weight(1f))
            StatCard("18", "Modules", Color(0xFFA78BFA), modifier = Modifier.weight(1f))
            StatCard("3", "Threats", Color(0xFFF87171), modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Quick actions grid
        Text("Quick Actions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        var rowItems = actions.chunked(3)
        rowItems.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { action ->
                    Card(
                        modifier = Modifier.weight(1f).padding(vertical = 4.dp).clickable { navController.navigate(action.route) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(action.icon, contentDescription = null, tint = action.color, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(action.label, fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
                // Fill remaining spots if row has less than 3
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 10.sp, color = Color.Gray)
        }
    }
}
