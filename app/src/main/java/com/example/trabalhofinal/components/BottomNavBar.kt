package com.example.trabalhofinal.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    onCadastrarViagem: () -> Unit = {},
    onHome: () -> Unit = {},
    onSair: () -> Unit = {}
) {
    var selectedIndex by remember { mutableStateOf(1) }

    NavigationBar(
        modifier = modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                onCadastrarViagem()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Cadastrar Viagem"
                )
            },
            label = { Text("Cadastrar") },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
                onHome()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = {
                selectedIndex = 2
                onSair()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Sair"
                )
            },
            label = { Text("Sair") },
            alwaysShowLabel = true
        )
    }
}