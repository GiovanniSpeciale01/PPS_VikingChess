package model.ia.minimax

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import model.ia.messages.Messages.{EndTimeMsg, StartMsg}

import scala.concurrent.duration.FiniteDuration

/**
 * Actor representing a timer
 */
case class TimeActor() extends Actor {
  import context._

  private val delayTime: FiniteDuration = FiniteDuration(1000, TimeUnit.MILLISECONDS)

  /**
   * @inheritdoc
   */
  override def receive: Receive = {
    case _: StartMsg => context.system.scheduler.scheduleOnce(delayTime, context.sender(), EndTimeMsg())
  }
}