package com.shield.antitheft.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import com.shield.antitheft.ui.screens.*

data class NavItem(val route: String, val icon: ImageVector, val label: String)

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val items = listOf(
        NavItem("dashboard", Icons.Default.Home, "Dashboard"),
        NavItem("modules", Icons.Default.Extension, "Modules"),
        NavItem("command", Icons.Default.FlashOn, "Commands"),
        NavItem("simulator", Icons.Default.PlayArrow, "Simulator"),
        NavItem("evidence", Icons.Default.Visibility, "Evidence"),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1E293B)) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = navController.currentBackStackEntryAsState().value?.destination?.route == item.route,
                        onClick = { navController.navigate(item.route) { popUpTo("dashboard") } },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontSize = 8.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF22D3EE),
                            indicatorColor = Color(0x3322D3EE)
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = "dashboard", modifier = Modifier.padding(padding)) {
            composable("dashboard") { DashboardScreen(navController) }
            composable("modules") { ModulesScreen(navController) }
            composable("command") { CommandScreen(navController) }
            composable("simulator") { SimulatorScreen(navController) }
            composable("evidence") { EvidenceScreen(navController) }
            composable("reverse") { ReverseScreen(navController) }
            composable("install") { InstallScreen(navController) }
            composable("stealth") { StealthScreen(navController) }
            composable("security") { SecurityLabScreen(navController) }
            composable("alerts") { AlertScreen(navController) }
            composable("bypass") { BypassScreen(navController) }
            composable("restrictions") { RestrictionsScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
        }
    }
}
