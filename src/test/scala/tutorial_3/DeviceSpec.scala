package tutorial_3

import akka.actor.{ActorRef, ActorSystem}
import akka.japi.Option.Some
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.concurrent.duration.MILLISECONDS
import tutorial_3.Device._

/**
  * @Description:
  * @Date 上午2:13 2019/1/2
  * @Author: joker
  */
class DeviceSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("device_spec"))

  override protected def afterAll(): Unit = {
    //_system.terminate()
    shutdown(system)
  }

  "Device actor" must {

    "reply with empty reading if no temperature is known" in {

      val probe = TestProbe()
      val deviceActor: ActorRef = system.actorOf(Device.props("group", "device"))

      deviceActor.tell(Device.ReadTemperature(requestId = 42), probe.ref)
      val response = probe.expectMsgType[Device.RespondTemperature]
      response.requestId should === (42L)
      response.value should === (None)
    }

    "reply with latest temperature reading" in {
      val probe = TestProbe()
      val deviceActor = system.actorOf(Device.props("group", "device"))

      deviceActor.tell(Device.RecordTemperature(requestId = 1, 24.0), probe.ref)
      probe.expectMsg(Device.TemperatureRecorded(requestId = 1))

      deviceActor.tell(Device.ReadTemperature(requestId = 2), probe.ref)
      val response = probe.expectMsgType[Device.RespondTemperature]
      response.requestId should === (2L)
      response.value should === (Some(24.0))

      deviceActor.tell(Device.RecordTemperature(requestId = 3, 55.0), probe.ref)
      probe.expectMsg(Device.TemperatureRecorded(requestId = 3))

      deviceActor.tell(Device.ReadTemperature(requestId = 4), probe.ref)
      val response2 = probe.expectMsgType[Device.RespondTemperature]
      response2.requestId should ===(4L)
      response2.value should ===(Some(55.0))
    }


  }



}
