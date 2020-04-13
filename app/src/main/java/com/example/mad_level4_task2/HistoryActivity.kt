package com.example.mad_level4_task2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val games = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)

        initViews()

        gameRepository = GameRepository(this)
    }


    private fun initViews(){
        rvGamesHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvGamesHistory.adapter = gameAdapter
        rvGamesHistory.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        getGameListFromDatabase()
    }

    private fun getGameListFromDatabase() {
        mainScope.launch {
            val newGames = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            games.clear()
            games.addAll(newGames)
            gameAdapter.notifyDataSetChanged()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_history -> {
                deleteGamesList()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteGamesList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGames()
            }
            getGameListFromDatabase()
        }
    }
}
