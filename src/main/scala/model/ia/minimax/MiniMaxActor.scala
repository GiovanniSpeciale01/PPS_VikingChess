package model.ia.minimax

import akka.actor.{Actor, ActorRef, Props}
import model.ia.evaluation_function.EvaluationFunction
import model.ia.messages.Messages.{CheckLeafOrBranchMsg, EvaluationMsg, GenerateChildrenMsg, InitMsg, MakeMoveMsg, ReturnBestMoveMsg, ValueSonMsg}
import model.game.Level.Level
import model.game.Player.Player
import model.game.{GameSnapshot, Move, MoveGenerator, Player}

import scala.collection.{immutable, mutable}

/**
  * State of the actor in current instant of computation
  *
  * @param gameSnapshot
  *         game state
  *
  * @param depth
  *         depth of exploration according to difficulty
  *
  * @param move
  *         move chosen by child actor
  *
  * @param fatherRef
  *         father reference
  *
  * @param alpha
  *         value used for comparison in minimax
  */
case class ActorState(gameSnapshot: GameSnapshot, depth: Int, move: Option[Move], fatherRef: ActorRef, alpha: Int)

/**
 * An abstract implementation of a viking chess minimax node actor
 *
 * @param levelIA
 *        IA difficulty
 */
abstract class MiniMaxActor(levelIA: Level) extends Actor {

  /**
   * @inheritdoc
   */
  override def receive: Receive = {
    case event: InitMsg =>
      context.become(makingMoveBehaviour(ActorState(event.gameSnapshot, event.depth, event.move, sender(), initAlpha)))
      self ! MakeMoveMsg()
  }

  /**
   * Behaviour in which the actor has to make his move
   *
   * @param actorState
   *        actor state
   */
  def makingMoveBehaviour(actorState: ActorState): Receive = {
    case _: MakeMoveMsg => makeMove(actorState)
  }

  /**
   * Behaviour in which the actor has to understand if it is a branch or a leaf
   *
   * @param actorState
   *        actor state
   */
  def exploringBehaviour(actorState: ActorState): Receive = {
    case _: CheckLeafOrBranchMsg => checkLeafOrBranch(actorState)
  }

  /**
   * Behaviour in which the actor is a leaf and evaluates his score
   *
   * @param actorState
   *        actor state
   */
  def leafBehaviour(actorState: ActorState): Receive = {
    case _: EvaluationMsg => computeEvaluationFunction(actorState.fatherRef, actorState.gameSnapshot)
  }

  /**
   * Behaviour in which the actor is a branch and generates new children
   *
   * @param actorState
   *        actor state
   */
  def branchBehaviour(actorState: ActorState): Receive = {
    case _: GenerateChildrenMsg => generateChildren(actorState)
  }

  /**
    * Behaviour in which the actor is waiting for his children evaluations and makes minimax comparison
    *
    * @param hashMapSonRef
    *         Map reference of his children
    *
    * @param numberOfChildren
    *         number of his children
    *
    * @param bestMove
    *         best move
    *
    * @param alpha
    *         value to check for minimax algorithm
    *
    * @param fatherRef
    *         reference to the father
    */
  def evaluatingChildrenBehaviour(hashMapSonRef: immutable.HashMap[ActorRef, Move], numberOfChildren: Int, bestMove: Option[Move], alpha: Int, fatherRef: ActorRef): Receive = {
    case event: ValueSonMsg => miniMax(hashMapSonRef, numberOfChildren, bestMove, alpha, fatherRef, sender(), event.score)
  }

  /**
   * Method that creates child
   */
  protected def createChild(): Props

  /**
   * Method that compares values to determine best score
   */
  protected def miniMaxComparison: (Int, Int) => Boolean

  /**
   * Method that initializes alpha value
   */
  protected def initAlpha: Int

  private def makeMove(actorState: ActorState): Unit = {
    val currentSnapshot = newMoveSnapshot(actorState.gameSnapshot, actorState.move)

    context.become(exploringBehaviour(ActorState(currentSnapshot, actorState.depth, actorState.move, actorState.fatherRef,
      actorState.alpha)))
    self ! CheckLeafOrBranchMsg()
  }

  private def newMoveSnapshot(fatherSnapshot: GameSnapshot, move: Option[Move]): GameSnapshot =
    if (move.nonEmpty)
      MoveGenerator.makeMove(fatherSnapshot, move.get)
    else
      fatherSnapshot

  private def checkLeafOrBranch(actorState: ActorState): Unit =
    if (isTerminalNode(actorState.gameSnapshot.getWinner, actorState.depth)) {
      context.become(leafBehaviour(actorState))
      self ! EvaluationMsg()
    } else {
      context.become(branchBehaviour(actorState))
      self ! GenerateChildrenMsg()
    }

  private def isTerminalNode(gameStatus: Player, depth: Int): Boolean = !gameStatus.equals(Player.None) || depth == 0

  private def computeEvaluationFunction(fatherRef: ActorRef, currentGame: GameSnapshot): Unit =
    fatherRef ! ValueSonMsg(EvaluationFunction(currentGame).score(levelIA))

  private def generateChildren(actorState: ActorState): Unit = {
    val gamePossibleMoves = MoveGenerator.gamePossibleMoves(actorState.gameSnapshot.getCopy)
    val hashMapSonRef: mutable.HashMap[ActorRef, Move] = mutable.HashMap.empty

    for (possibleMove <- gamePossibleMoves) {
      val sonActor: Props = createChild()
      hashMapSonRef += context.actorOf(sonActor) -> possibleMove
    }

    context.become(evaluatingChildrenBehaviour(toImmutableMap(hashMapSonRef), gamePossibleMoves.size, Option.empty, actorState.alpha, actorState.fatherRef))

    hashMapSonRef.foreach { case (k, v) => k ! InitMsg(actorState.gameSnapshot.getCopy, actorState.depth - 1, Option(v)) }
  }

  private def miniMax(hashMapSonRef: immutable.HashMap[ActorRef, Move], numberOfChildren: Int, bestMove: Option[Move], alpha: Int,
              fatherRef: ActorRef, sonRef: ActorRef, score: Int): Unit = {
    val newNumberOfChildren = numberOfChildren - 1
    var newAlpha = alpha
    var newBestMove = bestMove
    if (miniMaxComparison(score, alpha)) {
      newAlpha = score
      newBestMove = updateBestMove(hashMapSonRef, sonRef)
    }
    if (newNumberOfChildren == 0) {
      if (newBestMove.nonEmpty)
        fatherRef ! ReturnBestMoveMsg(newBestMove.get)
      else {
        fatherRef ! ValueSonMsg(alpha)
      }
    } else {
      context.become(evaluatingChildrenBehaviour(hashMapSonRef, newNumberOfChildren, newBestMove, newAlpha, fatherRef))
    }
  }

  /**
   * Method which updates the best move if the node is the root or returns Option.empty
   *
   * @param hashMapSonRef
   *        children map
   * @param sonRef
   *        children reference
   * @return best move updated
   */
  protected def updateBestMove(hashMapSonRef: immutable.HashMap[ActorRef, Move], sonRef: ActorRef): Option[Move] =
    Option.empty

  private def toImmutableMap(mutableHashMap: mutable.HashMap[ActorRef, Move]): immutable.HashMap[ActorRef, Move] =
    collection.immutable.HashMap(mutableHashMap.toSeq: _*)
}

