package model.game

import model.game.OrthogonalDirection.OrthogonalDirection
import model.game.Piece.Piece

import scala.collection.immutable.HashMap

object Coordinate {
  val COORD_STRING: String = "p"

  /**
   * Implicit to order the coordinates according to xs and ys
   *
   * @return comparing value
   */
  implicit val coordinateOrdering: Ordering[Coordinate] = (a: Coordinate, b: Coordinate) => {
    a.x compare b.x match {
      case 0 => a.y compare b.y
      case c => c
    }
  }

}

/**
 * Class representing a two dimensional coordinate
 */
case class Coordinate(x: Int, y: Int) {
  override def toString: String = Coordinate.COORD_STRING + "(" + x + "," + y + ")"
}

/**
 * Class representing a move from coordinate to coordinate.
 */
case class Move(from: Coordinate, to: Coordinate) {
  override def toString: String = "move(from(" + from + ", to(" + to + "))"
}

object BoardGame {

  /**
   * Represents a viking chess board cell
   */
  trait BoardCell {

    /**
     * Gets the piece in the cell.
     *
     * @return the piece type
     */
    def getPiece: Piece

    /**
     * Gets the coordinate of the cell.
     *
     * @return the coordinate
     */
    def getCoordinate: Coordinate

  }

  object BoardCell {

    val CELL_STRING: String = "c"

    def apply(coordinateCell: Coordinate, piece: Piece): BoardCell = BoardCellImpl(coordinateCell, piece)

    /**
     * A class representing a viking chess board cell
     *
     * @param coordinateCell
     *        cell coordinate
     * @param pieceCell piece in the cell
     */
    case class BoardCellImpl(private val coordinateCell: Coordinate, private val pieceCell: Piece) extends BoardCell {

      /**
       * @inheritdoc
       */
      override def getPiece: Piece = pieceCell

      /**
       * @inheritdoc
       */
      override def getCoordinate: Coordinate = coordinateCell

      /**
       * @inheritdoc
       */
      override def toString: String = BoardCell.CELL_STRING + "(" + coordinateCell.toString + "," + pieceCell.toString + ")"
    }

  }

  /**
   * Represents a viking chess board
   */
  trait Board {

    /**
     * Returns the cells of the board.
     *
     * @return board rows
     */
    def rows: Seq[Seq[BoardCell]]

    /**
     * Returns the size of the board.
     *
     * @return size of the board
     */
    def size: Int

    /**
     * Gets the cell of the board in the specified coordinate.
     *
     * @param coordinate
     *        coordinate specified
     * @return cell in the specified coordinate
     */
    def getCell(coordinate: Coordinate): BoardCell

    /**
     * Sets a cell in the board.
     *
     * @param cell
     *        cell to set
     */
    def setCell(cell: BoardCell)

    /**
     * Returns a map of orthogonal cell for each direction of the given coordinate.
     *
     * @param coordinate
     *        coordinate specified
     * @return
     *        orthogonal cells
     */
    def orthogonalCells(coordinate: Coordinate): Map[OrthogonalDirection, List[BoardCell]]

    /**
     * Returns the list of special coordinates.
     *
     * @return list of special coordinates
     */
    def specialCoordinates: List[Coordinate]

    /**
     * Returns the list of corner coordinates.
     *
     * @return list of corner coordinates.
     */
    def cornerCoordinates: List[Coordinate]

    /**
     * Returns the throne cell coordinate (central cell coordinate).
     *
     * @return central coordinate
     */
    def centerCoordinates: Coordinate

    /**
     * Returns a copy of the board.
     *
     * @return a clone of the board
     */
    def getCopy: Board

    /**
     * Returns an intuitive console representation of the board.
     *
     * @return a console representation of the board
     */
    def consoleRepresentation: String

    /**
     * Returns an Option of adjacent coordinates for all four orthogonal directions.
     *
     * @param coordinate
     *        coordinate of which to find adjacent coordinates.
     * @return a map of OrthogonalDirection and Option of adjacent coordinate if it is in board.
     */
    def adjacentCoordinates(coordinate: Coordinate): Map[OrthogonalDirection, Option[Coordinate]]

