package com.shield.antitheft.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ReverseMethod(val title: String, val desc: String, val icon: String, val color: Color, val details: List<String>, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReverseScreen(navController: NavController) {
    val context = LocalContext.current

    val methods = listOf(
        ReverseMethod("Reverse SSH Tunnel", "Phone creates SSH tunnel to your server. You connect back through it.", "🔗", Color(0xFF22D3EE),
            listOf("Phone initiates SSH connection to your server", "Creates reverse tunnel: server:8080 → phone:8080", "You SSH into your server from anywhere", "Connect to localhost:8080 to access phone", "Full shell access to thief's device", "Browse files via SFTP", "Stream camera feed in real-time"),
            "val session = JSch().getSession(\"user\", \"your-server.com\", 22)\nsession.setPassword(\"password\")\nsession.setConfig(\"StrictHostKeyChecking\", \"no\")\nsession.connect()\nsession.setPortForwardingR(0, \"localhost\", 8080)\n// Now connect to your-server.com:8080 → thief's device"),
        ReverseMethod("Ngrok Reverse Proxy", "Phone runs ngrok exposing ADB to public URL. No server needed.", "🚇", Color(0xFF34D399),
            listOf("Phone downloads and runs ngrok binary", "Creates public URL for local ADB port", "You connect via public ngrok URL", "Full ADB access from anywhere", "No server infrastructure needed", "Free tier: 1 tunnel, 40 connections/min", "URL changes each restart (use reserved for fixed)"),
            "Runtime.getRuntime().exec(\"./ngrok tcp 5555\")\n// Read stdout for public URL\nval url = reader.readLine() // tcp://0.tcp.ngrok.io:12345\n// Connect: adb connect 0.tcp.ngrok.io:12345"),
        ReverseMethod("Cloudflare Tunnel", "Enterprise free tunnel via Cloudflare. Custom domain, HTTPS.", "☁️", Color(0xFFFBBF24),
            listOf("Install cloudflared on device", "Authenticate with your Cloudflare account", "Create tunnel to localhost:8080", "Access via your custom domain", "Automatic HTTPS encryption", "Free for unlimited bandwidth", "Persistent across reboots"),
            "cloudflared tunnel create shield-tunnel\ncloudflared tunnel route dns shield-tunnel shield.yourdomain.com\ncloudflared tunnel run --url localhost:8080 shield-tunnel\n// Access at: https://shield.yourdomain.com"),
        ReverseMethod("WebRTC P2P Direct", "Browser-to-phone P2P. No server after handshake. Ultra-low latency.", "📹", Color(0xFF8B5CF6),
            listOf("Use STUN/TURN servers for NAT traversal", "Establish peer-to-peer connection", "Browser connects directly to phone", "Stream video, audio, data channels", "No server stores your data", "End-to-end encrypted", "Sub-100ms latency"),
            "val peerConnection = PeerConnectionFactory.builder().createPeerConnection(iceServers, observer)\nval dataChannel = peerConnection.createDataChannel(\"commands\", DataChannelInit())\ndataChannel.send(DataChannel.Buffer(ByteBuffer.wrap(\"lock\".toByteArray())))\n// P2P direct connection established"),
        ReverseMethod("Tor Hidden Service", "Phone becomes .onion service. Anonymous, firewall-proof access.", "🧅", Color(0xFFA78BFA),
            listOf("Phone runs Tor client", "Creates hidden service on port 8080", "You get .onion address", "Connect via Tor Browser from anywhere", "Completely anonymous", "Bypasses all firewalls", "No port forwarding needed"),
            "val tor = TorClient()\ntor.createHiddenService(8080)\nval onionAddress = tor.getOnionAddress()\n// onionAddress = abcdef123456.onion\n// Connect via Tor Browser to abcdef123456.onion:8080"),
        ReverseMethod("DNS Tunnel (Iodine)", "Tunnel IP over DNS. Works on captive portals, restricted WiFi.", "🌐", Color(0xFFF87171),
            listOf("Phone runs iodine client", "Tunnels all traffic over DNS queries", "Works on WiFi that only allows DNS", "Bypasses captive portals (hotels, airports)", "You run iodine server on your VPS", "Full IP connectivity through DNS", "~100KB/s speed (sufficient for commands)"),
            "# On your server:\niodined -P password 10.0.0.1 tunnel.yourdomain.com\n# On phone:\niodine -P password tunnel.yourdomain.com\n# Now you can SSH to 10.0.0.2 (phone)"),
        ReverseMethod("WebSocket Shell", "Phone connects to your server. Send commands, get output.", "💻", Color(0xFF22D3EE),
            listOf("Phone maintains WebSocket to your server", "You send commands via web dashboard", "Phone executes and returns output", "Real-time bidirectional communication", "Low overhead, battery efficient", "Works through most firewalls", "Auto-reconnect on disconnect"),
            "val ws = OkHttpClient().newWebSocket(Request.Builder().url(\"wss://server.com/ws\").build(), object : WebSocketListener() {\n    override fun onMessage(ws: WebSocket, text: String) {\n        val output = Runtime.getRuntime().exec(text).inputStream.bufferedReader().readText()\n        ws.send(output)\n    }\n})"),
        ReverseMethod("MQTT C2 Channel", "Lightweight IoT protocol. Phone subscribes to commands.", "📡", Color(0xFF059669),
            listOf("Phone connects to MQTT broker", "Subscribes to command topic", "You publish commands to topic", "Phone executes and publishes result", "Extremely lightweight protocol", "Ideal for battery-constrained devices", "QoS levels ensure delivery"),
            "val mqtt = MqttAndroidClient(ctx, \"tcp://broker.emqx.io:1883\", deviceId)\nmqtt.connect()\nmqtt.subscribe(\"cmd/\" + deviceId, 1) { _, msg ->\n    executeCommand(String(msg.payload))\n    mqtt.publish(\"result/\" + deviceId, MqttMessage(output.toByteArray()))\n}"),
        ReverseMethod("Telegram Bot Control", "Control phone through Telegram chat. Easiest setup.", "🤖", Color(0xFF0A84FF),
            listOf("Create Telegram bot via @BotFather", "Phone connects as bot client", "You send commands to bot in chat", "Bot forwards to phone for execution", "Phone returns output to chat", "Works on any network with internet", "No server needed"),
            "val bot = TelegramBot(\"YOUR_BOT_TOKEN\")\nbot.onCommand(\"photo\") { msg ->\n    val photo = captureSilentPhoto()\n    bot.sendPhoto(msg.chat.id, photo)\n}\nbot.onCommand(\"lock\") { devicePolicyManager.lockNow() }\nbot.startPolling()"),
        ReverseMethod("Bluetooth PAN", "Connect via Bluetooth. Works offline without internet.", "🔵", Color(0xFF6366F1),
            listOf("Phone creates Bluetooth PAN", "You connect your device via Bluetooth", "Direct device-to-device connection", "No internet required", "Range: ~10-100m depending on class", "Useful when thief is nearby", "Low latency, decent bandwidth"),
            "val adapter = BluetoothAdapter.getDefaultAdapter()\nadapter.listenUsingRfcommWithServiceRecord(\"Shield\", UUID.fromString(\"00001101-0000-1000-8000-00805F9B34FB\"))\n// Accept incoming Bluetooth connections\n// Send commands over Bluetooth socket"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Reverse Connection", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF22D3EE).copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("10 Connection Methods", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22D3EE))
                        Text("Connect to thief's device through any network", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
            items(methods) { method ->
                var expanded by remember { mutableStateOf(false) }
                var showCode by remember { mutableStateOf(false) }
                
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { expanded = !expanded },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(method.icon, fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(method.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = method.color)
                                Text(method.desc, fontSize = 10.sp, color = Color.Gray)
                            }
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                        }
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                method.details.forEach { detail ->
                                    Text("• $detail", fontSize = 10.sp, color = Color(0xFFA0A0B8), modifier = Modifier.padding(vertical = 1.dp))
                                }
                                Spacer(Modifier.height(8.dp))
                                Surface(modifier = Modifier.fillMaxWidth().clickable { showCode = !showCode },
                                    color = Color(0xFF0F172A), shape = MaterialTheme.shapes.small) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Code, null, tint = method.color, modifier = Modifier.size(14.dp))
                                            Spacer(Modifier.width(6.dp))
                                            Text("Implementation", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                            Text(if (showCode) "Hide" else "View", fontSize = 9.sp, color = method.color)
                                        }
                                        AnimatedVisibility(visible = showCode) {
                                            Text(method.code, fontSize = 7.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF34D399), modifier = Modifier.padding(top = 6.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
