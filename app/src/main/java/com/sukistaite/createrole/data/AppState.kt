package com.sukistaite.createrole.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** 用户设置 —— 对齐网页版设置面板 */
data class Settings(
    val theme: String = Config.DEFAULT_THEME,
    val dark: Boolean = false,
    val style: String = "ios27",          // sakura / ios27 / material3
    val animLevel: String = "全",          // 关 / 少 / 全
    val flowMode: String = "标准",         // 标准 / 流畅
    val fontScale: Float = 1f,
    val language: String = "zh",
    val sound: Boolean = Config.DEFAULT_SOUND,
    val askMode: String = Config.ASK_SINGLE,
    val batchSize: Int = 5,
    val memoryFreq: Int = Config.DEFAULT_MEMORY_FREQ,
    val typeSpeed: Int = Config.TYPE_SPEED_MS,
    val autoNext: Boolean = true,
    val showImmersive: Boolean = true,
    val nsfwMask: Boolean = false,        // NSFW 私密感
    val testMode: Boolean = false,
)

/** 全局状态（Compose 响应式） */
class AppState {
    var meta by mutableStateOf(Meta())
    val answers = mutableStateMapOf<Int, String>()

    var plan by mutableStateOf<List<Engine.PlanItem>>(emptyList())
    var planBuilt by mutableStateOf(false)

    var stage by mutableStateOf("welcome")   // welcome/meta/ask/archive/check/register/test/settings
    var currentIndex by mutableStateOf(0)     // 计划内当前题下标（单题模式）

    var settings by mutableStateOf(Settings())

    var checks by mutableStateOf(listOf<Engine.CheckOut>())
    var registers by mutableStateOf(mapOf<String, Boolean>())
    var card by mutableStateOf(mapOf<String, Any?>())

    var stamps by mutableStateOf(setOf<String>())
    var log = mutableListOf<String>()

    // 沉浸感
    var immersiveQuote by mutableStateOf("")
    var immersiveFeedback by mutableStateOf("")
    var lastLayer by mutableStateOf("")
    var previewText by mutableStateOf("")

    // 记忆存档（qid -> 当时的答案，用于回看）
    val memoryByQid = mutableStateMapOf<Int, String>()

    // 测试模式
    var testSceneId by mutableStateOf("")

    /* ---------- 便捷访问 ---------- */
    fun hasAnswer(qid: Int): Boolean = Schema.hasAnswer(answers[qid])
    fun answer(qid: Int): String? = answers[qid]

    fun setAnswer(qid: Int, value: String?) {
        if (value == null || value.trim().isEmpty()) answers.remove(qid)
        else answers[qid] = value.trim()
    }

    fun clearAnswer(qid: Int) {
        answers.remove(qid)
        memoryByQid.remove(qid)
    }

    fun planQuestionAt(index: Int): Engine.PlanItem? = plan.getOrNull(index)

    fun progress(): Engine.Progress = Engine.countProgress(plan, answers)

    fun archive(): Engine.Archive = Engine.buildArchive(meta, answers, Questions.list)

    fun resetAll() {
        meta = Meta()
        answers.clear()
        plan = emptyList()
        planBuilt = false
        stage = "welcome"
        currentIndex = 0
        checks = emptyList()
        registers = emptyMap()
        card = emptyMap()
        stamps = emptySet()
        memoryByQid.clear()
        immersiveFeedback = ""
    }
}
