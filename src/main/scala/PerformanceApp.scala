import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import model.game.GameSnapshot.GameSnapshotImpl
import model.game.GameVariant.GameVariant
import model.game.Level.Level
import model.game.{GameSnapshot, GameVariant, Level}
import model.ia.messages.Messages.PerformFindBestMoveMsg
import model.ia.minimax.ArtificialIntelligence
import model.ia.pruning_alpha_beta.MiniMaxImpl
import model.prolog.{ParserProlog, PrologSnapshot}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * For evaluating IA performances
 */
object PerformanceApp extends App {

  val newcomer: Level = Level.Newcomer
  val standard: Level = Level.Standard
  val advanced: Level = Level.Advanced
  val levelList: List[Level] = List(newcomer, standard, advanced)

  val brandubh: GameVariant = GameVariant.Brandubh
  val hnefatafl: GameVariant = GameVariant.Hnefatafl
  val tablut: GameVariant = GameVariant.Tablut
  val tawlbwrdd: GameVariant = GameVariant.Tawlbwrdd
  val variantList: List[GameVariant] = List(hnefatafl, tablut, brandubh, tawlbwrdd)

  var system: ActorSystem = _
  var miniMaxImpl: MiniMaxImpl = _

  var game: PrologSnapshot = _
  var snapshot: GameSnapshot = _

   /**
   * IA performance sequential test
   */
    println("Sequential")
    for (variant <- variantList;
         level <- levelList) {

      game = ParserProlog.createGame(variant.toString.toLowerCase)
      snapshot = GameSnapshotImpl(variant, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
      miniMaxImpl = MiniMaxImpl(level)

      val startTime: Long = System.currentTimeMillis()
      miniMaxImpl.findBestMove(snapshot)
      val finalTime: Long = System.currentTimeMillis() - startTime

      println("Variant: " + variant.toString + " Level: " + level.toString + " Compute Time: " + finalTime)
    }

    /**
     * IA performance parallel test
     */
    println("")
    println("Parallel")
    for (variant <- variantList;
         level <- levelList) {

      game = ParserProlog.createGame(variant.toString.toLowerCase)
      snapshot = GameSnapshotImpl(variant, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
      system = ActorSystem()

      val refIA = system.actorOf(Props(ArtificialIntelligence(null, level)))
      refIA ! PerformFindBestMoveMsg(snapshot, variant,level)
      Await.ready(system.whenTerminated, Duration(1, TimeUnit.MINUTES))
    }

}
