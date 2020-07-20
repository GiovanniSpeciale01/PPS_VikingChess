package model.ia.minimax

import akka.actor.Props
import model.game.Level.Level

/**
 * Representing a minimax node or leaf which maximizes return value
 *
 * @param levelIA
 *                IA difficulty
 */
class MaxActor(levelIA: Level) extends MiniMaxActor(levelIA) {

  /**
   * @inheritdoc
   */
  override def initAlpha: Int = Int.MinValue

  /**
   * @inheritdoc
   */
  override def createChild(): Props = Props(new MinActor(levelIA))

  /**
   * @inheritdoc
   */
  override def miniMaxComparison: (Int, Int) => Boolean = _ > _

}
