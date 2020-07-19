package model.prolog

import java.io.{BufferedReader, FileInputStream}

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory}
import model.game.Player.Player
import model.game.BoardGame.Board.BoardImpl
import model.game.BoardGame.{Board, BoardCell}
import model.game.{Coordinate, Move, Piece, Player}
import view.utils.ResourceLoader

/**
 * A prolog viking chess parser
 */
sealed trait ParserPrologTrait {

  /**
   * Creates a new Game.
   *
   * @return new game state
   */
  def createGame(newVariant: String): PrologSnapshot

  /**
   * Returns the possible moves from the specified cell.
   *
   * @param cell
   *        coordinate of the Cell.
   * @return list of the possible coordinates where the piece in the specified coordinate can move
   */
  def showPossibleCells(cell: Coordinate): Seq[Coordinate]

  /**
   * Makes a move if it is legit.
   *
   * @param move
   *        move to make
   * @return new game state
   */
  def makeLegitMove(move: Move): Option[PrologSnapshot]

  /**
   * Finds the king in the game board.
   *
   * @return king's coordinate.
   */
  def findKing(): Coordinate

  /**
   * Checks if the specified coordinate is the central one.
   *
   * @param coordinate
   *        coordinate of the cell to inspect
   * @return if the specified coordinate is the central one.
   */
  def isCentralCell(coordinate: Coordinate): Boolean

  /**
   * Checks if the specified coordinate is a corner one.
   *
   * @param coordinate
   *        coordinate of the cell to inspect
   * @return if the specified coordinate is a corner one
   */
  def isCornerCell(coordinate: Coordinate): Boolean

  /**
   * Checks if the specified coordinate is an initial pawn cell.
   *
   * @param coordinate
   *        coordinate of the cell to inspect
   * @return if the specified coordinate is an initial pawn cell
   */
  def isPawnCell(coordinate: Coordinate): Boolean

  /**
   * Undoes the last move.
   *
   * @param oldBoard
   *        Board before the last move
   */
  def undoMove(oldBoard: Board)
}

/**
 * Representing a prolog result game representation
 *
 * @param playerToMove
 *        next player to move
 * @param winner
 *        new game winner
 * @param board
 *        new board
 * @param nCaptures
 *        captures occured after the last move
 */
case class PrologSnapshot(playerToMove: Player, winner: Player, board: Board, nCaptures: Int)

/**
 * A prolog viking chess parser
 */
object ParserProlog extends ParserPrologTrait {

  import ImplicitParser._

  private val engine: Prolog = new Prolog()
  private var goal: SolveInfo = _
  private var list: Term = new Struct()
  private var winner: Term = new Struct()
  private var playerToMove: Term = new Struct()
  private var board: Term = new Struct()
  private var variant: Term = new Struct()

  private object Predicate extends Enumeration {
    type Predicate = Value
    val NewGame: Value = Value("newGame")
    val GetCoordPossibleMoves: Value = Value("getCoordPossibleMoves")
    val MakeLegitMove: Value = Value("makeLegitMove")
    val FindKing: Value = Value("findKing")
    val BoardSize: Value = Value("boardSize")
    val CornerCellCoord: Value = Value("cornerCellCoord")
    val CentralCellCoord: Value = Value("centralCellCoord")
    val IsInitialPawnCoord: Value = Value("isInitialPawnCoord")
    val UndoMove: Value = Value("undoMove")
  }

  engine.setTheory(new Theory(GameRules.theory()))

  /**
   * @inheritdoc
   */
  override def createGame(newVariant: String): PrologSnapshot = {
    goal = engine.solve(s"${Predicate.NewGame}($newVariant,(V,P,W,B)).")
    setGameTerms(goal)
    PrologSnapshot(playerToMove.parsePlayer, winner.parsePlayer, board.parseBoard(getBoardSize), 0)
  }

  /**
   * @inheritdoc
   */
  override def showPossibleCells(cell: Coordinate): Seq[Coordinate] = {
    goal = engine.solve(s"${Predicate.GetCoordPossibleMoves}(($variant,$playerToMove,$winner,$board), ${cell.toString}, L).")
    list = goal.getTerm("L")
    list.parseMoveList
  }

