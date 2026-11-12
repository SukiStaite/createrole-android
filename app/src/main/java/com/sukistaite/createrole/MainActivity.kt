package com.sukistaite.createrole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sukistaite.createrole.ui.MainScreen
import com.sukistaite.createrole.ui.theme.CreateroleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        App.init(this)
        setContent {
            CreateroleTheme(App.state.settings.theme, App.state.settings.dark) {
                MainScreen()
            }
        }
    }
}
