package br.com.fiap.antifraudequod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define o conteúdo da MainActivity para exibir a HomeScreen diretamente
        setContent {
            AntiFraudeQuodTheme {
                HomeScreen()
            }
        }
    }
}
