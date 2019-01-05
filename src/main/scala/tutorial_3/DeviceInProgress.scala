package tutorial_3

import akka.actor.{Actor, ActorLogging, Props}
import tutorial_3.Device.{ReadTemperature, RespondTemperature}

/**
  * Akka provides the following behavior for message sends:
  * <br/><br/>
  * At-most-once delivery, that is, no guaranteed delivery.<br/><br/>
  * Message ordering is maintained per sender, receiver pair.<br/><br/>
  *
  * https://doc.akka.io/docs/akka/2.5/guide/tutorial_3.html#message-delivery<br/><br/>
  *
  * At-most-once delivery — each message is delivered zero or one time; in more causal terms it means that messages can be lost, but are never duplicated.<br/>
  * At-least-once delivery — potentially multiple attempts are made to deliver each message, until at least one succeeds; again, in more causal terms this means that messages can be duplicated but are never lost.<br/>
  * Exactly-once delivery — each message is delivered exactly once to the recipient; the message can neither be lost nor be duplicated.<br/>
  * <br/><br/>
  * The first behavior, the one used by Akka, is the cheapest and results in the highest performance. It has the least implementation overhead because it can be done in a fire-and-forget fashion without keeping the state at the sending end or in the transport mechanism. The second, at-least-once, requires retries to counter transport losses. This adds the overhead of keeping the state at the sending end and having an acknowledgment mechanism at the receiving end. Exactly-once delivery is most expensive, and results in the worst performance: in addition to the overhead added by at-least-once delivery, it requires the state to be kept at the receiving end in order to filter out duplicate deliveries.<br/>
  */
class DeviceInProgress {

}





