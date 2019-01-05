package tutorial_4

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import tutorial_4.DeviceGroup.{ReplyDeviceList, RequestDeviceList}
import tutorial_4.DeviceManager.RequestTrackDevice

/**
  * @Description:
  * @Date 上午10:51 2019/1/3
  * @Author: joker
  */
object DeviceGroup {

  def props(groupId: String) = Props(new DeviceGroup(groupId))

  final case class RequestDeviceList(requestId: Long)
  final case class ReplyDeviceList(requestId: Long, ids: Set[String])

}

class DeviceGroup(groupId: String) extends Actor with ActorLogging {

  var deviceIdToActor = Map.empty[String, ActorRef]

  var actorToDeviceId = Map.empty[ActorRef, String]

  override def preStart(): Unit = log.info("DeviceGroup {} started", groupId)

  override def postStop(): Unit = log.info("DeviceGroup {} stopped", groupId)


  override def receive: Receive = {

    case trackMsg @ RequestTrackDevice(`groupId`, _) =>
      deviceIdToActor.get(trackMsg.deviceId) match {
        case Some(deviceActorRef) =>
          deviceActorRef forward trackMsg//tell(message, context.sender())
        case None =>
          //actor同时只会处理一条信息  不用dcl
          log.info("Creating device actor {} for group: {}", trackMsg.deviceId, `groupId`)
          val deviceActorRef = context.actorOf(Device.props(groupId, trackMsg.deviceId), s"device-${trackMsg.deviceId}")

          /**
            * The watcher can either handle this message explicitly or will fail with a DeathPactException
            */
          context.watch(deviceActorRef)

          actorToDeviceId += deviceActorRef -> trackMsg.deviceId
          deviceIdToActor += trackMsg.deviceId -> deviceActorRef

          deviceActorRef forward trackMsg
      }


    case RequestTrackDevice(groupId, deviceId) =>
      log.warning(
        "Ignoring TrackDevice request {} for group {}. This actor is responsible for {}.",
        deviceId, groupId, this.groupId
      )

    case RequestDeviceList(requestId) =>
      sender() ! ReplyDeviceList(requestId, deviceIdToActor.keySet)

    case Terminated(deviceActor) =>
      val deviceId = actorToDeviceId(deviceActor)
      log.info("Device actor for {} has been terminated", deviceId)

      actorToDeviceId -= deviceActor
      deviceIdToActor -= deviceId
  }
}
