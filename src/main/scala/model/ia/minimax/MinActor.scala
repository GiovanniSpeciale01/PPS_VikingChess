package model.ia.minimax

import akka.actor.Props
import model.game.Level.Level

/**
 * Representing a minimax node or leaf which minimizes return value
 *
 * @param levelIA
 *                IA difficulty
 */
class MinActor(levelIA: Level) extends MiniMaxActor(levelIA) {

  /**
   * @inheritdoc
   */
  override def initAlpha: Int = Int.MaxValue

  /**
   * @inheritdoc
   */
  override def createChild(): Props = Props(new MaxActor(levelIA))

  /**
   * @inheritdoc
   */
  override def miniMaxComparison: (Int, Int) => Boolean = _ < _

}
