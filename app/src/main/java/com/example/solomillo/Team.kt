package com.example.solomillo

data class Team(var name: String, var points: Int){
    fun increase(amount: Int = 1) {
        points = (points + amount).coerceAtMost(40)
    }

    fun decrease(amount: Int = 1) {
        points = (points - amount).coerceAtLeast(0)
    }

    fun reset() {
        points = 0
    }
}
