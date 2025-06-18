package com.example.trabalhofinal.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trabalhofinal.components.ErrorDialog
import com.example.trabalhofinal.components.MyPasswordField
import com.example.trabalhofinal.components.MyTextField
import com.example.trabalhofinal.database.AppDatabase

@Composable
fun RegisterUserMainScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val ctx = LocalContext.current
    val userDao = AppDatabase.getDatabase(ctx).userDao()
    val registerUserViewModel: RegisterUserViewModel = viewModel(
        factory = RegisterUserViewModelFactory(userDao)
    )

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterUserFields(registerUserViewModel, onRegisterSuccess, onNavigateToLogin)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserFields(
    registerUserViewModel: RegisterUserViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val registerUser = registerUserViewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    MyTextField(
        label = "Usuário",
        value = registerUser.value.user,
        onValueChange = { registerUserViewModel.onUserChange(it) }
    )
    MyTextField(
        label = "Nome Completo",
        value = registerUser.value.name,
        onValueChange = { registerUserViewModel.onNameChange(it) }
    )
    MyTextField(
        label = "E-mail",
        value = registerUser.value.email,
        onValueChange = { registerUserViewModel.onEmailChange(it) }
    )
    MyPasswordField(
        label = "Senha",
        value = registerUser.value.password,
        errorMessage = registerUser.value.validatePassord(),
        onValueChange = { registerUserViewModel.onPasswordChange(it) }
    )
    MyPasswordField(
        label = "Confirmar Senha",
        value = registerUser.value.confirmPassword,
        errorMessage = registerUser.value.validateConfirmPassword(),
        onValueChange = { registerUserViewModel.onConfirmPassword(it) }
    )

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
            registerUserViewModel.register()
        }
    ) {
        Text(text = "Registrar Usuario")
    }

    Button(
        modifier = Modifier.padding(top = 8.dp),
        onClick = { onNavigateToLogin() }
    ) {
        Text(text = "Voltar para o login")
    }

    if (registerUser.value.errorMessage.isNotBlank()) {
        ErrorDialog(
            error = registerUser.value.errorMessage,
            onDismissRequest = { registerUserViewModel.cleanDisplayValues() }
        )
    }

    LaunchedEffect(registerUser.value.isSaved) {
        if (registerUser.value.isSaved) {
            Toast.makeText(ctx, "User registered!", Toast.LENGTH_SHORT).show()
            registerUserViewModel.cleanDisplayValues()
            onRegisterSuccess()
        }
    }
}
