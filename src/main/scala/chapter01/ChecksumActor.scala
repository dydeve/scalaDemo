package chapter01

import akka.actor.{Actor, ActorSystem}

/**
  * java线程模型围绕共享内存实现(shared memory),
  * 难以避免死锁和竞态(deadlock, race condition).
  *
  * scala采用akka的消息传递(message passing)
  *
  * <a href="https://doc.akka.io/docs/akka/2.5.4/scala/general/jmm.html">Akka and the Java Memory Model</a>
  */
/*
class ChecksumActor extends Actor {

  var sum = 0

  override def receive: Receive = {
    case Data(byte) => sum += byte
    case GetChecksum(requester) => {
      val checksum = ~(sum & 0xFF) + 1
      requester ! checksum
    }
  }
}

case class Data(byte: Byte)

case class GetChecksum(requester: Actor)


object Run extends App {

  val system = ActorSystem("testSystem")

  def props: Props =
    Props(new PrintMyActorRefActor)

  val firstRef = system.actorOf()

}*/
