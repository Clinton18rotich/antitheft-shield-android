package com.shield.antitheft.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("shield_prefs", Context.MODE_PRIVATE)
    
    var emergencyEmail by remember { mutableStateOf(prefs.getString("emergency_email", "") ?: "") }
    var emergencyPhone by remember { mutableStateOf(prefs.getString("emergency_phone", "") ?: "") }
    var backupEmail by remember { mutableStateOf(prefs.getString("backup_email", "") ?: "") }
    var secretPIN by remember { mutableStateOf(prefs.getString("secret_pin", "7443") ?: "7443") }

    fun save() {
        prefs.edit()
            .putString("emergency_email", emergencyEmail)
            .putString("emergency_phone", emergencyPhone)
            .putString("backup_email", backupEmail)
            .putString("secret_pin", secretPIN)
            .apply()
        Toast.makeText(context, "✅ Settings saved", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFBBF24).copy(alpha = 0.1f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🚨 Emergency Contacts", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFBBF24))
                    Text("These contacts receive alerts when theft is detected", fontSize = 10.sp, color = Color.Gray)
                }
            }

            OutlinedTextField(value = emergencyEmail, onValueChange = { emergencyEmail = it }, label = { Text("Primary Email") }, placeholder = { Text("your@gmail.com") }, leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFF22D3EE)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = emergencyPhone, onValueChange = { emergencyPhone = it }, label = { Text("Emergency Phone") }, placeholder = { Text("+254700000000") }, leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color(0xFF34D399)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = backupEmail, onValueChange = { backupEmail = it }, label = { Text("Backup Email") }, placeholder = { Text("backup@gmail.com") }, leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFFA78BFA)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Divider(color = Color(0xFF333333))
            
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF8B5CF6).copy(alpha = 0.1f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔐 Security", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                }
            }

            OutlinedTextField(value = secretPIN, onValueChange = { if (it.length <= 6) secretPIN = it.filter { c -> c.isDigit() } }, label = { Text("Secret PIN (Calculator)") }, placeholder = { Text("7443") }, leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFFFBBF24)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { save() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22D3EE))) {
                Text("💾 Save Settings", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
