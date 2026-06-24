package com.shield.antitheft.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

data class SecurityTest(val name: String, val desc: String, val color: Color, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityLabScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var results by remember { mutableStateOf(mapOf<String, Pair<Boolean, String>>()) }
    var running by remember { mutableStateOf("") }

    fun checkRoot(): Pair<Boolean, String> {
        val paths = arrayOf("/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su")
        for (path in paths) { if (File(path).exists()) return Pair(false, "Root binary found: $path") }
        try { Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su")); return Pair(false, "SU command accessible") } catch (e: Exception) {}
        return Pair(true, "No root access detected")
    }

    fun checkADB(): Pair<Boolean, String> {
        val adb = Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0)
        return if (adb == 1) Pair(false, "USB Debugging is ENABLED - security risk!") else Pair(true, "USB Debugging disabled")
    }

    fun checkDeveloperOptions(): Pair<Boolean, String> {
        val dev = Settings.Global.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0)
        return if (dev == 1) Pair(false, "Developer Options enabled") else Pair(true, "Developer Options disabled")
    }

    fun checkUnknownSources(): Pair<Boolean, String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (context.packageManager.canRequestPackageInstalls()) Pair(false, "Unknown sources allowed") else Pair(true, "Unknown sources blocked")
        } else {
            val val_ = Settings.Secure.getInt(context.contentResolver, Settings.Secure.INSTALL_NON_MARKET_APPS, 0)
            if (val_ == 1) Pair(false, "Unknown sources allowed") else Pair(true, "Unknown sources blocked")
        }
    }

    fun checkEmulator(): Pair<Boolean, String> {
        val isEmu = (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || "google_sdk" == Build.PRODUCT)
        return if (isEmu) Pair(false, "Running on emulator - not a real device") else Pair(true, "Real device detected")
    }

    fun checkEncryption(): Pair<Boolean, String> {
        val dm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? android.app.admin.DevicePolicyManager
        val status = dm?.storageEncryptionStatus
        return when (status) {
            android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE -> Pair(true, "Storage encrypted")
            android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE -> Pair(false, "Storage NOT encrypted")
            else -> Pair(true, "Encryption status unknown")
        }
    }

    val tests = listOf(
        SecurityTest("Root Detection", "Check if device is rooted", Color(0xFFFBBF24), "val paths = arrayOf(\"/system/app/Superuser.apk\",\"/sbin/su\")\nfor (path in paths) { if (File(path).exists()) return true }"),
        SecurityTest("USB Debug Check", "Verify ADB is disabled", Color(0xFF22D3EE), "val adb = Settings.Global.getInt(contentResolver, ADB_ENABLED, 0)\nreturn adb == 0"),
        SecurityTest("Developer Options", "Check if developer mode is on", Color(0xFFFB923C), "val dev = Settings.Global.getInt(contentResolver, DEVELOPMENT_SETTINGS_ENABLED, 0)\nreturn dev == 0"),
        SecurityTest("Unknown Sources", "Check if untrusted APKs allowed", Color(0xFFA78BFA), "if (Build.VERSION.SDK_INT >= O) {\n    return !packageManager.canRequestPackageInstalls()\n}"),
        SecurityTest("Emulator Detection", "Verify running on real device", Color(0xFF34D399), "val isEmu = Build.FINGERPRINT.startsWith(\"generic\")\n    || Build.MODEL.contains(\"Emulator\")\nreturn !isEmu"),
        SecurityTest("Storage Encryption", "Check if device storage is encrypted", Color(0xFF8B5CF6), "val dm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager\nreturn dm.storageEncryptionStatus == ENCRYPTION_STATUS_ACTIVE"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Security Lab", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF34D399).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("6 Security Tests", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399))
                        Text("Tap Run to execute real security checks on this device", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
            items(tests) { test ->
                var showCode by remember { mutableStateOf(false) }
                val result = results[test.name]
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(test.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = test.color)
                                Text(test.desc, fontSize = 11.sp, color = Color.Gray)
                                if (result != null) {
                                    Text(if (result.first) "✅ ${result.second}" else "❌ ${result.second}",
                                        fontSize = 11.sp, color = if (result.first) Color(0xFF34D399) else Color(0xFFF87171))
                                }
                            }
                            Button(onClick = {
                                running = test.name
                                scope.launch {
                                    delay(800)
                                    val r = when (test.name) {
                                        "Root Detection" -> checkRoot()
                                        "USB Debug Check" -> checkADB()
                                        "Developer Options" -> checkDeveloperOptions()
                                        "Unknown Sources" -> checkUnknownSources()
                                        "Emulator Detection" -> checkEmulator()
                                        "Storage Encryption" -> checkEncryption()
                                        else -> Pair(false, "Unknown test")
                                    }
                                    results = results + (test.name to r)
                                    running = ""
                                    Toast.makeText(context, test.name + ": " + if (r.first) "PASSED" else "FAILED", Toast.LENGTH_SHORT).show()
                                }
                            }, enabled = running.isEmpty(), colors = ButtonDefaults.buttonColors(containerColor = test.color)) {
                                Text(if (running == test.name) "..." else "Run", fontSize = 11.sp)
                            }
                        }
                        TextButton(onClick = { showCode = !showCode }) {
                            Text(if (showCode) "Hide Code" else "View Code", fontSize = 10.sp, color = test.color)
                        }
                        if (showCode) {
                            Surface(color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                Text(test.code, fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
