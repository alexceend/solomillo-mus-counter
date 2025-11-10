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
}