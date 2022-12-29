const fs = require("fs")

function addCoordinateSet(set, coordinate) {
  set.add(coordinate.toString())
}
function coordinateSetToArray(set) {
  return Array.from(set).map((c) => c.split(",").map((p) => parseInt(p)))
}
function readInput() {
  const input = fs.readFileSync("./inputday12.txt", { encoding: "utf8" })
  return input.split("\n").filter((i) => !!i)
}

function constructGraph(graph, currentCoordinates, level, visited) {
  let newSetOfCurrentCooridnates = new Set()
  for (const currentCoodinate of currentCoordinates) {
    const label = getLabel(graph, currentCoodinate)
    if (label === "E") {
      return level
    }
    const walkAbleCoordinates = findWalkAbleCoordinates(
      graph,
      currentCoodinate
    ).filter((c) => !visited.has(c.toString()))
    walkAbleCoordinates.forEach((w) =>
      addCoordinateSet(newSetOfCurrentCooridnates, w)
    )
  }
  if (newSetOfCurrentCooridnates.size === 0) {
    throw new Error(`Path not found for ${currentCoordinates}`)
  }
  const newArrayOfCurrentCooridnates = coordinateSetToArray(
    newSetOfCurrentCooridnates
  )
  newArrayOfCurrentCooridnates.forEach((n) => addCoordinateSet(visited, n))
  return constructGraph(graph, newArrayOfCurrentCooridnates, level + 1, visited)
}

function getLabel(graph, coordinate) {
  return (graph[coordinate[0]] || [])[coordinate[1]]
}

function findWalkAbleCoordinates(graph, currentCoodinate) {
  const label = getLabel(graph, currentCoodinate)
  const y = parseInt(currentCoodinate[0])
  const x = parseInt(currentCoodinate[1])
  const adjacentCoodinates = [
    [y + 1, x],
    [y, x + 1],
    [y, x - 1],
    [y - 1, x],
  ]
  return adjacentCoodinates.filter((coordinate) => {
    const adjacentLabel = getLabel(graph, coordinate)
    return adjacentLabel && isWalkable(label, adjacentLabel)
  })
}

function isWalkable(label1, label2) {
  if (label1 === "S") {
    label1 = "a"
  }
  if (label2 === "E") {
    label2 = "z"
  }
  return label1.charCodeAt(0) >= label2.charCodeAt(0) - 1
}

function findStartingPoint(graph) {
  for (let i = 0; i <= graph.length; i++) {
    for (let j = 0; j <= graph[i].length; j++) {
      if (graph[i][j] === "S") {
        return [i, j]
      }
    }
  }
  throw Error("Start not found")
}

function main() {
  const graph = readInput()
  const result = constructGraph(graph, [findStartingPoint(graph)], 0, new Set())
  console.log(result)
}

if (process.env.NODE_ENV !== "test") {
  main()
}

module.exports = {
  isWalkable,
}
