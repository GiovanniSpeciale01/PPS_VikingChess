package ia

import actor_ia.{MoveGenerator, ScoreProvider}
import model._
import utils.BoardGame.OrthogonalDirection.OrthogonalDirection
import utils.BoardGame.{Board, BoardCell, OrthogonalDirection}
import utils.{Coordinate, Move}

import scala.collection.mutable.ListBuffer

object EvaluationFunction {

  private var boardSize: Int = _
  private var kingCoord: Coordinate = _
  private var cornerCoordinates: List[Coordinate] = _
  private var centralCoordinate: Coordinate = _
  private var kingOrthogonalCells: Map[OrthogonalDirection, List[BoardCell]] = _
  private var kingAdjacentCells: Map[OrthogonalDirection, Option[BoardCell]] = _
  private var blackCoords: Seq[Coordinate] = _
  private var whiteCoords: Seq[Coordinate] = _
  private var gamePossibleMoves: Seq[Move] = _
  private var board: Board = _

  def score(snapshot: GameSnapshot): Int = snapshot.getWinner match {
    case Player.Black => -ScoreProvider.BlackWinScore
    case Player.White => ScoreProvider.WhiteWinScore
    case Player.Draw => ScoreProvider.DrawScore
    case Player.None => computeScore(snapshot)
  }

  def usefulValues(gameSnapshot: GameSnapshot): Unit = {
    board = gameSnapshot.getBoard
    boardSize = board.size
    kingCoord = MoveGenerator.findKing(board)

    kingOrthogonalCells = board.orthogonalCells(kingCoord)

    kingAdjacentCells = kingOrthogonalCells.map{case (k,v) => (k, v.take(1))}
      .map{case (k,v) => if(v.nonEmpty) (k,Option(v.head)) else (k,Option.empty)}

    cornerCoordinates = board.cornerCoordinates
    centralCoordinate = board.centerCoordinates
    //quadrants = splitMatrixInFourPart(board.rows)
    blackCoords = board.rows.flatten.filter(_.getPiece.equals(Piece.BlackPawn)).map(_.getCoordinate).toList
    whiteCoords = board.rows.flatten.filter(c => c.getPiece.equals(Piece.WhitePawn) || c.getPiece.equals(Piece.WhiteKing))
      .map(_.getCoordinate).toList
    gamePossibleMoves = MoveGenerator.gamePossibleMoves(gameSnapshot)
  }

  def computeScore(gameSnapshot: GameSnapshot): Int = {
    usefulValues(gameSnapshot)
    var score: Int = 0
    if(kingAdjacentToCorner()) {
      score += ScoreProvider.KingNearCornerScore
    }
    else
      if(kingToCornerInOne()){
        score += ScoreProvider.KingToCornerScore
    }
      if(kingCapturedInOne(gameSnapshot)) {
        score -= ScoreProvider.KingCatchableInOne
      }
      if(score == 0) {
        val scoreRowOrColumnFree = scoreOwnerFirstLastThreeRowsOrColumns()
        score -= computeBlackBetterPositions(gameSnapshot) + scoreRowOrColumnFree._2
        score += computeWhiteBetterPositions(gameSnapshot) + scoreRowOrColumnFree._1
    }
    score
  }

  def kingAdjacentToCorner(): Boolean = kingAdjacentCells.values.filter(_.nonEmpty).map(_.get.getCoordinate).exists(cornerCoordinates.contains(_))

  def kingToCornerInOne(): Boolean = {
    var returnValue = false
    for(i <- kingOrthogonalCells.keys)
      if (kingOrthogonalCells(i).nonEmpty &&
        cornerCoordinates.contains(kingOrthogonalCells(i).last.getCoordinate) &&
        (kingOrthogonalCells(i).count(_.getPiece.equals(Piece.Empty)) == kingOrthogonalCells(i).size))
          returnValue = true
    returnValue
  }

  def kingCapturedInOne(gameSnapshot: GameSnapshot): Boolean = gameSnapshot.getVariant match {
    case GameVariant.Hnefatafl | GameVariant.Tawlbwrdd => kingCapturedInOneBigBoard()
    case GameVariant.Brandubh | GameVariant.Tablut => kingCapturedInOneSmallBoard(gameSnapshot)
  }

