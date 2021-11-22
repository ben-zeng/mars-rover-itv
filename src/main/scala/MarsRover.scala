import Grid.Grid
import RoverMovements.{Position, calcForwardPosition, calcRotations}

class MarsRover(grid: Grid, position: Position) {
  def moveForwards: Position = {
    val forwards = calcForwardPosition(position, grid)

    Position(forwards.xPos, forwards.yPos, position.direction)
  }

  def rotateAntiClockwise: Position = {
    val direction = calcRotations(position.direction).rotateAnticlockwise

    Position(position.xPos, position.yPos, direction)
  }

  def rotateClockwise: Position = {
    val direction = calcRotations(position.direction).rotateClockwise

    Position(position.xPos, position.yPos, direction)
  }

  def startPosition: Position = position
}




