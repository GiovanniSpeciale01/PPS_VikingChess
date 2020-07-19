package actor_ia

import actor_ia.RootActor.{MaxRootActor, MinRootActor}
import akka.actor.{Actor, Props}
import model._
import utils.Move

case class ReturnBestMoveMsg(bestMove: Move)

case class FindBestMoveMsg(gameSnapshot: GameSnapshot)

object ArtificialIntelligenceImpl {

  def apply(model: ModelHnefatafl, depth: Int): ArtificialIntelligenceImpl = new ArtificialIntelligenceImpl(model, depth)
}

case class ArtificialIntelligenceImpl(model: ModelHnefatafl, depth: Int) extends Actor {

  override def receive: Receive = {
    case event: FindBestMoveMsg => findBestMove(event.gameSnapshot)
    case event: ReturnBestMoveMsg => model.makeMove(event.bestMove); context.stop(sender())
  }

  def findBestMove(gameSnapshot: GameSnapshot): Unit = {
    var sonActor: Props = Props.empty
    if (iaIsBlack(gameSnapshot.getPlayerToMove))
      sonActor = Props(MinRootActor())
    else
      sonActor = Props(MaxRootActor())
    val refSonActor = context.actorOf(sonActor)
    refSonActor ! InitMsg(gameSnapshot.getCopy, depth, Option.empty)

  }

  private def iaIsBlack(iaPlayer: Player.Val): Boolean = iaPlayer.equals(Player.Black)

}