package utils

import model.Piece
import utils.BoardGame.{Board, BoardCell}

import scala.collection.mutable.ListBuffer

/**
 * Util class representing a 2D coordinate
 */

object Coordinate {
  val COORD_STRING: String = "p"
}

case class Coordinate(x: Int, y: Int)  {
  override def toString: String = Coordinate.COORD_STRING + "(" + x + "," + y + ")"
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
    def cells: Seq[Seq[BoardCell]]

    /**
     * Defines size of board's side.
     */
    def size: Int

    /**
     * Gets a cell in the board from a coordinate.
     */
    def getCell(coordinate: Coordinate): BoardCell

    def toString: String
  }

  object Board {

    def apply(cells: Seq[Seq[BoardCell]]): Board = BoardImpl(cells)

    case class BoardImpl(private val allCells: Seq[Seq[BoardCell]]) extends Board {

      override def cells: Seq[Seq[BoardCell]] = allCells

      override def size: Int = allCells.length

      override def getCell(coordinate: Coordinate): BoardCell = allCells (coordinate.x) (coordinate.y)

      override def equals(obj: Any): Boolean = this.cells.equals(obj.asInstanceOf[Board].cells)

      override def toString: String = cells.map(_.mkString("[", ",", "]")).mkString("[", ",", "]")
    }

  }

}

object prova extends App {
  var cellList: ListBuffer[ListBuffer[BoardCell]] = ListBuffer.empty
  for(x <- 1 to 11) {
    var row: ListBuffer[BoardCell] = ListBuffer.empty
    for (y <- 1 to 11)
      row += BoardCell(Coordinate(x, y), Piece.Empty)
    cellList += row
  }
  println(Board(cellList))
}