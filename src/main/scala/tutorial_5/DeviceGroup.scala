package tutorial_5

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import tutorial_5.DeviceGroup.{ReplyDeviceList, RequestAllTemperatures, RequestDeviceList}
import tutorial_5.DeviceManager.RequestTrackDevice
import scala.concurrent.duration._

/**
  *  Remember that when we start a query,
  *  we need to take a snapshot of the devices present
  *  and start a timer so that we can enforce the deadline.
  *  In the meantime, another query can arrive.
  *  For the second query we need to keep track of the exact
  *  same information but in isolation from the previous query.
  *  This would require us to maintain separate mappings between queries and device actors.
  */
object DeviceGroup {

  def props(groupId: String): Props = Props(new DeviceGroup(groupId))

  final case class RequestDeviceList(requestId: Long)
  final case class ReplyDeviceList(requestId: Long, ids: Set[String])

  final case class RequestAllTemperatures(requestId: Long)
  final case class RespondAllTemperatures(requestId: Long, temperatures: Map[String, TemperatureReading])

  sealed trait TemperatureReading

  final case class Temperature(value: Double) extends TemperatureReading
  case object TemperatureNotAvailable extends TemperatureReading
  case object DeviceNotAvailable extends TemperatureReading
  case object DeviceTimedOut extends TemperatureReading
}

class DeviceGroup(groupId: String) extends Actor with ActorLogging {

  var deviceIdToActor = Map.empty[String, ActorRef]
  var actorToDeviceId = Map.empty[ActorRef, String]

  var nextCollectionId = 0L

  override def preStart(): Unit = log.info("DeviceGroup {} started", groupId)

  override def postStop(): Unit = log.info("DeviceGroup {} stopped", groupId)

  override def receive: Receive = {
    //#query-added
    case trackMsg @ RequestTrackDevice(`groupId`, _) ⇒
      deviceIdToActor.get(trackMsg.deviceId) match {
        case Some(ref) ⇒
          //tell(message, context.sender())
          ref forward trackMsg
        case None ⇒
          log.info("Creating device actor for {}", trackMsg.deviceId)
          val deviceActor = context.actorOf(Device.props(groupId, trackMsg.deviceId), "device-" + trackMsg.deviceId)
          context.watch(deviceActor)
          deviceActor forward trackMsg
          deviceIdToActor += trackMsg.deviceId -> deviceActor
          actorToDeviceId += deviceActor -> trackMsg.deviceId
      }

    case RequestTrackDevice(groupId, deviceId) ⇒
      log.warning(
        "Ignoring TrackDevice request for {}. This actor is responsible for {}.",
        groupId, this.groupId
      )

    case RequestDeviceList(requestId) ⇒
      sender() ! ReplyDeviceList(requestId, deviceIdToActor.keySet)

    case Terminated(deviceActor) ⇒
      val deviceId = actorToDeviceId(deviceActor)
      log.info("Device actor for {} has been terminated", deviceId)
      actorToDeviceId -= deviceActor
      deviceIdToActor -= deviceId

    //#query-added
    // ... other cases omitted

    case RequestAllTemperatures(requestId) ⇒
      context.actorOf(DeviceGroupQuery.props(
        actorToDeviceId = actorToDeviceId,
        requestId = requestId,
        requester = sender(),
        3.seconds
      ))
  }

}
