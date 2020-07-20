package model.ia.messages

import model.game.GameVariant.GameVariant
import model.game.Level.Level
import model.game.{GameSnapshot, Move}


/**
 * Actor messages
 */
object Messages {

  /**
   * Message used by the minimax root to notify the IA to return to model the best computed move
   *
   * @param bestMove
   *        best move
   */
  case class ReturnBestMoveMsg(bestMove: Move)

  /**
   * Message used by the model to notify the IA to start searching for the best move for that snapshot
   *
   * @param gameSnapshot
   *        game snapshot
   */
  case class FindBestMoveMsg(gameSnapshot: GameSnapshot)

  /**
   * Message used to change behaviour of the actor to dying when best move evaluation is no longer needed.
   */
  case class CloseMsg()

  /**
   * Message notifying the end of the delay time
   */
  case class EndTimeMsg()

  /**
   * Message notifying the timer actor to activate
   */
  case class StartMsg()

  /**
   * Message notifying the actors to start exploring the tree starting from a specific snapshot,
   * with a specific level of depth and for a specific move.
   *
   * @param gameSnapshot
   *        initial game snapshot
   * @param depth
   *        tree search depth limit
   * @param move
   *        move to calculate in the node
   */
  case class InitMsg(gameSnapshot: GameSnapshot, depth: Int, move: Option[Move])

  /**
   * Message notifying the node to make the move and calculate the new game snapshot
   */
  case class MakeMoveMsg()

  /**
   * Message notifying the node to check if it is a terminal node,
   * otherwise it continues to analyze the child nodes
   */
  case class CheckLeafOrBranchMsg()

  /**
   * Message notifying to evaluate the game snapshot
   */
  case class EvaluationMsg()

  /**
   * Message notifying to generate children
   */
  case class GenerateChildrenMsg()

  /**
   * Message sent from children to father with computed score
   *
   * @param score
   *        computed score
   */
  case class ValueSonMsg(score: Int)

  /**
   * Message sent from Performance class for start test about IA response time
   *
   * @param gameSnapshot
   *          game snapshot
   * @param variant
   *          game variant
   * @param level
   *          game level
   */
  case class PerformFindBestMoveMsg(gameSnapshot: GameSnapshot, variant: GameVariant, level: Level)

}