  def kingCapturedInOneBigBoard(): Boolean = {
    val nonWhiteKingAdjacentCells = kingAdjacentCells.values.filter(_.nonEmpty).filter(!_.get.getPiece.equals(Piece.WhitePawn))
    if(nonWhiteKingAdjacentCells.size == 4) {
      val emptyKingAdjacentCells = nonWhiteKingAdjacentCells.filter(coord => coord.get.getPiece.equals(Piece.Empty) &&
        !coord.get.getCoordinate.equals(centralCoordinate))
      if (emptyKingAdjacentCells.size == 1) {
        gamePossibleMoves.map(_.to).contains(emptyKingAdjacentCells.head.get.getCoordinate)
      } else
        false
    } else
        false
  }

  def kingCapturedInOneSmallBoard(gameSnapshot: GameSnapshot): Boolean = {
    var returnValue = false
    if (MoveGenerator.kingOnThrone(gameSnapshot, kingCoord) || MoveGenerator.kingNextToThrone(gameSnapshot, kingCoord)) {
      returnValue = kingCapturedInOneBigBoard()
    }
    else
      for(i <- kingAdjacentCells.keys)
        if(kingAdjacentCells(i).nonEmpty &&
          kingAdjacentCells(i).get.getPiece.equals(Piece.BlackPawn) &&
          kingAdjacentCells(oppositeOrthogonalDirection(i)).nonEmpty &&
          gamePossibleMoves.map(_.to).contains(kingAdjacentCells(oppositeOrthogonalDirection(i)).get.getCoordinate))
            returnValue = true
    returnValue
  }

  private def oppositeOrthogonalDirection(orthogonalDirection: OrthogonalDirection): OrthogonalDirection = orthogonalDirection match {
    case OrthogonalDirection.Up => OrthogonalDirection.Down
    case OrthogonalDirection.Right => OrthogonalDirection.Left
    case OrthogonalDirection.Down => OrthogonalDirection.Up
    case OrthogonalDirection.Left => OrthogonalDirection.Right
  }

  def scoreKingOnThrone(gameSnapshot: GameSnapshot): Int = {
    if(kingCoord.equals(gameSnapshot.getBoard.centerCoordinates)) ScoreProvider.KingOnThroneScore
    else if (quadraticDistanceBetweenCells(kingCoord, findCloserCorner(kingCoord)) == 0) ScoreProvider.KingDistanceToCornerDividend
    else ScoreProvider.KingDistanceToCornerDividend / quadraticDistanceBetweenCells(kingCoord, findCloserCorner(kingCoord))
  }

  def scoreTower(gameSnapshot: GameSnapshot): Int = {
    val board = gameSnapshot.getBoard

    def isSquare(list: List[BoardCell]):Boolean = {
      list.count(cell => cell.getPiece.equals(Piece.WhitePawn) || cell.getPiece.equals(Piece.WhiteKing)) == 3
    }

    var score: Double = 0

    for {
      coord <- whiteCoords
      i = coord.x
      j = coord.y
      if i < boardSize && j < boardSize
      if isSquare(List(board.getCell(Coordinate(i, j+1)),
        board.getCell(Coordinate(i+1, j)),
        board.getCell(Coordinate(i+1, j+1))))
    } yield score += ScoreProvider.TowerCoefficient * quadraticDistanceBetweenCells(coord, centralCoordinate)

    score.toInt
  }


  def computeWhiteBetterPositions(gameSnapshot: GameSnapshot): Int = {
    scoreKingOnThrone(gameSnapshot) +
    scoreTower(gameSnapshot) +
    scoreKingIsInFreeRowOrColumn() +
    scoreCapturedBlack(gameSnapshot)
  }

  def computeBlackBetterPositions(gameSnapshot: GameSnapshot): Int = {

    scoreBlackSurroundTheKing() +
    scoreCapturedWhite(gameSnapshot) +
    + scoreBlackCordon(board)

  }

  /**
  * White score
  */

  def scoreCapturedBlack(gameSnapshot: GameSnapshot):Int = gameSnapshot.getNumberCapturedBlacks * ScoreProvider.BlackCapturedScore

  // Positive score if king is in free row or column

