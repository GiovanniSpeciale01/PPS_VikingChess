package utils

import model.Piece

/**
 * Util class representing a 2D coordinate
 */

object Coordinate {
  val COORD_STRING: String = "p"
}

case class Coordinate(x: Int, y: Int)  {
  override def toString: String = Coordinate.COORD_STRING + "(" + x + "," + y + ")"
}

case class Move(from: Coordinate, to: Coordinate) {
  override def toString: String = "move(from(" + from + ", to(" + to + "))"
}

object BoardGame {

  trait BoardCell {

    /**
     * Gets piece in the cell
     *
     * @return
     *         the piece type
     */
    def getPiece: Piece.Val

    /**
     * Gets coordinate of the cell
     *
     * @return
     *         the coordinate
     */
    def getCoordinate: Coordinate

    /**
     * Returns a string representation of the board
     *
     * @return
     *         a string representation of the board
     */
    def toString: String

  }

  object BoardCell {

    val CELL_STRING: String = "c"

    def apply(coordinateCell: Coordinate, piece: Piece.Val): BoardCell = BoardCellImpl(coordinateCell, piece)

    case class BoardCellImpl(private val coordinateCell: Coordinate, private val pieceCell: Piece.Val) extends BoardCell {

      override def getPiece: Piece.Val = pieceCell

      override def getCoordinate: Coordinate = coordinateCell

      override def toString: String = BoardCell.CELL_STRING + "(" + coordinateCell.toString + "," + pieceCell.pieceString + ")"
    }

  }

  trait Board {
    /**
     * Defines board's cells list.
     */
    def rows: Seq[Seq[BoardCell]]

    /**
     * Defines size of board's side.
     */
    def size: Int

    /**
     * Gets a cell in the board from a coordinate.
     */
    def getCell(coordinate: Coordinate): BoardCell

    def setCell(cell: BoardCell)

    def toString: String

    def orthogonalCells(coordinate: Coordinate): List[List[BoardCell]]

    def orthogonalCoordinates(coordinate: Coordinate): List[List[Coordinate]]

    def specialCoordinates: List[Coordinate]

  }

  object Board {

    def apply(cells: Seq[Seq[BoardCell]]): Board = BoardImpl(cells)

    case class BoardImpl(private var allCells: Seq[Seq[BoardCell]]) extends Board {

      override def rows: Seq[Seq[BoardCell]] = allCells

      override def size: Int = allCells.length

      override def getCell(coordinate: Coordinate): BoardCell = allCells (coordinate.x - 1) (coordinate.y - 1)

      override def setCell(cell: BoardCell): Unit =
        allCells = allCells.map(_.map(c => if(c.getCoordinate.equals(cell.getCoordinate)) cell else c))

      override def equals(obj: Any): Boolean = this.rows.equals(obj.asInstanceOf[Board].rows)

      override def toString: String = rows.map(_.mkString("[", ",", "]")).mkString("[", ",", "]")

      override def orthogonalCells(coordinate: Coordinate): List[List[BoardCell]] =
        List(upCells(coordinate), rightCells(coordinate), downCells(coordinate), leftCells(coordinate))

      private def upCells(coordinate: Coordinate): List[BoardCell] =
        upCoords(coordinate).map(getCell)

      private def rightCells(coordinate: Coordinate): List[BoardCell] =
        rightCoords(coordinate).map(getCell)

      private def downCells(coordinate: Coordinate): List[BoardCell] =
        downCoords(coordinate).map(getCell)

      private def leftCells(coordinate: Coordinate): List[BoardCell] =
        leftCoords(coordinate).map(getCell)

      override def orthogonalCoordinates(coordinate: Coordinate): List[List[Coordinate]] =
        List(upCoords(coordinate), rightCoords(coordinate), downCoords(coordinate), leftCoords(coordinate))

      private def upCoords(coordinate: Coordinate): List[Coordinate] =
        (coordinate.x - 1 to 1 by -1).toList.map(Coordinate(_, coordinate.y))

      private def rightCoords(coordinate: Coordinate): List[Coordinate] =
        (coordinate.y + 1 to size).toList.map(Coordinate(coordinate.x, _))

      private def downCoords(coordinate: Coordinate): List[Coordinate] =
        (coordinate.x + 1 to size).toList.map(Coordinate(_, coordinate.y))

      private def leftCoords(coordinate: Coordinate): List[Coordinate] =
        (coordinate.y - 1 to 1 by - 1).toList.map(Coordinate(coordinate.x, _))

      override def specialCoordinates: List[Coordinate] =
        List(Coordinate(1, 1), Coordinate(1, size), Coordinate(size, 1),
          Coordinate(size, size), Coordinate(size / 2 + 1, size / 2 + 1))

    }

  }

}