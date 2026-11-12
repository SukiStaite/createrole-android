package com.sukistaite.createrole.data

import android.content.Context
import org.json.JSONObject

/** 题库仓库：从 assets/questions.json 加载（与网页 applyQuestionExt 合并结果一致） */
object Questions {
    var list: List<Question> = emptyList()
        private set
    private var byId: Map<Int, Question> = emptyMap()

    fun load(context: Context) {
        val text = context.assets.open("questions.json").bufferedReader().use { it.readText() }
        val root = JSONObject(text)
        val arr = root.optJSONArray("questions")
        val out = mutableListOf<Question>()
        if (arr != null) for (i in 0 until arr.length()) out.add(Question.fromJson(arr.optJSONObject(i)))
        list = out
        byId = out.associateBy { it.id }
    }

    fun byId(id: Int): Question? = byId[id]
}
