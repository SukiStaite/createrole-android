package com.sukistaite.createrole.data

import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/** 引擎 —— 纯业务逻辑，逐字段对齐 engine.js（V4.1） */
object Engine {

    data class PlanItem(
        val qid: Int, val layer: String, val cat: String, val title: String,
        val parse: String, val options: List<String>, val required: Boolean,
    )

    data class RowVal(val label: String, val value: String)
    data class SectionVal(val title: String, val rows: List<RowVal>)
    data class Archive(
        val meta: Meta, val name: String, val sections: List<SectionVal>, val core: String,
    )

    data class Progress(
        val total: Int, val done: Int, val remaining: Int,
        val layerStats: Map<String, Pair<Int, Int>>, // layer -> (total, done)
    )

    data class CheckOut(val id: String, val title: String, val desc: String, val status: String, val reason: String)

    /* ---------- 洗牌（Fisher-Yates） ---------- */
    fun <T> shuffle(arr: List<T>, rng: Random = Random.Default): List<T> {
        val a = arr.toMutableList()
        for (i in a.size - 1 downTo 1) {
            val j = rng.nextInt(i + 1)
            val t = a[i]; a[i] = a[j]; a[j] = t
        }
        return a
    }

    /* ---------- 可问性判定 ---------- */
    fun isEligible(q: Question, meta: Meta): Boolean {
        if (q.layer == "NSFW") return meta.orientation != "SFW"
        if (q.layer == "DLC") {
            val isFuta = q.cat.contains("双性配置")
            if (isFuta) return meta.dlc9 == "是" && meta.orientation != "SFW"
            return true
        }
        return true
    }

    /* ---------- 体量配额换算 ---------- */
    fun getLayerQuota(meta: Meta): Config.Quota {
        val vol = meta.volume
        Config.VOLUME_MAP[vol]?.let { return it }
        val total = minOf(Config.TOTAL_QUESTIONS, maxOf(10, vol))
        var sfw = Math.round(total * Config.CUSTOM_RATIO.sfw).toInt()
        var nsfw = Math.round(total * Config.CUSTOM_RATIO.nsfw).toInt()
        var dlc = Math.round(total * Config.CUSTOM_RATIO.dlc).toInt()
        if (meta.dlc9 == "否") { nsfw += dlc; dlc = 0 }
        if (meta.orientation == "SFW") { sfw += nsfw; nsfw = 0 }
        return Config.Quota(sfw, nsfw, dlc)
    }

    /* ---------- 分层随机抽题 ---------- */
    fun buildPlan(meta: Meta, questions: List<Question>, rng: Random = Random.Default): List<PlanItem> {
        val coreIds = Schema.getCoreIds(meta).toSet()
        val quota = getLayerQuota(meta)
        val plan = mutableListOf<PlanItem>()
        val layers = listOf(
            Triple("SFW", quota.sfw, false),
            Triple("NSFW", quota.nsfw, false),
            Triple("DLC", quota.dlc, false),
            Triple("体系登记", 0, true),
            Triple("补完", 0, true),
        )
        layers.forEach { (name, target, isInf) ->
            val pool = questions.filter { it.layer == name && isEligible(it, meta) }
            val core = pool.filter { coreIds.contains(it.id) }
            val rest = pool.filter { !coreIds.contains(it.id) }
            val need = if (isInf) rest.size else maxOf(0, target - core.size)
            val extra = shuffle(rest, rng).take(need)
            val block = (core + extra).sortedBy { it.id }
            block.forEach { q ->
                plan.add(PlanItem(q.id, q.layer, q.cat, q.title, q.parse, q.options, q.required))
            }
        }
        return plan
    }

    /* ---------- 进度统计 ---------- */
    fun countProgress(plan: List<PlanItem>, answers: Map<Int, String>): Progress {
        val total = plan.size
        val done = plan.count { Schema.hasAnswer(answers[it.qid]) }
        val layerStats = mutableMapOf<String, Pair<Int, Int>>()
        plan.forEach { p ->
            val cur = layerStats[p.layer] ?: (0 to 0)
            layerStats[p.layer] = (cur.first + 1) to (cur.second + if (Schema.hasAnswer(answers[p.qid])) 1 else 0)
        }
        return Progress(total, done, total - done, layerStats)
    }

