package model

import akka.actor.{ActorRef, ActorSystem, Props}
import controller.ControllerHnefatafl
import ia.messages.Messages._
import ia.minimax.ArtificialIntelligence
import model.game.GameMode.GameMode
import model.game.GameSnapshot.GameSnapshotImpl
import model.game.GameVariant.GameVariant
import model.game.Level.Level
import model.game.Player.Player
import model.game.Snapshot.Snapshot
import model.game._
import model.prolog.{ParserProlog, PrologSnapshot}

/**
 * Represents a viking chess game.
 */
trait ModelHnefatafl {

  /**
    * Returns the dimension of the board of the current variant.
    *
    * @return dimension of the board
    */
  def getDimension: Int

  /**
   * Creates a new game.
   *
   * @return new game snapshot
   */
  def createGame(): GameSnapshot

  /**
    * Starts a created game.
    */
  def startGame()

  /**
   * Returns the possible moves from the specified cell.
   *
   * @param cell
   *        coordinate of the cell
   *
   * @return list of the possible coordinates where the piece in the specified coordinate can move
   */
  def showPossibleCells(cell: Coordinate): Seq[Coordinate]

  /**
    * Makes the best move for the IA
    */
  def iaBestMove(move: Move)

  /**
   * Makes a move if it is legit.
   *
   * @param move
   *        move to make
   */
  def makeMove(move: Move)

  /**
   * Checks if the specified coordinate is the central coordinate.
   *
   * @param coordinate
   *        coordinate to inspect
   *
   * @return if the specified coordinate is the central coordinate
   */
  def isCentralCell(coordinate: Coordinate): Boolean

  /**
   * Checks if the specified coordinate is a corner coordinate.
   *
   * @param coordinate
   *        coordinate of the cell to inspect
   *
   * @return if the specified coordinate is a corner coordinate
   */
  def isCornerCell(coordinate: Coordinate): Boolean

  /**
   * Checks if the specified coordinate is an initial pawn cell.
   *
   * @param coordinate
   *         coordinate to inspect
   *
   * @return if the specified coordinate is an initial pawn cell
   */
  def isPawnCell(coordinate: Coordinate): Boolean

  /**
   * Finds the king in the game board.
   *
   * @return king's coordinate.
   */
  def findKing(): Coordinate

  /**
   * Returns a previous or later state of the current board.
   *
   * @param snapshotToShow
   *        indicates the snapshot to show.
   */
  def changeSnapshot(snapshotToShow: Snapshot)

  /**
   * Undoes the last move.
   */
  def undoMove()
}

/**
 * Represents a viking chess game.
 */
object ModelHnefatafl {

  val MIN_IA_TIME = 750

  def apply(gameVariant: GameVariant, gameMode: GameMode, levelIA: Level, playerChosen: Player): ModelHnefatafl =
    ModelHnefataflImpl(gameVariant, gameMode, levelIA, playerChosen)