  /**
   * @inheritdoc
   */
  override def makeLegitMove(move: Move): Option[PrologSnapshot] = {
    goal = engine.solve(s"${Predicate.MakeLegitMove}(($variant, $playerToMove, $winner, $board)," +
      s"${move.from.toString}," +
      s"${move.to.toString}, L, (V,P,W,B)).")

    if(goal.isSuccess) {
      setGameTerms(goal)
      Option(PrologSnapshot(playerToMove.parsePlayer, winner.parsePlayer, board.parseBoard(getBoardSize), goal.getTerm("L").parseInt))
    } else
      Option.empty
  }

  /**
   * @inheritdoc
   */
  override def findKing(): Coordinate =
    engine.solve(s"${Predicate.FindKing}($board, Coord).").getTerm("Coord").parseMoveList.head

  /**
   * @inheritdoc
   */
  override def isCentralCell(coordinate: Coordinate): Boolean =
    engine.solve(s"${Predicate.BoardSize}($variant,S), ${Predicate.CentralCellCoord}(S, ${coordinate.toString}).").isSuccess

  /**
   * @inheritdoc
   */
  override def isCornerCell(coordinate: Coordinate): Boolean =
    engine.solve(s"${Predicate.BoardSize}($variant,S), ${Predicate.CornerCellCoord}(S, ${coordinate.toString}).").isSuccess

  /**
   * @inheritdoc
   */
  override def isPawnCell(coordinate: Coordinate): Boolean =
    engine.solve(s"${Predicate.IsInitialPawnCoord}($variant, ${coordinate.toString}).").isSuccess

  /**
   * @inheritdoc
   */
  override def undoMove(oldBoard: Board): Unit =
    setGameTerms(engine.solve(s"${Predicate.UndoMove}(($variant,$playerToMove,$winner,$board), $oldBoard, (V, P, W, B))."))

  private def setGameTerms(goal: SolveInfo): Unit = {
    variant = goal.getTerm("V")
    playerToMove = goal.getTerm("P")
    winner = goal.getTerm("W")
    board = goal.getTerm("B")
  }

  private def getBoardSize: Int = engine.solve(s"${Predicate.BoardSize}($variant,S).").getTerm("S").parseInt

  object ImplicitParser {

    private val prologListUselessChars: List[String] = List("','", "[", "]", "),", "(", ")")
    private val prologBoardUselessChars: List[String] = List("[", "]", "(", ")")

    implicit class StringUtils(base: String) {
      implicit def clearChars(chars: List[String]): String = base.filter(e => !chars.contains(e.toString))
      implicit def dropRightComma: String = if (base.last.equals(',')) base.dropRight(1) else base
    }

    implicit class BoardParser(base: Term) {
      implicit def parseBoard(size: Int): Board =
        BoardImpl(base.toString.clearChars(prologBoardUselessChars)
          .split(BoardCell.CELL_STRING).tail.map(_.parseCell).grouped(size).toSeq.map(_.toSeq))
    }

    implicit class CellParser(base: String) {
      implicit def parseCell: BoardCell = {
        val cell = base.dropRightComma
        BoardCell(cell.parseCoordinate, Piece.withName(cell.last.toString))
      }
    }

    private def arrayToCoordinate(a: Array[String]): Coordinate = Coordinate(a(0).toInt, a(1).toInt)

    implicit class CoordinateParser(base: String) {
      implicit def parseCoordinate: Coordinate = {
        val coordinate = base.replace(Coordinate.COORD_STRING, "").substring(0, base.length - 1).split(",")
        arrayToCoordinate(coordinate)
      }
    }

    implicit class MovesParser(base: Term) {
      implicit def parseMoveList: Seq[Coordinate] =
        base.toString.clearChars(prologListUselessChars).split(Coordinate.COORD_STRING).tail
          .map(e => arrayToCoordinate(e.split(",")))
    }

    implicit class PlayerParser(base: Term) {
      implicit def parsePlayer: Player = Player.values.filter(_.parserString.equals(base.toString)).head
    }

    implicit class IntParser(base: Term) {
      implicit def parseInt: Int = base.toString.toInt
    }
  }
}
