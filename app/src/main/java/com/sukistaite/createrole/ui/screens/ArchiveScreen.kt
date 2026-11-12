package com.sukistaite.createrole.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukistaite.createrole.App
import com.sukistaite.createrole.data.Engine
import com.sukistaite.createrole.ui.components.GlassCard
import com.sukistaite.createrole.ui.components.StampChip
import com.sukistaite.createrole.ui.components.WashiTitle

@Composable
fun ArchiveScreen() {
    val arch = App.state.archive()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { App.toCheck() }) { Text("✔ 检查") }
            TextButton(onClick = { App.toRegister() }) { Text("📋 登记") }
            TextButton(onClick = { App.toTest() }) { Text("🎭 测试") }
            TextButton(onClick = { App.toSettings() }) { Text("⚙") }
        }
        WashiTitle("角色档案")
        Spacer(Modifier.height(4.dp))
        Text(arch.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        if (App.state.stamps.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                App.state.stamps.forEach { StampChip(it) }
            }
        }
        Spacer(Modifier.height(14.dp))
        GlassCard {
            Column(Modifier.padding(14.dp)) {
                Text("🎯 一句话人设内核", fontSize = 13.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(6.dp))
                Text(arch.core, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
            }
        }
        Spacer(Modifier.height(14.dp))
        arch.sections.forEach { sec ->
            GlassCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                Column(Modifier.padding(14.dp)) {
                    WashiTitle(sec.title, accent = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    sec.rows.forEach { r ->
                        Row(Modifier.padding(vertical = 3.dp)) {
                            Text(r.label + "：", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(r.value, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                App.shareText("角色档案 · ${arch.name}", Engine.archiveToMarkdown(arch, App.state.meta))
            }, Modifier.weight(1f)) { Text("导出 MD") }
            OutlinedButton(onClick = {
                App.shareText("角色档案 JSON", Engine.archiveToJSON(arch, App.state.answers, mapOf(), App.state.registers, App.state.card).toString())
            }, Modifier.weight(1f)) { Text("导出 JSON") }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = {
            App.shareText("SillyTavern 角色卡", Engine.archiveToSillyTavern(arch, App.state.meta, 2, App.state.card).toString())
        }, Modifier.fillMaxWidth()) { Text("导出角色卡 V2") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { App.toWelcome() }, Modifier.fillMaxWidth()) { Text("🏠 回到主页") }
        Spacer(Modifier.height(16.dp))
    }
}
