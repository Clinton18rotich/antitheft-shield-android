package com.shield.antitheft.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
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
    var disableNotifications by remember { mutableStateOf(true) }
    var antiUninstall by remember { mutableStateOf(false) }
    var survivalMode by remember { mutableStateOf(true) }
    var dialerAccess by remember { mutableStateOf(true) }
    var processHiding by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Stealth Mode", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF8B5CF6).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("👻 Stealth Configuration", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                    Text("Make the app invisible while maintaining full functionality", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StealthToggle("Hide App Icon", "Removes app from launcher. Access via dialer code *#*#744335#*#*", hideIcon) { hideIcon = it }
                    StealthToggle("Disguise as System App", "Shows as 'System Services' in app list", disguiseName) { disguiseName = it }
                    StealthToggle("Fake Calculator Front", "Opens real calculator on wrong PIN. Opens shield on correct PIN", fakeCalculator) { fakeCalculator = it }
                    StealthToggle("Disable Notifications", "No visible notifications. All alerts silent", disableNotifications) { disableNotifications = it }
                    StealthToggle("Anti-Uninstall", "Prevents uninstall. Requires admin password", antiUninstall) { antiUninstall = it }
                    StealthToggle("Survival Mode", "Survives factory reset as system app (requires root)", survivalMode) { survivalMode = it }
                    StealthToggle("Dialer Code Access", "Open app via *#*#SHIELD#*#* from any dialer", dialerAccess) { dialerAccess = it }
                    StealthToggle("Process Hiding", "Obfuscate process name in task manager", processHiding) { processHiding = it }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Implementation", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Text("val pm = packageManager\npm.setComponentEnabledSetting(\n    ComponentName(this, MainActivity::class.java),\n    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,\n    PackageManager.DONT_KILL_APP\n)\n// App icon disappears from launcher\n// Still accessible via dialer code", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399))
                }
            }
        }
    }
}

@Composable
fun StealthToggle(title: String, desc: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) { Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White); Text(desc, fontSize = 10.sp, color = Color.Gray) }
        Switch(checked = checked, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF8B5CF6), checkedThumbColor = Color.White))
    }
}
