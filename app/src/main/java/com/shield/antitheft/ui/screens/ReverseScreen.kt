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
fun ReverseScreen(navController: NavController) {
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("8080") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Reverse Connection", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Connect to Thief's Device", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = ip, onValueChange = { ip = it }, label = { Text("Device IP Address") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = port, onValueChange = { port = it }, label = { Text("Port") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22D3EE))) { Text("Connect") }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Available Commands:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("• Shell access", fontSize = 11.sp, color = Color.White)
                    Text("• File browser", fontSize = 11.sp, color = Color.White)
                    Text("• Camera stream", fontSize = 11.sp, color = Color.White)
                    Text("• Location tracking", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}
