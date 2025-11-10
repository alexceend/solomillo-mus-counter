package com.example.solomillo

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.solomillo.dialogs.TextBox
import kotlin.math.abs

@Suppress("NOTHING_TO_OVERRIDE", "ACCIDENTAL_OVERRIDE")

class MainActivity : AppCompatActivity() {

    // Properties
    lateinit var gameManager: GameManager
    lateinit var teamAPoints: TextView
    lateinit var teamBPoints: TextView
    lateinit var teamAName: TextView
    lateinit var teamBName: TextView
    var grandePoints: TextView? = null
    var chicaPoints: TextView? = null
    var parPoints: TextView? = null
    var juegoPoints: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //set properties
        gameManager = GameManager()

        val teamAContainer = findViewById<LinearLayout>(R.id.team_a_container)
        teamAPoints = teamAContainer.findViewById(R.id.score_team_a)
        teamAName = teamAContainer.findViewById(R.id.team_a_label)

        val teamBContainer = findViewById<ConstraintLayout>(R.id.team_b_container)
        teamBPoints = teamBContainer.findViewById(R.id.score_team_b)
        teamBName = teamBContainer.findViewById(R.id.team_b_label)

        grandePoints = findViewById(R.id.score_grande)
        chicaPoints = findViewById(R.id.score_chica)
        parPoints = findViewById(R.id.score_par)
        juegoPoints = findViewById(R.id.score_juego)

        val scoreViews = listOf(
            ScoreView(grandePoints, ScoreType.GRANDE),
            ScoreView(chicaPoints, ScoreType.CHICA),
            ScoreView(parPoints, ScoreType.PAR),
            ScoreView(juegoPoints, ScoreType.JUEGO)
        )

        teamAName.setOnClickListener {
            TextBox.showNameInputDialog("Team A", this) { newName ->
                gameManager.getTeamA().name = newName
                teamAName.text = newName
            }
        }
        teamBName.setOnClickListener {
            TextBox.showNameInputDialog("Team B", this) { newName ->
                gameManager.getTeamB().name = newName
                teamBName.text = newName
            }
        }

        //Single taps
        bindSingleTap(gameManager.getTeamA(), teamAPoints)
        bindSingleTap(gameManager.getTeamB(), teamBPoints)

        //Swipe listeners
        bindTeamSwipe(gameManager.getTeamA(), teamAPoints)
        bindTeamSwipe(gameManager.getTeamB(), teamBPoints)

        //Grande, chica, par y juego
        scoreViews.filter { it.view != null }.forEach { bindScoreSwipe(it) }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun bindScoreSwipe(scoreView: ScoreView) {
        val view = scoreView.view ?: return

        view.setOnClickListener {
            gameManager.increaseScore(scoreView.type)
            view.text = gameManager.getScore(scoreView.type).toString()
        }

        setSwipeListener(
            view,
            onSwipeDown = {
                if (gameManager.getScore(scoreView.type) > 0) {
                    gameManager.decreaseScore(scoreView.type)
                    view.text = gameManager.getScore(scoreView.type).toString()
                }
            },
            onSwipeRight = {
                gameManager.getTeamB().increase(gameManager.getScore(scoreView.type))
                gameManager.resetScore(scoreView.type)
                teamBPoints.text = gameManager.getTeamB().points.toString()
                view.text = gameManager.getScore(scoreView.type).toString()
            },
            onSwipeLeft = {
                gameManager.getTeamA().increase(gameManager.getScore(scoreView.type))
                gameManager.resetScore(scoreView.type)
                teamAPoints.text = gameManager.getTeamA().points.toString()
                view.text = gameManager.getScore(scoreView.type).toString()
            },
            onLongPress = {
                gameManager.resetScore(scoreView.type)
                view.text = gameManager.getScore(scoreView.type).toString()
            }
        )
    }

    private fun bindSingleTap(team: Team, pointsView: TextView) {
        pointsView.setOnClickListener {
            team.increase();
            pointsView.text = team.points.toString();
            checkWinner(team)
        }
    }

