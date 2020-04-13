package com.example.mad_level4_task2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "game_table")
data class Game(

    val result: String,
    val computerMove: Int,
    val playerMove: Int,
    val date: Date,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)