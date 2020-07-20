package controller

import model.game.GameMode.GameMode
import model.game.GameVariant.GameVariant
import model.game.Level.Level
import model.game.Player.Player
import model.game.Snapshot.Snapshot
import model._
import model.game.{Coordinate, GameSnapshot, Move}
import view.ViewHnefatafl

/**
 * Represents a viking chess game controller.
 */
trait ControllerHnefatafl {

  /**
    * Starts ViewGame.
    */
  def start()

  /**
    * Calls model to a new game.
    *
    * @return new game snapshot
    */
  def newGame(variant: GameVariant, gameMode: GameMode, levelIA: Level, playerChosen: Player): GameSnapshot

  /**
    * Calls model to initialize IA in PVE mode.
    */
  def startGame()

  /**
    * Calls model to get dimension of board.
    *
    * @return board dimension
    */
  def getDimension: Int

  /**
    * Calls model for the possible moves from a specified coordinate.
    *
    * @return list of possible coordinate where you can move from the specified one
    */
  def getPossibleMoves(coordinate: Coordinate): Seq[Coordinate]

  /**
    * Calls model for making a move from coordinate to coordinate.
    *
    * @param move
    *        move to make
    */
  def makeMove(move: Move)

  /**
    * Notifies the view that the move has been updated.
    *
    * @param gameSnapshot
    *         snapshot to show.
    */
  def updateView(gameSnapshot: GameSnapshot)

  /**
    * Checks if the cell at the specified coordinate is the central cell.
    *
    * @param coordinate
    *         coordinate of the cell to inspect
    * @return if the cell at the specified coordinate is the central cell
    */
  def isCentralCell(coordinate: Coordinate): Boolean

  /**
    * Checks if the cell at the specified coordinate is a corner cell.
    *
    * @param coordinate
    *         coordinate of the cell to inspect
    * @return if the cell at the specified coordinate is a corner cell
    */
  def isCornerCell(coordinate: Coordinate): Boolean

  /**
    * Checks if the cell at the specified coordinate is a init pawn cell.
    *
    * @param coordinate
    *         coordinate of the cell to inspect
    * @return if the cell at the specified coordinate is a init pawn cell
    */
  def isPawnCell(coordinate: Coordinate): Boolean

  /**
    * Finds king coordinate in the current board.
    *
    * @return king coordinate to list.
    */
  def findKing(): Coordinate

  /**
    * Returns a previous or later state of the current board.
    *
    * @param snapshotToShow indicates snapshot to show.
    */
  def changeSnapshot(snapshotToShow: Snapshot)

  /**
    * Undoes last move.
    */
  def undoMove()

  /**
    * Enables next and last move.
    */
  def activeNextLast()

  /**
   * Disables next and last move.
   */
  def disableNextLast()

  /**
    * Enables previous and first move.
    */
  def activeFirstPrevious()

  /**
   * Disables previous and first move.
   */
  def disableFirstPrevious()

  /**
    * Enables undo move.
    */
  def activeUndo()

  /**
   * Disables undo move.
   */
  def disableUndo()
}

/**
 * A hnefatafl game controller implementation.
 */
object ControllerHnefatafl extends ControllerHnefatafl {

  private var viewGame: ViewHnefatafl = _
  private var modelGame: ModelHnefatafl = _

  /**
   * @inheritdoc
   */
  override def start(): Unit = viewGame = ViewHnefatafl()

  /**
   * @inheritdoc
   */
  override def newGame(variant: GameVariant, gameMode: GameMode, levelIA: Level, playerChosen: Player): GameSnapshot = {
    modelGame = ModelHnefatafl(variant, gameMode, levelIA, playerChosen)
    modelGame.createGame()
  }

  /**
   * @inheritdoc
   */
  override def startGame(): Unit = modelGame.startGame()

  /**
   * @inheritdoc
   */
  override def getPossibleMoves(coordinate: Coordinate): Seq[Coordinate] = modelGame.showPossibleCells(coordinate)

  /**
   * @inheritdoc
   */
  override def getDimension: Int = modelGame.getDimension

  /**
   * @inheritdoc
   */
  override def makeMove(move: Move): Unit = modelGame.makeMove(move: Move)

  /**
   * @inheritdoc
   */
  override def updateView(gameSnapshot: GameSnapshot): Unit = viewGame.update(gameSnapshot)

  /**
   * @inheritdoc
   */
  override def isCentralCell(coordinate: Coordinate): Boolean = modelGame.isCentralCell(coordinate)

  /**
   * @inheritdoc
   */
  override def isCornerCell(coordinate: Coordinate): Boolean = modelGame.isCornerCell(coordinate)

  /**
   * @inheritdoc
   */
  override def isPawnCell(coordinate: Coordinate): Boolean = modelGame.isPawnCell(coordinate)

  /**
   * @inheritdoc
   */
  override def findKing(): Coordinate = modelGame.findKing()

  /**
   * @inheritdoc
   */
  override def changeSnapshot(snapshotToShow: Snapshot): Unit = modelGame.changeSnapshot(snapshotToShow)

  /**
   * @inheritdoc
   */
  override def undoMove(): Unit = modelGame.undoMove()

  /**
   * @inheritdoc
   */
  override def disableNextLast(): Unit = viewGame.disableNextLast()

  /**
   * @inheritdoc
   */
  override def disableFirstPrevious(): Unit = viewGame.disableFirstPrevious()

  /**
   * @inheritdoc
   */
  override def activeUndo(): Unit = viewGame.activeUndo()

  /**
   * @inheritdoc
   */
  override def disableUndo(): Unit = viewGame.disableUndo()

  /**
   * @inheritdoc
   */
  override def activeNextLast(): Unit = viewGame.activeNextLast()

  /**
   * @inheritdoc
   */
  override def activeFirstPrevious(): Unit = viewGame.activeFirstPrevious()

}

