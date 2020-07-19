package controller

import model._
import view.ViewHnefatafl
import utils.BoardGame.Board
import utils.{Coordinate, Move}

trait ControllerHnefatafl {

  /**
    * Calls model to a new game.
    *
    * @return board and player to move.
    */
  def newGame(variant: GameVariant.Val, gameMode: GameMode.Value, levelIA: Level.Val, playerChosen: Player.Value): (Board, Player.Value)

  /**
    * Calls model to initialize IA in PVE mode.
    */
  def startGame(): Unit

  /**
    * Calls model to get dimension of board.
    *
    * @return dimension
    */
  def getDimension: Int

  /**
    * Calls model for the possible moves from a specified coordinate.
    *
    * @return list of coordinates
    */
  def getPossibleMoves(coordinate: Coordinate): Seq[Coordinate]

  /**
    * Calls model for making a move from coordinate to coordinate.
    *
    * @param move
    *               move to make
    *
    * @return (board, numberBlackPiecesCaptured, numberWhitePiecesCaptured)
    */
  def makeMove(move: Move): Unit

  /**
    * Notifies the view that the move has been updated.
    *
    * @param gameSnapshot
    *                 snapshot to show.
    */
  def updateView(gameSnapshot: GameSnapshot): Unit

  /**
    * Notifies the viewer a change snapshot to view.
    *
    * @param gameSnapshot
    *                 snapshot to show.
    */
  def changeSnapshotView(gameSnapshot: GameSnapshot): Unit

  /**
   * Checks if the cell at the specified coordinate is the central cell.
   *
   * @param coordinate
   *                   coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCentralCell(coordinate: Coordinate): Boolean

  /**
   * Checks if the cell at the specified coordinate is a corner cell.
   *
   * @param coordinate
   *                   coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCornerCell(coordinate: Coordinate): Boolean

  /**
    * Checks if the cell at the specified coordinate is a init pawn cell.
    *
    * @param coordinate
    *                   coordinate of the cell to inspect
    *
    * @return boolean.
    */
  def isPawnCell(coordinate: Coordinate): Boolean

  /**
    * Find king coordinate in the current board.
    *
    * @return king coordinate to list.
    */
  def findKing(): Coordinate

  /**
    * Returns a previous or later state of the current board.
    *
    * @param snapshotToShow
    *                       indicates snapshot to show.
    */
  def changeSnapshot(snapshotToShow: Snapshot.Value): Unit

  /**
   * Undoes last move.
   */
  def undoMove(): Unit

  /**
    * Actives/Disables next and last move.
    */
  def activeNextLast()
  def disableNextLast()

  /**
    * Actives/Disables previous and first move.
    */
  def activeFirstPrevious()
  def disableFirstPrevious()

  /**
    * Actives/Disables undo move.
    */
  def activeUndo()
  def disableUndo()
}

object ControllerHnefatafl {

  def apply: ControllerHnefatafl = ControllerHnefataflImpl()

  case class ControllerHnefataflImpl() extends ControllerHnefatafl {

    private val viewGame: ViewHnefatafl = ViewHnefatafl(this)
    private var modelGame: ModelHnefatafl = _

    override def newGame(variant: GameVariant.Val, gameMode: GameMode.Value, levelIA: Level.Val, playerChosen: Player.Value): (Board, Player.Value) = {
      modelGame = ModelHnefatafl(this, variant, gameMode, levelIA, playerChosen)
      modelGame.createGame()
    }

    override def startGame(): Unit = modelGame.startGame()

    override def getPossibleMoves(coordinate: Coordinate): Seq[Coordinate] = modelGame.showPossibleCells(coordinate)

    override def getDimension: Int = modelGame.getDimension

    override def makeMove(move: Move): Unit = modelGame.makeMove(move: Move)

    override def updateView(gameSnapshot: GameSnapshot): Unit = viewGame.update(gameSnapshot)

    override def changeSnapshotView(gameSnapshot: GameSnapshot): Unit = viewGame.changeSnapshot(gameSnapshot)

    override def isCentralCell(coordinate: Coordinate): Boolean = modelGame.isCentralCell(coordinate)

    override def isCornerCell(coordinate: Coordinate): Boolean = modelGame.isCornerCell(coordinate)

    override def isPawnCell(coordinate: Coordinate): Boolean = modelGame.isPawnCell(coordinate)

    override def findKing(): Coordinate = modelGame.findKing()

    override def changeSnapshot(snapshotToShow: Snapshot.Value): Unit = modelGame.changeSnapshot(snapshotToShow)

    override def undoMove(): Unit = modelGame.undoMove()

    override def disableNextLast(): Unit = viewGame.disableNextLast()

    override def disableFirstPrevious(): Unit = viewGame.disableFirstPrevious()

    override def activeUndo(): Unit = viewGame.activeUndo()

    override def disableUndo(): Unit = viewGame.disableUndo()

    override def activeNextLast(): Unit = viewGame.activeNextLast()

    override def activeFirstPrevious(): Unit = viewGame.activeFirstPrevious()

  }
}

