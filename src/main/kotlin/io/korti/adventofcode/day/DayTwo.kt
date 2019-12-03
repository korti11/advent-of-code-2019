package io.korti.adventofcode.day

class DayTwo : AbstractDay() {

    override fun getDay(): Int {
        return 2
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val opCodes = input.map { it.split(",") }.first().map { it.toInt() }.toMutableList()
        for(noun in 0..99) {
            for(verb in 0..99) {
                val result = runModifiedCode(opCodes, noun, verb)[0]
                if(result == 19690720) return (100 * noun + verb).toString()
            }
        }
        return "0"
    }

    private fun runModifiedCode(opCodes: List<Int>, noun: Int, verb: Int): List<Int> {
        val modifiedCodes = opCodes.toMutableList()
        modifiedCodes[1] = noun
        modifiedCodes[2] = verb
        return runCode(modifiedCodes)
    }


    private fun runCode(opCodes: List<Int>): List<Int> {
        val codes = opCodes.toMutableList()
        code@for (index in 0..codes.size step 4) {
            when (codes[index]) {
                1 -> {
                    val (x1, x2, y) = getPositions(codes, index)
                    codes[y] = codes[x1] + codes[x2]
                }
                2 -> {
                    val (x1, x2, y) = getPositions(codes, index)
                    codes[y] = codes[x1] * codes[x2]
                }
                99 -> {
                    break@code
                }
            }
        }
        return codes
    }

    private fun getPositions(opCodes: List<Int>, index: Int): Triple<Int, Int, Int> {
        return Triple(opCodes[index + 1], opCodes[index + 2], opCodes[index + 3])
    }

}