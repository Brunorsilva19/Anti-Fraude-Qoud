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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.antifraudequod.R.drawable.ic_rosto
import br.com.fiap.antifraudequod.components.Footer
import br.com.fiap.antifraudequod.components.Header
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color


class FacialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiFraudeQuodTheme {
                FacialScreen()
            }
        }
    }
}

@Composable
fun FacialScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "facial") {
        composable("facial") {
            FacialPage(navController = navController)
        }
        composable("home") {
            HomePage(navController = navController)
        }
        composable("documento") {
            DocumentoPage(navController = navController)
        }
        composable("formulario") {
            FormularioPage(navController = navController)
        }
        composable("biometria") {
            BiometriaPage(navController = navController)
        }
    }
}

@Composable
fun FacialPage(navController: NavHostController) {
    var showPopup by remember { mutableStateOf(false) }
    var showSubPopup by remember { mutableStateOf(false) }
    var selectedFacial by remember { mutableStateOf("") }
    var isFacialValid by remember { mutableStateOf(false) }

    // Scaffold para organizar o layout
    Scaffold(
        topBar = {
            Header() // Cabeçalho
        },
        bottomBar = {
            Footer(navController = navController) // Rodapé
        }
    ) { paddingValues ->

        // Conteúdo principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Coluna centralizada para o ícone
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = painterResource(id = ic_rosto),
                    contentDescription = "Ícone de Facial",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { showPopup = true } // Abre o pop-up de escolha
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botões no rodapé
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Button(
                    onClick = {
                        // Lógica de Verificação
                        isFacialValid = selectedFacial == "Facial 1" // Verifique se o facial selecionado é válido
                        Toast.makeText(
                            navController.context,
                            if (isFacialValid) "Facial válida!" else "Facial inválida!",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFacialValid) Color.Red else Color.Gray
                    )
                ) {
                    Text(text = "Verificar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("home") },
                    enabled = isFacialValid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFacialValid) Color.Red else Color.Gray
                    )
                ) {
                    Text(text = "Avançar")
                }
            }
        }
    }

    // Pop-up inicial
    if (showPopup) {
        FacialOptionsDialog(
            onDismiss = { showPopup = false },
            onUseExisting = {
                showSubPopup = true
                showPopup = false // Fecha o pop-up atual
            },
            onAddNew = {
                Toast.makeText(navController.context, "Adicionar nova facial não suportado", Toast.LENGTH_SHORT).show()
                showPopup = false
            }
        )
    }

    // Pop-up de seleção de facial
    if (showSubPopup) {
        FacialSelectionDialog(
            onDismiss = { showSubPopup = false },
            onFacialSelected = { facial ->
                selectedFacial = facial
                showSubPopup = false
                Toast.makeText(navController.context, "Selecionado: $facial", Toast.LENGTH_SHORT).show()
            }
        )
    }
}


@Composable
fun FacialOptionsDialog(
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
                    Text(text = "Usar facial existente", color = Color.White) // Texto branco
                }
                Button(
                    onClick = onAddNew,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Cor vermelha
                ) {
                    Text(text = "Adicionar facial", color = Color.White) // Texto branco
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
fun FacialSelectionDialog(
    onDismiss: () -> Unit,
    onFacialSelected: (String) -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Escolha um facial") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onFacialSelected("Facial 1") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Cor vermelha
                ) {
                    Text(text = "Facial 1", color = Color.White) // Texto branco
                }
                Button(
                    onClick = { onFacialSelected("Facial 2") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Cor vermelha
                ) {
                    Text(text = "Facial 2", color = Color.White) // Texto branco
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
fun FacialScreenPreview() {
    AntiFraudeQuodTheme {
        FacialPage(navController = rememberNavController())
    }
}
