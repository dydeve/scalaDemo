package chapter01

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * java线程模型围绕共享内存实现(shared memory),
  * 难以避免死锁和竞态(deadlock, race condition).
  *
  * scala采用akka的消息传递(message passing)
  *
  * <a href="https://doc.akka.io/docs/akka/2.5.4/scala/general/jmm.html">Akka and the Java Memory Model</a>
  */
object ChecksumActor {
  def props() = Props(new ChecksumActor)
}

class ChecksumActor extends Actor {

  var sum = 0

  override def receive: Receive = {

    case Data(x) => sum += x

    case GetChecksum(requester) => {
      val checksum = ~(sum & 0xFF) + 1
      requester ! "result: " + checksum
    }

  }
}

final case class Data(value: Int)

final case class GetChecksum(requester: ActorRef)


object Run extends App {


  val system = ActorSystem("testSystem")

  val checksumActor = system.actorOf(ChecksumActor.props(), "checksum")

  checksumActor ! 1
  checksumActor ! 2
  checksumActor ! 3


}
