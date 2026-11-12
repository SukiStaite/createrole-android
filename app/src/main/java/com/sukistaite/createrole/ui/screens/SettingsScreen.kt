package com.sukistaite.createrole.ui.screens

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
import com.sukistaite.createrole.data.Config
import com.sukistaite.createrole.data.Settings
import com.sukistaite.createrole.ui.components.GlassCard
import com.sukistaite.createrole.ui.components.WashiTitle

@Composable
private fun ToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit, sub: String? = null) {
    Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(label, fontSize = 14.sp)
            if (sub != null) Text(sub, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen() {
    val s = App.state.settings
    fun update(s2: Settings) { App.state.settings = s2; App.save() }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { App.toWelcome() }) { Text("←") }
            WashiTitle("设置")
        }
        GlassCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Column(Modifier.padding(14.dp)) {
                Text("🎨 主题色", fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Config.THEMES.keys.forEach { t ->
                        FilterChip(selected = s.theme == t, onClick = { update(s.copy(theme = t)) }, label = { Text(t, fontSize = 12.sp) })
                    }
                }
                ToggleRow("暗色模式", s.dark, { update(s.copy(dark = it)) })
            }
        }
        GlassCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Column(Modifier.padding(14.dp)) {
                Text("✨ 沉浸感", fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                ToggleRow("答完自动下一题", s.autoNext, { update(s.copy(autoNext = it)) }, "单题模式答完直接跳下一题")
                ToggleRow("沉浸反馈（语气词/小片段）", s.showImmersive, { update(s.copy(showImmersive = it)) })
                ToggleRow("NSFW 私密感", s.nsfwMask, { update(s.copy(nsfwMask = it)) })
                Text("动画等级", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Config.ANIM_LEVELS.forEach { a ->
                        FilterChip(selected = s.animLevel == a, onClick = { update(s.copy(animLevel = a)) }, label = { Text(a, fontSize = 12.sp) })
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text("主题色 / 暗色 / 动画已就绪", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        GlassCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Column(Modifier.padding(14.dp)) {
                Text("🧪 答题与测试", fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text("提问模式", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(selected = s.askMode == Config.ASK_SINGLE, onClick = { update(s.copy(askMode = Config.ASK_SINGLE)) }, label = { Text("单题", fontSize = 12.sp) })
                    FilterChip(selected = s.askMode == Config.ASK_BATCH, onClick = { update(s.copy(askMode = Config.ASK_BATCH)) }, label = { Text("批题", fontSize = 12.sp) })
                }
                Spacer(Modifier.height(6.dp))
                Text("记忆存档频率", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Config.MEMORY_FREQ.forEach { f ->
                        FilterChip(selected = s.memoryFreq == f, onClick = { update(s.copy(memoryFreq = f)) }, label = { Text("$f", fontSize = 12.sp) })
                    }
                }
                ToggleRow("测试模式", s.testMode, { update(s.copy(testMode = it)) })
            }
        }
        GlassCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Column(Modifier.padding(14.dp)) {
                Text("🔤 语言 / 字号", fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(selected = s.language == "zh", onClick = { update(s.copy(language = "zh")) }, label = { Text("中文", fontSize = 12.sp) })
                    FilterChip(selected = s.language == "en", onClick = { update(s.copy(language = "en")) }, label = { Text("English", fontSize = 12.sp) })
                }
                Spacer(Modifier.height(6.dp))
                Text("字号缩放", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Config.FONT_SCALES.forEach { fs ->
                        FilterChip(selected = s.fontScale == fs, onClick = { update(s.copy(fontScale = fs)) }, label = { Text("${fs.toInt()}x", fontSize = 12.sp) })
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
