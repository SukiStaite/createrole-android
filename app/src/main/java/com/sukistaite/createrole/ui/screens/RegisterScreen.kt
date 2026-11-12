package com.sukistaite.createrole.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukistaite.createrole.App
import com.sukistaite.createrole.data.Engine
import com.sukistaite.createrole.data.Schema
import com.sukistaite.createrole.ui.components.GlassCard
import com.sukistaite.createrole.ui.components.WashiTitle

@Composable
fun RegisterScreen() {
    val preview = Engine.registerPreview(App.state.answers)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { App.toCheck() }) { Text("← 检查") }
            WashiTitle("体系登记流水")
        }
        GlassCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Column(Modifier.padding(14.dp)) {
                preview.forEach { p ->
                    Row(Modifier.padding(vertical = 4.dp)) {
                        Text(p[0] + "：", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(p[1], fontSize = 13.sp)
                        Spacer(Modifier.weight(1f))
                        Text(p[2], fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        Schema.REGISTER_LIST.forEach { r ->
            val checked = App.state.registers[r.id] ?: false
            Surface(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                color = if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ) {
                Row(Modifier.clickable { App.toggleRegister(r.id) }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(r.icon, fontSize = 18.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(r.title, fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        Text(r.sub, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.weight(1f))
                    Checkbox(checked = checked, onCheckedChange = { App.toggleRegister(r.id) })
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            App.addStamp("体系")
            if (App.state.registers.values.all { it }) App.addStamp("完整")
            App.toArchive()
        }, Modifier.fillMaxWidth().height(48.dp)) { Text("完成登记 → 档案 🎀") }
        Spacer(Modifier.height(16.dp))
    }
}
