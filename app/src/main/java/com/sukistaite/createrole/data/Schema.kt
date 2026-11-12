package com.sukistaite.createrole.data

typealias GetFn = (Int) -> String

/** 档案模板 / 检查清单 / 登记清单 —— 逐字段对齐 schema.js（V4.1） */
object Schema {
    const val DASH = "—"

    data class ArchiveRow(
        val label: String,
        val qids: List<Int>,
        val onlyIf: ((Meta) -> Boolean)? = null,
        val get: (GetFn) -> String,
    )

    data class ArchiveSection(
        val title: String,
        val rows: List<ArchiveRow>,
        val onlyIf: ((Meta) -> Boolean)? = null,
    )

    data class CoreField(val label: String, val qids: List<Int>, val get: (GetFn) -> String)

    data class CheckResult(val status: String, val reason: String)

    data class CheckItem(
        val id: String,
        val title: String,
        val desc: String,
        val auto: (Meta, Map<Int, String>) -> CheckResult,
    )

    data class RegisterItem(val id: String, val title: String, val sub: String, val icon: String)

    /* ---------- 档案模板（qids 决定核心题集） ---------- */
    val ARCHIVE_SCHEMA: List<ArchiveSection> = listOf(
        ArchiveSection("基础信息", listOf(
            ArchiveRow("姓名", listOf(1)) { g -> g(1) },
            ArchiveRow("昵称 / 小名", listOf(2)) { g -> g(2) },
            ArchiveRow("年龄（永久锁定）", listOf(3)) { g -> g(3) },
            ArchiveRow("身高 / 体重", listOf(4, 5)) { g -> "${g(4)} / ${g(5)}" },
            ArchiveRow("三围", listOf(6)) { g -> g(6) },
            ArchiveRow("腿长 / 脚码 / 脚型 / 手指", listOf(575, 576, 577)) { g -> "${g(575)} / ${g(576)} / ${g(577)}" },
            ArchiveRow("生日 / 星座", listOf(7, 8)) { g -> "${g(7)} / ${g(8)}" },
            ArchiveRow("籍贯 / 现居", listOf(9, 10)) { g -> "${g(9)} / ${g(10)}" },
            ArchiveRow("职业 / 身份", listOf(12)) { g -> g(12) },
            ArchiveRow("种族", listOf(466)) { g -> g(466) },
            ArchiveRow("血型", listOf(11)) { g -> g(11) },
            ArchiveRow("学历", listOf(13)) { g -> g(13) },
        )),
        ArchiveSection("外貌", listOf(
            ArchiveRow("发色 / 发型 / 发质", listOf(26, 27, 28)) { g -> "${g(26)} / ${g(27)} / ${g(28)}" },
            ArchiveRow("瞳色 / 眼型", listOf(29, 30)) { g -> "${g(29)} / ${g(30)}" },
            ArchiveRow("脸型 / 肤色 / 肤质", listOf(33, 34, 35)) { g -> "${g(33)} / ${g(34)} / ${g(35)}" },
            ArchiveRow("唇形 / 牙齿 / 笑容", listOf(40, 41, 42)) { g -> "${g(40)} / ${g(41)} / ${g(42)}" },
            ArchiveRow("体香三态", listOf(578)) { g -> g(578) },
            ArchiveRow("手部特征", listOf(37)) { g -> g(37) },
            ArchiveRow("腿部线条", listOf(38)) { g -> g(38) },
            ArchiveRow("足部", listOf(39, 576)) { g -> "${g(39)} / ${g(576)}" },
            ArchiveRow("标志性特征", listOf(43)) { g -> g(43) },
            ArchiveRow("最吸引人的细节", listOf(57)) { g -> g(57) },
        )),
        ArchiveSection("性格", listOf(
            ArchiveRow("核心性格", listOf(91)) { g -> g(91) },
            ArchiveRow("对外人格", listOf(92)) { g -> g(92) },
            ArchiveRow("对内（对辞宝）人格", listOf(93)) { g -> g(93) },
            ArchiveRow("反差点", listOf(94)) { g -> g(94) },
            ArchiveRow("口头禅", listOf(95)) { g -> g(95) },
            ArchiveRow("撒娇方式", listOf(101)) { g -> g(101) },
            ArchiveRow("安全感来源", listOf(108)) { g -> g(108) },
            ArchiveRow("最怕失去", listOf(109)) { g -> g(109) },
            ArchiveRow("情绪表现", listOf(96, 97, 98, 99, 100)) { g ->
                "开心：${g(96)}｜害羞：${g(97)}｜紧张：${g(98)}｜生气：${g(99)}｜吃醋：${g(100)}"
            },
        )),
        ArchiveSection("日常与作息", listOf(
            ArchiveRow("作息", listOf(570)) { g -> g(570) },
            ArchiveRow("饮食偏好", listOf(151)) { g -> g(151) },
            ArchiveRow("爱好习惯", listOf(141)) { g -> g(141) },
            ArchiveRow("微信档案", listOf(571, 572, 573, 574)) { g ->
                "打字：${g(571)}｜表情包：${g(572)}｜朋友圈：${g(573)}｜群聊：${g(574)}"
            },
            ArchiveRow("出行方式", listOf(21)) { g -> g(21) },
            ArchiveRow("运动", listOf(132)) { g -> g(132) },
            ArchiveRow("睡眠姿势", listOf(137)) { g -> g(137) },
        )),
        ArchiveSection("与辞宝的关系", listOf(
            ArchiveRow("关系定位", listOf(528)) { g -> g(528) },
            ArchiveRow("对辞宝称呼", listOf(227)) { g -> g(227) },
            ArchiveRow("希望被叫", listOf(228)) { g -> g(228) },
            ArchiveRow("称呼进阶链", listOf(591)) { g -> g(591) },
            ArchiveRow("独占欲", listOf(107)) { g -> g(107) },
            ArchiveRow("后宫接受度", listOf(543)) { g -> g(543) },
            ArchiveRow("第一梯队矩阵", listOf(590)) { g -> g(590) },
            ArchiveRow("关系一句话", listOf(545)) { g -> g(545) },
        )),
        ArchiveSection("身体数据（NSFW）", listOf(
            ArchiveRow("罩杯 / 乳型 / 乳晕", listOf(251, 252, 254)) { g -> "${g(251)} / ${g(252)} / ${g(254)}" },
            ArchiveRow("小穴类型", listOf(256)) { g -> g(256) },
            ArchiveRow("爱液", listOf(259, 260, 579)) { g -> "${g(259)} / ${g(260)} / ${g(579)}" },
            ArchiveRow("敏感部位排序", listOf(301)) { g -> g(301) },
            ArchiveRow("体毛状态", listOf(263)) { g -> g(263) },
            ArchiveRow("特殊标记", listOf(274)) { g -> g(274) },
            ArchiveRow("DLC9 特化", listOf(491, 492, 493, 507), onlyIf = { it.dlc9 == "是" }) { g ->
                "配置：${g(491)}｜尺寸：${g(492)} / ${g(493)}｜攻受：${g(507)}"
            },
        ), onlyIf = { it.orientation != "SFW" }),
        ArchiveSection("性偏好（NSFW）", listOf(
            ArchiveRow("经验与初夜", listOf(321, 580)) { g -> "${g(321)}｜${g(580)}" },
            ArchiveRow("AL 系数", listOf(566)) { g -> g(566) },
            ArchiveRow("前戏特化", listOf(584)) { g -> g(584) },
            ArchiveRow("体位前三 / 拒绝", listOf(586)) { g -> g(586) },
            ArchiveRow("耐力轮次", listOf(585)) { g -> g(585) },
            ArchiveRow("场所胆量", listOf(587)) { g -> g(587) },
            ArchiveRow("玩具偏好", listOf(588)) { g -> g(588) },
            ArchiveRow("骚话风格", listOf(396)) { g -> g(396) },
            ArchiveRow("红线", listOf(451)) { g -> g(451) },
            ArchiveRow("高潮决定权", listOf(589)) { g -> g(589) },
        ), onlyIf = { it.orientation != "SFW" }),
        ArchiveSection("发情期档案", listOf(
            ArchiveRow("日期段", listOf(568)) { g -> g(568) },
            ArchiveRow("个人特有症状", listOf(569)) { g -> g(569) },
            ArchiveRow("情绪差异", listOf(617)) { g -> g(617) },
        )),
        ArchiveSection("好感度档案", listOf(
            ArchiveRow("起点与增长", listOf(582)) { g -> g(582) },
            ArchiveRow("好感度 100 表现", listOf(583)) { g -> g(583) },
        )),
        ArchiveSection("特殊设定", listOf(
            ArchiveRow("种族能力", listOf(546)) { g -> g(546) },
            ArchiveRow("能力色色用途", listOf(547)) { g -> g(547) },
            ArchiveRow("形态切换", listOf(467, 469, 473), onlyIf = { it.raceMulti }) { g ->
                "形态：${g(467)}｜切换权：${g(469)}｜默认：${g(473)}"
            },
            ArchiveRow("能力互动", listOf(558)) { g -> g(558) },
        )),
        ArchiveSection("祈宝级补完", listOf(
            ArchiveRow("呆毛 / 情绪外显", listOf(596)) { g -> g(596) },
            ArchiveRow("伪装系统", listOf(597)) { g -> g(597) },
            ArchiveRow("深层恐惧", listOf(601)) { g -> g(601) },
            ArchiveRow("手指常驻位置", listOf(602)) { g -> g(602) },
            ArchiveRow("专属纪念日", listOf(606)) { g -> g(606) },
            ArchiveRow("专属暗号", listOf(624)) { g -> g(624) },
            ArchiveRow("命运级问题", listOf(630)) { g -> g(630) },
        )),
    )

