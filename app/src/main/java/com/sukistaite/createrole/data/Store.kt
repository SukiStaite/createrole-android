package com.sukistaite.createrole.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** 持久化：SharedPreferences 存整个状态为 JSON（对齐网页版 localStorage 语义） */
object Store {
    private const val PREFS = "aira_state"
    private const val KEY = "state"

    fun save(context: Context, s: AppState) {
        val o = JSONObject()
        val meta = JSONObject()
        meta.put("gender", s.meta.gender)
        meta.put("volume", s.meta.volume)
        meta.put("volumeCustom", s.meta.volumeCustom)
        meta.put("orientation", s.meta.orientation)
        meta.put("dlc9", s.meta.dlc9)
        meta.put("race", s.meta.race)
        meta.put("raceMulti", s.meta.raceMulti)
        meta.put("strictNaming", s.meta.strictNaming)
        o.put("meta", meta)

        val ans = JSONObject()
        s.answers.forEach { (k, v) -> ans.put(k.toString(), v) }
        o.put("answers", ans)

        val planQids = JSONArray()
        s.plan.forEach { planQids.put(it.qid) }
        o.put("planQids", planQids)
        o.put("planBuilt", s.planBuilt)

        val st = JSONObject()
        st.put("theme", s.settings.theme)
        st.put("dark", s.settings.dark)
        st.put("style", s.settings.style)
        st.put("animLevel", s.settings.animLevel)
        st.put("flowMode", s.settings.flowMode)
        st.put("fontScale", s.settings.fontScale.toDouble())
        st.put("language", s.settings.language)
        st.put("sound", s.settings.sound)
        st.put("askMode", s.settings.askMode)
        st.put("batchSize", s.settings.batchSize)
        st.put("memoryFreq", s.settings.memoryFreq)
        st.put("typeSpeed", s.settings.typeSpeed)
        st.put("autoNext", s.settings.autoNext)
        st.put("showImmersive", s.settings.showImmersive)
        st.put("nsfwMask", s.settings.nsfwMask)
        st.put("testMode", s.settings.testMode)
        o.put("settings", st)

        o.put("stage", s.stage)
        o.put("currentIndex", s.currentIndex)

        val regs = JSONObject()
        s.registers.forEach { (k, v) -> regs.put(k, v) }
        o.put("registers", regs)

        val cd = JSONObject()
        s.card.forEach { (k, v) -> if (v != null) cd.put(k, v) }
        o.put("card", cd)

        val stamps = JSONArray()
        s.stamps.forEach { stamps.put(it) }
        o.put("stamps", stamps)

        val mem = JSONObject()
        s.memoryByQid.forEach { (k, v) -> mem.put(k.toString(), v) }
        o.put("memory", mem)

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, o.toString()).apply()
    }

    fun load(context: Context, s: AppState) {
        val text = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, null) ?: return
        val o = try { JSONObject(text) } catch (e: Exception) { return }

        o.optJSONObject("meta")?.let { m ->
            s.meta = Meta(
                gender = m.optString("gender").ifEmpty { null },
                volume = m.optInt("volume", 240),
                volumeCustom = m.optString("volumeCustom"),
                orientation = m.optString("orientation").ifEmpty { null },
                dlc9 = m.optString("dlc9").ifEmpty { null },
                race = m.optString("race").ifEmpty { null },
                raceMulti = m.optBoolean("raceMulti"),
                strictNaming = m.optBoolean("strictNaming", true),
            )
        }
        s.answers.clear()
        o.optJSONObject("answers")?.let { a ->
            a.keys().forEach { k -> k.toIntOrNull()?.let { s.answers[it] = a.optString(k) } }
        }
        // 重建计划
        val qids = mutableListOf<Int>()
        o.optJSONArray("planQids")?.let { arr -> for (i in 0 until arr.length()) qids.add(arr.optInt(i)) }
        val rebuilt = qids.mapNotNull { id -> Questions.byId(id)?.let { q ->
            Engine.PlanItem(q.id, q.layer, q.cat, q.title, q.parse, q.options, q.required)
        } }
        s.plan = rebuilt
        s.planBuilt = o.optBoolean("planBuilt", rebuilt.isNotEmpty())

        o.optJSONObject("settings")?.let { st ->
            s.settings = Settings(
                theme = st.optString("theme", Config.DEFAULT_THEME),
                dark = st.optBoolean("dark"),
                style = st.optString("style", "ios27"),
                animLevel = st.optString("animLevel", "全"),
                flowMode = st.optString("flowMode", "标准"),
                fontScale = st.optDouble("fontScale", 1.0).toFloat(),
                language = st.optString("language", "zh"),
                sound = st.optBoolean("sound", Config.DEFAULT_SOUND),
                askMode = st.optString("askMode", Config.ASK_SINGLE),
                batchSize = st.optInt("batchSize", 5),
                memoryFreq = st.optInt("memoryFreq", Config.DEFAULT_MEMORY_FREQ),
                typeSpeed = st.optInt("typeSpeed", Config.TYPE_SPEED_MS),
                autoNext = st.optBoolean("autoNext", true),
                showImmersive = st.optBoolean("showImmersive", true),
                nsfwMask = st.optBoolean("nsfwMask"),
                testMode = st.optBoolean("testMode"),
            )
        }
        s.stage = o.optString("stage", "welcome")
        s.currentIndex = o.optInt("currentIndex")
        s.registers = mutableMapOf<String, Boolean>().apply {
            o.optJSONObject("registers")?.let { r -> r.keys().forEach { k -> this[k] = r.optBoolean(k) } }
        }
        s.card = mutableMapOf<String, Any?>().apply {
            o.optJSONObject("card")?.let { c -> c.keys().forEach { k -> this[k] = c.opt(k) } }
        }
        s.stamps = mutableSetOf<String>().apply {
            o.optJSONArray("stamps")?.let { a -> for (i in 0 until a.length()) add(a.optString(i)) }
        }
        s.memoryByQid.clear()
        o.optJSONObject("memory")?.let { m ->
            m.keys().forEach { k -> k.toIntOrNull()?.let { s.memoryByQid[it] = m.optString(k) } }
        }
    }

    fun hasSave(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, null) != null
}
