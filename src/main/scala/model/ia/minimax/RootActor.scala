package model.ia.minimax

import akka.actor.ActorRef
import model.game.Level.Level
import model.game.Move

import scala.collection.immutable

object RootActor {

  /** Represents maximizing root actor in minimax
    *
    * @param levelIA
    *         difficulty chosen by user
    */
  case class MaxRootActor(levelIA: Level) extends MaxActor(levelIA) {

    /**
     * @inheritdoc
     */
    override def updateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
      rootUpdateBestMove(hashMapSonRef, sonRef)

  }

  /** Represents minimizing root actor in minimax
    *
    * @param levelIA
    *         difficulty chosen by user
    */
  case class MinRootActor(levelIA: Level) extends MinActor(levelIA) {

    /**
     * @inheritdoc
     */
    override def updateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
      rootUpdateBestMove(hashMapSonRef, sonRef)

  }

  /** Updates best move
    *
    * @param hashMapSonRef
    *         Map of generated children
    *
    * @return best move updated
    */
  def rootUpdateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
    Option(hashMapSonRef(sonRef))
}
