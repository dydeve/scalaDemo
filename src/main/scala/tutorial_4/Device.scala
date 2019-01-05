package tutorial_4

import akka.actor.{Actor, ActorLogging, Props}
import akka.japi.Option.Some

/**
  * @Description:
  * @Date 下午5:22 2019/1/2
  * @Author: joker
  */
object Device {

  def props(groupId: String, deviceId: String) = Props(new Device(groupId, deviceId))

  //write protocol
  final case class RecordTemperature(requestId: Long, value: Double)

  final case class TemperatureRecorded(requestId: Long)

  //read protocol
  final case class ReadTemperature(requestId: Long)

  final case class RespondTemperature(requestId: Long, value: Option[Double])

}

class Device(groupId: String, deviceId: String) extends Actor with ActorLogging {

  import Device._

  private var lastTemperatureReading: Option[Double] = None

  override def preStart(): Unit = log.info("Device actor {}-{} started", groupId, deviceId)

  override def postStop(): Unit = log.info("Device actor {}-{} stopped", groupId, deviceId)


  override def receive: Receive = {
    case DeviceManager.RequestTrackDevice(`groupId`, `deviceId`) =>
      sender() ! DeviceManager.DeviceRegistered

    case DeviceManager.RequestTrackDevice(groupId, deviceId) =>
      log.warning("Ignoring TrackDevice request for {}-{}.This actor is responsible for {}-{}.",
        groupId,
        deviceId,
        this.groupId,
        this.deviceId)

    case RecordTemperature(requestId, value) =>
      log.info("Recorded temperature reading {} with {}", value, requestId)
      lastTemperatureReading = Some(value)
      sender() ! TemperatureRecorded(requestId)

    case ReadTemperature(requestId) =>
      sender() ! RespondTemperature(requestId, lastTemperatureReading)


  }
}
