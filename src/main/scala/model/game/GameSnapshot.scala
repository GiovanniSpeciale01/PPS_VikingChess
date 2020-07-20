package model.game

import model.game.BoardGame.Board
import model.game.GameVariant.GameVariant
import model.game.Player.Player

/**
 * Representing a state of a viking chess game
 */
trait GameSnapshot {

  /**
    * Returns the game variant.
    *
    * @return variant
    */
  def getVariant: GameVariant

  /**
    * Returns the current game player to move.
    *
    * @return player to move
    */
  def getPlayerToMove: Player

  /**
    * Returns the current game winner.
    *
    * @return winner
    */
  def getWinner: Player

  /**
    * Returns the current game board.
    *
    * @return variant
    */
  def getBoard: Board

  /**
    * Returns the last move.
    *
    * @return optional of the last move
    */
  def getLastMove: Option[Move]

  /**
    * Returns the current number of captured blacks.
    *
    * @return captured blacks number
    */
  def getNumberCapturedBlacks: Int

  /**
    * Returns the current number of captured whites.
    *
    * @return captured whites number
    */
  def getNumberCapturedWhites: Int

  /**
    * Returns a game snapshot copy.
    *
    * @return game snapshot copy
    */
  def getCopy: GameSnapshot
}

object GameSnapshot {

  def apply(variant: GameVariant, playerToMove: Player, winner: Player, board: Board, lastMove: Option[Move], numberCapturedBlacks: Int, numberCapturedWhites: Int): GameSnapshot =
    GameSnapshotImpl(variant, playerToMove, winner, board, lastMove, numberCapturedBlacks, numberCapturedWhites)

  /**
   * Representing a state of a viking chess game.
   *
   * @param variant
   *        game variant
   * @param playerToMove
   *        next player to move
   * @param winner
   *        game winner
   * @param board
   *        game board
   * @param lastMove
   *        last move
   * @param numberCapturedBlacks
   *        number of captured blacks
   * @param numberCapturedWhites
   *        number of captured whites
   */
  case class GameSnapshotImpl(variant: GameVariant, playerToMove: Player, winner: Player, board: Board, lastMove: Option[Move], numberCapturedBlacks: Int, numberCapturedWhites: Int) extends GameSnapshot {

    /**
     * @inheritdoc
     */
    override def getVariant: GameVariant = variant

    /**
     * @inheritdoc
     */
    override def getPlayerToMove: Player = playerToMove

    /**
     * @inheritdoc
     */
    override def getWinner: Player = winner

    /**
     * @inheritdoc
     */
    override def getBoard: Board = board

    /**
     * @inheritdoc
     */
    override def getLastMove: Option[Move] = lastMove

    /**
     * @inheritdoc
     */
    override def getNumberCapturedBlacks: Int = numberCapturedBlacks

    /**
     * @inheritdoc
     */
    override def getNumberCapturedWhites: Int = numberCapturedWhites

    /**
     * @inheritdoc
     */
    override def getCopy: GameSnapshot = GameSnapshotImpl(variant, playerToMove, winner, board.getCopy, lastMove, numberCapturedBlacks, numberCapturedWhites)
  }
}
