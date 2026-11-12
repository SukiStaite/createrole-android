package com.sukistaite.createrole.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukistaite.createrole.App
import com.sukistaite.createrole.data.Store
import com.sukistaite.createrole.ui.components.GlassCard
import com.sukistaite.createrole.ui.components.StampChip

@Composable
fun WelcomeScreen() {
    val hasSave = Store.hasSave(App.ctx)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { App.importUri(it.toString()) }
    }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("✨", fontSize = 52.sp)
        Spacer(Modifier.height(8.dp))
        Text("搭角色", fontSize = 34.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text("AIR A 档案工坊 · 为「她」造一个灵魂", fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(18.dp))
        if (App.state.stamps.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                App.state.stamps.forEach { StampChip(it) }
            }
            Spacer(Modifier.height(18.dp))
        }
        GlassCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { App.startNew() }, Modifier.fillMaxWidth().height(50.dp)) {
                    Text("开始搭角色 ✨")
                }
                if (hasSave) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(onClick = { App.resume() }, Modifier.fillMaxWidth().height(48.dp)) {
                        Text("继续上次")
                    }
                }
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { launcher.launch(arrayOf("text/plain", "application/json")) },
                    Modifier.fillMaxWidth().height(48.dp),
                ) { Text("📥 导入档案 / 角色卡") }
                Spacer(Modifier.height(10.dp))
                TextButton(onClick = { App.toSettings() }) { Text("⚙️ 设置") }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("V${com.sukistaite.createrole.data.Config.VERSION} · ${com.sukistaite.createrole.data.Config.CODE_NAME}",
            fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
