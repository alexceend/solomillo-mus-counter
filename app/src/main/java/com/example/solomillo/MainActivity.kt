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
    lateinit var teams: ArrayList<Team>
    lateinit var game: Game
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
        game = Game()
        teams = createTeams()

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
                teams[0].name = newName
                teamAName.text = newName
            }
        }

        teamAPoints.setOnClickListener {
            teams[0].points++;
            teamAPoints.text = teams[0].points.toString();
        }


        teamBName.setOnClickListener {
            TextBox.showNameInputDialog("Team B", this) { newName ->
                teams[1].name = newName
                teamBName.text = newName
            }
        }

        teamBPoints.setOnClickListener {
            teams[1].increase();
            teamBPoints.text = teams[1].points.toString();
        }


        //Swipe listeners
        setSwipeListener(teamAPoints,
            onSwipeDown = {
                teams[0].decrease()
                teamAPoints.text = teams[0].points.toString()
            },
            onSwipeRight = {
                teams[0].increase(5)
                teamAPoints.text = teams[0].points.toString()
            },
            onSwipeLeft = {
                teams[0].decrease(5)
                teamAPoints.text = teams[0].points.toString()
            })

        setSwipeListener(teamBPoints,
            onSwipeDown = {
                teams[1].decrease()
                teamBPoints.text = teams[1].points.toString()
            },
            onSwipeRight = {
                teams[1].increase(5)
                teamBPoints.text = teams[1].points.toString()
            }, onSwipeLeft = {
                teams[1].decrease(5)
                teamBPoints.text = teams[1].points.toString()
            })

        //Grande, chica, par y juego
        grandePoints?.setOnClickListener {
            game.grande++;
            grandePoints?.text = game.grande.toString();
        }

        chicaPoints?.setOnClickListener {
            game.chica++;
            chicaPoints?.text = game.chica.toString();
        }

        parPoints?.setOnClickListener {
            game.par++;
            parPoints?.text = game.par.toString();
        }

        juegoPoints?.setOnClickListener {
            game.juego++;
            juegoPoints?.text = game.juego.toString();
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
                    if (game.grande > 0) {
                        game.grande--
                        grandePoints?.text = game.grande.toString()
                    }
                },
                onSwipeRight = {
                    teams[1].points += game.grande
                    game.grande = 0
                    teamBPoints.text = teams[1].points.toString()
                    grandePoints?.text = game.grande.toString()
                },
                onSwipeLeft = {
                    teams[0].points += game.grande
                    game.grande = 0
                    teamAPoints.text = teams[0].points.toString()
                    grandePoints?.text = game.grande.toString()
                })

            setSwipeListener(chicaPoints!!,
                onSwipeDown = {
                    if (game.chica > 0) {
                        game.chica--
                        chicaPoints?.text = game.chica.toString()
                    }
                },
                onSwipeRight = {
                    teams[1].points += game.chica
                    game.chica = 0
                    teamBPoints.text = teams[1].points.toString()
                    chicaPoints?.text = game.chica.toString()
                }, onSwipeLeft = {
                    teams[0].points += game.chica
                    game.chica = 0
                    teamAPoints.text = teams[0].points.toString()
                    chicaPoints?.text = game.chica.toString()
                })


            setSwipeListener(parPoints!!,
                onSwipeDown = {
                    if (game.par > 0) {
                        game.par--
                        parPoints?.text = game.par.toString()
                    }
                },
                onSwipeRight = {
                    teams[1].points += game.par
                    game.par = 0
                    teamBPoints.text = teams[1].points.toString()
                    parPoints?.text = game.par.toString()
                },
                onSwipeLeft = {
                    teams[0].points += game.par
                    game.par = 0
                    teamAPoints.text = teams[0].points.toString()
                    parPoints?.text = game.par.toString()
                })

            setSwipeListener(juegoPoints!!,
                onSwipeDown = {
                    if (game.juego > 0) {
                        game.juego--
                        juegoPoints?.text = game.juego.toString()
                    }
                },
                onSwipeRight = {
                    teams[1].points += game.juego
                    game.juego = 0
                    teamBPoints.text = teams[1].points.toString()
                    juegoPoints?.text = game.juego.toString()
                },
                onSwipeLeft = {
                    teams[0].points += game.juego
                    game.juego = 0
                    teamAPoints.text = teams[0].points.toString()
                    juegoPoints?.text = game.juego.toString()
                })

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createTeams(): ArrayList<Team> {
        val array: ArrayList<Team> = ArrayList()
        array.add(Team("Team A", 0))
        array.add(Team("Team B", 0))
        return array
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
        outState.putInt("teamA_points", teams[0].points)
        outState.putInt("teamB_points", teams[1].points)
        outState.putInt("grande", game.grande)
        outState.putInt("chica", game.chica)
        outState.putInt("par", game.par)
        outState.putInt("juego", game.juego)
        outState.putString("teamA_name", teams[0].name)
        outState.putString("teamB_name", teams[1].name)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        teams[0].points = savedInstanceState.getInt("teamA_points")
        teams[1].points = savedInstanceState.getInt("teamB_points")
        teams[0].name = savedInstanceState.getString("teamA_name") ?: "Team A"
        teams[1].name = savedInstanceState.getString("teamB_name") ?: "Team B"
        game.grande = savedInstanceState.getInt("grande")
        game.chica = savedInstanceState.getInt("chica")
        game.par = savedInstanceState.getInt("par")
        game.juego = savedInstanceState.getInt("juego")

        // Update UI
        teamAPoints.text = teams[0].points.toString()
        teamBPoints.text = teams[1].points.toString()
        teamAName.text = teams[0].name
        teamBName.text = teams[1].name
        grandePoints?.text = game.grande.toString()
        chicaPoints?.text = game.chica.toString()
        parPoints?.text = game.par.toString()
        juegoPoints?.text = game.juego.toString()
    }

}