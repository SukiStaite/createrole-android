package com.sukistaite.createrole.data

import org.json.JSONObject

/** 题目 —— 对齐 data/questions.js 字段（含合并后的 optionDescs） */
data class Question(
    val id: Int,
    val layer: String,
    val cat: String,
    val title: String,
    val parse: String,
    val options: List<String>,
    val sfw: Boolean,
    val nsfw: Boolean,
    val required: Boolean,
    val dlc: Boolean,
    val optionDescs: Map<String, String>? = null,
) {
    companion object {
        fun fromJson(o: JSONObject): Question {
            val opts = mutableListOf<String>()
            o.optJSONArray("options")?.let { a -> for (i in 0 until a.length()) opts.add(a.optString(i)) }
            val descs = mutableMapOf<String, String>()
            o.optJSONObject("optionDescs")?.let { d ->
                d.keys().forEach { k -> descs[k] = d.optString(k) }
            }
            return Question(
                id = o.optInt("id"),
                layer = o.optString("layer"),
                cat = o.optString("cat"),
                title = o.optString("title"),
                parse = o.optString("parse"),
                options = opts,
                sfw = o.optBoolean("sfw"),
                nsfw = o.optBoolean("nsfw"),
                required = o.optBoolean("required"),
                dlc = o.optBoolean("dlc"),
                optionDescs = if (descs.isEmpty()) null else descs,
            )
        }
    }
}

/** 元信息 —— 对齐 engine.defaultMeta() */
data class Meta(
    val gender: String? = null,
    val volume: Int = 240,
    val volumeCustom: String = "",
    val orientation: String? = null,
    val dlc9: String? = null,
    val race: String? = null,
    val raceMulti: Boolean = false,
    val strictNaming: Boolean = true,
)
