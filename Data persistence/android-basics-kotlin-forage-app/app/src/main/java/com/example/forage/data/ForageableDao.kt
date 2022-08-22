package com.example.forage.data

import androidx.room.*
import com.example.forage.model.Forageable
import kotlinx.coroutines.flow.Flow


@Dao
interface ForageableDao {

    // TODO: implement a method to retrieve all Forageables from the database
    @Query("SELECT * FROM forageable_database")
    fun getForageables() : Flow<List<Forageable>>

    // TODO: implement a method to retrieve a Forageable from the database by id
    @Query("SELECT * FROM forageable_database WHERE id = :id")
    fun getForageable(id : Long) : Flow<Forageable>

    // TODO: implement a method to insert a Forageable into the database
    //  (use OnConflictStrategy.REPLACE)
    @Insert
    suspend fun insert(forageable: Forageable)

    // TODO: implement a method to update a Forageable that is already in the database
    @Update
    suspend fun update(forageable: Forageable)

    // TODO: implement a method to delete a Forageable from the database.
    @Delete
    suspend fun delete(forageable: Forageable)
}