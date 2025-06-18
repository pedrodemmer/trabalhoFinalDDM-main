package com.example.trabalhofinal.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.trabalhofinal.model.TripType

fun tripTypeIcon(type: TripType): ImageVector = when (type) {
    TripType.NEGOCIOS -> Icons.Default.Work
    TripType.LAZER -> Icons.Default.BeachAccess
    TripType.ESTUDOS -> Icons.Default.School
    TripType.OUTROS -> Icons.Default.MoreHoriz
}