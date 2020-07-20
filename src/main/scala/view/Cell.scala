package view

import javax.swing.JButton

/**
 * @author Luca Nannini
 */

trait Cell extends JButton {
  /**
   * Sets the cell as a possible move.
   */
  def setAsPossibleMove()

  /**
   * Unsets the cell as a possible move.
   */
  def unsetAsPossibleMove()

  /**
   * Sets the cell as the one where the king has been
   * captured.
   */
  def setAsKingCapturedCell()

  /**
    * Resets the cell as the one where the king has been
    * captured or escaped.
    */
  def resetKingCell()

  /**
   * Sets the cell as the one where the king has
   * escaped
   */
  def setAsKingEscapedCell()

  /**
   * Sets the cell as the one where the last move
   * occured.
   */
  def setAsLastMoveCell()

  /**
   * Unsets the cell as the one where the last move
   * occured.
   */
  def unsetAsLastMoveCell()

  /**
   * Sets the cell as the selected cell.
   */
  def setAsSelectedCell()

  /**
   * Unsets the cell as the selected cell.
   */
  def unsetAsSelectedCell()
}