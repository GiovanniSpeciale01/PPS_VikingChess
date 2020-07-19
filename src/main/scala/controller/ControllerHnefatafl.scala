package controller

import model.{GameVariant, ModelHnefatafl, Player}
import view.GameView
import utils.BoardGame.Board
import utils.Pair

trait ControllerHnefatafl {

  /**
    * Calls model for a new game.
    *
    * @return board and player to move.
    */
  def newGame(variant: GameVariant.Val): (Board, Player.Value)

  /**
    * Calls model for the possible moves from a specified coordinate.
    *
    * @return list of coordinates
    */
  def getPossibleMoves(coordinate: Pair[Int]): Seq[Pair[Int]]

  /**
    * Calls model for making a move from coordinate to coordinate.
    *
    * @return (board, numberBlackPiecesCaptured, numberWhitePiecesCaptured)
    */
  def makeMove(coordinateStart: Pair[Int], coordinateArrival: Pair[Int]): Unit

  /**
    * Calls view for indicate the winner of the game.
    */
  def gameEnded(winner: Player.Value, kingCoordinate: Option[Pair[Int]]): Unit

  /**
    * Notifies the view that the move has been updated.
    */
  def notifyMove(playerToMove: Player.Value, winner: Player.Value, board: Board, numberBlackCaptured: Int, numberWhiteCaptured: Int): Unit

  /**
   * Checks if the cell at the specified coordinate is the central cell.
   *
   * @param coordinate
   *                   coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCentralCell(coordinate: Pair[Int]): Boolean

  /**
   * Checks if the cell at the specified coordinate is a corner cell.
   *
   * @param coordinate
   *                   coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCornerCell(coordinate: Pair[Int]): Boolean
}

object ControllerHnefatafl {

  def apply: ControllerHnefatafl = ControllerHnefataflImpl()

  case class ControllerHnefataflImpl() extends ControllerHnefatafl {

    private val viewGame: GameView = GameView(this)
    private var modelGame: ModelHnefatafl = _

    override def newGame(variant: GameVariant.Val): (Board, Player.Value) = {
      modelGame = ModelHnefatafl(this)
      modelGame.createGame(variant)
    }

    override def getPossibleMoves(coordinate: Pair[Int]): Seq[Pair[Int]] = modelGame.showPossibleCells(coordinate)

    override def makeMove(coordinateStart: Pair[Int], coordinateArrival: Pair[Int]): Unit = {
      modelGame.makeMove(coordinateStart, coordinateArrival)
    }

    override def notifyMove(playerToMove: Player.Value, winner: Player.Value, board: Board, numberBlackCaptured: Int, numberWhiteCaptured: Int): Unit = {
      viewGame.updateMove(playerToMove, winner, board, numberBlackCaptured, numberWhiteCaptured)
    }

    override def gameEnded(winner: Player.Value, kingCoordinate: Option[Pair[Int]]): Unit = {
      viewGame.setEndGame(winner, kingCoordinate)
    }

    override def isCentralCell(coordinate: Pair[Int]): Boolean = modelGame.isCentralCell(coordinate)

    override def isCornerCell(coordinate: Pair[Int]): Boolean = modelGame.isCornerCell(coordinate)
  }
}

