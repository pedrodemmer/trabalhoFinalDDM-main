package com.example.trabalhofinal.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
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
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.Color

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
