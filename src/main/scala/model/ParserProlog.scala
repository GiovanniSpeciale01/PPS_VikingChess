package model

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory}
import model.GameRules
import utils.BoardGame.Board.BoardImpl
import utils.BoardGame.{Board, BoardCell}
import utils.{Coordinate, Move}

import scala.collection.mutable.ListBuffer

trait ParserProlog {

  /**
    * Creates a new Game.
    *
    * @return game as (PlayerToMove, Winner, Board, PiecesCapturedInTurn).
    */
  def createGame(newVariant: String): (Player.Val, Player.Val, Board, Int)

  /**
    * Gives possible moves from the selected cell.
    * @param cell
    *                 coordinate of the Cell.
    * @return list buffer of coordinates.
    */
  def showPossibleCells(cell: Coordinate): ListBuffer[Coordinate]

  /**
    * Sets player move if it's legit.
    * @param move
   *                   move to make
    * @return new board.
    */
  def makeLegitMove(move: Move): (Player.Val, Player.Val, Board, Int)

  /**
   * Undoes the last move.
   * @param move
   *                  move to undo
   * @return new board.
   */
  def makeNonLegitMove(move: Move): (Player.Val, Player.Val, Board, Int)

  /**
    * Finds king on game board.
    *
    * @return king's coordinate.
    */
  def findKing(): Coordinate

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
   * Copy himself.
   *
   * @return parser representing the game state.
   */

  def copy(): ParserProlog

  /**
   * Get the Board
   *
   * @return the actual board state.
   */
  def getActualBoard: Board


  /**
    * Player turn to move
    *
    * @return Player.Value.
    */
  def getPlayer: Player.Val


  /**
    * Checks if game state has a winner.
    *
    * @return Option[Player.Val].
    */

  def hasWinner: Option[Player.Val]

  /**
    * Undo some player move.
    *
    * @param oldBoard
    *                 Board before changes
    *
    * @return Unit.
    */
  def undoMove(oldBoard: Board): Unit

  /**
    * Takes all the possible moves of a player from a game state
    *
    * @return List of coordinates tuples
    */
  def gamePossibleMoves(): List[(Coordinate, Coordinate)]

}

object ParserPrologImpl {

  def apply(theory: String): ParserProlog = new ParserPrologImpl(theory)

  def apply(theory: String,_playerToWin: Term ,_playerToMove: Term , _board: Term, _variant: Term): ParserPrologImpl = {
    val ppi=new ParserPrologImpl(theory)
    ppi.winner= _playerToWin
    ppi.playerToMove= _playerToMove
    ppi.board= _board
    ppi.variant= _variant
    ppi
  }

}

