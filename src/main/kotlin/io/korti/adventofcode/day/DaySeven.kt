package io.korti.adventofcode.day

import io.korti.adventofcode.intcode.IntCodeProgram

class DaySeven : AbstractDay() {

    override fun getDay(): Int {
        return 7
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val intCode = input[0].split(",").map { it.toLong() }
        val programs = arrayOf(
            IntCodeProgram(intCode, headless = true, feedbackMode = true),
            IntCodeProgram(intCode, headless = true, feedbackMode = true),
            IntCodeProgram(intCode, headless = true, feedbackMode = true),
            IntCodeProgram(intCode, headless = true, feedbackMode = true),
            IntCodeProgram(intCode, headless = true, feedbackMode = true)
        )
        var maxThrust = Long.MIN_VALUE
        var output = 0L
        for (phase in 56789..98765) {
            if(validatePhase(phase).not()) continue
            programs.forEachIndexed { i, program ->
                program.inputBuffer.add(getPhase(phase, i).toLong())
            }
            do {
                programs.forEach { program ->
                    program.inputBuffer.add(output)
                    program.execute()
                    output = program.outputBuffer.poll()
                }
            } while (programs.last().isFinished().not())
            if(output > maxThrust) maxThrust = output
            output = 0
            programs.forEach(IntCodeProgram::reset)
        }
        return maxThrust.toString()
    }

    private fun validatePhase(phase: Int): Boolean {
        /*return phase.toString().let { s ->
            val sPhase = "0".repeat(5 - s.length) + s
            sPhase.count { it == '0' } == 1 &&
                    sPhase.count { it == '1' } == 1 &&
                    sPhase.count { it == '2' } == 1 &&
                    sPhase.count { it == '3' } == 1 &&
                    sPhase.count { it == '4' } == 1
        }*/
        return phase.toString().let { s ->
            s.count { it == '5' } == 1 &&
                    s.count { it == '6' } == 1 &&
                    s.count { it == '7' } == 1 &&
                    s.count { it == '8' } == 1 &&
                    s.count { it == '9' } == 1
        }
    }

    private fun getPhase(phase: Int, pos: Int): Int {
        /*return phase.toString().let {
            ("0".repeat(5 - it.length) + it)[pos].toString().toInt()
        }*/
        return phase.toString().let {
            it[pos].toString().toInt()
        }
    }

}