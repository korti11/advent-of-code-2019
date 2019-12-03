package io.korti.adventofcode.day

class DayOne : AbstractDay() {

    override fun getDay(): Int {
        return 1
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val fuelList: MutableList<Int> = mutableListOf()
        input.map { it.toInt() }.forEach {
            var currentAmount = it
            while (currentAmount > 0) {
                currentAmount = calcFuel(currentAmount)
                if (currentAmount > 0) {
                    fuelList.add(currentAmount)
                }
            }
        }
        return fuelList.sum().toString()
    }

    private fun calcFuel(mass: Int) : Int {
        return (mass / 3) - 2
    }

}