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
import com.sukistaite.createrole.data.Config
import com.sukistaite.createrole.ui.components.GlassCard
import com.sukistaite.createrole.ui.components.WashiTitle

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun ChoiceRow(label: String, options: List<String>, selected: String?, onSelect: (String) -> Unit) {
    Column(Modifier.padding(bottom = 14.dp)) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { opt ->
                val sel = opt == selected
                FilterChip(
                    selected = sel,
                    onClick = { onSelect(opt) },
                    label = { Text(opt, fontSize = 13.sp) },
                )
            }
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun MetaScreen() {
    val m = App.state.meta
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { App.toWelcome() }) { Text("←") }
            WashiTitle("元信息 · 她是谁")
        }
        // 预设
        GlassCard {
            Column(Modifier.padding(14.dp)) {
                Text("快速预设", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Config.PRESETS.forEach { p ->
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        ) {
                            Column(
                                Modifier.clickable { App.applyPreset(p) }.padding(horizontal = 12.dp, vertical = 8.dp),
                            ) {
                                Text("${p.icon} ${p.name}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text(p.desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
        GlassCard {
            Column(Modifier.padding(16.dp)) {
                ChoiceRow("性别", Config.GENDER_OPTIONS, m.gender) { App.state.meta = App.state.meta.copy(gender = it) }
                ChoiceRow("体量（题数）", Config.VOLUME_OPTIONS.map { it.toString() }, m.volume.toString()) {
                    App.state.meta = App.state.meta.copy(volume = it.toInt())
                }
                ChoiceRow("什么向", Config.ORIENTATION_OPTIONS, m.orientation) { App.state.meta = App.state.meta.copy(orientation = it) }
                ChoiceRow("DLC9", Config.DLC9_OPTIONS, m.dlc9) { App.state.meta = App.state.meta.copy(dlc9 = it) }
                ChoiceRow("种族", Config.RACE_OPTIONS, m.race) {
                    App.state.meta = App.state.meta.copy(race = it, raceMulti = it == "多形态")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("严格命名", fontSize = 13.sp)
                    Spacer(Modifier.weight(1f))
                    Switch(checked = m.strictNaming, onCheckedChange = { App.state.meta = App.state.meta.copy(strictNaming = it) })
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { App.buildPlanAndStart() }, Modifier.fillMaxWidth().height(50.dp)) {
                    Text("生成计划，开始答题 ✨")
                }
            }
        }
    }
}
