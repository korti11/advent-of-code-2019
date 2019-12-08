package io.korti.adventofcode.day

class DayEight : AbstractDay() {
    override fun getDay(): Int {
        return 8
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val layers = input[0].map { it.toString().toInt() }.chunked(25 * 6)
        val builder = StringBuilder()
        layers.first().forEachIndexed { index, pixel ->
            if (pixel != 2) {
                builder.append(pixel)
            } else {
                var layer = 1
                while (layers[layer][index] == 2 && layer < layers.size) {
                    layer++
                }
                builder.append(layers[layer][index])
            }
            if (index % 25 == 0 && index != 0) {
                builder.append("\n")
            }
        }
        return builder.toString()
    }

    private fun count(input: List<String>): String {
        return input[0].map { it.toString().toInt() }.chunked(25 * 6).minBy { list ->
            list.count { it == 0 }
        }!!.let { list -> list.count { it == 1 } * list.count { it == 2 } }.toString()
    }
}