package com.vhpg.popurrivault.data.db.dao

import androidx.room.*
import com.vhpg.popurrivault.data.db.model.MovementEntity
import com.vhpg.popurrivault.util.Constants

@Dao
interface MovementDao {
    //Create
    @Insert
    suspend fun insertMovement(mov: MovementEntity): Long
/*
    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_MOVEMENTS_TABLE}")
    suspend fun getAllMovs(): List<MovementEntity>

    //Update
    @Update
    suspend fun updateMov(mov: MovementEntity)

    //Delete
    @Delete
    suspend fun deleteMov(mov: MovementEntity)
*/
}