    /** 一句话人设内核（独立字段，qid 594） */
    val CORE_FIELD = CoreField("一句话人设内核", listOf(594)) { g -> g(594) }

    /* ---------- 校验辅助 ---------- */
    fun hasAnswer(v: Any?): Boolean =
        v != null && v.toString().trim().isNotEmpty() && v.toString().trim() != DASH

    fun firstNumber(str: Any?): Double? {
        if (str == null) return null
        val m = Regex("""\d+(?:\.\d+)?""").find(str.toString()) ?: return null
        return m.value.toDoubleOrNull()
    }

    fun alCoefficient(str: Any?): Double? {
        if (str == null) return null
        val s = str.toString()
        Regex("""×\s*(\d+(?:\.\d+)?)""").find(s)?.let { return it.groupValues[1].toDoubleOrNull() }
        return firstNumber(s)
    }

    /* ---------- 核心题提取（含 when 条件） ---------- */
    fun getCoreIds(meta: Meta): List<Int> {
        val set = sortedSetOf<Int>()
        ARCHIVE_SCHEMA.forEach { sec ->
            if (sec.onlyIf != null && !sec.onlyIf!!(meta)) return@forEach
            sec.rows.forEach { row ->
                if (row.onlyIf != null && !row.onlyIf!!(meta)) return@forEach
                row.qids.forEach { set.add(it) }
            }
        }
        CORE_FIELD.qids.forEach { set.add(it) }
        return set.toList()
    }

