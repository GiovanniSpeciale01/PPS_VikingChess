package actor_ia

import akka.actor.ActorRef
import utils.Move

import scala.collection.immutable

object RootActor {

  case class MaxRootActor() extends MaxActor() {

    override def updateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
      rootUpdateBestMove(hashMapSonRef, sonRef)

  }

  case class MinRootActor() extends MinActor() {

    override def updateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
      rootUpdateBestMove(hashMapSonRef, sonRef)

  }

  def rootUpdateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
    Option(hashMapSonRef(sonRef))
}