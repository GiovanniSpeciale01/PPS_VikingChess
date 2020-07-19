package actor_ia

import akka.actor.Props

class MinActor() extends MiniMaxActor() {

  override def initAlfa: Int = Int.MaxValue

  override def createChild(): Props =
    Props(new MaxActor())

  override def miniMaxComparison(score: Int, alfa: Int): Boolean = {
    score < alfa
  }

}
