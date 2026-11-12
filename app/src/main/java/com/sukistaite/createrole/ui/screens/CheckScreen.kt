package com.sukistaite.createrole.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukistaite.createrole.App
import com.sukistaite.createrole.ui.components.GlassCard
import com.sukistaite.createrole.ui.components.WashiTitle

@Composable
fun CheckScreen() {
    val checks = App.state.checks.ifEmpty { com.sukistaite.createrole.data.Engine.runChecks(App.state.meta, App.state.answers) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { App.toArchive() }) { Text("← 档案") }
            WashiTitle("世界观检查 · ${checks.count { it.status == "pass" }}/${checks.size} 通过")
        }
        checks.forEach { c ->
            val color = when (c.status) {
                "pass" -> Color(0xFF4c9a6a)
                "fail" -> Color(0xFFc05a5a)
                else -> Color(0xFFd99a3d)
            }
            GlassCard(Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(c.title, fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Text(when (c.status) { "pass" -> "通过"; "fail" -> "失败"; else -> "人工" },
                            fontSize = 12.sp, color = color)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(c.desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text(c.reason, fontSize = 12.sp, color = color)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = { App.toRegister() }, Modifier.fillMaxWidth().height(48.dp)) { Text("下一步 → 体系登记 📋") }
        Spacer(Modifier.height(16.dp))
    }
}
