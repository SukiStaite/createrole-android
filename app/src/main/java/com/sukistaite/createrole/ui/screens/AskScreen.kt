package com.sukistaite.createrole.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukistaite.createrole.App
import com.sukistaite.createrole.data.Config
import com.sukistaite.createrole.data.Questions
import com.sukistaite.createrole.ui.components.AccentProgressBar
import com.sukistaite.createrole.ui.components.GlassCard

@Composable
fun AskScreen() {
    val idx = App.state.currentIndex
    val q = App.state.planQuestionAt(idx) ?: return
    val total = App.state.plan.size
    val progress = if (total == 0) 0f else (idx + 1f) / total
    val selected = App.state.answer(q.qid)
    val isCustomSelected = selected == "自定义" || (selected != null && selected !in q.options)
    var customText by remember(q.qid) { mutableStateOf(if (isCustomSelected) selected.orEmpty() else "") }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { App.prevQuestion() }, enabled = idx > 0) { Text("‹ 上一题") }
            Spacer(Modifier.weight(1f))
            Text("${idx + 1} / $total", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        AccentProgressBar(progress)
        Spacer(Modifier.height(14.dp))
        val layer = Config.LAYER_INFO[q.layer]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("${layer?.icon ?: ""} ${layer?.name ?: q.layer}", fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(8.dp))
        GlassCard {
            Column(Modifier.padding(18.dp)) {
                Text(q.title, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground)
                if (q.parse.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(q.parse, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(16.dp))
                q.options.forEach { opt ->
                    val sel = opt == selected
                    Surface(
                        Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        color = if (sel) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    ) {
                        Column(Modifier.clickable { App.submitAnswer(q.qid, opt) }.padding(14.dp)) {
                            Text(opt, fontSize = 14.sp,
                                color = if (sel) MaterialTheme.colorScheme.onPrimaryContainer
                                       else MaterialTheme.colorScheme.onBackground)
                            Questions.byId(q.qid)?.optionDescs?.get(opt)?.let { d ->
                                if (sel) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(d, fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
                // 自定义输入
                if (q.options.contains("自定义")) {
                    Spacer(Modifier.height(6.dp))
                    TextField(
                        value = customText,
                        onValueChange = { customText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("自定义答案…", fontSize = 14.sp) },
                        label = { Text("自定义", fontSize = 13.sp) },
                        singleLine = false,
                        minLines = 1,
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { App.submitAnswer(q.qid, customText) },
                        Modifier.fillMaxWidth().height(44.dp),
                        enabled = customText.isNotBlank(),
                    ) { Text("提交自定义") }
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        // 沉浸反馈（答完后）
        if (App.state.immersiveFeedback.isNotBlank() && selected != null) {
            GlassCard {
                Column(Modifier.padding(16.dp)) {
                    Text(App.state.immersiveFeedback, fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        // 完成跳转
        if (idx == total - 1 && App.state.hasAnswer(q.qid)) {
            Button(onClick = { App.toArchive() }, Modifier.fillMaxWidth().height(50.dp)) {
                Text("完成 → 查看档案 🎀")
            }
        }
    }
}