    /* ---------- 档案组装（零留空） ---------- */
    fun buildArchive(meta: Meta, answers: Map<Int, String>, questions: List<Question>): Archive {
        val byId = questions.associateBy { it.id }
        val get: (Int) -> String = { id ->
            val v = answers[id]
            if (Schema.hasAnswer(v)) v!!.trim() else Schema.DASH
        }
        val name = if (Schema.hasAnswer(answers[1])) answers[1]!!.trim() else "未命名"
        val sections = mutableListOf<SectionVal>()
        Schema.ARCHIVE_SCHEMA.forEach { sec ->
            if (sec.onlyIf != null && !sec.onlyIf!!(meta)) return@forEach
            val rows = mutableListOf<RowVal>()
            sec.rows.forEach { row ->
                if (row.onlyIf != null && !row.onlyIf!!(meta)) return@forEach
                rows.add(RowVal(row.label, row.get(get)))
            }
            sections.add(SectionVal(sec.title, rows))
        }
        return Archive(meta, name, sections, Schema.CORE_FIELD.get(get))
    }

    /* ---------- 14 条自动校验 ---------- */
    fun runChecks(meta: Meta, answers: Map<Int, String>): List<CheckOut> =
        Schema.CHECK_LIST.map { c ->
            val r = try { c.auto(meta, answers) } catch (e: Exception) { Schema.CheckResult("manual", "校验异常：${e.message}") }
            CheckOut(c.id, c.title, c.desc, r.status, r.reason)
        }

    /* ---------- 登记值速览 ---------- */
    fun registerPreview(answers: Map<Int, String>): List<List<String>> {
        val get: (Int) -> String = { id ->
            val v = answers[id]
            if (Schema.hasAnswer(v)) v!!.trim() else Schema.DASH
        }
        return listOf(
            listOf("发情期日期段", get(568), "第三十一章"),
            listOf("初夜状态", get(580), "第四十七章"),
            listOf("好感度起点", get(582), "第三十八章"),
            listOf("AL 系数", get(566), "第四十四章"),
            listOf("称呼体系", get(591), "第二十七章"),
            listOf("作息类型", get(570), "第四十二章"),
            listOf("身体数据", "${get(251)} / ${get(256)}", "第三十三章"),
            listOf("微信档案", get(571), "第四十三章"),
        )
    }

