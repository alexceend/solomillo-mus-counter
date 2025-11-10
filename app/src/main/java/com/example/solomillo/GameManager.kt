package com.example.solomillo

class GameManager(
    var game: Game = Game(),
    var teams: ArrayList<Team> = arrayListOf()
) {
    init{
        teams.add(Team("Team A", 0))
        teams.add(Team("Team B", 0))
    }

    fun getTeamA() : Team{
        return teams[0]
    }

    fun getTeamB() : Team{
        return teams[1]
    }

    fun increaseScore(type: ScoreType, amount: Int = 1) {
        when (type) {
            ScoreType.GRANDE -> game.grande += amount
            ScoreType.CHICA -> game.chica += amount
            ScoreType.PAR -> game.par += amount
            ScoreType.JUEGO -> game.juego += amount
        }
    }

    // Decrease one of the game categories
    fun decreaseScore(type: ScoreType, amount: Int = 1) {
        when (type) {
            ScoreType.GRANDE -> if (game.grande > 0) game.grande -= amount
            ScoreType.CHICA -> if (game.chica > 0) game.chica -= amount
            ScoreType.PAR -> if (game.par > 0) game.par -= amount
            ScoreType.JUEGO -> if (game.juego > 0) game.juego -= amount
        }
    }

    fun getScore(type: ScoreType): Int {
        return when (type) {
            ScoreType.GRANDE -> game.grande
            ScoreType.CHICA -> game.chica
            ScoreType.PAR -> game.par
            ScoreType.JUEGO -> game.juego
        }
    }

    // Reset a specific score to zero
    fun resetScore(type: ScoreType) {
        when (type) {
            ScoreType.GRANDE -> game.grande = 0
            ScoreType.CHICA -> game.chica = 0
            ScoreType.PAR -> game.par = 0
            ScoreType.JUEGO -> game.juego = 0
        }
    }
}