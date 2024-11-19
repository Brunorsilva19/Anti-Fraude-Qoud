package br.com.fiap.antifraudequod

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import br.com.fiap.antifraudequod.ui.theme.AntiFraudeQuodTheme
import br.com.fiap.antifraudequod.R.drawable.ic_mao
import br.com.fiap.antifraudequod.components.Footer
import br.com.fiap.antifraudequod.components.Header
import android.content.Context
import androidx.biometric.BiometricManager

class BiometriaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiFraudeQuodTheme {
                BiometriaScreen(context = applicationContext)
            }
        }
    }
}

@Composable
fun BiometriaScreen(context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "biometria") {
        composable("biometria") {
            BiometriaPage(navController = navController, context = context)
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
fun BiometriaPage(navController: NavHostController, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabeçalho
        Header()

        // Ícone de dedo centralizado
        Image(
            painter = painterResource(id = ic_mao),
            contentDescription = "Ícone de Biometria",
            modifier = Modifier.size(80.dp) // Ajuste o tamanho do ícone
        )

        // Botão de verificação de biometria
        Button(
            onClick = { iniciarAutenticacaoBiometrica(context) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Verificar Digital")
        }

        // Rodapé na parte inferior
        Footer(navController = navController)
    }
}

fun iniciarAutenticacaoBiometrica(context: Context) {
    val biometricManager = BiometricManager.from(context)

    when (biometricManager.canAuthenticate()) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            val executor = ContextCompat.getMainExecutor(context)
            val biometricPrompt = BiometricPrompt(
                BiometricPrompt.AuthenticationCallback(),
                executor
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação Biométrica")
                .setSubtitle("Coloque seu dedo no sensor de impressão digital")
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Toast.makeText(context, "Este dispositivo não possui sensor biométrico", Toast.LENGTH_SHORT).show()
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Toast.makeText(context, "Sensor biométrico indisponível", Toast.LENGTH_SHORT).show()
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Toast.makeText(context, "Nenhuma digital registrada no dispositivo", Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BiometriaScreenPreview() {
    AntiFraudeQuodTheme {
        BiometriaPage(navController = rememberNavController(), context = Context)
    }
}
