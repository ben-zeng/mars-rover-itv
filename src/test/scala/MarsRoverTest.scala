import Direction._
import RoverMovements.{Grid, Position}
import org.scalatest.FunSuite
import org.scalatest.Matchers.{an, convertToAnyShouldWrapper}
import org.scalatest.prop.TableDrivenPropertyChecks

class MarsRoverTest extends FunSuite with TableDrivenPropertyChecks {

  test("Create a mars rover with operational grid size and position") {
    val grid = Grid(10, 30)
    val position = Position(1, 1, South)

    val marsRover = new MarsRover(grid, position)

    marsRover shouldBe an[MarsRover]
  }

  test("Mars rover moves correctly given direction") {
    val testCases = {
      Table(
        ("initialPosition", "expectedPosition"),
        (Position(5, 5, North), Position(5, 6, North)),
        (Position(5, 5, East), Position(6, 5, East)),
        (Position(5, 5, South), Position(5, 4, South)),
        (Position(5, 5, West), Position(4, 5, West))
      )
    }

    forAll(testCases) { (initialPosition, expectedPosition) =>
      val grid = Grid(10, 30)
      val marsRover = new MarsRover(grid, initialPosition)

      marsRover.moveForwards shouldBe expectedPosition
    }
  }

  test("Mars rover rotates anticlockwise correctly given position") {
    val testCases = {
      Table(
        ("initialPosition", "expectedPosition"),
        (Position(5, 5, North), Position(5, 5, West)),
        (Position(5, 5, East), Position(5, 5, North)),
        (Position(5, 5, South), Position(5, 5, East)),
        (Position(5, 5, West), Position(5, 5, South))
      )
    }

    forAll(testCases) { (initialPosition, expectedPosition) =>
      val grid = Grid(10, 30)
      val marsRover = new MarsRover(grid, initialPosition)

      marsRover.rotateAntiClockwise shouldBe expectedPosition
    }
  }

  test("Mars rover rotates clockwise correctly given position") {
    val testCases = {
      Table(
        ("initialPosition", "expectedPosition"),
        (Position(5, 5, North), Position(5, 5, East)),
        (Position(5, 5, East), Position(5, 5, South)),
        (Position(5, 5, South), Position(5, 5, West)),
        (Position(5, 5, West), Position(5, 5, North))
      )
    }

    forAll(testCases) { (initialPosition, expectedPosition) =>
      val grid = Grid(10, 30)
      val marsRover = new MarsRover(grid, initialPosition)

      marsRover.rotateClockwise shouldBe expectedPosition
    }
  }

  test("Mars rover reappears on the opposite side of the grid correctly when moving off the grid") {
    val testCases = {
      Table(
        ("initialPosition", "expectedPosition"),
        (Position(5, 10, North), Position(5, 0, North)),
        (Position(10, 5, East), Position(0, 5, East)),
        (Position(5, 0, South), Position(5, 10, South)),
        (Position(0, 5, West), Position(10, 5, West))
      )
    }

    forAll(testCases) { (initialPosition, expectedPosition) =>
      val grid = Grid(10, 10)
      val marsRover = new MarsRover(grid, initialPosition)

      marsRover.moveForwards shouldBe expectedPosition
    }
  }
}