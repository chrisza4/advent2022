const assert = require("assert")
const { isWalkable } = require("../day12")

describe("isWalkable", () => {
  it("Can walk from start point", () => {
    assert.ok(isWalkable("S", "a"))
    assert.ok(isWalkable("S", "b"))
    assert.ok(!isWalkable("S", "c"))
    assert.ok(!isWalkable("S", "z"))
  })

  it("Can walk to end point", () => {
    // Endpoint = z
    assert.ok(!isWalkable("a", "E"))
    assert.ok(!isWalkable("b", "E"))
    assert.ok(!isWalkable("c", "E"))
    assert.ok(isWalkable("z", "E"))
    assert.ok(isWalkable("y", "E"))
  })

  it("Can walk to to lower", () => {
    assert.ok(isWalkable("b", "a"))
    assert.ok(isWalkable("c", "a"))
  })

  it("Can walk to to same", () => {
    assert.ok(isWalkable("b", "b"))
    assert.ok(isWalkable("c", "c"))
  })

  it("Can walk to one-higher", () => {
    assert.ok(isWalkable("b", "c"))
    assert.ok(isWalkable("c", "d"))
  })
})
