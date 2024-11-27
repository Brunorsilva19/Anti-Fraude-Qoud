package br.com.fiap.antifraudequod

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
        composable("facial") {
            FacialPage(navController = navController) // Defina a tela de destino conforme necessário
        }
    }
}

@Composable
fun DocumentoPage(navController: NavHostController) {
    var showPopup by remember { mutableStateOf(false) }
    var facePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var documentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showError by remember { mutableStateOf("") } // Agora uma string para a mensagem de erro
    var isVerified by remember { mutableStateOf(false) }

    val getImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            facePhotoUri = it
            showError = "" // Limpa qualquer mensagem de erro
        }
    }

    val getDocumentImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            documentPhotoUri = it
            showError = "" // Limpa qualquer mensagem de erro
        }
    }

    // Scaffold para organizar o layout
    Scaffold(
        topBar = {
            Header() // Cabeçalho
        },
        bottomBar = {
            Footer(navController = navController) // Rodapé
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Para garantir que o conteúdo não sobreponha o rodapé
                .verticalScroll(rememberScrollState()) // Adiciona a rolagem vertical
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Caixa de conteúdo centralizada com ícone de câmera
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "Ícone de câmera",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { showPopup = true },
                    tint = Color.Black
                )

                // Popup para escolher a imagem
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

                                // Botão para foto do rosto
                                Button(
                                    onClick = {
                                        getImage.launch("image/*")
                                        showPopup = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Enviar foto do rosto")
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Botão para foto do documento
                                Button(
                                    onClick = {
                                        getDocumentImage.launch("image/*")
                                        showPopup = false
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

            // Exibição de erro ou mensagem de validação
            if (showError.isNotEmpty()) {
                Text(
                    text = showError,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botão Verificar
            Button(
                onClick = {
                    // Valida as imagens
                    when {
                        facePhotoUri == null && documentPhotoUri == null -> {
                            showError = "Documento inválido: Nenhuma imagem anexada."
                            isVerified = false
                        }
                        facePhotoUri == null || documentPhotoUri == null -> {
                            showError = "Documento inválido."
                            isVerified = false
                        }
                        else -> {
                            showError = "Documento válido."
                            isVerified = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isVerified) Color.Red else Color.Gray // Botão vermelho se verificado, cinza caso contrário
                )
            ) {
                Text("Verificar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botão Avançar, habilitado somente se as fotos forem enviadas
            Button(
                onClick = {
                    if (isVerified) {
                        navController.navigate("biometria")
                    }
                },
                enabled = isVerified, // Somente habilitado se a verificação for bem-sucedida
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isVerified) Color.Red else Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Avançar", color = Color.White)
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun DocumentoScreenPreview() {
    AntiFraudeQuodTheme {
        DocumentoScreen()
    }
}