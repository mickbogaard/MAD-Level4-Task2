package com.example.mad_level4_task2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRock.setOnClickListener{
            onRockPress()
        }
        btnPaper.setOnClickListener{
            onPaperPress()
        }
        btnScissors.setOnClickListener {
            onScissorsPress()
        }
        gameRepository = GameRepository(this)
    }


    override fun onStart() {
        getStatistics()
        super.onStart()
    }


    private fun onRockPress(){
        val computerChoice = getComputerChoice()
        val playerChoice = R.drawable.rock

        ivPlayerPick.setImageResource(playerChoice)

        val result = when (computerChoice) {
            R.drawable.rock -> getString(R.string.result_draw)
            R.drawable.scissors -> getString(R.string.result_win)
            else -> getString(R.string.result_loss)
        }
        tvWinLoseText.text = result
        addGame(result, computerChoice, playerChoice)
    }

    private fun onScissorsPress(){
        val computerChoice = getComputerChoice()
        val playerChoice = R.drawable.scissors

        ivPlayerPick.setImageResource(playerChoice)

        val result = when (computerChoice) {
            R.drawable.scissors -> getString(R.string.result_draw)
            R.drawable.paper -> getString(R.string.result_win)
            else -> getString(R.string.result_loss)
        }
        tvWinLoseText.text = result
        addGame(result, computerChoice, playerChoice)
    }

    private fun onPaperPress(){
        val computerChoice = getComputerChoice()
        val playerChoice = R.drawable.paper

        ivPlayerPick.setImageResource(playerChoice)

        val result = when (computerChoice) {
            R.drawable.paper -> getString(R.string.result_draw)
            R.drawable.rock -> getString(R.string.result_win)
            else -> getString(R.string.result_loss)
        }
        tvWinLoseText.text = result
        addGame(result, computerChoice, playerChoice)
    }

    private fun getComputerChoice(): Int{
        val computerChoice = when ((1..3).random()){
            1 -> R.drawable.rock
            2 -> R.drawable.paper
            else -> R.drawable.scissors
        }
        ivComputerPick.setImageResource(computerChoice)

        return computerChoice
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_show_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addGame(result: String, computerChoice: Int, playerChoice: Int) {
        mainScope.launch {

            val currentTime = Calendar.getInstance().time
            val game = Game(result, computerChoice, playerChoice, currentTime)

            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
            getStatistics()
        }
    }
    private fun getStatistics(){
        mainScope.launch {
            val winCount = withContext(Dispatchers.IO){
                gameRepository.getStatisticsByResults(getString(R.string.result_win))
            }
            val loseCount = withContext(Dispatchers.IO) {
                gameRepository.getStatisticsByResults(getString(R.string.result_loss))
            }
            val drawCount = withContext(Dispatchers.IO) {
                gameRepository.getStatisticsByResults(getString(R.string.result_draw))
            }
            tvStatistics.text = getString(R.string.win_draw_loss, winCount, drawCount, loseCount)
        }
    }

}