package com.example.trabalhofinal.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.trabalhofinal.components.BottomNavBar
import com.example.trabalhofinal.entity.Trip
import com.example.trabalhofinal.model.TripType
import java.text.SimpleDateFormat
import java.util.*
import com.example.trabalhofinal.R

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onNavigateToRegisterTrip: () -> Unit,
    trips: List<Trip>,
    onEditTrip: (Trip) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                onCadastrarViagem = onNavigateToRegisterTrip,
                onHome = {},
                onSair = onLogout
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trips.sortedBy { it.startDate }) { trip ->
                TripCard(trip, onLongPress = { selectedTrip ->
                    onEditTrip(selectedTrip)
                })
            }
        }
    }
}

@Composable
fun TripCard(
    trip: Trip,
    onLongPress: (Trip) -> Unit
) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val imageRes = when (trip.tripType) {
        TripType.NEGOCIOS,
        TripType.LAZER,
        TripType.ESTUDOS,
        TripType.OUTROS -> R.drawable.ic_launcher_foreground // Ícone padrão do Android Studio
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .pointerInput(trip) {
                detectTapGestures(
                    onLongPress = { onLongPress(trip) }
                )
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = trip.tripType.name,
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 12.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(trip.destination, style = MaterialTheme.typography.titleMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Início: ${formatter.format(trip.startDate)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Fim: ${formatter.format(trip.endDate)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    "Orçamento: R$ %.2f".format(trip.budget),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}