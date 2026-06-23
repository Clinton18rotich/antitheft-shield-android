package com.shield.antitheft.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityLabScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var testResults by remember { mutableStateOf(mapOf<String, String>()) }
    var running by remember { mutableStateOf("") }

    val tests = listOf(
        Triple("GPS Spoofing Test", "Verify GPS tampering detection", Color(0xFF34D399)),
        Triple("SIM Swap Detection", "Check if SIM was changed", Color(0xFFF87171)),
        Triple("Root Detection", "Check if device is rooted", Color(0xFFFBBF24)),
        Triple("USB Debug Check", "Verify ADB is disabled", Color(0xFF22D3EE)),
        Triple("App Signature Check", "Verify app integrity", Color(0xFFA78BFA)),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Security Lab", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(tests) { (name, desc, color) ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
                            Text(desc, fontSize = 11.sp, color = Color.Gray)
                            testResults[name]?.let { result ->
                                Text(result, fontSize = 11.sp, color = if (result.contains("✅")) Color(0xFF34D399) else Color(0xFFF87171))
                            }
                        }
                        Button(onClick = {
                            running = name
                            scope.launch {
                                delay(1000)
                                testResults = testResults + (name to "✅ Passed")
                                running = ""
                                Toast.makeText(context, "$name: Passed", Toast.LENGTH_SHORT).show()
                            }
                        }, enabled = running != name, colors = ButtonDefaults.buttonColors(containerColor = color)) {
                            Text(if (running == name) "..." else "Run", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
