import Grid.Grid
import enumeratum._

object RoverMovements {
  import Direction._

  final case class Position(xPos: Int, yPos: Int, direction: Direction)

  final case class PossibleRotations(rotateAnticlockwise: Direction,
                                     rotateClockwise: Direction)

  def calcForwardPosition(pos: Position, grid: Grid): Position = {
    pos match {
      case Position(x, y, North) => Position(x, if (y == grid.maxY) 0 else y + 1, North)
      case Position(x, y, East) => Position(if (x == grid.maxX) 0 else x + 1, y, East)
      case Position(x, y, South) => Position(x, if (y == 0) grid.maxY else y - 1, South)
      case Position(x, y, West) => Position(if (x == 0) grid.maxX else x - 1, y, West)
    }
  }

  val calcRotations: PartialFunction[Direction, PossibleRotations] = {
    case North => PossibleRotations(West, East)
    case East => PossibleRotations(North, South)
    case South => PossibleRotations(East, West)
    case West => PossibleRotations(South, North)
  }
}

sealed trait Direction extends EnumEntry
object Direction extends Enum[Direction] with CirceEnum[Direction] {
  case object North extends Direction
  case object East extends Direction
  case object South extends Direction
  case object West extends Direction

  val values: IndexedSeq[Direction] = findValues
}

sealed trait Action extends EnumEntry
object Action extends Enum[Action] with CirceEnum[Action] {
  case object RotateAntiClockwise extends Action
  case object RotateClockwise extends Action
  case object MoveStraight extends Action

  val values: IndexedSeq[Action] = findValues
}
