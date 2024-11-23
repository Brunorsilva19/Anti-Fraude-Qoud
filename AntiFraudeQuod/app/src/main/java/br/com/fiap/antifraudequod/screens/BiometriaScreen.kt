package br.com.fiap.antifraudequod

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val isAuthenticated = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()

        // Botão de Verificação de Digital
        Button(
            onClick = { iniciarAutenticacaoBiometrica(activity, isAuthenticated) },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isAuthenticated.value) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Gray
            )
        ) {
            Text(text = "Verificar Digital")
        }

        Footer(navController = navController)
    }
}

fun iniciarAutenticacaoBiometrica(activity: FragmentActivity, isAuthenticated: MutableState<Boolean>) {
    try {
        val biometricManager = BiometricManager.from(activity)

        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)

        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Mostrar diálogo de biometria
                exibirDialogoEscolha(activity, isAuthenticated)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // Falta hardware biométrico
                showErrorToast(activity, "Este dispositivo não possui sensor biométrico")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // Sensor biométrico indisponível
                showErrorToast(activity, "Sensor biométrico indisponível")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Nenhuma biometria cadastrada
                showErrorToast(activity, "Nenhuma digital registrada no dispositivo")
                abrirConfiguracoesBiometricas(activity)
            }
            else -> {
                // Erro desconhecido
                showErrorToast(activity, "Erro desconhecido ao verificar biometria")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        showErrorToast(activity, "Erro ao iniciar biometria: ${e.message}")
    }
}

fun showErrorToast(activity: FragmentActivity, message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

fun exibirDialogoEscolha(activity: FragmentActivity, isAuthenticated: MutableState<Boolean>) {
    val biometricPrompt = BiometricPrompt(activity, Executors.newSingleThreadExecutor(), object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            isAuthenticated.value = true
            Toast.makeText(activity, "Autenticação bem-sucedida!", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(activity, "Falha na autenticação", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            // Log do erro para entender melhor
            Log.e("Biometria", "Erro de autenticação: $errString")
            Toast.makeText(activity, "Erro na autenticação: $errString", Toast.LENGTH_SHORT).show()
        }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Autenticação Biométrica")
        .setSubtitle("Use sua digital para autenticar")
        .setNegativeButtonText("Cancelar")
        .build()

    biometricPrompt.authenticate(promptInfo)
}


@Preview(showBackground = true)
@Composable
fun BiometriaScreenPreview() {
    AntiFraudeQuodTheme {
        BiometriaPage(navController = rememberNavController(), activity = BiometriaActivity())
    }
}
