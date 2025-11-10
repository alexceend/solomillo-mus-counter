package com.example.solomillo

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.solomillo.dialogs.TextBox
import kotlin.math.abs

@Suppress("NOTHING_TO_OVERRIDE", "ACCIDENTAL_OVERRIDE")

class MainActivity : AppCompatActivity() {

    // Properties
    lateinit var gameManager : GameManager
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



        teamAName.setOnClickListener {
            TextBox.showNameInputDialog("Team A", this) { newName ->
                gameManager.getTeamA().name = newName
                teamAName.text = newName
            }
        }

        teamAPoints.setOnClickListener {
            gameManager.getTeamA().increase();
            teamAPoints.text = gameManager.getTeamA().points.toString();
        }


        teamBName.setOnClickListener {
            TextBox.showNameInputDialog("Team B", this) { newName ->
                gameManager.getTeamB().name = newName
                teamBName.text = newName
            }
        }

        teamBPoints.setOnClickListener {
            gameManager.getTeamB().increase();
            teamBPoints.text = gameManager.getTeamB().points.toString();
        }


        //Swipe listeners
        setSwipeListener(teamAPoints,
            onSwipeDown = {
                gameManager.getTeamA().decrease()
                teamAPoints.text = gameManager.getTeamA().points.toString()
            },
            onSwipeRight = {
                gameManager.getTeamA().increase(5)
                teamAPoints.text = gameManager.getTeamA().points.toString()
            },
            onSwipeLeft = {
                gameManager.getTeamA().decrease(5)
                teamAPoints.text = gameManager.getTeamA().points.toString()
            })

        setSwipeListener(teamBPoints,
            onSwipeDown = {
                gameManager.getTeamB().decrease()
                teamBPoints.text = gameManager.getTeamB().points.toString()
            },
            onSwipeRight = {
                gameManager.getTeamB().increase(5)
                teamBPoints.text = gameManager.getTeamB().points.toString()
            }, onSwipeLeft = {
                gameManager.getTeamB().decrease(5)
                teamBPoints.text = gameManager.getTeamB().points.toString()
            })

        //Grande, chica, par y juego
        grandePoints?.setOnClickListener {
            gameManager.game.grande++;
            grandePoints?.text = gameManager.game.grande.toString();
        }

        chicaPoints?.setOnClickListener {
            gameManager.game.chica++;
            chicaPoints?.text = gameManager.game.chica.toString();
        }

        parPoints?.setOnClickListener {
            gameManager.game.par++;
            parPoints?.text = gameManager.game.par.toString();
        }

        juegoPoints?.setOnClickListener {
            gameManager.game.juego++;
            juegoPoints?.text = gameManager.game.juego.toString();
        }

        //Swipe listeners de grande,chica, juego y par
        //No me juzgueis por estom estaba intentando hacer dos main activities
        //Una para cada layout pero no para de crashear asi q he optado
        //por esta guarrada
        if(grandePoints != null
            && chicaPoints != null
            && juegoPoints != null
            && parPoints != null){
            setSwipeListener(
                grandePoints!!,
                onSwipeDown = {
                    if (gameManager.game.grande > 0) {
                        gameManager.game.grande--
                        grandePoints?.text = gameManager.game.grande.toString()
                    }
                },
                onSwipeRight = {
                    gameManager.getTeamB().increase(gameManager.game.grande)
                    gameManager.game.grande = 0
                    teamBPoints.text = gameManager.getTeamB().points.toString()
                    grandePoints?.text = gameManager.game.grande.toString()
                },
                onSwipeLeft = {
                    gameManager.getTeamA().increase(gameManager.game.grande)
                    gameManager.game.grande = 0
                    teamAPoints.text = gameManager.getTeamA().points.toString()
                    grandePoints?.text = gameManager.game.grande.toString()
                })

            setSwipeListener(chicaPoints!!,
                onSwipeDown = {
                    if (gameManager.game.chica > 0) {
                        gameManager.game.chica--
                        chicaPoints?.text = gameManager.game.chica.toString()
                    }
                },
                onSwipeRight = {
                    gameManager.getTeamB().increase(gameManager.game.chica)
                    gameManager.game.chica = 0
                    teamBPoints.text = gameManager.getTeamB().points.toString()
                    chicaPoints?.text = gameManager.game.chica.toString()
                }, onSwipeLeft = {
                    gameManager.getTeamA().increase(gameManager.game.chica)
                    gameManager.game.chica = 0
                    teamAPoints.text = gameManager.getTeamA().points.toString()
                    chicaPoints?.text = gameManager.game.chica.toString()
                })


            setSwipeListener(parPoints!!,
                onSwipeDown = {
                    if (gameManager.game.par > 0) {
                        gameManager.game.par--
                        parPoints?.text = gameManager.game.par.toString()
                    }
                },
                onSwipeRight = {
                    gameManager.getTeamB().increase(gameManager.game.par)
                    gameManager.game.par = 0
                    teamBPoints.text = gameManager.getTeamB().points.toString()
                    parPoints?.text = gameManager.game.par.toString()
                },
                onSwipeLeft = {
                    gameManager.getTeamA().increase(gameManager.game.par)
                    gameManager.game.par = 0
                    teamAPoints.text = gameManager.getTeamA().points.toString()
                    parPoints?.text = gameManager.game.par.toString()
                })

            setSwipeListener(juegoPoints!!,
                onSwipeDown = {
                    if (gameManager.game.juego > 0) {
                        gameManager.game.juego--
                        juegoPoints?.text = gameManager.game.juego.toString()
                    }
                },
                onSwipeRight = {
                    gameManager.getTeamB().increase(gameManager.game.juego)
                    gameManager.game.juego = 0
                    teamBPoints.text = gameManager.getTeamB().points.toString()
                    juegoPoints?.text = gameManager.game.juego.toString()
                },
                onSwipeLeft = {
                    gameManager.getTeamA().increase(gameManager.game.juego)
                    gameManager.game.juego = 0
                    teamAPoints.text = gameManager.getTeamA().points.toString()
                    juegoPoints?.text = gameManager.game.juego.toString()
                })

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setSwipeListener(
        view: View,
        onSwipeDown: (() -> Unit)? = null,
        onSwipeLeft: (() -> Unit)? = null,
        onSwipeRight: (() -> Unit)? = null
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