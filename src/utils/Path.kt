package utils

import java.util.*

fun <P> aStar(start: P,
              goalFunction: (P) -> Boolean,
              successorFunction: (P) -> List<P>,
              heuristicFunction: (P) -> Double
): List<P> {
    val visited = mutableSetOf<P>()
    val path = mutableMapOf<P, P>()
    val distance = mutableMapOf<P, Double>()
    val frontier = PriorityQueue<Pair<P, Double>>(compareBy { it.second })
    distance[start] = 0.0
    frontier.add(start to 0.0)
    while (frontier.isNotEmpty()) {
        val (node, _) = frontier.poll()
        if (node in visited) continue
        if (goalFunction(node)) return reconstructPath(path, start, node)
        visited.add(node)
        for (successor in successorFunction(node)) {
            frontier.add(successor to (distance[node]!! + 1.0 + heuristicFunction(successor)))
            if (successor !in distance || distance[node]!! + 1.0 < distance[successor]!!) {
                distance[successor] = distance[node]!! + 1.0
                path[successor] = node
            }
        }
    }
    return emptyList()
}

fun <P> reconstructPath(path: MutableMap<P, P>, start: P, end: P): List<P> {
    var node = end
    val reversePath = mutableListOf(end)
    while (node != start) {
        node = path[node]!!
        reversePath.add(node)
    }
    return reversePath.reversed()
}