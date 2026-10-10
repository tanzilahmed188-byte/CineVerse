package com.noor.dua

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.noor.dua.data.DuaRepository
import com.noor.dua.ui.NoorApp
import com.noor.dua.ui.theme.NoorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        DuaRepository.load(applicationContext)
        setContent {
            NoorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NoorApp()
                }
            }
        }
    }
}
