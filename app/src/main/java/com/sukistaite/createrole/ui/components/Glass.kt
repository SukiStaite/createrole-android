package com.sukistaite.createrole.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukistaite.createrole.data.Config

/** 液态玻璃卡片：半透明 + 渐变高光 + 圆角 + 描边（方案一琉璃梦境） */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    radius: Int = 22,
    content: @Composable () -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary
    val onBg = MaterialTheme.colorScheme.onBackground
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(radius.dp))
            .border(1.dp, primary.copy(alpha = 0.22f), RoundedCornerShape(radius.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.60f),
                    )
                )
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(radius.dp),
        content = content,
    )
}

/** 手帐胶带标题：小色块 + 标题（方案二手帐） */
@Composable
fun WashiTitle(
    text: String,
    modifier: Modifier = Modifier,
    accent: Color? = null,
) {
    val color = accent ?: MaterialTheme.colorScheme.primary
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .width(6.dp)
                .height(22.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

/** 沉浸渐变背景（琉璃梦境氛围） */
@Composable
fun AmbientBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    val bg = MaterialTheme.colorScheme.background
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(listOf(bg, primary.copy(alpha = 0.10f), bg))
        )
    ) { content() }
}

/** 印章收集项 */
@Composable
fun StampChip(name: String, modifier: Modifier = Modifier) {
    val icon = Config.STAMPS[name] ?: "✨"
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
    ) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 15.sp)
            Spacer(Modifier.width(5.dp))
            Text(name, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 12.sp)
        }
    }
}

/** 主题色进度条 */
@Composable
fun AccentProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(primary)
        )
    }
}
