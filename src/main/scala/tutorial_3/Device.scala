package tutorial_3

import akka.actor.{Actor, ActorLogging, Props}

/**
  * https://doc.akka.io/docs/akka/2.5/guide/tutorial_4.html#device-manager-hierarchy
  * <br/><br/>
  * We chose this three-layered architecture for these reasons:
  * <br/><br/>
  * Having groups of individual actors:
  * <br/><br/>
  * Isolates failures that occur in a group. If a single actor managed all device groups, an error in one group that causes a restart would wipe out the state of groups that are otherwise non-faulty.
  * <br/><br/>
  * Simplifies the problem of querying all the devices belonging to a group. Each group actor only contains state related to its group.
  * <br/><br/>
  * Increases parallelism in the system. Since each group has a dedicated actor, they run concurrently and we can query multiple groups concurrently.
  *
  * <br/><br/>
  * Having sensors modeled as individual device actors:
  *
  * <br/><br/>
  * Isolates failures of one device actor from the rest of the devices in the group.
  * <br/><br/>
  * Increases the parallelism of collecting temperature readings. Network connections from different sensors communicate with their individual device actors directly, reducing contention points.
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

  var lastTemperatureReading: Option[Double] = None


  override def preStart(): Unit = log.info("Device actor {}-{} started", groupId, deviceId)

  override def postStop(): Unit = log.info("Device actor {}-{} stopped", groupId, deviceId)

  override def receive: Receive = {
    case RecordTemperature(requestId, value) =>
      log.info("Recorded temperature reading {} with {}", value, requestId)
      lastTemperatureReading = Some(value)
      sender() ! TemperatureRecorded(requestId)

    case ReadTemperature(requestId) =>
      sender() ! RespondTemperature(requestId, lastTemperatureReading)
  }
}