    /* ---------- 14 条世界观检查清单 ---------- */
    val CHECK_LIST: List<CheckItem> = listOf(
        CheckItem("cw1", "明渊市女孩子通用特质", "是否符合明渊市女孩子的通用特质（可爱、灵动、符合世界观基调）") { _, a ->
            if (hasAnswer(a[91])) CheckResult("manual", "核心性格「${a[91]}」已填，请人工确认符合明渊市女孩子通用特质。")
            else CheckResult("manual", "尚未填写核心性格，请确认符合明渊市女孩子通用特质。")
        },
        CheckItem("cw2", "年龄永久锁定", "永恒法则：年龄必须≥18岁且永久锁定") { _, a ->
            val n = firstNumber(a[3])
            if (n == null) CheckResult("manual", "尚未填写年龄，请确认≥18岁并永久锁定。")
            else if (n >= 18) CheckResult("pass", "年龄 ${n} 岁，满足永恒法则（≥18）。")
            else CheckResult("fail", "年龄 ${n} 岁 < 18 岁，违反永恒法则，必须修改。")
        },
        CheckItem("cw3", "无尽精力法则适配", "是否具备无尽精力法则的适配设定") { _, a ->
            if (hasAnswer(a[585]) || hasAnswer(a[566])) CheckResult("manual", "耐力/AL 字段已填，请人工确认具备无尽精力法则适配。")
            else CheckResult("manual", "尚未填写耐力/AL 字段，请确认无尽精力法则适配。")
        },
        CheckItem("cw4", "指令条系统一致", "指令条系统相关设定是否与主文件一致") { _, a ->
            if (hasAnswer(a[527])) CheckResult("manual", "指令条相关已填，请人工确认与主文件一致。")
            else CheckResult("manual", "未填写指令条相关设定，请确认一致性。")
        },
        CheckItem("cw5", "姓名查重", "不得与既有角色同名/近音（苏/林/洛/宋现有全员对照）") { _, a ->
            val name = if (hasAnswer(a[1])) a[1]!!.trim() else ""
            if (name.isEmpty()) CheckResult("manual", "尚未填写姓名，无法查重。")
            else {
                val dup = Config.OCCUPIED_NAMES.any { n -> n == name || (name.length > 1 && (name.contains(n) || n.contains(name))) }
                if (dup) CheckResult("fail", "姓名「$name」与既有角色同名/近音，必须改名。")
                else CheckResult("pass", "姓名「$name」查重通过。")
            }
        },
        CheckItem("cw6", "昵称查重", "宝系昵称占用表：祈宝/糖宝/栖宝/绮宝/曦宝/念宝/清宝/染宝/萱宝/颜宝/晴宝/浅宝已占用；笙宝=洛柚笙、浅笙=林浅笙均不可复用") { _, a ->
            val nick = if (hasAnswer(a[2])) a[2]!!.trim() else ""
            if (nick.isEmpty()) CheckResult("manual", "尚未填写昵称，无法查重。")
            else {
                val dup = Config.OCCUPIED_NICKNAMES.any { n -> nick == n || (nick.length > 1 && nick.contains(n)) }
                if (dup) CheckResult("fail", "昵称「$nick」已占用，必须更换。")
                else CheckResult("pass", "昵称「$nick」查重通过。")
            }
        },
        CheckItem("cw7", "发情期日期段", "不得与「主世界既定剧情日」冲突（如纪念日当天的归属）") { _, a ->
            if (hasAnswer(a[568]) && firstNumber(a[568]) != null) CheckResult("manual", "日期段「${a[568]}」已填，请人工确认不与既定剧情日冲突。")
            else CheckResult("manual", "尚未填写发情期日期段。")
        },
        CheckItem("cw8", "初夜状态明确", "必须明确✅/❌并登记第四十七章") { _, a ->
            val s = if (hasAnswer(a[580])) a[580]!!.toString() else ""
            when {
                Regex("✅|❌|已|未|发生").containsMatchIn(s) -> CheckResult("pass", "初夜状态「$s」已明确。")
                s.isEmpty() -> CheckResult("manual", "尚未填写初夜状态（需明确✅/❌）。")
                else -> CheckResult("fail", "初夜状态「$s」未明确 ✅/❌，请补充。")
            }
        },
        CheckItem("cw9", "AL 系数区间", "AL系数必须在0.5~1.6区间内（超出需说明理由）") { _, a ->
            val v = alCoefficient(a[566])
            when {
                v == null -> CheckResult("manual", "尚未填写 AL 系数，无法判定区间。")
                v in 0.5..1.6 -> CheckResult("pass", "AL 系数 $v，在 0.5~1.6 区间内。")
                else -> CheckResult("fail", "AL 系数 $v 超出 0.5~1.6 区间，需说明理由或修改。")
            }
        },
        CheckItem("cw10", "种族能力边界", "种族能力不得凌驾于辞宝「唯一男性」地位与「高潮决定权」条款之上") { _, a ->
            if (hasAnswer(a[546])) CheckResult("manual", "种族能力已填，请人工确认不凌驾于「唯一男性」与「高潮决定权」。")
            else CheckResult("manual", "尚未填写种族能力。")
        },
        CheckItem("cw11", "第一梯队关系矩阵", "至少预设两条关系线，不得与既有关系矛盾") { _, a ->
            val s = if (hasAnswer(a[590])) a[590]!!.toString() else ""
            if (s.isEmpty()) CheckResult("manual", "尚未填写第一梯队关系矩阵。")
            else {
                val sep = s.split(Regex("[、，,和与;；/]")).count { it.trim().isNotEmpty() }
                if (sep >= 2) CheckResult("pass", "已预设 $sep 条关系线。")
                else CheckResult("fail", "关系线不足 2 条，请至少预设两条。")
            }
        },
        CheckItem("cw12", "居住区域与职业", "需符合明渊市行政区划现实（第十八章扩展世界观）") { _, a ->
            when {
                hasAnswer(a[10]) && hasAnswer(a[12]) -> CheckResult("pass", "居住区与职业均已填写。")
                !hasAnswer(a[10]) -> CheckResult("manual", "尚未填写现居地址。")
                else -> CheckResult("manual", "尚未填写职业/身份。")
            }
        },
        CheckItem("cw13", "称呼体系", "与第二十三章称呼表不冲突") { _, a ->
            if (hasAnswer(a[591])) CheckResult("manual", "称呼体系已填，请人工确认与称呼表不冲突。")
            else CheckResult("manual", "尚未填写称呼体系。")
        },
        CheckItem("cw14", "体系登记流水", "登记所需信息已备齐（登记页需全部勾选完成）") { _, a ->
            val needFields = listOf(568, 580, 582, 566, 591, 570, 251, 256, 571)
            val missing = needFields.filter { !hasAnswer(a[it]) }
            if (missing.isEmpty()) CheckResult("pass", "登记所需信息已全部备齐。")
            else CheckResult("fail", "登记信息缺失：${missing.joinToString("、") { "#$it" }}，需补填。")
        },
    )

    /* ---------- 登记流水清单 ---------- */
    val REGISTER_LIST: List<RegisterItem> = listOf(
        RegisterItem("r1", "发情期表", "第三十一章", "🌙"),
        RegisterItem("r2", "初夜档案室", "第四十七章", "💌"),
        RegisterItem("r3", "好感度表", "第三十八章", "💗"),
        RegisterItem("r4", "身体数据总表", "第三十三章", "📏"),
        RegisterItem("r5", "称呼表", "第二十七章", "🏷️"),
        RegisterItem("r6", "微信档案", "第四十三章", "💬"),
        RegisterItem("r7", "作息矩阵", "第四十二章", "🕐"),
        RegisterItem("r8", "关系矩阵", "第三十二章", "🕸️"),
        RegisterItem("r9", "亲密空间", "第四十章（如有）", "🏠"),
    )
}
