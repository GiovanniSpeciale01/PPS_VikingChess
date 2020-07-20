package actor_ia

import model.{GameSnapshot, GameVariant, Piece, Player}
import utils.BoardGame.{Board, BoardCell, OrthogonalDirection}
import utils.BoardGame.OrthogonalDirection.OrthogonalDirection
import utils.{Coordinate, Move}

object MoveGenerator {

  def gamePossibleMoves(gameSnapshot: GameSnapshot): List[Move] = {
    def _moves(cell: BoardCell): List[Move] = {
      gameSnapshot.getBoard.orthogonalCells(cell.getCoordinate).values
        .flatMap(_cutAfterPiece(_, cell)).toList
    }

    def _cutAfterPiece(sequence: List[BoardCell], cell: BoardCell): List[Move] =
      sequence.takeWhile(_.getPiece.equals(Piece.Empty))
        .filter(_filterIfPawn(_, cell))
        .map(c => Move(cell.getCoordinate, c.getCoordinate))

    def _filterIfPawn(cellToInspect: BoardCell, movingCell: BoardCell): Boolean = {
      (!movingCell.getPiece.equals(Piece.WhiteKing) && !gameSnapshot.getBoard.specialCoordinates.contains(cellToInspect.getCoordinate)) ||
        movingCell.getPiece.equals(Piece.WhiteKing)
    }

    gameSnapshot.getBoard.rows.flatMap(_.flatMap(c =>
      if (isOwner(c.getPiece, gameSnapshot.getPlayerToMove))
        _moves(c)
      else List.empty
    ))
  }.toList

