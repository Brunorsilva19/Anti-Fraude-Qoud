package br.com.fiap.antifraudequod

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.antifraudequod.components.Footer
import br.com.fiap.antifraudequod.components.Header
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme

class DocumentoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiFraudeQuodTheme {
                DocumentoScreen()
            }
        }
    }
}

@Composable
fun DocumentoScreen() {
    // Criação do NavController para navegação
    val navController = rememberNavController()

    // Definição do NavHost para navegar entre as telas
    NavHost(navController = navController, startDestination = "documento") {
        composable("documento") {
            DocumentoPage(navController = navController)
        }
        composable("home") {
            HomePage(navController = navController) // Navegação para a tela de formulário
        }
        composable("formulario") {
            FormularioPage(navController = navController) // Defina a tela de destino conforme necessário
        }
        composable("biometria") {
            BiometriaPage(navController = navController) // Defina a tela de destino conforme necessário
        }
    }
}

@Composable
fun DocumentoPage(navController: NavHostController) {
    var showPopup by remember { mutableStateOf(false) }
    var facePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var documentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var facePhotoAttached by remember { mutableStateOf(false) }
    var documentPhotoAttached by remember { mutableStateOf(false) }

    // Launcher para selecionar a imagem
    val getImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Quando uma imagem for selecionada, armazene a URI
            facePhotoUri = it
            facePhotoAttached = true
        }
    }

    val getDocumentImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Quando uma imagem for selecionada, armazene a URI
            documentPhotoUri = it
            documentPhotoAttached = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Cabeçalho
        Header()

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Ícone de câmera
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_camera),
                contentDescription = "Ícone de câmera",
                modifier = Modifier
                    .size(64.dp)
                    .clickable { showPopup = true }, // Abre o popup ao clicar
                tint = Color.Black
            )

            // Popup com as opções de envio
            if (showPopup) {
                Dialog(onDismissRequest = { showPopup = false }) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Escolha uma opção", style = MaterialTheme.typography.titleMedium)

                            Spacer(modifier = Modifier.height(8.dp))

                            // Enviar foto do rosto
                            Button(
                                onClick = {
                                    getImage.launch("image/*") // Abre a galeria para selecionar uma imagem
                                    showPopup = false // Fecha o popup
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Enviar foto do rosto")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Enviar foto do documento
                            Button(
                                onClick = {
                                    getDocumentImage.launch("image/*") // Abre a galeria para selecionar uma imagem
                                    showPopup = false // Fecha o popup
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Enviar foto do documento")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Avançar com fundo vermelho
        Button(
            onClick = {
                // Ação para avançar
            },
            enabled = facePhotoAttached && documentPhotoAttached, // Habilita o botão somente após os anexos
            colors = ButtonDefaults.buttonColors(
                containerColor = if (facePhotoAttached && documentPhotoAttached) Color.Red else Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Texto do botão
            Text("Avançar", color = Color.White)
        }

        // Rodapé
        Footer(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun DocumentoScreenPreview() {
    AntiFraudeQuodTheme {
        DocumentoScreen()
    }
}