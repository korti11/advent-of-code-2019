package io.korti.adventofcode

import io.korti.adventofcode.day.AbstractDay
import io.korti.adventofcode.day.DayOne
import io.korti.adventofcode.day.DayThree
import io.korti.adventofcode.day.DayTwo

fun main(args: Array<String>) {
    val level = getArgument("day", args)
    val implementedLevel = getDay(level.toInt())
    implementedLevel.execute()
}

private fun getArgument(argument: String, args: Array<String>): String {
    val index = args.indexOf("--$argument")
    return args[index + 1]
}

private fun getDay(level: Int): AbstractDay {
    return when (level) {
        1 -> {
            DayOne()
        }
        2 -> {
            DayTwo()
        }
        3 -> {
            DayThree()
        }
        else -> {
            throw Exception("Selected level does not exists!")
        }
    }
}