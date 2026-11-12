package com.sukistaite.createrole.data

/**
 * 全局配置常量 —— 逐字段对齐网页版 config.js。
 * 主题主色 #f06a8c（琉璃梦境×手帐，默认浅色）。
 */
object Config {
    const val VERSION = "1.0"
    const val CODE_NAME = "双子"
    const val STORAGE_KEY = "aira_dajuese_v42"
    const val MEMORY_KEY = "aira_dajuese_memories_v42"

    const val TOTAL_QUESTIONS = 630
    const val AUTOSAVE_DELAY_MS = 600L
    const val DEFAULT_MEMORY_FREQ = 20
    const val TYPE_SPEED_MS = 18
    const val DEFAULT_SOUND = true
    const val SECONDS_PER_QUESTION = 30

    data class Style(val name: String, val desc: String, val radius: Int, val pill: Boolean)
    val STYLES: Map<String, Style> = mapOf(
        "sakura" to Style("粉嫩", "现在 · 温柔治愈", 14, false),
        "ios27" to Style("iOS 27", "苹果风 · 玻璃拟态", 16, true),
        "material3" to Style("Material 3", "谷歌风 · 大圆角胶囊", 22, true),
    )
    val ANIM_LEVELS = listOf("关", "少", "全")
    val FLOW_MODES = listOf("标准", "流畅")
    val FONT_SCALES = listOf(0.9f, 1f, 1.1f, 1.2f)

    data class LayerInfo(val name: String, val color: String, val icon: String)
    val LAYER_INFO: Map<String, LayerInfo> = mapOf(
        "SFW" to LayerInfo("SFW", "#4c9a6a", "🌸"),
        "NSFW" to LayerInfo("NSFW", "#c05a5a", "🔥"),
        "DLC" to LayerInfo("DLC特化", "#7b6fc0", "🦋"),
        "体系登记" to LayerInfo("体系登记", "#d99a3d", "📋"),
        "补完" to LayerInfo("祈宝级补完", "#c05a7e", "💎"),
    )

    data class Quota(val sfw: Int, val nsfw: Int, val dlc: Int)
    data class Ratio(val sfw: Double, val nsfw: Double, val dlc: Double)
    val VOLUME_MAP: Map<Int, Quota> = mapOf(
        120 to Quota(60, 60, 0),
        180 to Quota(60, 60, 60),
        240 to Quota(100, 100, 40),
        300 to Quota(100, 100, 100),
    )
    val CUSTOM_RATIO = Ratio(0.4, 0.4, 0.2)

    val GENDER_OPTIONS = listOf("男", "女", "扶她", "其他")
    val ORIENTATION_OPTIONS = listOf("SFW", "NSFW", "SFW＋NSFW", "SM", "BDSM", "BL", "GL", "BG")
    val DLC9_OPTIONS = listOf("是", "否")
    val RACE_OPTIONS = listOf("纯人类", "猫娘", "魅魔", "史莱姆", "龙娘", "天使", "精灵", "多形态")
    val VOLUME_OPTIONS = listOf(120, 180, 240, 300)

    const val ASK_SINGLE = "single"
    const val ASK_BATCH = "batch"
    val BATCH_SIZES = listOf(3, 5, 8, 10)
    val MEMORY_FREQ = listOf(10, 20, 50, 100)

    data class TestScene(val id: String, val icon: String, val title: String, val desc: String)
    val TEST_SCENES = listOf(
        TestScene("daily", "🌅", "日常温馨版", "清晨醒来，阳光洒进房间，她揉着眼睛看你——一个平凡而甜蜜的日常开场。"),
        TestScene("flirt", "💫", "暧昧试探版", "夜色渐浓，她轻轻拉你的衣角，眼神闪烁——一场心跳加速的暧昧试探。"),
        TestScene("nsfw", "🔥", "高张力 NSFW 版", "灯光昏黄，她呼吸微乱地望着你，空气中弥漫着某种气息——一场炽热的高张力开场。"),
    )

    val OCCUPIED_NAMES = listOf("苏祈", "林晴祈", "洛柚笙", "宋栖", "林浅笙", "洛绮", "苏糖", "苏曦", "林念", "洛清", "林染", "洛萱", "宋颜", "林晴", "洛浅")
    val OCCUPIED_NICKNAMES = listOf("祈宝", "糖宝", "栖宝", "绮宝", "曦宝", "念宝", "清宝", "染宝", "萱宝", "颜宝", "晴宝", "浅宝", "笙宝", "浅笙")

