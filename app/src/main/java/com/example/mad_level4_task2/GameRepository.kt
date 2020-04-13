package com.example.mad_level4_task2

import android.content.Context


class GameRepository(context: Context) {

    private val gameDAO: GameDAO?

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDAO = database?.gameDAO()
    }

    suspend fun getAllGames(): List<Game> {
        return gameDAO?.getAllGames() ?: emptyList()
    }

    suspend fun insertGame(game: Game) {
        gameDAO?.insertGame(game)
    }

    suspend fun deleteAllGames() {
        gameDAO?.deleteAllGames()
    }

    suspend fun getStatisticsByResults(resultToCount: String): Int{
        return gameDAO?.getStatisticsByResult(resultToCount) ?: 0
    }
}
