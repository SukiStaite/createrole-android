package com.sukistaite.createrole.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukistaite.createrole.App
import com.sukistaite.createrole.data.Config
import com.sukistaite.createrole.ui.components.GlassCard
import com.sukistaite.createrole.ui.components.WashiTitle

@Composable
fun TestScreen() {
    var preview by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { App.toArchive() }) { Text("← 档案") }
            WashiTitle("小剧场 · 祈宝试演")
        }
        GlassCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Column(Modifier.padding(14.dp)) {
                Text("选择一个开场场景，预览「她」的第一句台词。", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Config.TEST_SCENES.forEach { sc ->
            Surface(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ) {
                Column(Modifier.clickable {
                    val name = App.state.archive().name
                    preview = "${sc.icon} ${sc.title}\n\n${sc.desc}\n\n「早上好呀，辞宝…」${name}揉着眼睛，声音带着刚醒的软糯。今天也要一起度过呀。"
                }.padding(14.dp)) {
                    Text("${sc.icon} ${sc.title}", fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(sc.desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        if (preview.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(preview, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
