package br.com.fiap.antifraudequod.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red) // Cor de fundo vermelha
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // Centralizando o título
    ) {
        // Título "Anti Fraude" no centro do cabeçalho
        Text(text = "Anti Fraude", color = Color.White, style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Header()
}
