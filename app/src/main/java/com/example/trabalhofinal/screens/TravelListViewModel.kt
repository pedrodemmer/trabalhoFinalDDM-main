package com.example.trabalhofinal.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinal.dao.TripDao
import com.example.trabalhofinal.entity.Trip
import kotlinx.coroutines.launch

class TravelListViewModel(private val tripDao: TripDao) : ViewModel() {

    private val _trips = mutableStateListOf<Trip>()
    val trips: List<Trip> get() = _trips

    fun loadTrips() {
        viewModelScope.launch {
            val result = tripDao.getAll()
            _trips.clear()
            _trips.addAll(result)
        }
    }
}
