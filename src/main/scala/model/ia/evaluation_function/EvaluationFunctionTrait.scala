package model.ia.evaluation_function

import model.game.Level.Level

/**
 * Represents an evaluation function for viking chess.
 */
trait EvaluationFunctionTrait {

  /**
   * Returns final score.
   *
   * @param levelIA
   *        difficulty chosen.
   * @return total score
   */
  def score(levelIA: Level): Int

  /**
   * Checks if the king is near a corner.
   *
   * @return if the king is near a corner
   */
  def kingAdjacentToCorner: Boolean

  /**
   * Checks if the king can move near a corner in one move.
   *
   * @return if the king can move near a corner in one move
   */
  def kingNearCornerInOne: Boolean

  /**
   * Checks if the king can move to a corner in one move.
   *
   * @return if the king can move to a corner in one move
   */
  def kingToCornerInOne: Boolean

  /**
   * Checks if the king is catchable in one move.
   *
   * @return if the king is catchable in one move
   */
  def kingCapturedInOne: Boolean

  /**
   * Returns score for a wrong barricade.
   *
   * @return a FactionsScore for white and black scores for a wrong barricade.
   */
  def scoreWrongBarricade: FactionsScore

  /**
   * Returns score for the last pawn moved if it is catchable in one.
   *
   * @return score for last pawn moved catchable in one
   */
  def scoreLastPawnMovedCatchableInOne: Int

  /**
   * Returns score according to its distance from the throne.
   *
   * @return score for the king location
   */
  def scoreKingOnThrone: Int

  /**
   * Returns score white towers.
   *
   * @return score white towers
   */
  def scoreTower: Int

  /**
   * Returns score for a captured black piece.
   *
   * @return score for a captured black piece
   */
  def scoreCapturedBlack: Int

  /**
   * Returns score for the row and column controlled from king.
   *
   * @return score row and column owned by king
   */
  def scoreKingIsInFreeRowOrColumn: Int

  /**
   * Returns score for a captured white piece.
   *
   * @return score for a captured white piece
   */
  def scoreCapturedWhite: Int

  /**
   * Returns score according to number of blacks surrounding king.
   *
   * @return score for black pieces surrounding the king
   */
  def scoreBlackSurroundTheKing: Int

  /**
   * Returns score according to number of blacks on king's diagonals.
   *
   * @return score for black pieces on king's diagonals
   */
  def scoreBlackOnKingDiagonal: Int

  /**
   * Returns score for black cordons.
   *
   * @return score for black cordons
   */
  def scoreBlackCordon: Int

  /**
   * Returns score according to king possible moves number.
   *
   * @return score according to king possible moves number
   */
  def scoreMovesKing: Int

}

/**
 * Class representing a particular score that can be obtained both from white
 * or black player.
 *
 * @param whiteScore
 *        score for white
 * @param blackScore
 *        score for black
 */
case class FactionsScore(whiteScore: Int, blackScore: Int)