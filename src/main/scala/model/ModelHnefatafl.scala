package model

import actor_ia.{ArtificialIntelligenceImpl, FindBestMoveMsg}
import akka.actor.{ActorRef, ActorSystem, Props}
import controller.ControllerHnefatafl
import ia.{MiniMax, MiniMaxImpl}
import model.GameSnapshot.GameSnapshotImpl
import utils.BoardGame.Board
import utils.{Coordinate, Move}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait ModelHnefatafl {

  /**
   * Calls parser for a new Game.
   *
   * @return created board and player to move.
   */
  def createGame(): (Board, Player.Val)

  /**
   * Calls parser for the possible moves from a cell.
   *
   * @param cell
   *                  coordinate of the Cell.
   *
   * @return list buffer of the possible computed moves.
   */
  def showPossibleCells(cell: Coordinate): ListBuffer[Coordinate]

  /**
    * Sends a message to IA Actor for the best move.
    */
  def makeMoveIA()

  /**
   * Calls parser for making a move from coordinate to coordinate.
   *
   * @param move
   *                    move to make
   *
   * @return updated board.
   */
  def makeMove(move: Move): Unit

  /**
   * Checks if the cell at the specified coordinate is the central cell.
   *
   * @param coordinate
   *                      coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCentralCell(coordinate: Coordinate): Boolean

  /**
   * Checks if the cell at the specified coordinate is a corner cell.
   *
   * @param coordinate
   *                        coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCornerCell(coordinate: Coordinate): Boolean

  /**
   * Checks if the cell at the specified coordinate is a init pawn cell.
   *
   * @param coordinate
   *                        coordinate of the cell to inspect
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
   *                        indicates snapshot to show.
   *
   * @return required board
   */
  def changeSnapshot(snapshotToShow: Snapshot.Value): Unit

  /**
   * Undoes last move.
   */
  def undoMove(): Unit
}

object ModelHnefatafl {

  def apply(controller: ControllerHnefatafl, newVariant: GameVariant.Val, gameMode: GameMode.Value, levelIA: Level.Val): ModelHnefatafl = ModelHnefataflImpl(controller, newVariant, gameMode, levelIA)

