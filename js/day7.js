const fs = require("fs");

const isUnique = (arr) => new Set(arr).size === arr.length;

function cd(current, newDirectory) {
  if (newDirectory === "..") {
    return current.slice(0, current.length - 1);
  }
  return current.concat([newDirectory]);
}

function recordSize(sizeMap, currentDir, fileSize) {
  let dir = currentDir;
  while (dir.length > 0) {
    const key = dir.join("-");
    if (!sizeMap[key]) {
      sizeMap[key] = 0;
    }
    sizeMap[key] += fileSize;
    dir = dir.slice(0, dir.length - 1);
  }
}

async function main() {
  const input = fs
    .readFileSync("./inputday7.txt", { encoding: "utf8" })
    .split("\n");
  let currentDir = [];
  let sizeMap = {};

  for (let index = 0; index < input.length; index++) {
    const fileLineRegExp = new RegExp(/(\d+)\s(.*)/, "y");
    const currentLine = input[index];
    if (currentLine.startsWith("$ cd")) {
      const newDir = currentLine.substring(5);
      currentDir = cd(currentDir, newDir);
    } else if (currentLine.match(fileLineRegExp)) {
      fileLineRegExp.lastIndex = 0;
      const match = currentLine.match(fileLineRegExp);
      const fileSize = parseInt(match[1]);
      console.log(`Record file size ${fileSize} of file ${match[2]}`);
      recordSize(sizeMap, currentDir, fileSize);
    }
  }

  // Puzzle
  const result = Object.values(sizeMap)
    .filter((c) => c <= 100000)
    .reduce((acc, val) => acc + val, 0);
  console.log("Puzzle1:", result);

  const usedSpace = sizeMap["/"];
  const totalSize = 70000000;
  const availableSpace = totalSize - usedSpace;
  const neededSpace = 30000000;
  const gap = neededSpace - availableSpace;
  const result2 = Object.values(sizeMap)
    .filter((c) => c >= gap)
    .reduce((acc, val) => Math.min(acc, val), Number.MAX_SAFE_INTEGER);

  console.log("Puzzle2:", result2);
}

main();
