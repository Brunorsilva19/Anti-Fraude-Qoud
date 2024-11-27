package br.com.fiap.antifraudequod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.antifraudequod.components.Header
import br.com.fiap.antifraudequod.components.Footer
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme

// Activity principal que exibe a HomeScreen
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiFraudeQuodTheme {
                // Tela da Home com navegação
                HomeScreen()
            }
        }
    }
}

// Tela principal com navegação
@Composable
fun HomeScreen() {
    // Criação do NavController para navegação
    val navController = rememberNavController()

    // Definição do NavHost para navegar entre as telas
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(navController = navController)
        }
        composable("formulario") {
            // Navegação para a tela de formulário
            FormularioPage(navController = navController) // Importar corretamente a tela de formulário
        }
        composable("documento") {
            DocumentoPage(navController = navController) // Defina a tela de destino conforme necessário
        }
        composable("biometria") {
            BiometriaPage(navController = navController) // Defina a tela de destino conforme necessário
        }
        composable("facial") {
            FacialPage(navController = navController) // Defina a tela de destino conforme necessário
        }
    }
}

// Tela HomePage com cabeçalho e rodapé
@Composable
fun HomePage(navController: NavHostController) {
    // Definindo a rolagem vertical
    val scrollState = rememberScrollState()

    // Usando o Scaffold para organização do layout
    Scaffold(
        topBar = {
            Header() // Cabeçalho
        },
        bottomBar = {
            Footer(navController = navController) // Rodapé
        }
    ) { paddingValues ->
        // Conteúdo principal da tela
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Para garantir que o conteúdo não sobreponha o rodapé
                .verticalScroll(scrollState) // Adiciona a rolagem vertical
        ) {
            // Espaçamento superior
            Spacer(modifier = Modifier.height(16.dp))

            // Corpo da tela (conteúdo adicional ou formulário, se necessário)
            Box(modifier = Modifier.weight(1f)) {
                // Adicionar o conteúdo que você precisa aqui
            }
        }
    }
}

// Função de pré-visualização da tela Home
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AntiFraudeQuodTheme {
        // Visualização da tela Home sem a necessidade de navegação
        HomeScreen()
    }
}
