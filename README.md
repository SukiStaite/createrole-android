# 搭角色（AIRA）· Android

为「她」造一个灵魂的角色创建工具。原生 Android 版，Kotlin + Jetpack Compose + Material 3。

> 网页版见 [SukiStaite/createrole](https://github.com/SukiStaite/createrole)。

## ✨ 特色

- **琉璃梦境 × 少女笔记**：液态玻璃卡片 + 手帐胶带标题，默认浅色，超强沉浸感
- **630 题深度题库**（SFW / NSFW / DLC / 体系登记 / 祈宝级补完），自动分层抽题、档案零留空
- **14 条世界观检查**：年龄锁定、姓名/昵称查重、AL 系数区间、关系矩阵等自动校验
- **沉浸感**：答题后祈宝语气词 + 小片段、印章收集、小剧场试演
- **答完自动下一题**、随时回上一题
- **角色卡导入/编辑**：支持 SillyTavern V2/V3 角色卡、自家档案 JSON、纯文本，自动识别，字段全可改
- **导出**：Markdown / JSON 档案 / SillyTavern 角色卡（V2/V3）
- **设置**：主题色、暗色模式、动画等级、语言、NSFW 私密感、记忆存档频率、批题/单题

## 🛠 构建

要求：JDK 17，Android SDK（compileSdk 35，minSdk 26）。

```bash
./gradlew assembleDebug   # 调试包
./gradlew assembleRelease # 发布包（R8 混淆）
```

安装：`./gradlew installDebug` 或直接安装 `app/build/outputs/apk/debug/*.apk`。

## 📄 数据

题库存于 `app/src/main/assets/questions.json`（由网页版 `data/questions.js` + `data/question_ext.js` 合并生成，630 题全带选项描述）。

## 📜 许可证

[MIT](./LICENSE) © SukiStaite
