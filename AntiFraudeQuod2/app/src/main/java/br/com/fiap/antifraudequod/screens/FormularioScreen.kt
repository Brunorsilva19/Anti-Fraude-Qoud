package br.com.fiap.antifraudequod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.antifraudequod.components.Header
import br.com.fiap.antifraudequod.components.Footer
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme
import java.util.regex.Pattern

class FormularioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiFraudeQuodTheme {
                FormualarioScreen() // Configura a tela principal com navegação
            }
        }
    }
}
@Composable
fun FormualarioScreen() {
    // Criação do NavController para navegação
    val navController = rememberNavController()

    // Definição do NavHost para navegar entre as telas
    NavHost(navController = navController, startDestination = "formulario") {
        composable("formulario") {
            FormularioPage(navController = navController)
        }
        composable("home") {
            HomePage(navController = navController) // Navegação para a tela de formulário
        }
        composable("documento") {
            DocumentoPage(navController = navController) // Defina a tela de destino conforme necessário
        }
        composable("biometria") {
            BiometriaPage(navController = navController, activity = BiometriaActivity()) // Defina a tela de destino conforme necessário
        }
    }
}

@Composable
fun FormularioPage(navController: NavHostController) {

    val scrollState = rememberScrollState() // Estado de rolagem

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Adiciona a rolagem vertical
    ) {
        // Cabeçalho
        Header()
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Conteúdo do formulário
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // Estados dos campos
                var cpf by remember { mutableStateOf("") }
                var nome by remember { mutableStateOf("") }
                var endereco by remember { mutableStateOf("") }
                var telefone by remember { mutableStateOf("") }

                // Mensagens de erro
                var cpfError by remember { mutableStateOf("") }
                var nomeError by remember { mutableStateOf("") }
                var enderecoError by remember { mutableStateOf("") }
                var telefoneError by remember { mutableStateOf("") }

                // Estado do score
                var score by remember { mutableStateOf(0) }
                var scoreMessage by remember { mutableStateOf("") }
                var scoreColor by remember { mutableStateOf(Color.Transparent) }

                // Estados de verificação e ativação do botão Avançar
                var isVerified by remember { mutableStateOf(false) }
                var isFormValid by remember { mutableStateOf(false) }

                // Função de validação do telefone
                fun validatePhone(phone: String): Boolean {
                    return if (phone.isNotEmpty() && phone.last().isDigit()) {
                        phone.last().digitToInt() % 2 == 0 // Verifica se o último dígito é par
                    } else {
                        false
                    }
                }

                // Função para calcular o score
                fun calculateScore(cpf: String): Int {
                    return when (cpf.firstOrNull()) {
                        '1' -> (300..400).random()
                        '2' -> (700..800).random()
                        '3' -> (100..200).random()
                        '4' -> (600..700).random()
                        '5' -> (900..1000).random()
                        '6' -> (500..600).random()
                        '7' -> (0..100).random()
                        '8' -> (200..300).random()
                        '9' -> (400..500).random()
                        else -> 0 // CPF inválido
                    }
                }

                // Validação de CPF
                fun validateCpf(): Boolean {
                    score = calculateScore(cpf)

                    // Definir as mensagens e cores com base no score
                    when {
                        score < 100 -> {
                            scoreMessage = "Score Suspeito"
                            scoreColor = Color.Red
                            return false
                        }
                        score in 100..300 -> {
                            scoreMessage = "Score Muito Abaixo"
                            scoreColor = Color.Red
                            return false
                        }
                        score in 300..500 -> {
                            scoreMessage = "Score Mediano"
                            scoreColor = Color.Yellow
                        }
                        score in 500..700 -> {
                            scoreMessage = "Score Bom"
                            scoreColor = Color.Green
                        }
                        score > 700 -> {
                            scoreMessage = "Score Muito Bom"
                            scoreColor = Color.Green
                        }
                    }

                    return true
                }

                // Validação geral dos campos
                fun validateFields(): Boolean {
                    var isValid = true

                    // Validação de CPF
                    cpfError = if (!isValidCpf(cpf) || !validateCpf()) {
                        isValid = false
                        "CPF inválido"
                    } else {
                        ""
                    }

                    // Validação de Nome
                    nomeError = if (nome.isBlank()) {
                        isValid = false
                        "Nome é obrigatório"
                    } else {
                        ""
                    }

                    // Validação de Endereço
                    enderecoError = if (endereco.isBlank()) {
                        isValid = false
                        "Endereço é obrigatório"
                    } else {
                        ""
                    }

                    // Validação de Telefone
                    val isPhoneValid = validatePhone(telefone)
                    telefoneError = if (!isPhoneValid) {
                        isValid = false
                        "Telefone inválido"
                    } else {
                        ""
                    }

                    // Atualize o estado do formulário após a validação
                    isFormValid = isValid
                    return isValid
                }

                // Função para validar e ativar o botão Avançar
                fun handleVerification() {
                    isVerified = true
                    isFormValid = validateFields()
                }

                // Título do formulário
                Text(
                    text = "Autenticação Cadastral",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                // Campo CPF
                CustomTextField(
                    label = "CPF",
                    placeholder = "Digite seu CPF",
                    value = cpf,
                    onValueChange = { cpf = it },
                    errorMessage = cpfError,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Exibir mensagem de score
                if (cpf.isNotEmpty()) {
                    Text(
                        text = "Score: $score - $scoreMessage",
                        color = scoreColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campos de Nome, Endereço e Telefone...
                CustomTextField(
                    label = "Nome Completo",
                    placeholder = "Digite seu nome",
                    value = nome,
                    onValueChange = { nome = it },
                    errorMessage = nomeError,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    label = "Endereço",
                    placeholder = "Digite seu endereço",
                    value = endereco,
                    onValueChange = { endereco = it },
                    errorMessage = enderecoError,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Exibir o campo do telefone
                CustomTextField(
                    label = "Telefone Celular",
                    placeholder = "(XX) XXXXX-XXXX",
                    value = telefone,
                    onValueChange = { telefone = it },
                    errorMessage = telefoneError,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botões de Verificação e Avanço
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botão Verificar
                    Button(
                        onClick = {
                            handleVerification() // Valida todos os campos e ativa a verificação
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isVerified && isFormValid) Color.Red else Color.Gray
                        )
                    ) {
                        Text(text = "Verificar", color = Color.White)
                    }

                    // Botão Avançar
                    Button(
                        onClick = {
                            if (isFormValid) {
                                navController.navigate("documento") // Navegação para próxima tela
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = isFormValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFormValid) Color.Red else Color.Gray
                        )
                    ) {
                        Text(text = "Avançar", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Footer(navController = navController)
            }
        }
    }
}


@Composable
fun CustomTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    keyboardOptions: KeyboardOptions
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            isError = errorMessage.isNotEmpty()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Red),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


fun isValidCpf(cpf: String): Boolean {
    val pattern = Pattern.compile("^(\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2})$")
    return pattern.matcher(cpf).matches()
}

fun isValidPhone(phone: String): Boolean {
    val pattern = Pattern.compile("^\\(\\d{2}\\) \\d{5}-\\d{4}$")
    return pattern.matcher(phone).matches()
}



@Preview(showBackground = true)
@Composable
fun FormularioScreenPreview() {
    AntiFraudeQuodTheme {
        FormualarioScreen()
    }
}