    /**
     * Returns the side of the board in which the coordinate stands or Option.empty
     *
     * @param coordinate
     *        coordinate to inspect
     * @return side of the board in which the coordinate stands
     */
    def getSide(coordinate: Coordinate): Option[OrthogonalDirection]

    /**
     * Checks if the board is on any side of the board.
     *
     * @param coordinate
     *        coordinate to inspect
     * @param sides
     *        eventual side to inspect (all four sides are default)
     * @return if the board is on any side of the board
     */
    def isOnAnySides(coordinate: Coordinate, sides: Seq[OrthogonalDirection] =
      Seq(OrthogonalDirection.Up, OrthogonalDirection.Down, OrthogonalDirection.Right, OrthogonalDirection.Left)): Boolean

    /**
     * Returns an Option of adjacent cells for all four orthogonal directions.
     *
     * @param coordinate
     *        coordinate of which to find adjacent cells.
     * @return a map of OrthogonalDirection and Option of adjacent cell if it is in board.
     */
    def adjacentCells(coordinate: Coordinate): Map[OrthogonalDirection, Option[BoardCell]]

    /**
     * Returns specific orthogonal cells of the specified coordinate.
     *
     * @param coordinate
     *        coordinate to inspect
     * @param orthogonalDirection
     *        orthogonalDirection
     * @return orthogonal cells of the specified coordinate in the specified orthogonal direction
     */
    def getSpecificOrthogonalCells(coordinate: Coordinate, orthogonalDirection: OrthogonalDirection): Seq[BoardCell]

  }

  object Board {

    def apply(cells: Seq[Seq[BoardCell]]): Board = BoardImpl(cells)