  def makeMove(gameSnapshot: GameSnapshot, move: Move): GameSnapshot = {
    def _move(): Unit = {
      gameSnapshot.getBoard.setCell(BoardCell(move.to, gameSnapshot.getBoard.getCell(move.from).getPiece))
      gameSnapshot.getBoard.setCell(BoardCell(move.from, Piece.Empty))
    }

    def _checkCaptures(adjacentCells: List[BoardCell]): List[Coordinate] = adjacentCells match {
      case l if l.size < 2 => Nil
      case h :: t
        if (!isOwner(h.getPiece, gameSnapshot.getPlayerToMove) && !h.getPiece.equals(Piece.WhiteKing) && !h.getPiece.equals(Piece.Empty)) &&
        (isOwner(t.head.getPiece, gameSnapshot.getPlayerToMove) || (gameSnapshot.getBoard.specialCoordinates.contains(t.head.getCoordinate) &&
          t.head.getPiece.equals(Piece.Empty)))
        => List(h.getCoordinate)
      case _ => Nil
    }

    def _incrementCapturedPieces(piecesCaptured: Int): (Int, Int) = gameSnapshot.getPlayerToMove match {
      case Player.Black => (gameSnapshot.getNumberCapturedBlacks, gameSnapshot.getNumberCapturedWhites  + piecesCaptured)
      case Player.White => (gameSnapshot.getNumberCapturedBlacks + piecesCaptured, gameSnapshot.getNumberCapturedWhites)
      case _ => null
    }

    def switchPlayer(): Player.Val = gameSnapshot.getPlayerToMove match {
      case Player.Black => Player.White
      case Player.White => Player.Black
      case _ => Player.None
    }

    def checkWinner(): Player.Val = (checkVictory(), checkDraw()) match {
      case (true, false) => gameSnapshot.getPlayerToMove
      case (false, true) => Player.Draw
      case _ => Player.None
    }

    def checkVictory(): Boolean = (gameSnapshot.getVariant, gameSnapshot.getPlayerToMove) match {
      case (GameVariant.Hnefatafl | GameVariant.Tawlbwrdd, Player.Black) => checkBlackBigBoardVictory()
      case (GameVariant.Brandubh | GameVariant.Tablut , Player.Black) => checkBlackSmallBoardVictory()
      case (_, Player.White) => gameSnapshot.getBoard.cornerCoordinates.contains(move.to)
      case _ => false
    }

    def checkDraw(): Boolean = {
      gamePossibleMoves(GameSnapshot(gameSnapshot.getVariant, switchPlayer(), Player.None, gameSnapshot.getBoard, Option(move), 0, 0)).isEmpty
    }


    def checkBlackBigBoardVictory(): Boolean = {
      val adjacentCells = gameSnapshot.getBoard.orthogonalCells(findKing(gameSnapshot.getBoard))

      fourOrThreeSideCondition(adjacentCells)
    }

    def checkBlackSmallBoardVictory(): Boolean = {
      val kingCoord = findKing(gameSnapshot.getBoard)
      val adjacentCells = gameSnapshot.getBoard.orthogonalCells(kingCoord)

      checkKingCapturedSmallBoard(kingCoord, adjacentCells)
    }

    def checkKingCapturedSmallBoard(kingCoord: Coordinate, adjacentCells: Map[OrthogonalDirection, List[BoardCell]]): Boolean = {
      if(kingOnThrone(gameSnapshot, kingCoord) || kingNextToThrone(gameSnapshot, kingCoord))
        fourOrThreeSideCondition(adjacentCells)
      else
        kingCapturedTwoSides(adjacentCells)
    }

    def fourOrThreeSideCondition(adjacentCells: Map[OrthogonalDirection, List[BoardCell]]): Boolean =
      adjacentCells.values.filter(_.nonEmpty).map(_.head).count(cell => cell.getPiece.equals(Piece.BlackPawn) ||
        cell.getCoordinate.equals(gameSnapshot.getBoard.centerCoordinates)) == 4

    def kingCapturedTwoSides(adjacentCells: Map[OrthogonalDirection, List[BoardCell]]): Boolean =
      (adjacentCells(OrthogonalDirection.Right).nonEmpty &&
        adjacentCells(OrthogonalDirection.Left).nonEmpty &&
        kingCapturedInLine(adjacentCells(OrthogonalDirection.Right).head, adjacentCells(OrthogonalDirection.Left).head)) ||
        (adjacentCells(OrthogonalDirection.Up).nonEmpty &&
          adjacentCells(OrthogonalDirection.Down).nonEmpty &&
          kingCapturedInLine(adjacentCells(OrthogonalDirection.Up).head, adjacentCells(OrthogonalDirection.Down).head))

    def kingCapturedInLine(lineCells1: BoardCell, lineCells2: BoardCell): Boolean =
      (move.to.equals(lineCells1.getCoordinate) || move.to.equals(lineCells2.getCoordinate)) &&
      lineCells1.getPiece.equals(Piece.BlackPawn) && lineCells2.getPiece.equals(Piece.BlackPawn)


    _move()

    val listCapturesCoordinate = gameSnapshot.getBoard.orthogonalCells(move.to).values.flatMap(l => _checkCaptures(l.take(2)))
    listCapturesCoordinate.foreach(c => gameSnapshot.getBoard.setCell(BoardCell(c, Piece.Empty)))
    val capturedPieces = _incrementCapturedPieces(listCapturesCoordinate.size)

    GameSnapshot(gameSnapshot.getVariant, switchPlayer(), checkWinner(), gameSnapshot.getBoard, Option(move), capturedPieces._1, capturedPieces._2)
  }

  def findKing(board: Board):Coordinate = {
    board.rows.flatten.filter(_.getPiece.equals(Piece.WhiteKing)).head.getCoordinate
  }

  def kingOnThrone(gameSnapshot: GameSnapshot, kingCoord: Coordinate): Boolean =
    kingCoord.equals(gameSnapshot.getBoard.centerCoordinates)

  def kingNextToThrone(gameSnapshot: GameSnapshot, kingCoord: Coordinate): Boolean =
    gameSnapshot.getBoard.orthogonalCells(gameSnapshot.getBoard.centerCoordinates).values.map(_.head.getCoordinate)
      .toList.contains(kingCoord)

  private def isOwner(pawn: Piece.Value, player: Player.Value): Boolean = (pawn, player) match {
    case (Piece.WhitePawn, Player.White) => true
    case (Piece.WhiteKing, Player.White) => true
    case (Piece.BlackPawn, Player.Black) => true
    case _ => false
  }

}