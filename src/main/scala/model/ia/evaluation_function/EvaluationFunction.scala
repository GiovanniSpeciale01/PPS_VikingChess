package model.ia.evaluation_function

import model.game.BoardGame.{Board, BoardCell}
import model.game.Level.Level
import model.game.OrthogonalDirection.OrthogonalDirection
import model.game.Piece.Piece
import model.game.{Coordinate, GameSnapshot, GameVariant, Level, Move, MoveGenerator, OrthogonalDirection, Piece, Player}

import scala.collection.immutable.HashMap

object EvaluationFunction {
  val MIN_CORDON_SIZE = 3
  val MIN_INNER_WHITE_IN_CORDON = 4
  val TOWER_SIZE = 4
}

/**
 * A viking chess evaluation function implementation with different IA levels.
 * A negative score means black is better while a positive one means white is better.
 *
 * @param gameSnapshot
 *        game snapshot to evaluate
 */
case class EvaluationFunction(gameSnapshot: GameSnapshot) extends EvaluationFunctionTrait {

  private val board: Board = gameSnapshot.getBoard
  private val boardSize: Int = board.size
  private val kingCoordinate: Coordinate = MoveGenerator.findKing(board)
  private val cornerCoordinates: List[Coordinate] = board.cornerCoordinates
  private val centralCoordinate: Coordinate = board.centerCoordinates
  private val kingOrthogonalCells: Map[OrthogonalDirection, List[BoardCell]] = board.orthogonalCells(kingCoordinate)
  private val kingAdjacentCells: Map[OrthogonalDirection, Option[BoardCell]] = board.adjacentCells(kingCoordinate)
  private val blackCoordinates: Seq[Coordinate] = board.rows.flatten
    .filter(_.getPiece.equals(Piece.BlackPawn)).map(_.getCoordinate).toList
  private val whiteCoordinates: Seq[Coordinate] = board.rows.flatten
    .filter(c => c.getPiece.equals(Piece.WhitePawn) || c.getPiece.equals(Piece.WhiteKing))
    .map(_.getCoordinate).toList
  private val gamePossibleMoves: Seq[Move] = MoveGenerator.gamePossibleMoves(gameSnapshot)

  /**
   * @inheritdoc
   */
  override def score(levelIA: Level): Int = gameSnapshot.getWinner match {
    case Player.Black => -ScoreProvider.BlackWin
    case Player.White => ScoreProvider.WhiteWin
    case Player.Draw => ScoreProvider.Draw
    case _ => computeScore(levelIA)
  }

  private def computeScore(levelIA: Level): Int = levelIA match {
    case Level.Newcomer => computeNewcomerScore
    case Level.Standard => computeStandardScore
    case _ => computeAdvancedScore
  }

  private def computeNewcomerScore: Int = {
    var newcomerScore: Int = 0
    if (kingAdjacentToCorner)
      newcomerScore = ScoreProvider.KingEscapeToCorner
    else {
      if (kingToCornerInOne && gameSnapshot.getPlayerToMove.equals(Player.White))
        newcomerScore = ScoreProvider.KingEscapeToCorner
      else if (kingNearCornerInOne && gameSnapshot.getPlayerToMove.equals(Player.White))
        newcomerScore = ScoreProvider.KingEscapeNearCorner

      if (kingCapturedInOne && gameSnapshot.getPlayerToMove.equals(Player.Black))
        newcomerScore = -ScoreProvider.KingCatchableInOne

      if (newcomerScore == 0) {
        newcomerScore -= scoreBlackSurroundTheKing +
          scoreBlackOnKingDiagonal +
          scoreCapturedWhite
        newcomerScore += scoreKingOnThrone +
          scoreKingIsInFreeRowOrColumn +
          scoreCapturedBlack
      }
    }
    newcomerScore
  }

  private def computeStandardScore: Int = {
    var standardScore: Int = computeNewcomerScore
    if (standardScore == 0 && standardScore.abs != ScoreProvider.PossibleWinInOne && standardScore.abs != ScoreProvider.PossibleWinInTwo) {
      val scoreErroneousBarricade: FactionsScore = scoreWrongBarricade
      standardScore -= scoreBlackCordon - scoreErroneousBarricade.blackScore
      standardScore += scoreMovesKing + scoreTower - scoreErroneousBarricade.whiteScore
    }
    standardScore
  }

  private def computeAdvancedScore: Int = {
    var advancedScore: Int = computeStandardScore
    if (advancedScore == 0 && advancedScore.abs != ScoreProvider.PossibleWinInOne && advancedScore.abs != ScoreProvider.PossibleWinInTwo) {
      val scoreRowOrColumnFree: FactionsScore = scoreOwnerFirstLastThreeRowsOrColumns
      advancedScore -= scoreLastPawnMovedCatchableInOne + scoreRowOrColumnFree.blackScore
      advancedScore += scoreLastPawnMovedCatchableInOne + scoreRowOrColumnFree.whiteScore
    }
    advancedScore
  }

  /**
   * @inheritdoc
   */
  override def kingAdjacentToCorner: Boolean = kingAdjacentCells.values
    .filter(_.nonEmpty)
    .map(_.get.getCoordinate).exists(cornerCoordinates.contains(_))

  /**
   * @inheritdoc
   */
  override def kingNearCornerInOne: Boolean = kingCanMoveTo(getNearCornerCells)

  /**
   * @inheritdoc
   */
  override def kingToCornerInOne: Boolean = kingCanMoveTo(cornerCoordinates)

  /**
   * @inheritdoc
   */
  override def kingCapturedInOne: Boolean = gameSnapshot.getVariant match {
    case GameVariant.Hnefatafl | GameVariant.Tawlbwrdd => kingCapturedInOneBigBoard
    case _ => kingCapturedInOneSmallBoard
  }

  private def scoreOwnerFirstLastThreeRowsOrColumns: FactionsScore = {
    var whiteScore = 0
    var blackScore = 0
    val boardTranspose = board.rows.toList.transpose
    val listRowsAndColumns: Seq[Seq[BoardCell]] = board.rows
      .take(3).toList ::: board.rows.takeRight(3).toList ::: boardTranspose.take(3) ::: boardTranspose.takeRight(3)

    listRowsAndColumns.foreach(
      rowOrColumnOwner(_) match {
        case Piece.WhiteKing => whiteScore += ScoreProvider.FirstLastRowOrColumnOwnedByKing
        case Piece.WhitePawn => whiteScore += ScoreProvider.FirstLastRowOrColumnOwnedByWhite
        case Piece.BlackPawn => blackScore += ScoreProvider.FirstLastRowOrColumnOwnedByBlack
        case _ =>
      }
    )
    FactionsScore(whiteScore, blackScore)
  }

  /**
   * @inheritdoc
   */
  override def scoreWrongBarricade: FactionsScore = {
    var whiteScore = 0
    var blackScore = 0
    board.cornerCoordinates.flatMap(c => board.adjacentCoordinates(c)).filter(p => p._2 != Option.empty).grouped(2).foreach {
      case h :: t =>
        val cells = (board.getCell(h._2.get), board.getCell(t.head._2.get))
        blackScore += wrongBarricadeType(Piece.BlackPawn, cells)
        whiteScore += wrongBarricadeType(Piece.WhitePawn, cells)
      case _ =>
    }
    FactionsScore(whiteScore, blackScore)
  }

  /**
   * @inheritdoc
   */
  override def scoreLastPawnMovedCatchableInOne: Int = {
    val lastMove = gameSnapshot.getLastMove

    if (lastMove.nonEmpty) {
      if(pawnCatchableInOne(lastMove.get.to))
      if (gameSnapshot.getPlayerToMove.equals(Player.Black))
        return ScoreProvider.LastWhiteMovedCatchableInOne
      else if (gameSnapshot.getPlayerToMove.equals(Player.White))
        return ScoreProvider.LastBlackMovedCatchableInOne
    }
    0
  }

  /*
    WHITE SCORES
   */

  /**
   * @inheritdoc
   */
  override def scoreKingOnThrone: Int = {
    if (kingCoordinate.equals(board.centerCoordinates)) ScoreProvider.KingOnThrone
    else if (quadraticDistanceBetweenCells(kingCoordinate, findCloserCorner(kingCoordinate)) == 0)
      ScoreProvider.KingDistanceToCornerDividend
    else
      ScoreProvider.KingDistanceToCornerDividend / quadraticDistanceBetweenCells(kingCoordinate, findCloserCorner(kingCoordinate))
  }

  /**
   * @inheritdoc
   */
  override def scoreTower: Int = {
    def _isSquare(list: List[BoardCell]): Boolean =
      list.count(cell => cell.getPiece.equals(Piece.WhitePawn) || cell.getPiece.equals(Piece.WhiteKing)) ==
        EvaluationFunction.TOWER_SIZE - 1

    {
      for {
        coordinate <- whiteCoordinates
        i = coordinate.x
        j = coordinate.y
        if i < boardSize && j < boardSize
        if _isSquare(List(board.getCell(Coordinate(i, j + 1)),
          board.getCell(Coordinate(i + 1, j)),
          board.getCell(Coordinate(i + 1, j + 1))))
      } yield ScoreProvider.TowerCoefficient * quadraticDistanceBetweenCells(coordinate, centralCoordinate)
    }.sum.toInt
  }

  /**
   * @inheritdoc
   */
  override def scoreCapturedBlack: Int = gameSnapshot.getNumberCapturedBlacks * ScoreProvider.BlackCaptured

  /**
   * @inheritdoc
   */
  override def scoreKingIsInFreeRowOrColumn: Int = {
    val rowWithoutKing = kingOrthogonalCells(OrthogonalDirection.Right) ++ kingOrthogonalCells(OrthogonalDirection.Left)
    val columnWithoutKing = kingOrthogonalCells(OrthogonalDirection.Up) ++ kingOrthogonalCells(OrthogonalDirection.Down)

    def _scoreKingIsInFreeRowOrColumn: Int = (rowWithoutKing, columnWithoutKing) match {
      case (row, column) if isSequenceOfEmptyCells(row) && isSequenceOfEmptyCells(column) => ScoreProvider.RowAndColumnOwnedKing
      case (row, _) if isSequenceOfEmptyCells(row) => ScoreProvider.RowOrColumnOwnedKing
      case (_, column) if isSequenceOfEmptyCells(column) => ScoreProvider.RowOrColumnOwnedKing
      case _ => 0
    }

    _scoreKingIsInFreeRowOrColumn
  }

  /*
   * BLACK SCORES
   */

  /**
   * @inheritdoc
   */
  override def scoreCapturedWhite: Int = gameSnapshot.getNumberCapturedWhites * ScoreProvider.WhiteCaptured

  /**
   * @inheritdoc
   */
  override def scoreBlackSurroundTheKing: Int = {
    kingAdjacentCells.values.count(c => c.nonEmpty &&
      c.get.getPiece.equals(Piece.BlackPawn) &&
      !pawnCatchableInOne(c.get.getCoordinate)) * ScoreProvider.BlackNearKing
  }

  /**
   * @inheritdoc
   */
  override def scoreBlackOnKingDiagonal: Int =
    findNearBlacks(kingCoordinate).filter(c => (c.x != kingCoordinate.x) && (c.y != kingCoordinate.y))
      .count(board.getCell(_).getPiece.equals(Piece.BlackPawn)) * ScoreProvider.BlackOnDiagonalKing

  /**
   * @inheritdoc
   */
  override def scoreBlackCordon: Int = blackCoordinates.map(detectCordon(_, Seq.empty)).distinct
    .filter(_.size >= EvaluationFunction.MIN_CORDON_SIZE).foldLeft(0)(_ + scoreCorrectCordon(_))

  /**
   * @inheritdoc
   */
  override def scoreMovesKing: Int =
    MoveGenerator.cellPossibleMoves(BoardCell(kingCoordinate, Piece.WhiteKing), gameSnapshot).size * ScoreProvider.PossibleKingMove

  /**
   * @inheritdoc
   */
  override def toString: String = {
    val wrongBarricade = scoreWrongBarricade
    val rowsAndColumnOwner = scoreOwnerFirstLastThreeRowsOrColumns
    "Total Newcomer Score: " + score(Level.Newcomer) + "\n" +
      "Total Standard Score: " + score(Level.Standard) + "\n" +
      "Total Advanced Score: " + score(Level.Advanced) + "\n" +
      "King Adjacent To Corner: " + kingAdjacentToCorner + "\n" +
      "King To Corner: " + kingToCornerInOne + "\n" +
      "King Near Corner: " + kingNearCornerInOne + "\n" +
      "King Captured: " + kingCapturedInOne + "\n" +
      "Black Surround King: " + scoreBlackSurroundTheKing + "\n" +
      "Blacks On King's Diagonal: " + scoreBlackOnKingDiagonal + "\n" +
      "Captured Whites: " + scoreCapturedWhite + "\n" +
      "Captured Blacks: " + scoreCapturedBlack + "\n" +
      "King On Throne: " + scoreKingOnThrone + "\n" +
      "King In Free Row Column: " + scoreKingIsInFreeRowOrColumn + "\n" +
      "Black Cordons: " + scoreBlackCordon + "\n" +
      "White Towers : " + scoreTower + "\n" +
      "Possible Moves King : " + scoreMovesKing + "\n" +
      "Wrong Black Barricades: " + wrongBarricade.blackScore + "\n" +
      "Wrong White Barricades: " + wrongBarricade.whiteScore + "\n" +
      "Captured Last Pawn Moved: " + scoreLastPawnMovedCatchableInOne + "\n" +
      "Rows And Columns Owned Black: " + rowsAndColumnOwner.blackScore + "\n" +
      "Rows And Columns Owned White: " + rowsAndColumnOwner.whiteScore
  }

  private def kingCanMoveTo(listCoordinates: List[Coordinate]): Boolean =
    MoveGenerator.cellPossibleMoves(BoardCell(kingCoordinate, Piece.WhiteKing), gameSnapshot).map(_.to).exists(listCoordinates.contains)

  private def wrongBarricadeType(color: Piece, cells: (BoardCell, BoardCell)): Int =
    (cells._1.getPiece, cells._2.getPiece) match {
      case (`color`, `color`) => ScoreProvider.WrongBarricade * 2
      case (`color`, _) | (_, `color`) => ScoreProvider.WrongBarricade
      case _ => 0
    }

  private def getNearCornerCells: List[Coordinate] =
    board.cornerCoordinates.flatMap(board.orthogonalCells).map(_._2).filter(_.nonEmpty).map(_.head).map(_.getCoordinate)

  private def detectCordon(fromCoordinate: Coordinate, cordon: Seq[Coordinate]): Seq[Coordinate] = {
    def _detectCordon(fromCoordinate: Coordinate, cordon: Seq[Coordinate]): Seq[Coordinate] = {
      @annotation.tailrec
      def _explore(coordinates: Seq[Coordinate], cordon: Seq[Coordinate]): Seq[Coordinate] =
        coordinates match {
          case Seq() => cordon
          case _ =>
            val nears = coordinates.flatMap(c => findNearBlacks(c).filter(!cordon.contains(_)))
            _explore(nears, (cordon ++ nears).distinct)
        }

      _explore(Seq(fromCoordinate), cordon)
    }

    val result = _detectCordon(fromCoordinate, cordon)
    result.filter(c => !isRedundantInCordon(c, result)).sorted
  }

  private def isRedundantInCordon(coordinate: Coordinate, cordon: Seq[Coordinate]): Boolean = {
    val cycles = get3CycleCoordinates(coordinate)
    for (d <- OrthogonalDirection.values) {
      if (cycles(d).size == 2 && cycles(d).forall(cordon.contains))
        return true
    }
    false
  }

  private def get3CycleCoordinates(c: Coordinate): Map[OrthogonalDirection, List[Coordinate]] =
    HashMap(
      OrthogonalDirection.Up ->
        List(board.adjacentCoordinates(c)(OrthogonalDirection.Left),
          board.adjacentCoordinates(c)(OrthogonalDirection.Up)).filter(_.nonEmpty).map(_.get),
      OrthogonalDirection.Right ->
        List(board.adjacentCoordinates(c)(OrthogonalDirection.Up),
          board.adjacentCoordinates(c)(OrthogonalDirection.Right)).filter(_.nonEmpty).map(_.get),
      OrthogonalDirection.Down ->
        List(board.adjacentCoordinates(c)(OrthogonalDirection.Right),
          board.adjacentCoordinates(c)(OrthogonalDirection.Down)).filter(_.nonEmpty).map(_.get),
      OrthogonalDirection.Left ->
        List(board.adjacentCoordinates(c)(OrthogonalDirection.Down),
          board.adjacentCoordinates(c)(OrthogonalDirection.Left)).filter(_.nonEmpty).map(_.get)
    )

  private def findNearBlacks(c: Coordinate): Seq[Coordinate] = {
    List(Coordinate(c.x + 1, c.y), Coordinate(c.x, c.y + 1),
      Coordinate(c.x - 1, c.y), Coordinate(c.x, c.y - 1),
      Coordinate(c.x - 1, c.y - 1), Coordinate(c.x + 1, c.y + 1),
      Coordinate(c.x - 1, c.y + 1), Coordinate(c.x + 1, c.y - 1))
      .filter(c => c.x <= boardSize && c.x >= 1 && c.y <= boardSize && c.y >= 1)
      .map(board.getCell).filter(_.getPiece.equals(Piece.BlackPawn)).map(_.getCoordinate)
  }

  private def scoreCorrectCordon(cordon: Seq[Coordinate]): Int =
    if (isCircleCordon(cordon))
      scoreCircleCordon(cordon)
    else
      scoreNotCircleCordon(cordon)

  private def scoreCircleCordon(cordon: Seq[Coordinate]): Int = {
    val inn = splitBoardWithCircleCordon(cordon).inner
    if (isCorrectCircleCordon(inn))
      cordon.size * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon +
        inn.count(c => c.getPiece.equals(Piece.WhitePawn) || c.getPiece.equals(Piece.WhiteKing)) * ScoreProvider.WhiteInsideCordon
    else
      cordon.size * ScoreProvider.PawnInCordon - ScoreProvider.WrongCordon
  }

  private def scoreNotCircleCordon(cordon: Seq[Coordinate]): Int = {
    if (isCorrectNotCircleCordon(cordon)) {
      val sequences = splitCordon(cordon)

      if (controlEmptyPortion(sequences.inner) || controlEmptyPortion(sequences.outer)) {
        cordon.size * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon
      } else if (sequences.inner.map(_.getCoordinate).contains(board.centerCoordinates))
        outlierPenalty(sequences.inner, cordon)
      else
        outlierPenalty(sequences.outer, cordon)
    } else
      0
  }

  private def outlierPenalty(seq: Seq[BoardCell], cordon: Seq[Coordinate]): Int =
    (cordon.size * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon) - (seq.count(c => c.getPiece.equals(Piece.WhitePawn) ||
      c.getPiece.equals(Piece.WhiteKing)) * ScoreProvider.WhitePawnOuterCordon)

  private def isCorrectCircleCordon(innerCells: Seq[BoardCell]): Boolean = innerCells.count(_.getPiece.equals(Piece.Empty)) == 0 ||
    innerCells.count(_.getPiece.equals(Piece.WhitePawn)) >= EvaluationFunction.MIN_INNER_WHITE_IN_CORDON ||
    innerCells.exists(_.getPiece.equals(Piece.WhiteKing))

  private def isCorrectNotCircleCordon(cordon: Seq[Coordinate]): Boolean =
    cordon.filter(board.isOnAnySides(_)).map(board.getSide).filter(_.nonEmpty).map(_.get).distinct.size >= 2

  private def splitBoardWithCircleCordon(cordon: Seq[Coordinate]): SplitCordon = {
    @scala.annotation.tailrec
    def _splitBoardWithCircleCordon(cordon: Seq[Coordinate], output: Seq[(Seq[BoardCell], Seq[BoardCell])]): Seq[(Seq[BoardCell], Seq[BoardCell])] =
      cordon match {
        case Nil => output
        case h1 :: h2 :: t if h1.x == h2.x => _splitBoardWithCircleCordon(t, output :+ getRightAndLeft(h1, h2))
        case _ :: t => _splitBoardWithCircleCordon(t, output)
      }

    def getRightAndLeft(right: Coordinate, left: Coordinate): (Seq[BoardCell], Seq[BoardCell]) =
      (board.getSpecificOrthogonalCells(right, OrthogonalDirection.Right), board.getSpecificOrthogonalCells(left, OrthogonalDirection.Left))

    val inner = _splitBoardWithCircleCordon(cordon, Seq.empty).flatMap(elem => elem._1.intersect(elem._2)).toList
    val external = board.rows.flatten.diff(inner).filter(c => !cordon.contains(c.getCoordinate)).toList
    SplitCordon(inner, external)
  }

  private def splitBoardWithNotCircleCordon(cordon: Seq[Coordinate], outerOrthogonalDirection: OrthogonalDirection): SplitCordon = {
    val outerCells: Seq[BoardCell] = cordon.flatMap(board.getSpecificOrthogonalCells(_, outerOrthogonalDirection))
      .filter(c => !cordon.contains(c.getCoordinate)).distinct.toList
    val innerCells: Seq[BoardCell] = board.rows.flatten.diff(outerCells).filter(cell => !cordon.contains(cell.getCoordinate)).toList

    SplitCordon(innerCells, outerCells)
  }

  private def isCircleCordon(cordon: Seq[Coordinate]): Boolean =
    cordon.count(findNearBlacks(_).count(cordon.contains(_)) >= 2) == cordon.size

  private def getCordonSides(cordon: Seq[Coordinate]): Seq[OrthogonalDirection] =
    cordon.filter(board.isOnAnySides(_)).map(board.getSide).filter(_.nonEmpty).map(_.get).distinct

  private def splitCordon(cordon: Seq[Coordinate]): SplitCordon = {
    getCordonSides(cordon).sorted match {
      case OrthogonalDirection.Up :: OrthogonalDirection.Right :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Right)
      case OrthogonalDirection.Up :: OrthogonalDirection.Left :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Up)
      case OrthogonalDirection.Up :: OrthogonalDirection.Down :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Right)
      case OrthogonalDirection.Right :: OrthogonalDirection.Down :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Right)
      case OrthogonalDirection.Right :: OrthogonalDirection.Left :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Up)
      case OrthogonalDirection.Down :: OrthogonalDirection.Left :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Left)
      case OrthogonalDirection.Up :: OrthogonalDirection.Right :: OrthogonalDirection.Down :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Right)
      case OrthogonalDirection.Up :: OrthogonalDirection.Down :: OrthogonalDirection.Left :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Left)
      case OrthogonalDirection.Up :: OrthogonalDirection.Right :: OrthogonalDirection.Left :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Up)
      case OrthogonalDirection.Right :: OrthogonalDirection.Down :: OrthogonalDirection.Left :: Nil =>
        splitBoardWithNotCircleCordon(cordon, OrthogonalDirection.Down)
    }
  }

  private case class SplitCordon(inner: Seq[BoardCell], outer: Seq[BoardCell])

  private def kingCapturedInOneBigBoard: Boolean = {
    val nonWhiteKingAdjacentCells = kingAdjacentCells.values.filter(_.nonEmpty).filter(!_.get.getPiece.equals(Piece.WhitePawn))
    if (nonWhiteKingAdjacentCells.size == 4) {
      val emptyKingAdjacentCells = nonWhiteKingAdjacentCells.filter(c => c.get.getPiece.equals(Piece.Empty) &&
        !c.get.getCoordinate.equals(centralCoordinate))
      if (emptyKingAdjacentCells.size == 1) {
        gamePossibleMoves.map(_.to).contains(emptyKingAdjacentCells.head.get.getCoordinate)
      } else
        false
    } else
      false
  }

  private def kingCapturedInOneSmallBoard: Boolean = {
    if (MoveGenerator.kingOnThrone(gameSnapshot, kingCoordinate) || MoveGenerator.kingNextToThrone(gameSnapshot, kingCoordinate)) {
      return kingCapturedInOneBigBoard
    }
    else
      return pawnCatchableInOne(kingCoordinate)
    false
  }

  private def controlEmptyPortion(sequence: Seq[BoardCell]): Boolean =
    sequence.count(c => c.getPiece.equals(Piece.WhitePawn) || c.getPiece.equals(Piece.WhiteKing)) == 0

  private def rowOrColumnOwner(cellsSeq: Seq[BoardCell]): Piece = {
    val nonEmptyCells = cellsSeq.filter(!_.getPiece.equals(Piece.Empty))
    if ((nonEmptyCells.size == 1) && nonEmptyCells.head.getPiece.equals(Piece.WhiteKing))
      Piece.WhiteKing
    else if (samePlayerPawns(nonEmptyCells, Piece.WhitePawn))
      Piece.WhitePawn
    else if (samePlayerPawns(nonEmptyCells, Piece.BlackPawn))
      Piece.BlackPawn
    else
      Piece.Empty
  }

  private def pawnCatchableInOne(coordinate: Coordinate): Boolean = {
    val adjOrthogonalCells = board.adjacentCells(coordinate)
    for (orthogonalDirection <- adjOrthogonalCells.keys) {
      if (adjOrthogonalCells.isDefinedAt(orthogonalDirection) && adjOrthogonalCells(orthogonalDirection).nonEmpty &&
        adjOrthogonalCells(orthogonalDirection).get.getPiece.pieceOwner.equals(gameSnapshot.getPlayerToMove) &&
        !board.getCell(coordinate).getPiece.pieceOwner.equals(gameSnapshot.getPlayerToMove) &&
        adjOrthogonalCells(orthogonalDirection.opposite).nonEmpty &&
        gamePossibleMoves.map(_.to).contains(adjOrthogonalCells(orthogonalDirection.opposite).get.getCoordinate))
        return true
    }
    false
  }

  private def samePlayerPawns(l: Seq[BoardCell], p: Piece): Boolean =
    l.nonEmpty && l.size.equals(l.count(_.getPiece.equals(p)))

  private def quadraticDistanceBetweenCells(start: Coordinate, end: Coordinate): Int =
    (scala.math.pow(start.x - end.x, 2) + scala.math.pow(start.y - end.y, 2)).toInt

  private def findCloserCorner(coordinate: Coordinate): Coordinate =
    cornerCoordinates.map(c => (c, quadraticDistanceBetweenCells(coordinate, c))).minBy(_._2)._1

  private def isSequenceOfEmptyCells(seq: Seq[BoardCell]): Boolean =
    seq.count(boardCell => !boardCell.getPiece.equals(Piece.Empty)) == 0

}