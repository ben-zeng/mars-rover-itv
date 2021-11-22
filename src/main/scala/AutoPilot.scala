import Direction._
import RoverMovements.{Position, calcRotations}
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps

object AutoPilot {
  import Action._

  def printPlan(moves: Seq[Move]): Json = {
    moves.asJson
  }

  def navigate(marsRover: MarsRover, destination: Destination): Seq[Move] = {
    val startPosition = marsRover.startPosition
    val diffX = destination.xPos - startPosition.xPos
    val diffY = destination.yPos - startPosition.yPos

    val rotateX = if (diffX > 0) rotate(startPosition, North) else rotate(startPosition, South)

    val moveX = rotateX.lastOption.fold(Seq.empty[Move])(lastMove =>
      if (diffX > 0) move(diffX, lastMove.position, North) else move(diffX, lastMove.position, South))

    val rotateY = moveX.lastOption.fold(Seq.empty[Move])(lastMove =>
      if (diffY > 0) rotate(lastMove.position, East) else rotate(lastMove.position, West))

    val moveY = rotateY.lastOption.fold(Seq.empty[Move])(lastMove =>
      if (diffY > 0) move(diffY, lastMove.position, East) else move(diffX, lastMove.position, West))

    rotateX ++ moveX ++ rotateY ++ moveY
  }

  def move(units: Int, startPosition: Position, direction: Direction): Seq[Move] = {
    direction match {
      case North => (1 to Math.abs(units)).map { i =>
        Move(MoveStraight, Position(startPosition.xPos, startPosition.yPos + i, startPosition.direction))}
      case East => (1 to Math.abs(units)).map { i =>
        Move(MoveStraight, Position(startPosition.xPos + i, startPosition.yPos, startPosition.direction))}
      case South => (1 to Math.abs(units)).map { i =>
        Move(MoveStraight, Position(startPosition.xPos, startPosition.yPos - i, startPosition.direction))}
      case West => (1 to Math.abs(units)).map { i =>
        Move(MoveStraight, Position(startPosition.xPos - i, startPosition.yPos, startPosition.direction))}
    }
  }

  def rotate(startPosition: Position, requiredDirection: Direction ): Seq[Move] = {
    calcRotations(startPosition.direction) match {
      case r if r.rotateAnticlockwise == requiredDirection =>
        Seq(Move(RotateAntiClockwise, Position(startPosition.xPos, startPosition.yPos, r.rotateAnticlockwise)))
      case r if r.rotateClockwise == requiredDirection =>
        Seq(Move(RotateClockwise, Position(startPosition.xPos, startPosition.yPos, r.rotateClockwise)))
      case r if startPosition.direction != requiredDirection =>
        Seq(Move(RotateClockwise, Position(startPosition.xPos, startPosition.yPos, r.rotateClockwise)),
          Move(RotateClockwise,Position(startPosition.xPos, startPosition.yPos, calcRotations(r.rotateClockwise).rotateClockwise)))
      case _ => Seq.empty
    }
  }

  case class Destination(xPos: Int, yPos: Int)
  case class Move(action: Action, position: Position)
}

