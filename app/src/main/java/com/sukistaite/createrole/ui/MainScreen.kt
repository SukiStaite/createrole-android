package com.sukistaite.createrole.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sukistaite.createrole.App
import com.sukistaite.createrole.ui.components.AmbientBackground
import com.sukistaite.createrole.ui.screens.*

/** 顶层路由：按阶段切屏 */
@Composable
fun MainScreen() {
    val stage = App.state.stage
    AmbientBackground(Modifier.fillMaxSize()) {
        when (stage) {
            "meta" -> MetaScreen()
            "ask" -> AskScreen()
            "archive" -> ArchiveScreen()
            "check" -> CheckScreen()
            "register" -> RegisterScreen()
            "test" -> TestScreen()
            "settings" -> SettingsScreen()
            else -> WelcomeScreen()
        }
    }
}
