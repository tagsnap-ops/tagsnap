package com.tagsnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tagsnap.ui.AppRoot
import com.tagsnap.ui.theme.TagSnapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TagSnapTheme {
                AppRoot()
            }
        }
    }
}
