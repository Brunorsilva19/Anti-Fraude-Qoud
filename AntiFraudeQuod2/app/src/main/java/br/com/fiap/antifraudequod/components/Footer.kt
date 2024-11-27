package br.com.fiap.antifraudequod.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.antifraudequod.R.drawable.ic_home
import br.com.fiap.antifraudequod.R.drawable.ic_camera
import br.com.fiap.antifraudequod.R.drawable.ic_formulario
import br.com.fiap.antifraudequod.R.drawable.ic_mao
import br.com.fiap.antifraudequod.R.drawable.ic_rosto


@Composable
fun Footer(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botão de ícone de câmera no lado esquerdo, que navega para a página "documento"
        IconButton(onClick = {
            navController.navigate("documento")
        }) {
            Icon(
                painter = painterResource(id = ic_camera),
                contentDescription = "Câmera",
                tint = Color.White
            )
        }

        // Botão de ícone de casa (home) no centro
        IconButton(onClick = {
            navController.navigate("home")
        }) {
            Icon(
                painter = painterResource(id = ic_home),
                contentDescription = "Home",
                tint = Color.White
            )
        }

        // Botão com ícone de formulário no lado direito
        IconButton(onClick = {
            navController.navigate("formulario")
        }) {
            Icon(
                painter = painterResource(id = ic_formulario),
                contentDescription = "Formulário",
                tint = Color.White
            )
        }

        // Botão com ícone de biometria
        IconButton(onClick = {
            navController.navigate("biometria")
        }) {
            Icon(
                painter = painterResource(id = ic_mao),
                contentDescription = "Biometria",
                tint = Color.White
            )
        }
        IconButton(onClick = {
            navController.navigate("facial")
        }) {
            Icon(
                painter = painterResource(id = ic_rosto),
                contentDescription = "Facial",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview() {
    Footer(navController = rememberNavController())
}