  def scoreKingIsInFreeRowOrColumn(): Int = {
    val rowWhithoutKing = kingOrthogonalCells(OrthogonalDirection.Right) ++ kingOrthogonalCells(OrthogonalDirection.Left)
    val columnWhithoutKing = kingOrthogonalCells(OrthogonalDirection.Up) ++ kingOrthogonalCells(OrthogonalDirection.Down)

    def _scoreKingIsInFreeRowOrColumn(): Int = (rowWhithoutKing, columnWhithoutKing) match {
      case (row, column) if isSequenceFreeCells(row) && isSequenceFreeCells(column) => 50
      case (row, _) if isSequenceFreeCells(row) => 25
      case (_, column) if isSequenceFreeCells(column) => 25
      case _ => 0
    }

    _scoreKingIsInFreeRowOrColumn()
  }

  /**
  * Black score
  */
  def scoreCapturedWhite(gameSnapshot: GameSnapshot): Int = gameSnapshot.getNumberCapturedWhites * ScoreProvider.WhiteCapturedScore

  //Positive score if the black pieces surround The King
  def scoreBlackSurroundTheKing(): Int = {
    kingAdjacentCells.values.count(c => c.nonEmpty && c.get.getPiece.equals(Piece.BlackPawn)) * ScoreProvider.BlackNearKing
  }

  def scoreBlackCordon(board: Board):Int = {
    val boardSize = board.size
    val blackCoords = board.rows.flatten.filter(_.getPiece.equals(Piece.BlackPawn)).map(_.getCoordinate).toList

    def getOtherSides(coordinate: Coordinate): List[Coordinate] = coordinate match {
      case Coordinate(x,_) if x == boardSize => List(Coordinate(-1,x), Coordinate(1,-1), Coordinate(-1,1))
      case Coordinate(_,x) if x == boardSize => List(Coordinate(x,-1), Coordinate(1,-1), Coordinate(-1,1))
      case Coordinate(1,_) => List(Coordinate(-1,1) , Coordinate(boardSize,-1), Coordinate(-1,boardSize))
      case Coordinate(_,1) => List(Coordinate(1,-1) , Coordinate(boardSize,-1), Coordinate(-1,boardSize))
      case _ => List(Coordinate(1,-1) , Coordinate(-1,1), Coordinate(boardSize,-1), Coordinate(-1,boardSize))
    }

    def getCurrentSide(coordinate: Coordinate):Coordinate = coordinate match {
      case Coordinate(x,_) if x == boardSize => Coordinate(x,-1)
      case Coordinate(_,x) if x == boardSize => Coordinate(-1,x)
      case Coordinate(1,_) => Coordinate(1,-1)
      case _ => Coordinate(-1,1)
    }

    def isOnAnySides(currentCoord:Coordinate, otherSideCoord: List[Coordinate]): Boolean = otherSideCoord.exists(isOnSide(currentCoord, _))

    def isOnSide(coord:Coordinate, startSideCoord: Coordinate ): Boolean = coord.x == startSideCoord.x || coord.y == startSideCoord.y

    def findNearBlack(c: Coordinate):List[Coordinate] = {
      val nearCord = List(Coordinate(c.x + 1, c.y),Coordinate(c.x, c.y + 1),
        Coordinate(c.x -1 , c.y),Coordinate(c.x, c.y - 1),
        Coordinate(c.x - 1, c.y - 1 ),Coordinate(c.x +1, c.y + 1),
        Coordinate(c.x - 1, c.y + 1 ),Coordinate(c.x +1, c.y - 1))
        .filter(coord => coord.x <= boardSize && coord.x >= 1 &&
          coord.y <= boardSize && coord.y >= 1)

      nearCord.map(board.getCell).filter(_.getPiece.equals(Piece.BlackPawn)).map(_.getCoordinate)
    }

    var endingCordonCoords: ListBuffer[Coordinate] = ListBuffer.empty

    def countPawnInCordon2( nearPawnsCoords: List[Coordinate], startCoord: Coordinate, otherSideCoord: List[Coordinate], step: Int, count:Int ): Int = nearPawnsCoords match {
      case Nil => count
      case h :: _ if step > 3 && h.equals(startCoord) => endingCordonCoords += h ;  count
      case h :: _ if isOnAnySides(h, otherSideCoord) => endingCordonCoords += h ;  count + 1
      case h :: t if  isOnSide(h, getCurrentSide(startCoord) ) || endingCordonCoords.contains(h) =>
        endingCordonCoords += h
        countPawnInCordon2(t, startCoord, otherSideCoord, step, count);
      case h :: t => endingCordonCoords += h ;
        countPawnInCordon2(t, startCoord, otherSideCoord, step, count + countPawnInCordon2(findNearBlack(h), startCoord, otherSideCoord, step + 1, count))
    }

    val blacksToCordon = blackCoords.map(coord =>  countPawnInCordon2( findNearBlack(coord), coord, getOtherSides(coord),0, 1   )).filter(_>2).sum

    blacksToCordon * ScoreProvider.CordonPawn
  }



