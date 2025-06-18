package com.example.trabalhofinal.screens

import com.example.trabalhofinal.util.tripTypeIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.trabalhofinal.components.BottomNavBar
import com.example.trabalhofinal.entity.Trip
import com.example.trabalhofinal.model.TripType
import java.text.SimpleDateFormat
import java.util.*
import com.example.trabalhofinal.R
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onNavigateToRegisterTrip: () -> Unit,
    trips: List<Trip>,
    onEditTrip: (Trip) -> Unit,
    onDeleteTrip: (Trip) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botão Sair à esquerda
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Sair"
                    )
                }
                Text(
                    text = "Agenda de viagens",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(48.dp)) // Espaço para alinhar visualmente
            }
        },
        bottomBar = {
            BottomNavBar(
                onCadastrarViagem = onNavigateToRegisterTrip,
                onHome = {},
                onSobre = { /* ação para sobre */ }
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
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToEnd) {
                            onDeleteTrip(trip)
                        }
                        false
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd),
                    background = {
                        val color = if (dismissState.targetValue == DismissValue.DismissedToEnd)
        Color.Red.copy(alpha = 0.3f) // vermelho transparente
    else
        Color.LightGray
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(start = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Deletar",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
},
                    dismissContent = {
                        TripCard(trip, onLongPress = { selectedTrip -> onEditTrip(selectedTrip) })
                    }
                )
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
            Icon(
                imageVector = tripTypeIcon(trip.tripType),
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
                    val startDateStr = trip.startDate?.let { formatter.format(it) } ?: "-"
                    val endDateStr = trip.endDate?.let { formatter.format(it) } ?: "-"
                    Text(
                        "Início: $startDateStr",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Fim: $endDateStr",
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