case class ParserPrologImpl(theory: String) extends ParserProlog {

  private var goalString: String = ""

  private val engine = new Prolog()
  private var goal: SolveInfo = _
  private var list: Term = new Struct()
  private var winner: Term = new Struct()
  private var playerToMove: Term = new Struct()
  private var board: Term = new Struct()
  private var variant: Term = new Struct()

  def getPlayerToWin:Term = winner

  def getPlayerToMove:Term =  playerToMove

  def getBoard: Term = board

  def getVariant: Term = variant

  engine.setTheory(new Theory(GameRules.theory()))

  override def getActualBoard: Board = {
    goalString = replaceBoardString(board)
    parseBoard(goalString)
  }

  override def getPlayer: Player.Val = {
    setPlayer(playerToMove.toString)
  }

  override def hasWinner: Option[Player.Val] = setPlayer( winner.toString) match {
    case Player.Black => Option( Player.Black)
    case Player.White => Option( Player.White)
    case _ => Option.empty
  }

  override def createGame(newVariant: String): (Player.Val, Player.Val, Board, Int) = {
    goal = engine.solve(s"newGame($newVariant,(V,P,W,B)).")

    setGameTerms(goal)

    goalString = replaceBoardString(board)

    (setPlayer(goal.getTerm("P").toString), setPlayer(goal.getTerm("W").toString), parseBoard(goalString), 0)
  }

  override def showPossibleCells(cell: Coordinate): ListBuffer[Coordinate] = {

    goal = engine.solve(s"getCoordPossibleMoves(($variant,$playerToMove,$winner,$board), p(${cell.x}, ${cell.y}), L).")

    list = goal.getTerm("L")

    goalString = replaceListCellsString(list)

    setListCellsView(goalString)
  }

  override def makeLegitMove(move: Move): (Player.Val, Player.Val, Board, Int) =
    this.synchronized {
      setMove(move, "makeLegitMove")
    }

  override def makeNonLegitMove(move: Move): (Player.Val, Player.Val, Board, Int) =
    setMove(move, "makeMove")

  private def setMove(move: Move, predicate: String): (Player.Val, Player.Val, Board, Int) = {
    goal = engine.solve(s"$predicate(($variant, $playerToMove, $winner, $board)," +
      s"${move.from.toString}," +
      s"${move.to.toString}, L, (V,P,W,B)).")

    setGameTerms(goal)

    goalString = replaceBoardString(board)

    (setPlayer(goal.getTerm("P").toString), setPlayer(goal.getTerm("W").toString),
      parseBoard(goalString),
      goal.getTerm("L").toString.toInt)
  }

  override def findKing(): Coordinate = {
    goal = engine.solve(s"findKing($board, Coord).")
    list = goal.getTerm("Coord")

    goalString = replaceListCellsString(list)

    setListCellsView(goalString).head
  }

  override def isCentralCell(coordinate: Coordinate): Boolean = {
    goal = engine.solve(s"boardSize($variant,S), centralCellCoord(S, ${coordinate.toString}).")

    goal.isSuccess
  }

  override def isCornerCell(coordinate: Coordinate): Boolean = {
    goal = engine.solve(s"boardSize($variant,S), cornerCellCoord(S, ${coordinate.toString}).")

    goal.isSuccess
  }

  override def isPawnCell(coordinate: Coordinate): Boolean = {
    goal = engine.solve(s"isInitialPawnCoord($variant, ${coordinate.toString}).")

    goal.isSuccess
  }

  /**
   * Sets game's terms returned from prolog.
   */
  private def setGameTerms(goal: SolveInfo): Unit = {
    variant = goal.getTerm("V")
    playerToMove = goal.getTerm("P")
    winner = goal.getTerm("W")
    board = goal.getTerm("B")
  }

  /**
   * Cleans the board returned in output.
   *
   * @param board
   *              board identification term.
   *
   * @return board to string.
   */
  private def replaceBoardString(board: Term): String = board.toString.replace("[", "").replace("]", "")
    .replace("(", "").replace(")", "").replace(Coordinate.COORD_STRING, "")

  /**
   * Cleans the pieces captured list returned in output.
   *
   * @param list
   *              list identification term.
   *
   * @return list to string.
   */
  private def replaceListCellsString(list: Term): String = list.toString.replace("','", "").replace("[", "").replace("]", "")
    .replace("),", "").replace("(", "").replace(")", "")

  /**
   * Parses the board: from prolog to scala style.
   *
   * @param stringBoard
   *              board identification string.
   */
  private def parseBoard(stringBoard: String): Board = {

    var listCells: ListBuffer[BoardCell] = ListBuffer.empty[BoardCell]

    stringBoard.split(BoardCell.CELL_STRING).tail.foreach(elem => {
      var coordinateCell: Array[String] = null
      if (elem.contains(Piece.Empty.pieceString)) {
        coordinateCell = elem.substring(0, elem.length - Piece.Empty.pieceString.length - 1).split(",")
        listCells += BoardCell(Coordinate(coordinateCell(0).toInt, coordinateCell(1).toInt), Piece.Empty)
      } else if (elem.contains(Piece.BlackPawn.pieceString)) {
        coordinateCell = elem.substring(0, elem.length - Piece.BlackPawn.pieceString.length - 1).split(",")
        listCells += BoardCell(Coordinate(coordinateCell(0).toInt, coordinateCell(1).toInt), Piece.BlackPawn)
      } else if (elem.contains(Piece.WhitePawn.pieceString)) {
        coordinateCell = elem.substring(0, elem.length - Piece.WhitePawn.pieceString.length - 1).split(",")
        listCells += BoardCell(Coordinate(coordinateCell(0).toInt, coordinateCell(1).toInt), Piece.WhitePawn)
      } else {
        coordinateCell = elem.substring(0, elem.length - Piece.WhiteKing.pieceString.length - 1).split(",")
        listCells += BoardCell(Coordinate(coordinateCell(0).toInt, coordinateCell(1).toInt), Piece.WhiteKing)
      }
    })

    BoardImpl(listCells.grouped(getBoardSize).toSeq.map(_.toSeq))
  }

  private def getBoardSize: Int = {
    val goal: SolveInfo = engine.solve(s"boardSize($variant,S).")
    goal.getTerm("S").toString.toInt
  }

  /**
   * Parses the pieces captured list: from prolog to scala style.
   *
   * @param stringList
   *              list identification string.
   *
   * @return parsed list.
   */
  private def setListCellsView(stringList: String): ListBuffer[Coordinate] = {
    var listPossibleCoordinates: ListBuffer[Coordinate] = ListBuffer.empty[Coordinate]

    goalString.split(Coordinate.COORD_STRING).tail.foreach(elem => {
      var coordinateCells: Array[String] = null
      coordinateCells = elem.split(",")
      listPossibleCoordinates += Coordinate(coordinateCells(0).toInt, coordinateCells(1).toInt)
    })
    listPossibleCoordinates
  }


  /**
   * Parses the string player in a enum.
   *
   * @param stringPlayer
   *              player identification string.
   *
   * @return parsed player.
   */
  private def setPlayer(stringPlayer: String): Player.Val = stringPlayer match {
    case Player.White.playerString => Player.White
    case Player.Black.playerString => Player.Black
    case Player.Draw.playerString => Player.Draw
    case _ => Player.None
  }


  override def copy(): ParserProlog = ParserPrologImpl(theory, winner, playerToMove, board, variant)


  override def equals(that: Any): Boolean = that match {
    case that: ParserPrologImpl =>
      that.isInstanceOf[ParserPrologImpl] &&
        that.variant.isEqualObject(this.variant) &&
        that.playerToMove.isEqualObject(this.playerToMove) &&
        that.winner.isEqualObject(this.winner) &&
        that.board.isEqualObject(this.board)
    case _ => false

  }


  override def undoMove(oldBoard: Board): Unit = {
    goal = engine.solve(s"undoMove(($variant,$playerToMove,$winner,$board), $oldBoard, (V, P, W, B)).")
    setGameTerms(goal)
  }


  override def gamePossibleMoves(): List[(Coordinate, Coordinate)] = {
    goal = engine.solve(s"gamePossibleMoves(($variant,$playerToMove,$winner,$board), L).")

    list = goal.getTerm("L")

    goalString = replaceListCellsString(list)

    setListCellsView(goalString).grouped(2).toList.map(listBuffer => (listBuffer.head, listBuffer(1)))
  }

}