    private fun bindTeamSwipe(team: Team, pointsView: TextView) {
        setSwipeListener(
            pointsView,
            onSwipeDown = {
                team.decrease()
                pointsView.text = team.points.toString()
            },
            onSwipeRight = {
                team.increase(5)
                pointsView.text = team.points.toString()
                checkWinner(team)
            },
            onSwipeLeft = {
                team.decrease(5)
                pointsView.text = team.points.toString()
                checkWinner(team)
            },
            onLongPress = {
                team.points = 0
                pointsView.text = team.points.toString()
            }
        )
    }


    private fun setSwipeListener(
        view: View,
        onSwipeDown: (() -> Unit)? = null,
        onSwipeLeft: (() -> Unit)? = null,
        onSwipeRight: (() -> Unit)? = null,
        onLongPress: (() -> Unit)? = null
    ) {
        var isSwiping = false

        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                private val SWIPE_THRESHOLD = 150f
                private val SWIPE_VELOCITY_THRESHOLD = 0f

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {

                    try {
                        val diffY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f
                        val diffX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f

                        if (abs(diffX) > abs(diffY)) {
                            if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                isSwiping = true
                                if (diffX > 0) {
                                    onSwipeRight?.invoke()
                                } else {
                                    onSwipeLeft?.invoke()
                                }
                            }
                            return true
                        }

                        if (abs(diffY) > abs(diffX)) {
                            if (diffY > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                isSwiping = true
                                onSwipeDown?.invoke()
                                return true;
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return false
                }

                override fun onDown(e: MotionEvent?): Boolean {
                    isSwiping = false
                    return true
                }

                override fun onLongPress(e: MotionEvent?) {
                    onLongPress?.invoke()
                }
            })

        view.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            if (isSwiping) {
                v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                true
            } else {
                false
            }
        }
    }

    private fun checkWinner(team: Team){
        if(team.points >= 40){
            AlertDialog.Builder(this)
                .setTitle("Winner!")
                .setMessage("${team.name} won !")
                .setPositiveButton("OK"){
                    dialog, _ -> dialog.dismiss()
                }
                .show()
            resetGame()
        }
    }

    private fun resetGame(){
        gameManager.getTeamA().reset()
        gameManager.getTeamB().reset()

        // Reset individual scores
        gameManager.game.grande = 0
        gameManager.game.chica = 0
        gameManager.game.par = 0
        gameManager.game.juego = 0

        // Update UI
        teamAPoints.text = gameManager.getTeamA().points.toString()
        teamBPoints.text = gameManager.getTeamB().points.toString()
        grandePoints?.text = gameManager.game.grande.toString()
        chicaPoints?.text = gameManager.game.chica.toString()
        parPoints?.text = gameManager.game.par.toString()
        juegoPoints?.text = gameManager.game.juego.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("teamA_points", gameManager.getTeamA().points)
        outState.putInt("teamB_points", gameManager.getTeamB().points)
        outState.putInt("grande", gameManager.game.grande)
        outState.putInt("chica", gameManager.game.chica)
        outState.putInt("par", gameManager.game.par)
        outState.putInt("juego", gameManager.game.juego)
        outState.putString("teamA_name", gameManager.getTeamA().name)
        outState.putString("teamB_name", gameManager.getTeamB().name)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        gameManager.getTeamA().points = savedInstanceState.getInt("teamA_points")
        gameManager.getTeamB().points = savedInstanceState.getInt("teamB_points")
        gameManager.getTeamA().name = savedInstanceState.getString("teamA_name") ?: "Team A"
        gameManager.getTeamB().name = savedInstanceState.getString("teamB_name") ?: "Team B"
        gameManager.game.grande = savedInstanceState.getInt("grande")
        gameManager.game.chica = savedInstanceState.getInt("chica")
        gameManager.game.par = savedInstanceState.getInt("par")
        gameManager.game.juego = savedInstanceState.getInt("juego")

        // Update UI
        teamAPoints.text = gameManager.getTeamA().points.toString()
        teamBPoints.text = gameManager.getTeamB().points.toString()
        teamAName.text = gameManager.getTeamA().name
        teamBName.text = gameManager.getTeamB().name
        grandePoints?.text = gameManager.game.grande.toString()
        chicaPoints?.text = gameManager.game.chica.toString()
        parPoints?.text = gameManager.game.par.toString()
        juegoPoints?.text = gameManager.game.juego.toString()
    }

}