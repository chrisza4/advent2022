const fs = require("fs")

const isUnique = (arr) => new Set(arr).size === arr.length

async function main() {
  const input = fs.readFileSync("./inputday7.txt", { encoding: "utf8" })
  let charQueue = []
  for (let index = 0; index < input.length; index++) {
    const currentChar = input[index]
    charQueue.push(currentChar)
    if (charQueue.length > 14) {
      charQueue.shift()
    }
    if (isUnique(charQueue) && charQueue.length === 14) {
      console.log("Found at Index:", index)
      console.log("Message:", charQueue.join(""))
      return
    }
  }
}

main()
