package com.test.test_karim2.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "movie_genre", primaryKeys = ["id"])
data class MovieGenre(
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "name")
        var name: String = ""
){
    @Ignore constructor() : this(0)
}

@Dao
interface MovieGenreDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(data: MovieGenre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<MovieGenre>)

    @Query("DELETE FROM movie_genre")
    suspend fun deleteAll()

    @Query("DELETE FROM movie_genre WHERE id = :id")
    suspend fun deleteById(id : Int)

    @Query("""SELECT * FROM movie_genre""")
    fun all(): Flow<List<MovieGenre>>

    @Query("""SELECT * FROM movie_genre""")
    suspend fun allS(): List<MovieGenre>
}