    /* ---------- markdown 导出 ---------- */
    fun archiveToMarkdown(archive: Archive, meta: Meta): String {
        val md = StringBuilder()
        md.append("# 角色档案 · ").append(archive.name).append("（V4.2 全字段版 · 精细度对标林晴祈）").append("\n\n")
        md.append("> 生成时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
            .append(" ｜ 工具：搭角色 V4.2 ｜ 题量：").append(meta.volume).append(" 题\n\n")
        archive.sections.forEach { sec ->
            md.append("## ").append(sec.title).append("\n\n")
            sec.rows.forEach { r -> md.append("- **").append(r.label).append("**：").append(r.value).append("\n") }
            md.append("\n")
        }
        md.append("## ").append(Schema.CORE_FIELD.label).append("\n\n> ").append(archive.core).append("\n\n")
        md.append("---\n## 体系登记流水\n\n")
        listOf("发情期表（第三十一章）", "初夜档案室（第四十七章）", "好感度表（第三十八章）",
            "身体数据总表（第三十三章）", "称呼表（第二十七章）", "微信档案（第四十三章）",
            "作息矩阵（第四十二章）", "关系矩阵（第三十二章）", "亲密空间（第四十章，如有）").forEach {
            md.append("- [ ] ").append(it).append("\n")
        }
        return md.toString()
    }

    fun defaultMeta() = Meta()

    /* ---------- SillyTavern 角色卡导出（V2 / V3） ---------- */
    fun archiveToSillyTavern(archive: Archive, meta: Meta, version: Int, card: Map<String, Any?>?): JSONObject {
        val md = archiveToMarkdown(archive, meta)
        val name = archive.name
        var personality = archive.core
        val charSec = archive.sections.find { it.title.contains("性格") }
        if (charSec != null) {
            personality = charSec.rows.take(5).joinToString("\n") { "${it.label}：${it.value}" }
        }
        val data = JSONObject()
        data.put("name", name)
        data.put("description", md)
        data.put("personality", personality)
        data.put("scenario", meta.orientation ?: "")
        data.put("first_mes", "「早上好呀，辞宝…」${name}揉着眼睛，声音带着刚醒的软糯。今天也要一起度过呀。")
        data.put("mes_example", "<START>\n{{char}}：辞宝，你今天心情怎么样呀？")
        data.put("creator_notes", "由搭角色 V${Config.VERSION} 生成 ｜ 性别：${meta.gender ?: "-"} ｜ 题量：${meta.volume} ｜ 什么向：${meta.orientation ?: "-"} ｜ DLC9：${meta.dlc9 ?: "-"} ｜ 种族：${meta.race ?: "-"}")
        data.put("system_prompt", "你是一个温柔、黏人、占有欲很强的女孩子（祈宝系角色）。请始终以第一人称、口语化、有亲密感的方式回应。")
        data.put("post_history_instructions", "")
        data.put("alternate_greetings", JSONArray())
        data.put("tags", JSONArray(listOf("搭角色", "AIRA", "RP")))
        data.put("creator", "AIRA · 搭角色")
        data.put("character_version", Config.VERSION)
        data.put("extensions", JSONObject())
        // 合并导入/编辑过的角色卡字段（非空覆盖默认）
        if (card != null) {
            card.forEach { (k, v) ->
                if (v != null && v.toString().isNotEmpty()) data.put(k, v)
            }
        }
        val wrapper = JSONObject()
        if (version == 3) {
            wrapper.put("spec", "chara_card_v3")
            wrapper.put("spec_version", "3.0")
            data.put("group_only_greetings", JSONArray())
            wrapper.put("data", data)
        } else {
            wrapper.put("spec", "chara_card_v2")
            wrapper.put("spec_version", "2.0")
            wrapper.put("data", data)
        }
        return wrapper
    }

    /* ---------- 完整 JSON 档案导出（便于二次编辑/导入） ---------- */
    fun archiveToJSON(
        archive: Archive,
        answers: Map<Int, String>,
        checks: Map<String, Any?>,
        registers: Map<String, Any?>,
        card: Map<String, Any?>,
    ): JSONObject {
        val out = JSONObject()
        out.put("format", "aira-archive")
        out.put("version", Config.VERSION)
        out.put("exportedAt", java.time.Instant.now().toString())
        out.put("meta", JSONObject().also { m -> m.put("gender", archive.meta.gender); m.put("volume", archive.meta.volume); m.put("orientation", archive.meta.orientation); m.put("dlc9", archive.meta.dlc9); m.put("race", archive.meta.race); m.put("raceMulti", archive.meta.raceMulti); m.put("strictNaming", archive.meta.strictNaming) })
        out.put("name", archive.name)
        out.put("core", archive.core)
        val secs = JSONArray()
        archive.sections.forEach { s ->
            val so = JSONObject()
            so.put("title", s.title)
            val rows = JSONArray()
            s.rows.forEach { r -> rows.put(JSONObject().put("label", r.label).put("value", r.value)) }
            so.put("rows", rows)
            secs.put(so)
        }
        out.put("sections", secs)
        out.put("answers", JSONObject(answers.mapKeys { it.key.toString() }))
        out.put("checks", JSONObject(checks))
        out.put("registers", JSONObject(registers))
        out.put("card", JSONObject(card))
        return out
    }
}
