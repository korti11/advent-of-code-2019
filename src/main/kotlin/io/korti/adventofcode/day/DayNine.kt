package io.korti.adventofcode.day

import io.korti.adventofcode.intcode.IntCodeProgram

class DayNine : AbstractDay() {
    override fun getDay(): Int {
        return 9
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val intCode = input[0].split(",").map { it.toLong() }
        val program = IntCodeProgram(intCode, headless = false, feedbackMode = false)
        program.execute()
        return program.outputBuffer.toString()
    }
}