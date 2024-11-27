package br.com.fiap.antifraudequod

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme
import br.com.fiap.antifraudequod.R.drawable.ic_mao
import br.com.fiap.antifraudequod.components.Footer
import br.com.fiap.antifraudequod.components.Header

class BiometriaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiFraudeQuodTheme {
                BiometriaScreen()
            }
        }
    }
}

@Composable
fun BiometriaScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "biometria") {
        composable("biometria") {
            BiometriaPage(navController = navController)
        }
        composable("home") {
            HomePage(navController = navController)
        }
        composable("facial") {
            FacialPage(navController = navController) // Defina a tela de destino conforme necessário
        }
        composable("formulario") {
            FormularioPage(navController = navController) // Defina a tela de destino conforme necessário
        }
        composable("documento") {
            DocumentoPage(navController = navController) // Defina a tela de destino conforme necessário
        }
    }
}

@Composable
fun BiometriaPage(navController: NavHostController) {
    var showFirstDialog by remember { mutableStateOf(false) }
    var showSecondDialog by remember { mutableStateOf(false) }
    var selectedBiometria by remember { mutableStateOf<String?>(null) }
    var isBiometriaValid by remember { mutableStateOf(false) }

    // Scaffold para organizar o layout
    Scaffold(
        topBar = {
            Header() // Cabeçalho
        },
        bottomBar = {
            Footer(navController = navController) // Rodapé
        }
    ) { paddingValues -> // Padding para o conteúdo não sobrepor o rodapé
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Para garantir que o conteúdo não sobreponha o rodapé
                .verticalScroll(rememberScrollState()) // Adiciona rolagem vertical
        ) {
            // Ícone de dedo centralizado
            Image(
                painter = painterResource(id = ic_mao),
                contentDescription = "Ícone de Biometria",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .clickable { showFirstDialog = true } // Abre o popup ao clicar no ícone
            )

            // Botões de ação no canto inferior central
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp), // Espaçamento do rodapé
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (selectedBiometria == "Biometria 1") {
                            isBiometriaValid = true
                            Toast.makeText(navController.context, "Biometria válida!", Toast.LENGTH_SHORT).show()
                        } else {
                            isBiometriaValid = false
                            Toast.makeText(navController.context, "Biometria inválida!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBiometriaValid) Color.Red else Color.Gray
                    )
                ) {
                    Text(text = "Verificar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("facial") },
                    enabled = isBiometriaValid, // Apenas habilitado se a verificação foi bem-sucedida
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBiometriaValid) Color.Red else Color.Gray
                    )
                ) {
                    Text(text = "Avançar")
                }
            }
        }

        // Primeira caixa de diálogo
        if (showFirstDialog) {
            BiometricOptionsDialog(
                onDismiss = { showFirstDialog = false },
                onUseExisting = {
                    showFirstDialog = false
                    showSecondDialog = true // Mostra o segundo popup
                },
                onAddNew = {
                    showFirstDialog = false
                    Toast.makeText(navController.context, "Adicionar nova digital não suportado", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Segunda caixa de diálogo
        if (showSecondDialog) {
            BiometriaSelectionDialog(
                onDismiss = { showSecondDialog = false },
                onBiometriaSelected = { biometria ->
                    selectedBiometria = biometria
                    showSecondDialog = false
                    Toast.makeText(
                        navController.context,
                        "Selecionado: $biometria",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}

@Composable
fun BiometricOptionsDialog(
    onDismiss: () -> Unit,
    onUseExisting: () -> Unit,
    onAddNew: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Escolha uma opção") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onUseExisting,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Cor vermelha
                ) {
                    Text(text = "Usar digital existente", color = Color.White) // Texto branco
                }
                Button(
                    onClick = onAddNew,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Cor vermelha
                ) {
                    Text(text = "Adicionar nova digital", color = Color.White) // Texto branco
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray) // Cor do botão confirmar
            ) {
                Text("Fechar", color = Color.White) // Texto branco
            }
        }
    )
}

@Composable
fun BiometriaSelectionDialog(
    onDismiss: () -> Unit,
    onBiometriaSelected: (String) -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Selecione a biometria") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onBiometriaSelected("Biometria 1") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Cor vermelha
                ) {
                    Text(text = "Biometria 1", color = Color.White) // Texto branco
                }
                Button(
                    onClick = { onBiometriaSelected("Biometria 2") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Cor vermelha
                ) {
                    Text(text = "Biometria 2", color = Color.White) // Texto branco
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray) // Cor do botão confirmar
            ) {
                Text("Fechar", color = Color.White) // Texto branco
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BiometriaScreenPreview() {
    AntiFraudeQuodTheme {
        BiometriaPage(navController = rememberNavController())
    }
}
