package io.korti.adventofcode.day

class DaySix : AbstractDay() {

    override fun getDay(): Int {
        return 6
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val mapData = input.map {
            val (from, to) = it.split(")")
            Pair(from, to)
        }.groupBy(Pair<String, String>::first, Pair<String, String>::second)
        return traverseMapTo(mapData, "YOU", "", 0).toString()
    }

    private fun traverseMap(mapData: Map<String, List<String>>, cur: String, depth: Int): Int {
        val nextOrbits = mapData[cur] ?: return depth
        var orbitCount = 0
        for (nextOrbit in nextOrbits) {
            orbitCount += traverseMap(mapData, nextOrbit, depth + 1)
        }
        return orbitCount + depth
    }

    private fun traverseMapTo(mapData: Map<String, List<String>>, cur: String, prev: String, depth: Int): Pair<Int, Boolean> {
        val nextOrbits = mapData[cur]
        var found = false
        var path = 0
        if (nextOrbits != null) {
            for (nextOrbit in nextOrbits) {
                when {
                    found -> {
                        return Pair(path, true)
                    }
                    nextOrbit == "SAN" -> {
                        return Pair(depth - 1, true)
                    }
                    nextOrbit != prev -> {
                        val ret = traverseMapTo(mapData, nextOrbit, cur, depth + 1)
                        path = ret.first
                        found = ret.second
                    }
                }
            }
        }
        if(found.not()){
            val nextOrbit = prevOrbit(mapData, cur)
            if(nextOrbit != null && nextOrbit != prev) {
                val ret = traverseMapTo(mapData, nextOrbit, cur, depth + 1)
                path = ret.first
                found = ret.second
            }
        }
        return Pair(path, found)
    }

    private fun prevOrbit(mapData: Map<String, List<String>>, cur: String): String? {
        return mapData.keys.find { (mapData[it] ?: error("This should not happen :P")).contains(cur) }
    }

}