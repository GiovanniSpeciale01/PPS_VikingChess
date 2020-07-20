package actor_ia

import akka.actor.Props

class MaxActor() extends MiniMaxActor() {

  override def initAlfa: Int = Int.MinValue

  override def createChild(): Props =
    Props(new MinActor())

  override def miniMaxComparison(score: Int, alfa: Int): Boolean = {
    score > alfa
  }

}
