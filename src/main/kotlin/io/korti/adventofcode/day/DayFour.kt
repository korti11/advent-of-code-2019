package io.korti.adventofcode.day

class DayFour : AbstractDay() {
    override fun getDay(): Int {
        return 4
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val (min, max) = input.first().split("-").map { it.toInt() }
        var passCounter = 0
        for(pass in min..max) {
            if (containsDouble(pass) && onlyIncreasing(pass)) {
                passCounter++
            }
        }
        return passCounter.toString()
    }

    private fun containsDouble(pass: Int): Boolean {
        pass.toString().toCharArray().apply {
            forEachIndexed { index, char ->
                val before: Boolean
                val after: Boolean
                if (index + 1 < size && char == get(index + 1)) {
                    before = index - 1 >= 0 && char == get(index - 1)
                    after = index + 2 < size && char == get(index + 2)
                    if(before.not() && after.not()) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun onlyIncreasing(pass: Int): Boolean {
        pass.toString().toCharArray().apply {
            forEachIndexed { index, char ->
                if(index + 1 < size && char > get(index + 1)) {
                    return false
                }
            }
        }
        return true
    }
}