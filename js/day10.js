const fs = require("fs");

function getXs() {
  const input = fs
    .readFileSync("./inputday10.txt", { encoding: "utf8" })
    .split("\n");
  const recordedX = [1];
  let x = 1;
  let cycle = input.length;

  for (let i = 0; i < cycle; i++) {
    recordedX.push(x);
    const line = input[i];
    if (!line) {
      continue;
    }
    if (line.startsWith("addx")) {
      const addBy = parseInt(line.substring(5));
      recordedX.push(x);
      x = x + addBy;
      cycle += 1;
    }
  }
  return recordedX;
}

async function main() {
  const result = getXs();
  result.forEach((c, i) => console.log(`${i}: ${c}`));
  draw(result);
}

function signalStrength(recorded) {
  let result = 0;
  for (let cycle = 20; cycle <= 220; cycle += 40) {
    console.log(`Cycle: ${cycle}`);
    result += recorded[cycle] * cycle;
  }
  return result;
}

function draw(recorded) {
  const result = Array.from({ length: 6 }, () =>
    Array.from({ length: 40 }, () => ".")
  );
  let spritePosition = 1;
  function getDraw(spritePosition, currentDrawingPosition) {
    if (
      currentDrawingPosition <= spritePosition + 1 &&
      currentDrawingPosition >= spritePosition - 1
    ) {
      return "#";
    }
    return ".";
  }
  const drawingRegisters = recorded.splice(1);
  for (let cycle = 0; cycle < 240; cycle++) {
    spritePosition = drawingRegisters[cycle];
    const currentDrawingPosition = cycle % 40;
    const currentRow = parseInt(cycle / 40);
    const toDraw = getDraw(spritePosition, currentDrawingPosition);
    result[currentRow][currentDrawingPosition] = toDraw;
  }

  console.log(result.map((c) => c.join("")).join("\n"));
}

main();