  case class ModelHnefataflImpl(private val gameVariant: GameVariant, private val gameMode: GameMode, private val levelIA: Level,
                                private val playerChosen: Player) extends ModelHnefatafl {

    private val SIZE_DRAW: Int = 9

    private var storySnapshot: Seq[GameSnapshot] = _
    private var currentSnapshot: Int = 0
    private var system: ActorSystem = _
    private var refIA: Option[ActorRef] = Option.empty
    private var iaSnapshot: GameSnapshot = _

    /**
     * @inheritdoc
     */
    override def getDimension: Int = gameVariant.boardSize

    /**
     * @inheritdoc
     */
    override def createGame(): GameSnapshot = {
      val game: PrologSnapshot = ParserProlog.createGame(gameVariant.toString.toLowerCase)
      storySnapshot = Seq(GameSnapshotImpl(gameVariant, game.playerToMove, game.winner, game.board, Option.empty, 0, 0))
      storySnapshot.last
    }

    /**
     * @inheritdoc
     */
    override def startGame(): Unit = {
      if(gameMode.equals(GameMode.PVE)) {
        initIA()
        if(iaTurn)
          makeMoveIA()
      }
    }

    /**
     * @inheritdoc
     */
    override def showPossibleCells(cell: Coordinate): Seq[Coordinate] = {
      if(!iaTurn)
        if(showingCurrentSnapshot)
          ParserProlog.showPossibleCells(cell)
        else
          Seq.empty
      else
        Seq.empty
    }

    /**
     * @inheritdoc
     */
    override def makeMove(move: Move): Unit = {
      val game: Option[PrologSnapshot]  = ParserProlog.makeLegitMove(move)

      if(game.nonEmpty) {
        val g = game.get
        val pieceCaptured: CapturedPieces = incrementCapturedPieces(g.playerToMove, g.nCaptures)
        var winner: Player = g.winner

        if(checkThreefoldRepetition())
          winner = Player.Draw

        storySnapshot :+=
          GameSnapshot(gameVariant, g.playerToMove, winner, g.board, Option(move), pieceCaptured.blackCaptured, pieceCaptured.whiteCaptured)

        currentSnapshot += 1

        ControllerHnefatafl.activeFirstPrevious()
        ControllerHnefatafl.activeUndo()

        ControllerHnefatafl.updateView(storySnapshot.last)

        if(gameMode.equals(GameMode.PVE) && storySnapshot.last.getWinner.equals(Player.None) && iaTurn)
          makeMoveIA()
      }
    }

    /**
     * @inheritdoc
     */
    override def iaBestMove(move: Move): Unit = 
      if(notUndone) 
        makeMove(move)

    /**
     * @inheritdoc
     */
    override def isCentralCell(coordinate: Coordinate): Boolean = ParserProlog.isCentralCell(coordinate)

    /**
     * @inheritdoc
     */
    override def isCornerCell(coordinate: Coordinate): Boolean = ParserProlog.isCornerCell(coordinate)

    /**
     * @inheritdoc
     */
    override def isPawnCell(coordinate: Coordinate): Boolean = ParserProlog.isPawnCell(coordinate)

    /**
     * @inheritdoc
     */
    override def findKing(): Coordinate = ParserProlog.findKing()

    /**
     * @inheritdoc
     */
    override def changeSnapshot(previousOrNext: Snapshot): Unit = {
      previousOrNext match {
        case Snapshot.Previous => decrementCurrentSnapshot()
        case Snapshot.Next => incrementCurrentSnapshot()
        case Snapshot.First => currentSnapshot = 0; ControllerHnefatafl.disableFirstPrevious(); ControllerHnefatafl.activeNextLast()
        case Snapshot.Last => currentSnapshot = storySnapshot.size - 1; ControllerHnefatafl.disableNextLast(); ControllerHnefatafl.activeFirstPrevious()
      }
      val gameSnapshot = storySnapshot(currentSnapshot)
      ControllerHnefatafl.updateView(gameSnapshot)
    }

    /**
     * @inheritdoc
     */
    override def undoMove(): Unit = {
      if(showingCurrentSnapshot && storySnapshot.last.getLastMove.nonEmpty) {
        storySnapshot = storySnapshot.filter(!_.equals(storySnapshot.last))
        currentSnapshot -= 1
        ControllerHnefatafl.activeFirstPrevious()
        ParserProlog.undoMove(storySnapshot.last.getBoard)
        ControllerHnefatafl.updateView(storySnapshot.last)
      }
      if(storySnapshot.size == 1) {
        ControllerHnefatafl.disableNextLast()
        ControllerHnefatafl.disableFirstPrevious()
        ControllerHnefatafl.disableUndo()
      }
      if(gameMode.equals(GameMode.PVE))
        if(iaTurn)
          makeMoveIA()
    }

    private def notUndone: Boolean = iaSnapshot.equals(storySnapshot.last)

    private def makeMoveIA(): Unit = {
      //SEQUENTIAL PRUNING ALPHA-BETA
      //makeMove(sequIA.findBestMove(storySnapshot.last))

      //MINIMAX ACTORS
      iaSnapshot = storySnapshot.last
      if(refIA.nonEmpty)
        refIA.get ! CloseMsg()

      refIA = Option(system.actorOf(Props(ArtificialIntelligence(this, levelIA))))
      refIA.get ! FindBestMoveMsg(storySnapshot.last)
    }

    private case class CapturedPieces(blackCaptured: Int, whiteCaptured: Int)

    private def incrementCapturedPieces(player: Player, piecesCaptured: Int): CapturedPieces = player match {
      case Player.Black => CapturedPieces(storySnapshot.last.getNumberCapturedBlacks + piecesCaptured, storySnapshot.last.getNumberCapturedWhites)
      case _ => CapturedPieces(storySnapshot.last.getNumberCapturedBlacks, storySnapshot.last.getNumberCapturedWhites + piecesCaptured)
    }

    private def checkThreefoldRepetition(): Boolean = storySnapshot.reverse.take(SIZE_DRAW) match {
      case l if l.isEmpty || l.size < SIZE_DRAW => false
      case l if l.head.equals(l(4)) && l(4).equals(l(8)) => true
      case _ => false
    }

    private def incrementCurrentSnapshot(): Unit = {
      if(!showingCurrentSnapshot) {
        currentSnapshot += 1
        ControllerHnefatafl.activeFirstPrevious()
      }
      if(showingCurrentSnapshot) ControllerHnefatafl.disableNextLast()
    }

    private def decrementCurrentSnapshot(): Unit = {
      if(currentSnapshot > 0) {
        currentSnapshot -= 1
        ControllerHnefatafl.activeNextLast()
      }
      if(currentSnapshot == 0) ControllerHnefatafl.disableFirstPrevious()
    }

    private def showingCurrentSnapshot: Boolean = currentSnapshot == storySnapshot.size - 1

    private def initIA(): Unit = {
      system = ActorSystem()
      //sequIA = MiniMaxImpl(levelIA)
    }

    private def iaTurn: Boolean = !(storySnapshot.last.getPlayerToMove.equals(playerChosen) || playerChosen.equals(Player.None))
  }
}