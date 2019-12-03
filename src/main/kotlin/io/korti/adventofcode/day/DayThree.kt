package io.korti.adventofcode.day

import java.lang.Integer.sum
import kotlin.math.abs

class DayThree : AbstractDay() {
    override fun getDay(): Int {
        return 3
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val wireOne = mutableListOf<Pos>()
        val wireCrossings = sortedSetOf<Pair<Pos, Pos>>(
            Comparator { o1, o2 ->
                sum(o1.first.length, o1.second.length).compareTo(sum(o2.first.length, o2.second.length))
            }
        )

        val pathOne = input[0].split(",")
        val pathTwo = input[1].split(",")
        val startPos = Pos(0, 0)

        wireOne.add(startPos)
        var currentPos = startPos

        for (move in pathOne) {
            val direction = move[0]
            val moves = move.substring(1).toInt()
            for(i in 1..moves) {
                currentPos = currentPos.moveInDirection(direction)
                wireOne.add(currentPos)
            }
        }

        currentPos = startPos

        for (move in pathTwo) {
            val direction = move[0]
            val moves = move.substring(1).toInt()
            for(i in 1..moves) {
                currentPos = currentPos.moveInDirection(direction)
                val index = wireOne.indexOf(currentPos)
                if (index != -1) {
                    wireCrossings.add(Pair(wireOne[index], currentPos))
                }
            }
        }

        return wireCrossings.first().let { it.first.length + it.second.length }.toString()
    }

}

private data class Pos(val x: Int, val y: Int, val length: Int = 0) : Comparable<Pos> {

    fun moveInDirection(direction: Char): Pos {
        return when (direction) {
            'U' -> Pos(x, y + 1, length + 1)
            'R' -> Pos(x + 1, y, length + 1)
            'D' -> Pos(x, y - 1, length + 1)
            'L' -> Pos(x - 1, y, length + 1)
            else -> throw RuntimeException("Direction $direction not supported.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if((other is Pos).not()) {
            return false
        }
        val otherPos = other as Pos
        return x == otherPos.x && y == other.y
    }

    fun getDistanceFromCenter(): Int {
        return abs(x) + abs(y)
    }

    override fun compareTo(other: Pos): Int {
        return getDistanceFromCenter().compareTo(other.getDistanceFromCenter())
    }
}