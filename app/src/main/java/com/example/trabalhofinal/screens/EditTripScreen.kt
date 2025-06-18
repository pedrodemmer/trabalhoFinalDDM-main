package com.example.trabalhofinal.screens

import android.widget.Toast
import com.example.trabalhofinal.util.tripTypeIcon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.trabalhofinal.entity.Trip
import com.example.trabalhofinal.model.TripType
import com.example.travelapp.components.DatePickerField
import kotlinx.coroutines.launch
import java.util.*
import com.example.trabalhofinal.components.BottomNavBar
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import com.example.trabalhofinal.database.AppDatabase
import com.example.trabalhofinal.util.CurrencyVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripScreen(
    tripId: Long,
    onEditSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var trip by remember { mutableStateOf<Trip?>(null) }
    
    LaunchedEffect(tripId) {
        val db = AppDatabase.getDatabase(context)
        trip = db.tripDao().getTripById(tripId)
    }

    trip?.let { tripData ->
        var destination by remember { mutableStateOf(tripData.destination) }
        var startDate by remember { mutableStateOf(tripData.startDate?.let { java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) } ?: "") }
        var endDate by remember { mutableStateOf(tripData.endDate?.let { java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) } ?: "") }
        var budgetRaw by remember { mutableStateOf((tripData.budget * 100).toInt().toString()) }
        var selectedTripType by remember { mutableStateOf(tripData.tripType) }
        var expanded by remember { mutableStateOf(false) }

        Scaffold(
            bottomBar = {
                BottomNavBar(
                    onCadastrarViagem = {},
                    onHome = onEditSuccess,
                    onSair = { onEditSuccess() }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Editar Viagem", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = { Text("Destino") },
                    modifier = Modifier.fillMaxWidth()
                )

                DatePickerField(
                    label = "Data de Início",
                    date = startDate,
                    onDateSelected = { startDate = it }
                )

                DatePickerField(
                    label = "Data de Término",
                    date = endDate,
                    onDateSelected = { endDate = it }
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedTripType.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Viagem") },
                        leadingIcon = {
                            Icon(
                                imageVector = tripTypeIcon(selectedTripType),
                                contentDescription = selectedTripType.name
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        TripType.values().forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = tripTypeIcon(type),
                                            contentDescription = type.name,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(type.name)
                                    }
                                },
                                onClick = {
                                    selectedTripType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = budgetRaw,
                    onValueChange = { newValue ->
                        budgetRaw = newValue.filter { it.isDigit() }
                    },
                    label = { Text("Orçamento") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = CurrencyVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (destination.isBlank() || startDate.isBlank() || endDate.isBlank() || budgetRaw.isBlank()) {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        } else {
                            scope.launch {
                                try {
                                    val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    val startDateParsed = java.time.LocalDate.parse(startDate, formatter)
                                    val endDateParsed = java.time.LocalDate.parse(endDate, formatter)

                                    val startDateDate = java.util.Date.from(
                                        startDateParsed.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
                                    )
                                    val endDateDate = java.util.Date.from(
                                        endDateParsed.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
                                    )

                                    val budgetValue = budgetRaw.toDoubleOrNull()?.div(100) ?: 0.0

                                    val updatedTrip = tripData.copy(
                                        destination = destination,
                                        tripType = selectedTripType,
                                        startDate = startDateDate,
                                        endDate = endDateDate,
                                        budget = budgetValue
                                    )

                                    val db = AppDatabase.getDatabase(context)
                                    db.tripDao().update(updatedTrip)

                                    Toast.makeText(context, "Viagem atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                    onEditSuccess()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Erro ao atualizar viagem: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar Alterações")
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