    data class ThemeColor(val primary: String, val primaryDark: String, val accent: String, val bg: String)
    val THEMES: Map<String, ThemeColor> = mapOf(
        "琉璃梦境" to ThemeColor("#f06a8c", "#d14a70", "#f6a8c2", "#fff7f3"),
        "祈宝粉" to ThemeColor("#e56a9a", "#c94f7f", "#f3a6c2", "#fdf6f9"),
        "樱草紫" to ThemeColor("#9a6fd0", "#7b53b0", "#cdb4f0", "#f8f4fd"),
        "青柠绿" to ThemeColor("#4c9a6a", "#35794e", "#a8d8b8", "#f3faf5"),
        "暖阳橙" to ThemeColor("#d98a3d", "#b06f2a", "#f0c9a0", "#fdf7f0"),
    )
    val DEFAULT_THEME = "琉璃梦境"

    data class Preset(val id: String, val icon: String, val name: String, val desc: String,
                      val gender: String?, val volume: Int, val orientation: String?, val dlc9: String?, val race: String?, val strictNaming: Boolean)
    val PRESETS = listOf(
        Preset("sweet", "🍬", "纯爱高甜", "SFW＋NSFW · 240 题 · 纯人类 · 严格命名", "女", 240, "SFW＋NSFW", "是", "纯人类", true),
        Preset("slow", "🌱", "慢热日常", "SFW · 180 题 · 纯人类 · 严格命名", "女", 180, "SFW", "是", "纯人类", true),
        Preset("dom", "🖤", "调教向", "SM · 300 题 · 魅魔 · 自由命名", "女", 300, "SM", "是", "魅魔", false),
        Preset("daily", "🌞", "日常向", "SFW · 120 题 · 猫娘 · 严格命名", "女", 120, "SFW", "否", "猫娘", true),
    )

    val QUOTES = listOf(
        "呐呐，你在想什么呀？我陪着你哦～",
        "这个问题…人家也想过好多次呢！",
        "嘿嘿，你认真的样子，好可爱～",
        "别急别急，我们慢慢来，我不走～",
        "嗯…让我想想，你会怎么回答呢？",
        "偷偷告诉你，我超喜欢这个环节的！",
        "今天也要一起把角色养得胖胖的哦～",
        "你答的每一句，我都记在心里啦。",
    )

    val MOOD_WORDS: Map<String, List<String>> = mapOf(
        "SFW" to listOf("嗯嗯，这个回答，人家很喜欢～", "记下啦记下啦！", "你好懂她呀！", "这个细节，超加分！"),
        "NSFW" to listOf("…这个，人家害羞了啦～", "唔…这样也可以哦…", "你、你居然问到这个…", "（脸红）记下了…"),
        "DLC" to listOf("嗯，这个设定可以哦～", "收到！放进小本本。", "哇，这个好有感觉！"),
        "REGISTER" to listOf("体系登记好了，放心～", "嗯，这个很重要，记牢啦。"),
        "COMPLETE" to listOf("补完了！她越来越完整啦～", "这一步做完，就更像她了！"),
    )

    val SNIPPETS: Map<String, List<String>> = mapOf(
        "SFW" to listOf("她悄悄弯起嘴角，把这个答案记进了心里。", "窗外的光落在她发梢，她轻轻点头。", "「嗯，这样就好。」她的声音软软的。"),
        "NSFW" to listOf("她的耳尖悄悄红了，声音低了几分。", "她别开视线，指尖轻轻勾住衣角。", "「…再说下去，我会当真的哦。」"),
        "DLC" to listOf("她把这份设定折进小册子，眼神亮晶晶的。", "「这个设定…你比我想得更用心呢。」"),
    )

    val STAMPS: Map<String, String> = mapOf("基础" to "🌸", "体系" to "📜", "补完" to "✨", "完整" to "👑")
    const val FINAL_WORDS = "谢谢你陪她走到这一步。她不再是纸上的字句——她活过来了。以后，请多关照。 —— 林晴祈"
    val SEASONS = listOf("春", "夏", "秋", "冬")

    data class I18n(val start: String, val cont: String, val confirm: String, val cancel: String, val close: String)
    val I18N: Map<String, I18n> = mapOf(
        "zh" to I18n("开始搭角色 ✨", "继续上次", "确认", "取消", "关闭"),
        "en" to I18n("Start ✨", "Resume", "Confirm", "Cancel", "Close"),
    )

    data class Music(val title: String, val artist: String, val url: String)
    val MUSIC_PLAYLIST = listOf(
        Music("星空漫步", "SoundHelix", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
        Music("月下祈愿", "SoundHelix", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
        Music("花间絮语", "SoundHelix", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"),
        Music("祈宝晚安", "SoundHelix", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"),
    )
    const val QQ_MUSIC_NAME = "祈宝的歌单"
    const val QQ_MUSIC_URL = "https://y.qq.com/n/ryqq/songDetail/0009fXyT0Wj9zL"
}
