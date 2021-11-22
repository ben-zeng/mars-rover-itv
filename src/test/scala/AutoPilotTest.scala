import Action.{MoveStraight, RotateAntiClockwise, RotateClockwise}
import AutoPilot.{Destination, Move}
import Direction.{East, North, South, West}
import Grid.Grid
import RoverMovements.Position
import io.circe.literal.JsonStringContext
import org.scalatest.FunSuite
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.TableDrivenPropertyChecks


class AutoPilotTest extends FunSuite with TableDrivenPropertyChecks {

  test("Rotate: rotate from any direction to North in the shorted amount of turns") {
    val finishFromNorth = Seq.empty[Move]
    val finishFromEast = Seq(Move(RotateAntiClockwise, Position(5, 5, North)))
    val finishFromSouth = Seq(
      Move(RotateClockwise, Position(5, 5, West)),
      Move(RotateClockwise, Position(5, 5, North))
    )
    val finishFromWest = Seq(Move(RotateClockwise, Position(5, 5, North)))

    val testCases = {
      Table(
        ("start", "moves"),
        (Position(5, 5, North), finishFromNorth),
        (Position(5, 5, East), finishFromEast),
        (Position(5, 5, South), finishFromSouth),
        (Position(5, 5, West), finishFromWest)
      )
    }

    forAll(testCases) { (start, moves) =>
      AutoPilot.rotate(start, North) shouldBe moves
    }
  }

  test("Rotate: rotate from any direction to East in the shortest amount of turns") {
    val finishFromNorth = Seq(Move(RotateClockwise, Position(5, 5, East)))
    val finishFromEast = Seq.empty[Move]
    val finishFromSouth = Seq(Move(RotateAntiClockwise, Position(5, 5, East)))
    val finishFromWest = Seq(
      Move(RotateClockwise, Position(5, 5, North)),
      Move(RotateClockwise, Position(5, 5, East))
    )

    val testCases = {
      Table(
        ("start", "moves"),
        (Position(5, 5, North), finishFromNorth),
        (Position(5, 5, East), finishFromEast),
        (Position(5, 5, South), finishFromSouth),
        (Position(5, 5, West), finishFromWest)
      )
    }

    forAll(testCases) { (start, moves) =>
      AutoPilot.rotate(start, East) shouldBe moves
    }
  }

  test("Rotate: rotate from any direction to South in the shortest amount of turns") {
    val finishFromNorth = Seq(
      Move(RotateClockwise, Position(5, 5, East)),
      Move(RotateClockwise, Position(5, 5, South))
    )
    val finishFromEast = Seq(Move(RotateClockwise, Position(5, 5, South)))
    val finishFromSouth = Seq.empty[Move]
    val finishFromWest = Seq(Move(RotateAntiClockwise, Position(5, 5, South)))

    val testCases = {
      Table(
        ("start", "moves"),
        (Position(5, 5, North), finishFromNorth),
        (Position(5, 5, East), finishFromEast),
        (Position(5, 5, South), finishFromSouth),
        (Position(5, 5, West), finishFromWest)
      )
    }

    forAll(testCases) { (start, moves) =>
      AutoPilot.rotate(start, South) shouldBe moves
    }
  }

  test("Rotate: rotate from any direction to West in the shortest amount of turns") {
    val finishFromNorth = Seq(Move(RotateAntiClockwise, Position(5, 5, West)))
    val finishFromEast = Seq(
      Move(RotateClockwise, Position(5, 5, South)),
      Move(RotateClockwise, Position(5, 5, West))
    )
    val finishFromSouth = Seq(Move(RotateClockwise, Position(5, 5, West)))
    val finishFromWest = Seq.empty[Move]

    val testCases = {
      Table(
        ("start", "moves"),
        (Position(5, 5, North), finishFromNorth),
        (Position(5, 5, East), finishFromEast),
        (Position(5, 5, South), finishFromSouth),
        (Position(5, 5, West), finishFromWest)
      )
    }

    forAll(testCases) { (start, moves) =>
      AutoPilot.rotate(start, West) shouldBe moves
    }
  }

