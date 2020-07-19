package actor_ia

import akka.actor.{Actor, ActorRef, Props}
import ia.EvaluationFunction
import model._
import utils.Move

import scala.collection.mutable
import scala.collection.immutable

case class InitMsg(gameSnapshot: GameSnapshot, depth: Int, move: Option[Move])

case class MakeMoveMsg()

case class CheckLeafOrBranchMsg()

case class EvaluationMsg()

case class GenerateChildrenMsg()

case class ValueSonMsg(score: Int)

case class ActorState(gameSnapshot: GameSnapshot, depth: Int, move: Option[Move], fatherRef: ActorRef, alfa: Int)

abstract class MiniMaxActor() extends Actor {

  override def receive: Receive = {
    case event: InitMsg =>
      context.become(initState(ActorState(event.gameSnapshot, event.depth, event.move, sender(), initAlfa)))
      self ! MakeMoveMsg()
  }

  def initState(actorState: ActorState): Receive = {
    case _: MakeMoveMsg => makeMove(actorState)
  }

  def exploringState(actorState: ActorState): Receive = {
    case _: CheckLeafOrBranchMsg => checkLeafOrBranch(actorState)
  }

  def leafState(actorState: ActorState): Receive = {
    case _: EvaluationMsg => computeEvaluationFunction(actorState.fatherRef, actorState.gameSnapshot, actorState.move.get)
  }

  def branchState(actorState: ActorState): Receive = {
    case _: GenerateChildrenMsg => generateChildren(actorState)
  }

  def evaluatingChildren(hashMapSonRef: immutable.HashMap[ActorRef, Move], numberOfChildren: Int, bestMove: Option[Move], alfa: Int, fatherRef: ActorRef): Receive = {
    case event: ValueSonMsg => miniMax(hashMapSonRef, numberOfChildren, bestMove, alfa, fatherRef, sender(), event.score)
  }

  def initAlfa: Int

  private def makeMove(actorState: ActorState): Unit = {
    val currentSnapshot = newMoveSnapshot(actorState.gameSnapshot, actorState.move)
    context.become(exploringState(ActorState(currentSnapshot, actorState.depth, actorState.move, actorState.fatherRef,
      actorState.alfa)))
    self ! CheckLeafOrBranchMsg()
  }

  def newMoveSnapshot(fatherSnapshot: GameSnapshot, move: Option[Move]): GameSnapshot =
    if (move.nonEmpty)
      MoveGenerator.makeMove(fatherSnapshot, move.get)
    else
      fatherSnapshot

  def checkLeafOrBranch(actorState: ActorState): Unit =
    if (isTerminalNode(actorState.gameSnapshot.getWinner, actorState.depth)) {
      context.become(leafState(actorState))
      self ! EvaluationMsg()
    } else {
      context.become(branchState(actorState))
      self ! GenerateChildrenMsg()
    }

  def isTerminalNode(gameStatus: Player.Val, depth: Int): Boolean = !gameStatus.equals(Player.None) || depth == 0

  def computeEvaluationFunction(fatherRef: ActorRef, currentGame: GameSnapshot, move: Move): Unit =
    fatherRef ! ValueSonMsg(EvaluationFunction.score(currentGame))

  def generateChildren(actorState: ActorState): Unit = {
    val gamePossibleMoves = MoveGenerator.gamePossibleMoves(actorState.gameSnapshot.getCopy)
    val hashMapSonRef: mutable.HashMap[ActorRef, Move] = mutable.HashMap.empty

    for (possibleMove <- gamePossibleMoves) {
      val sonActor: Props = createChild()
      hashMapSonRef += context.actorOf(sonActor) -> possibleMove
    }

    context.become(evaluatingChildren(toImmutableMap(hashMapSonRef), gamePossibleMoves.size, Option.empty, actorState.alfa, actorState.fatherRef))

    hashMapSonRef.foreach { case (k, v) => k ! InitMsg(actorState.gameSnapshot.getCopy, actorState.depth - 1, Option(v)) }

  }

  def createChild(): Props

  def miniMaxComparison(score: Int, alfa: Int): Boolean

  def miniMax(hashMapSonRef: immutable.HashMap[ActorRef, Move], numberOfChildren: Int, bestMove: Option[Move], alfa: Int,
              fatherRef: ActorRef, sonRef: ActorRef, score: Int): Unit = {
    val newNumberOfChildren = numberOfChildren - 1
    var newAlfa = alfa
    var newBestMove = bestMove
    if (miniMaxComparison(score, alfa)) {
      newAlfa = score
      newBestMove = updateBestMove(hashMapSonRef, sonRef)
    }
    if (newNumberOfChildren == 0) {
      if (newBestMove.nonEmpty)
        fatherRef ! ReturnBestMoveMsg(newBestMove.get)
      else
        fatherRef ! ValueSonMsg(alfa)
    } else {
      context.become(evaluatingChildren(hashMapSonRef, newNumberOfChildren, newBestMove, newAlfa, fatherRef))
    }
  }

  protected def updateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
    Option.empty

  private def toImmutableMap(mutableHashMap: mutable.HashMap[ActorRef, Move]): immutable.HashMap[ActorRef, Move] =
    collection.immutable.HashMap(mutableHashMap.toSeq: _*)
}