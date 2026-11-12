package com.sukistaite.createrole

import android.content.Context
import com.sukistaite.createrole.data.Config
import com.sukistaite.createrole.data.Engine
import com.sukistaite.createrole.data.Meta
import com.sukistaite.createrole.data.Questions
import com.sukistaite.createrole.data.Schema
import com.sukistaite.createrole.data.Store
import com.sukistaite.createrole.data.AppState
import kotlin.random.Random

/** 应用控制器：单例持有状态 + 全部动作（对齐 app.js 的 App 行为） */
object App {
    lateinit var ctx: Context
    val state = AppState()

    fun init(context: Context) {
        ctx = context.applicationContext
        Questions.load(ctx)
        Store.load(ctx, state)
    }

    /* ---------- 沉浸感随机 ---------- */
    fun pickQuote(): String = Config.QUOTES.random()
    fun pickMood(layer: String): String {
        val key = when (layer) {
            "体系登记" -> "REGISTER"; "补完" -> "COMPLETE"; else -> layer
        }
        return Config.MOOD_WORDS[key]?.random() ?: "嗯嗯～"
    }
    fun pickSnippet(layer: String): String =
        Config.SNIPPETS[layer]?.random() ?: "她轻轻笑了笑。"

    /* ---------- 元信息 ---------- */
    fun applyPreset(p: Config.Preset) {
        state.meta = Meta(
            gender = p.gender, volume = p.volume, orientation = p.orientation,
            dlc9 = p.dlc9, race = p.race, raceMulti = p.race == "多形态", strictNaming = p.strictNaming,
        )
    }

    /* ---------- 开始 ---------- */
    fun startNew() {
        state.resetAll()
        state.stage = "meta"
    }

    fun resume() {
        if (!state.planBuilt || state.plan.isEmpty()) state.stage = "meta"
        else state.stage = if (state.stage in listOf("welcome", "meta")) "ask" else state.stage
    }

    fun buildPlanAndStart() {
        state.plan = Engine.buildPlan(state.meta, Questions.list)
        state.planBuilt = true
        state.currentIndex = 0
        state.stage = "ask"
        state.checks = Engine.runChecks(state.meta, state.answers)
        save()
    }

    /* ---------- 答题 ---------- */
    fun submitAnswer(qid: Int, value: String?, autoAdvance: Boolean = true) {
        state.setAnswer(qid, value)
        val q = Questions.byId(qid)
        if (q != null) {
            state.lastLayer = q.layer
            if (state.settings.showImmersive) {
                state.immersiveFeedback = pickMood(q.layer) + "  " + pickSnippet(q.layer)
            }
        }
        if (autoAdvance && state.settings.askMode == Config.ASK_SINGLE) {
            nextQuestion()
        }
        state.checks = Engine.runChecks(state.meta, state.answers)
        save()
    }

    fun nextQuestion() {
        if (state.currentIndex < state.plan.size - 1) {
            state.currentIndex++
        } else {
            state.stage = "archive"
            addStamp("基础")
            if (allDone()) addStamp("完整")
            state.checks = Engine.runChecks(state.meta, state.answers)
            save()
        }
    }

    fun prevQuestion() {
        if (state.currentIndex > 0) state.currentIndex--
    }

    fun allDone(): Boolean = state.progress().remaining == 0

    fun toArchive() { state.stage = "archive"; save() }
    fun toCheck() { state.checks = Engine.runChecks(state.meta, state.answers); state.stage = "check"; save() }
    fun toRegister() { state.stage = "register"; save() }
    fun toTest() { state.stage = "test"; save() }
    fun toSettings() { state.stage = "settings"; save() }
    fun toWelcome() { state.stage = "welcome"; save() }

    /* ---------- 登记 ---------- */
    fun toggleRegister(id: String) {
        state.registers = state.registers + (id to !(state.registers[id] ?: false))
        save()
    }

    /* ---------- 印章 ---------- */
    fun addStamp(name: String) {
        if (name in Config.STAMPS) state.stamps = state.stamps + name
        save()
    }

    /* ---------- 角色卡导入（对齐 app.js applyCharacterCard） ---------- */
    fun applyCharacterCard(card: Map<String, Any?>) {
        (card["name"] as? String)?.takeIf { it.isNotBlank() }?.let { state.setAnswer(1, it) }
        (card["description"] as? String)?.takeIf { it.isNotBlank() }?.let { state.setAnswer(594, it) }
        state.card = card
        state.stage = "archive"
        save()
    }

    /* ---------- 分享导出 ---------- */
    fun shareText(subject: String, text: String) {
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
            putExtra(android.content.Intent.EXTRA_TEXT, text)
        }
        ctx.startActivity(android.content.Intent.createChooser(intent, "导出"))
    }

    /* ---------- 保存 ---------- */
    fun save() {
        if (::ctx.isInitialized) Store.save(ctx, state)
    }

    /* ---------- 导入（对齐 app.js importJSON 自动识别） ---------- */
    fun importUri(uri: String) {
        try {
            val text = ctx.contentResolver.openInputStream(android.net.Uri.parse(uri))
                ?.bufferedReader()?.use { it.readText() } ?: return
            importText(text)
        } catch (_: Exception) { }
    }

    fun importText(text: String) {
        val t = text.trim()
        if (t.isEmpty()) return
        try {
            val o = org.json.JSONObject(t)
            if (o.optString("format") == "aira-archive") { importAiraArchive(o); return }
            val data = when {
                o.optString("spec").startsWith("chara_card") -> o.optJSONObject("data")
                else -> o
            }
            if (data != null) importCharacterCard(data)
            return
        } catch (_: Exception) { }
        // 纯文本 → 人设内核
        state.setAnswer(594, t)
        state.stage = "archive"
        save()
    }

    private fun importAiraArchive(o: org.json.JSONObject) {
        o.optJSONObject("meta")?.let { m ->
            state.meta = Meta(
                gender = m.optString("gender").ifEmpty { null },
                volume = m.optInt("volume", 240),
                orientation = m.optString("orientation").ifEmpty { null },
                dlc9 = m.optString("dlc9").ifEmpty { null },
                race = m.optString("race").ifEmpty { null },
                raceMulti = m.optBoolean("raceMulti"),
                strictNaming = m.optBoolean("strictNaming", true),
            )
        }
        o.optJSONObject("answers")?.let { a ->
            a.keys().forEach { k -> k.toIntOrNull()?.let { id -> state.setAnswer(id, a.optString(k)) } }
        }
        o.optJSONObject("card")?.let { c ->
            state.card = mutableMapOf<String, Any?>().apply {
                c.keys().forEach { k -> this[k] = c.opt(k) }
            }
        }
        o.optJSONObject("registers")?.let { r ->
            state.registers = mutableMapOf<String, Boolean>().apply {
                r.keys().forEach { k -> this[k] = r.optBoolean(k) }
            }
        }
        state.stage = "archive"
        save()
    }

    private fun importCharacterCard(data: org.json.JSONObject) {
        val cardFields = listOf(
            "name", "description", "personality", "scenario", "first_mes", "mes_example",
            "system_prompt", "post_history_instructions", "creator_notes", "tags",
            "creator", "character_version",
        )
        val card = mutableMapOf<String, Any?>()
        cardFields.forEach { k -> if (data.has(k)) card[k] = data.opt(k) }
        (data.optString("name")).takeIf { it.isNotBlank() }?.let { state.setAnswer(1, it) }
        (data.optString("description")).takeIf { it.isNotBlank() }?.let { state.setAnswer(594, it) }
        state.card = card
        state.stage = "archive"
        save()
    }
}
