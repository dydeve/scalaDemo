package tutorial_2

import akka.actor.{Actor, ActorLogging, Props}

/**
  * @Description:
  * @Date 下午9:06 2018/12/31
  * @Author: joker
  */
object IotSupervisor {
  def props: Props = Props(new IotSupervisor)
}

class IotSupervisor extends Actor with ActorLogging {


  override def preStart(): Unit = log.info("IoT Application started")

  override def postStop(): Unit = log.info("IoT Application stopped")

  // No need to handle any messages
  override def receive: Receive = Actor.emptyBehavior
}

