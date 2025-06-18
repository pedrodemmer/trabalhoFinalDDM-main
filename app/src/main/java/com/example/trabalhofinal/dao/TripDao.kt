package com.example.trabalhofinal.dao

import androidx.room.*
import com.example.trabalhofinal.entity.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    suspend fun getAll(): List<Trip>

    @Query("SELECT * FROM trip WHERE id = :id LIMIT 1")
    suspend fun getTripById(id: Long): Trip?

    @Insert
    suspend fun insert(trip: Trip)

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)
}
