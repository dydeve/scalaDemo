package chapter01

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  * @Description:
  * @Date 下午11:35 2019/1/5
  * @Author: joker
  */
class TestCase (_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("test"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "test checkSum" in {
    val prob = TestProbe()
    val checksumActor = system.actorOf(ChecksumActor.props(), "checksum")

    checksumActor.tell(new Data(1), prob.ref)
    checksumActor.tell(new Data(1), prob.ref)

    checksumActor.tell(new GetChecksum(prob.ref), prob.ref)

    println(prob.receiveOne(remainingOrDefault))
  }


}