    /**
     * Class representing a viking chess board
     *
     * @param allCells
     *        rows
     */
    case class BoardImpl(private var allCells: Seq[Seq[BoardCell]]) extends Board {

      /**
       * @inheritdoc
       */
      override def rows: Seq[Seq[BoardCell]] = allCells

      /**
       * @inheritdoc
       */
      override def size: Int = allCells.length

      /**
       * @inheritdoc
       */
      override def getCell(coordinate: Coordinate): BoardCell = allCells(coordinate.x - 1)(coordinate.y - 1)

      /**
       * @inheritdoc
       */
      override def setCell(cell: BoardCell): Unit =
        allCells = allCells.map(_.map(c => if (c.getCoordinate.equals(cell.getCoordinate)) cell else c))

      /**
       * @inheritdoc
       */
      override def equals(obj: Any): Boolean = this.rows.equals(obj.asInstanceOf[Board].rows)

      /**
       * @inheritdoc
       */
      override def toString: String = rows.map(_.mkString("[", ",", "]")).mkString("[", ",", "]")

      /**
       * @inheritdoc
       */
      override def orthogonalCells(coordinate: Coordinate): Map[OrthogonalDirection, List[BoardCell]] =
        HashMap(
          OrthogonalDirection.Up -> upCells(coordinate),
          OrthogonalDirection.Right -> rightCells(coordinate),
          OrthogonalDirection.Down -> downCells(coordinate),
          OrthogonalDirection.Left -> leftCells(coordinate)
        )

      private def upCells(coordinate: Coordinate): List[BoardCell] =
        (coordinate.x - 1 to 1 by -1).toList.map(Coordinate(_, coordinate.y)).map(getCell)

      private def rightCells(coordinate: Coordinate): List[BoardCell] =
        (coordinate.y + 1 to size).toList.map(Coordinate(coordinate.x, _)).map(getCell)

      private def downCells(coordinate: Coordinate): List[BoardCell] =
        (coordinate.x + 1 to size).toList.map(Coordinate(_, coordinate.y)).map(getCell)

      private def leftCells(coordinate: Coordinate): List[BoardCell] =
        (coordinate.y - 1 to 1 by -1).toList.map(Coordinate(coordinate.x, _)).map(getCell)

      /**
       * @inheritdoc
       */
      override def specialCoordinates: List[Coordinate] =
        cornerCoordinates :+ centerCoordinates

      /**
       * @inheritdoc
       */
      override def cornerCoordinates: List[Coordinate] =
        List(Coordinate(1, 1), Coordinate(1, size), Coordinate(size, 1),
          Coordinate(size, size))

      /**
       * @inheritdoc
       */
      override def centerCoordinates: Coordinate = Coordinate(size / 2 + 1, size / 2 + 1)

      /**
       * @inheritdoc
       */
      override def getCopy: Board = BoardImpl(allCells)

      /**
       * @inheritdoc
       */
      override def consoleRepresentation: String =
        rows.map(_.map(_.getPiece.toString).flatMap(e => if (e == Piece.Empty.toString) "_" else e)
          .mkString("  ") + "\n").mkString

      /**
       * @inheritdoc
       */
      override def adjacentCoordinates(coordinate: Coordinate): Map[OrthogonalDirection, Option[Coordinate]] =
        HashMap(
          OrthogonalDirection.Up ->
            filterOutOfBoardCoordinate(Coordinate(coordinate.x - 1, coordinate.y)),
          OrthogonalDirection.Right ->
            filterOutOfBoardCoordinate(Coordinate(coordinate.x, coordinate.y + 1)),
          OrthogonalDirection.Down ->
            filterOutOfBoardCoordinate(Coordinate(coordinate.x + 1, coordinate.y)),
          OrthogonalDirection.Left ->
            filterOutOfBoardCoordinate(Coordinate(coordinate.x, coordinate.y - 1))
        )

      private def filterOutOfBoardCoordinate(coordinate: Coordinate): Option[Coordinate] =
        if (!outOfBoard(coordinate))
          Option(coordinate)
        else
          Option.empty

      private def outOfBoard(coordinate: Coordinate): Boolean =
        coordinate.x > size || coordinate.x < 1 || coordinate.y > size || coordinate.y < 1

      /**
       * @inheritdoc
       */
      override def getSide(coordinate: Coordinate): Option[OrthogonalDirection] = coordinate match {
        case c if isOnSide(c, OrthogonalDirection.Up) => Option(OrthogonalDirection.Up)
        case c if isOnSide(c, OrthogonalDirection.Right) => Option(OrthogonalDirection.Right)
        case c if isOnSide(c, OrthogonalDirection.Down) => Option(OrthogonalDirection.Down)
        case c if isOnSide(c, OrthogonalDirection.Left) => Option(OrthogonalDirection.Left)
        case _ => Option.empty
      }

      private def isOnSide(coordinate: Coordinate, side: OrthogonalDirection): Boolean = side match {
        case OrthogonalDirection.Up => coordinate.x == 1
        case OrthogonalDirection.Right => coordinate.y == size
        case OrthogonalDirection.Down => coordinate.x == size
        case OrthogonalDirection.Left => coordinate.y == 1
        case _ => false
      }

      /**
       * @inheritdoc
       */
      override def isOnAnySides(coordinate: Coordinate, sides: Seq[OrthogonalDirection] =
        Seq(OrthogonalDirection.Up, OrthogonalDirection.Down, OrthogonalDirection.Right, OrthogonalDirection.Left)): Boolean =
          sides.exists(isOnSide(coordinate, _))

      /**
       * @inheritdoc
       */
      override def adjacentCells(coordinate: Coordinate): Map[OrthogonalDirection, Option[BoardCell]] =
        adjacentCoordinates(coordinate).map { case (k, v) => if (v.nonEmpty) (k, Option(getCell(v.get))) else (k, Option.empty) }

      /**
       * @inheritdoc
       */
      override def getSpecificOrthogonalCells(coordinate: Coordinate, orthogonalDirection: OrthogonalDirection): Seq[BoardCell] =
        orthogonalDirection match {
          case OrthogonalDirection.Up => upCells(coordinate)
          case OrthogonalDirection.Right => rightCells(coordinate)
          case OrthogonalDirection.Down => downCells(coordinate)
          case _ => leftCells(coordinate)
        }

    }
  }
}