  /*
  * Jolly rules
  */

  def scoreOwnerFirstLastThreeRowsOrColumns(): (Int, Int) = {
    val boardTranspose =  board.rows.toList.transpose
    val listRowsAndColumns: Seq[Seq[BoardCell]] = board.rows.take(3).toList ::: board.rows.takeRight(3).toList ::: boardTranspose.take(3) ::: boardTranspose.takeRight(3)
    var whiteScore = 0
    var blackScore = 0

   listRowsAndColumns.foreach(
      isRowOrColumnOwner(_) match {
        case Piece.WhiteKing => whiteScore += ScoreProvider.KingRowOrColumnOwnerScore
        case Piece.WhitePawn => whiteScore += ScoreProvider.WhiteRowOrColumnOwnerScore
        case Piece.BlackPawn => blackScore += ScoreProvider.BlackRowOrColumnOwnerScore
        case _ =>
      }
   )
    (whiteScore, blackScore)
  }


  /* UTILS METHODS */


  def isRowOrColumnOwner(cellsSeq: Seq[BoardCell]): Piece.Value = {
    val nonEmptyCells = cellsSeq.filter(!_.getPiece.equals(Piece.Empty))
    if((nonEmptyCells.size == 1) && nonEmptyCells.head.getPiece.equals(Piece.WhiteKing))
      Piece.WhiteKing
    else if(samePlayerPawns(nonEmptyCells, Piece.WhitePawn))
      Piece.WhitePawn
    else if(samePlayerPawns(nonEmptyCells, Piece.BlackPawn))
      Piece.BlackPawn
    else
      Piece.Empty
  }

  private def samePlayerPawns(l: Seq[BoardCell], p: Piece.Val): Boolean =
    l.nonEmpty && l.size.equals(l.count(_.getPiece.equals(p)))

  def isItDistantFromCornerOf(coord: Coordinate, distance: Int): Boolean = {

    @scala.annotation.tailrec
    def _isItDistantFromTheCornerOf(coord: Coordinate, distance: Int, cornerCoord: List[Coordinate]): Boolean = cornerCoord match {
      case Nil => false
      case h :: _ if quadraticDistanceBetweenCells(h, coord) == distance => true
      case _ :: t => _isItDistantFromTheCornerOf(coord, distance, t)
    }

    _isItDistantFromTheCornerOf(coord, distance, cornerCoordinates)
  }

  def quadraticDistanceBetweenCells(start: Coordinate, end: Coordinate): Int = (scala.math.pow(start.x - end.x, 2) + scala.math.pow(start.y - end.y, 2)).toInt

  def findCloserCorner(coord: Coordinate): Coordinate = {
    def _getCloserCorner( closerCornerCord: Coordinate, closerDist: Int, cells: Seq[Coordinate] = cornerCoordinates ): Coordinate = cells match {
      case Nil => closerCornerCord
      case h :: t =>
        val distCoordFromCorner = quadraticDistanceBetweenCells(h, coord)
        val (newCloserCorner, newCloserDist) = if (distCoordFromCorner > closerDist) (closerCornerCord, closerDist) else (h, distCoordFromCorner)
        _getCloserCorner( newCloserCorner, newCloserDist,t)
    }

    _getCloserCorner(Coordinate(1,1), Int.MaxValue)
  }

  def isSequenceFreeCells(seq: Seq[BoardCell]): Boolean =
    seq.count(boardCell => !boardCell.getPiece.equals(Piece.Empty)) == 0
}