  case class ModelHnefataflImpl(controller: ControllerHnefatafl, newVariant: GameVariant.Val, gameMode: GameMode.Value, level: Level.Val) extends ModelHnefatafl {

    /**
     * Inits the parser prolog and set the file of the prolog rules.
     */
    private val THEORY: String = TheoryGame.GameRules.toString
    private val parserProlog: ParserProlog = ParserPrologImpl(THEORY)
    private var storySnapshot: mutable.ListBuffer[GameSnapshot] = _
    private var currentSnapshot: Int = 0

    private var refIA: ActorRef = _
    private val sequIA: MiniMax = MiniMaxImpl(level.depth,newVariant.size)

    /**
     * Defines status of the current game.
     */
    private var game: (Player.Val, Player.Val, Board, Int) = _

    private final val SIZE_DRAW: Int = 9

    /**
      * Defines the game variant.
      */
    private val currentVariant: GameVariant.Val = newVariant

    /**
      * Defines the chosen mode.
      */
    private val mode: GameMode.Value = gameMode

    /**
      * Defines the chosen level of IA.
      */
    private val levelIA: Level.Val = level

    override def createGame(): (Board, Player.Val) = {

      game = parserProlog.createGame(currentVariant.nameVariant.toLowerCase)

      storySnapshot = mutable.ListBuffer(GameSnapshotImpl(currentVariant, game._1, game._2, game._3, Option.empty, 0, 0))

      initIAIfPVEMode()

      (game._3, game._1)
    }

    override def showPossibleCells(cell: Coordinate): ListBuffer[Coordinate] = {
      if (showingCurrentSnapshot)
        parserProlog.showPossibleCells(cell)
      else ListBuffer.empty
    }

    override def makeMoveIA(): Unit = {
      //SEQUENTIAL IA
      //makeMove(sequIA.findBestMove(storySnapshot.last))

      //PARALLEL
      refIA ! FindBestMoveMsg(storySnapshot.last)
    }

    override def makeMove(move: Move): Unit = {

      game = parserProlog.makeLegitMove(move)

      val pieceCaptured: (Int, Int) = incrementCapturedPieces(game._1, game._4)
      var winner: Player.Val = game._2

      if (checkThreefoldRepetition())
        winner = Player.Draw

      storySnapshot += GameSnapshot(currentVariant, game._1, winner, game._3, Option(move), pieceCaptured._1, pieceCaptured._2)

      currentSnapshot += 1

      controller.activeFirstPrevious()
      controller.activeUndo()

      controller.updateView(storySnapshot.last)
    }

    override def isCentralCell(coordinate: Coordinate): Boolean = parserProlog.isCentralCell(coordinate)

    override def isCornerCell(coordinate: Coordinate): Boolean = parserProlog.isCornerCell(coordinate)

    override def isPawnCell(coordinate: Coordinate): Boolean = parserProlog.isPawnCell(coordinate)

    override def findKing(): Coordinate = parserProlog.findKing()

    override def changeSnapshot(previousOrNext: Snapshot.Value): Unit = {
      previousOrNext match {
        case Snapshot.Previous => decrementCurrentSnapshot()
        case Snapshot.Next => incrementCurrentSnapshot()
        case Snapshot.First => currentSnapshot = 0; controller.disableFirstPrevious(); controller.activeNextLast()
        case Snapshot.Last => currentSnapshot = storySnapshot.size - 1; controller.disableNextLast(); controller.activeFirstPrevious()
      }
      val gameSnapshot = storySnapshot(currentSnapshot)
      controller.updateView(gameSnapshot)
    }

    override def undoMove(): Unit = {
      if (showingCurrentSnapshot) {
        if (storySnapshot.last.getLastMove.nonEmpty) {
          storySnapshot -= storySnapshot.last
          controller.activeFirstPrevious()
          currentSnapshot -= 1
          parserProlog.undoMove(storySnapshot.last.getBoard)
          controller.updateView(storySnapshot.last)
        }
      }
      if(storySnapshot.size == 1) {
        controller.disableNextLast()
        controller.disableFirstPrevious()
        controller.disableUndo()
      }
    }

    /**
     * Increments the number of pieces captured of the player.
     */
    private def incrementCapturedPieces(player: Player.Val, piecesCaptured: Int): (Int, Int) = player match {
      case Player.Black => (storySnapshot.last.getNumberCapturedBlacks + piecesCaptured, storySnapshot.last.getNumberCapturedWhites)
      case Player.White => (storySnapshot.last.getNumberCapturedBlacks, storySnapshot.last.getNumberCapturedWhites + piecesCaptured)
      case _ => null
    }

    /**
     * Checks if there was a threefold repetition.
     *
     * @return boolean
     */
    private def checkThreefoldRepetition(): Boolean = storySnapshot.reverse.take(SIZE_DRAW) match {
      case l if l.isEmpty || l.size < SIZE_DRAW => false
      case l if l.head.equals(l(4)) && l(4).equals(l(8)) => true
      case _ => false
    }

    /**
      * Increments current snapshot.
      */
    private def incrementCurrentSnapshot(): Unit = {
      if(!showingCurrentSnapshot) {
        currentSnapshot += 1
        controller.activeFirstPrevious()
      }
      if(showingCurrentSnapshot) controller.disableNextLast()
    }

    /**
      * Decrements current snapshot.
      */
    private def decrementCurrentSnapshot(): Unit = {
      if(currentSnapshot > 0) {
        currentSnapshot -= 1
        controller.activeNextLast()
      }
      if(currentSnapshot == 0) controller.disableFirstPrevious()
    }

    /**
      * Checks if the currentSnapshot is the last.
      */
    private def showingCurrentSnapshot: Boolean = currentSnapshot == storySnapshot.size - 1

    /**
      * Actives the IA Actor
      */
    private def initIAIfPVEMode(): Unit = mode match {
      case GameMode.PVE =>
        val system: ActorSystem = ActorSystem()
        refIA = system.actorOf(Props(ArtificialIntelligenceImpl(this, levelIA.depth)))
      case _ =>
    }
  }
}