  test("Move: move correct number of places given units, start position and direction") {
    val finishNoMove = Seq.empty[Move]
    val finish1North = Seq(Move(MoveStraight, Position(5, 6, North)))
    val finish2East = Seq(
      Move(MoveStraight, Position(6, 5, East)),
      Move(MoveStraight, Position(7, 5, East))
    )
    val finish3South = Seq(
      Move(MoveStraight, Position(5, 4, South)),
      Move(MoveStraight, Position(5, 3, South)),
      Move(MoveStraight, Position(5, 2, South))
    )
    val finish4West = Seq(
      Move(MoveStraight, Position(4, 5, West)),
      Move(MoveStraight, Position(3, 5, West)),
      Move(MoveStraight, Position(2, 5, West)),
      Move(MoveStraight, Position(1, 5, West))
    )


    val testCases = {
      Table(
        ("units", "start", "direction", "expectedMoves"),
        (0, Position(5, 5, North), North, finishNoMove),
        (1, Position(5, 5, North), North, finish1North),
        (-2, Position(5, 5, East), East, finish2East),
        (3, Position(5, 5, South), South, finish3South),
        (-4, Position(5, 5, West), West, finish4West),
      )
    }

    forAll(testCases) { (units, start, direction, expectedMoves) =>
      AutoPilot.move(units, start, direction) shouldBe expectedMoves
    }
  }

  test("Move: move off the grid and appear on opposite side if path is shorter") {
    fail
  }

  test("Navigate: will return a Seq of Positions outlining the shortest path given starting and ending positions") {
    val grid = Grid(10, 30)
    val position = Position(1, 1, South)
    val marsRover = new MarsRover(grid, position)
    val destination = Destination(4, 4)

    AutoPilot.navigate(marsRover, destination) shouldBe Seq(
      Move(RotateClockwise, Position(1, 1, West)),
      Move(RotateClockwise, Position(1, 1, North)),
      Move(MoveStraight, Position(1, 2, North)),
      Move(MoveStraight, Position(1, 3, North)),
      Move(MoveStraight, Position(1, 4, North)),
      Move(RotateClockwise, Position(1, 4, East)),
      Move(MoveStraight, Position(2, 4, East)),
      Move(MoveStraight, Position(3, 4, East)),
      Move(MoveStraight, Position(4, 4, East))
    )
  }

  test("Navigate: avoid mountain ranges") {
    fail
  }

  test("PrintPlan: Will take a Seq of Move's and return it in Json form") {
    val moves = Seq(
      Move(RotateClockwise, Position(1, 1, West)),
      Move(RotateClockwise, Position(1, 1, North)),
      Move(MoveStraight, Position(1, 2, North)),
      Move(MoveStraight, Position(1, 3, North)),
      Move(MoveStraight, Position(1, 4, North)),
      Move(RotateClockwise, Position(1, 4, East)),
      Move(MoveStraight, Position(2, 4, East)),
      Move(MoveStraight, Position(3, 4, East)),
      Move(MoveStraight, Position(4, 4, East))
    )

    AutoPilot.printPlan(moves) shouldBe
      json"""
             [
                {
                  "action" : "RotateClockwise",
                  "position" : {
                    "xPos" : 1,
                    "yPos" : 1,
                    "direction" : "West"
                  }
                },
                {
                  "action" : "RotateClockwise",
                  "position" : {
                    "xPos" : 1,
                    "yPos" : 1,
                    "direction" : "North"
                  }
                },
                {
                  "action" : "MoveStraight",
                  "position" : {
                    "xPos" : 1,
                    "yPos" : 2,
                    "direction" : "North"
                  }
                },
                {
                  "action" : "MoveStraight",
                  "position" : {
                    "xPos" : 1,
                    "yPos" : 3,
                    "direction" : "North"
                  }
                },
                {
                  "action" : "MoveStraight",
                  "position" : {
                    "xPos" : 1,
                    "yPos" : 4,
                    "direction" : "North"
                  }
                },
                {
                  "action" : "RotateClockwise",
                  "position" : {
                    "xPos" : 1,
                    "yPos" : 4,
                    "direction" : "East"
                  }
                },
                {
                  "action" : "MoveStraight",
                  "position" : {
                    "xPos" : 2,
                    "yPos" : 4,
                    "direction" : "East"
                  }
                },
                {
                  "action" : "MoveStraight",
                  "position" : {
                    "xPos" : 3,
                    "yPos" : 4,
                    "direction" : "East"
                  }
                },
                {
                  "action" : "MoveStraight",
                  "position" : {
                    "xPos" : 4,
                    "yPos" : 4,
                    "direction" : "East"
                  }
                }
              ]
       """
  }
}
