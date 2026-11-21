package com.maharlika.executor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.luaj.vm2.Globals
import org.luaj.vm2.lib.jse.JsePlatform

private val Ink = Color(0xFF101318)
private val Panel = Color(0xFF1A2029)
private val Accent = Color(0xFF39C58A)
private val Muted = Color(0xFF9AA6B2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaharlikaApp() }
    }
}

@Composable
private fun MaharlikaApp() {
    var script by remember { mutableStateOf("-- Welcome to Maharlika Executor\nprint('Hello, Maharlika!')") }
    var output by remember { mutableStateOf("Ready. Lua sandbox initialized when you run a script.") }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Accent,
        background = Ink,
        surface = Panel,
        onPrimary = Ink,
        onBackground = Color.White,
        onSurface = Color.White
    )) {
        Column(
            modifier = Modifier.fillMaxSize().background(Ink).padding(18.dp)
        ) {
            Text("MAHARLIKA", color = Accent, fontSize = 13.sp, letterSpacing = 3.sp)
            Text("Executor", color = Color.White, fontSize = 30.sp)
            Text("Lua scripting workspace", color = Muted, fontSize = 13.sp)
            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("SCRIPT EDITOR", color = Muted, fontSize = 11.sp, letterSpacing = 1.5.sp)
                Text("LUA", color = Accent, fontSize = 11.sp)
            }
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.weight(1f).fillMaxWidth()
                    .background(Panel, RoundedCornerShape(14.dp)).padding(14.dp)
            ) {
                BasicTextField(
                    value = script,
                    onValueChange = { script = it },
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color(0xFFE4EAF0), fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace, lineHeight = 21.sp
                    )
                )
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        output = try {
                            val globals: Globals = JsePlatform.standardGlobals()
                            globals.load(script, "maharlika-script").call()
                            "Execution finished successfully."
                        } catch (error: Exception) {
                            "Lua error: ${error.message ?: "Unknown error"}"
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                ) { Text("Run Lua", color = Ink) }
                OutlinedButton(
                    onClick = { script = "" },
                    modifier = Modifier.weight(1f)
                ) { Text("Clear") }
            }
            Spacer(Modifier.height(18.dp))
            Text("CONSOLE", color = Muted, fontSize = 11.sp, letterSpacing = 1.5.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                output,
                modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(14.dp),
                color = Color(0xFFD5DEE7), fontSize = 12.sp, fontFamily = FontFamily.Monospace
            )
            Spacer(Modifier.height(8.dp))
            Text("Standalone Lua only. No Roblox injection or game-process access.", color = Muted, fontSize = 10.sp)
        }
    }
}
