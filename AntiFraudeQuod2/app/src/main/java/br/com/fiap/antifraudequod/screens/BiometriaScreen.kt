package br.com.fiap.antifraudequod

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.clickable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme
import br.com.fiap.antifraudequod.R.drawable.ic_mao
import br.com.fiap.antifraudequod.components.Footer
import br.com.fiap.antifraudequod.components.Header

class BiometriaActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiFraudeQuodTheme {
                BiometriaScreen(this)
            }
        }
    }
}

@Composable
fun BiometriaScreen(activity: FragmentActivity) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "biometria") {
        composable("biometria") {
            BiometriaPage(navController = navController, activity = activity)
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
    }
}

@Composable
fun BiometriaPage(navController: NavHostController, activity: FragmentActivity) {
    var showDialog by remember { mutableStateOf(false) }
    var isVerificationSuccessful by remember { mutableStateOf(false) }
    var areValidationsEnabled by remember { mutableStateOf(false) } // Estado para liberar validações

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabeçalho
        Header()

        // Ícone de dedo centralizado
        Image(
            painter = painterResource(id = ic_mao),
            contentDescription = "Ícone de Biometria",
            modifier = Modifier
                .size(100.dp)
                .clickable { showDialog = true } // Abre o popup ao clicar no ícone
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botões de ação
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (areValidationsEnabled) { // Apenas permite iniciar a verificação se habilitado
                        iniciarAutenticacaoBiometrica(activity, onSuccess = { isVerificationSuccessful = true })
                    }
                },
                enabled = areValidationsEnabled && !isVerificationSuccessful, // Depende do estado das validações
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Verificar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("home") },
                enabled = isVerificationSuccessful, // Apenas habilitado se a verificação foi bem-sucedida
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Avançar")
            }
        }

        // Rodapé na parte inferior
        Footer(navController = navController)

        // Popup com as opções
        if (showDialog) {
            BiometricOptionsDialog(
                onDismiss = { showDialog = false },
                onUseExisting = {
                    areValidationsEnabled = true // Libera as validações
                    showDialog = false
                    Toast.makeText(activity, "Usando digital existente", Toast.LENGTH_SHORT).show()
                },
                onAddNew = {
                    showDialog = false
                    Toast.makeText(activity, "Adicionar nova digital não suportado pelo emulador", Toast.LENGTH_SHORT).show()
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
        title = {
            Text(text = "Escolha uma opção")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onUseExisting, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Usar digital existente")
                }
                Button(onClick = onAddNew, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Adicionar nova digital")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}


fun iniciarAutenticacaoBiometrica(
    activity: FragmentActivity,
    onSuccess: () -> Unit
) {
    val biometricManager = BiometricManager.from(activity)

    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            val executor = ContextCompat.getMainExecutor(activity)
            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess()
                        Toast.makeText(activity, "Autenticação bem-sucedida", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(activity, "Autenticação falhou", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(activity, "Erro de autenticação: $errString", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação Biométrica")
                .setSubtitle("Coloque seu dedo no sensor de impressão digital")
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Toast.makeText(activity, "Este dispositivo não possui sensor biométrico", Toast.LENGTH_SHORT).show()
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Toast.makeText(activity, "Sensor biométrico indisponível", Toast.LENGTH_SHORT).show()
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Toast.makeText(activity, "Nenhuma digital registrada no dispositivo", Toast.LENGTH_SHORT).show()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BiometriaScreenPreview() {
    AntiFraudeQuodTheme {
        BiometriaPage(navController = rememberNavController(), activity = BiometriaActivity())
    }
}
