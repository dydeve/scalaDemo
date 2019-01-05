//#full-example
package chapter01

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

/**
  * https://developer.lightbend.com/guides/akka-quickstart-scala/
  *
  * Benefits of using the Actor Model
  * The following characteristics of Akka allow you to solve difficult concurrency
  * and scalability challenges in an intuitive way:
  *
  * Event-driven model — Actors perform work in response to messages.
  * Communication between Actors is asynchronous,
  * allowing Actors to send messages and continue their own work without blocking to wait for a reply.
  *
  * Strong isolation principles — Unlike regular objects in Scala,
  * an Actor does not have a public API in terms of methods that you can invoke.
  * Instead, its public API is defined through messages that the actor handles.
  * This prevents any sharing of state between Actors;
  * the only way to observe another actor’s state is by sending it a message asking for it.
  *
  * Location transparency — The system constructs Actors from a factory and returns references to the instances.
  * Because location doesn’t matter, Actor instances can start, stop, move,
  * and restart to scale up and down as well as recover from unexpected failures.
  *
  * Lightweight — Each instance consumes only a few hundred bytes,
  * which realistically allows millions of concurrent Actors to exist in a single application.
  *
  *
  * https://doc.akka.io/docs/akka/2.5/guide/introduction.html
  * Akka provides:
  *
  * Multi-threaded behavior without the use of low-level concurrency constructs like atomics or locks — relieving you from even thinking about memory visibility issues.
  * Transparent remote communication between systems and their components — relieving you from writing and maintaining difficult networking code.
  * A clustered, high-availability architecture that is elastic, scales in or out, on demand — enabling you to deliver a truly reactive system.
  */
//#greeter-companion
//#greeter-messages
object Greeter {
  //#greeter-messages
  def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor))
  //#greeter-messages
  final case class WhoToGreet(who: String)
  case object Greet
}
//#greeter-messages
//#greeter-companion

//#greeter-actor
class Greeter(message: String, printerActor: ActorRef) extends Actor {
  import Greeter._
  import Printer._

  var greeting = ""

  def receive = {
    case WhoToGreet(who) =>
      greeting = message + ", " + who
    case Greet           =>
      //#greeter-send-message
      printerActor ! Greeting(greeting)
      //#greeter-send-message
  }
}
//#greeter-actor

//#printer-companion
//#printer-messages
object Printer {
  //#printer-messages
  def props: Props = Props[Printer]
  //#printer-messages
  final case class Greeting(greeting: String)
}
//#printer-messages
//#printer-companion

//#printer-actor
class Printer extends Actor with ActorLogging {
  import Printer._

  def receive = {
    case Greeting(greeting) =>
      log.info("Greeting received (from " + sender() + "): " + greeting)
  }
}
//#printer-actor

//#main-class
object AkkaQuickstart extends App {
  import Greeter._

  //First, the main class creates an akka.actor.ActorSystem, a container in which Actors run.
  // Create the 'helloAkka' actor system
  val system: ActorSystem = ActorSystem("helloAkka")

  //Next, it creates three instances of a Greeter Actor and one instance of a Printer Actor.
  //#create-actors
  // Create the printer actor
  val printer: ActorRef = system.actorOf(Printer.props, "printerActor")

  // Create the 'greeter' actors
  val howdyGreeter: ActorRef =
    system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter")
  val helloGreeter: ActorRef =
    system.actorOf(Greeter.props("Hello", printer), "helloGreeter")
  val goodDayGreeter: ActorRef =
    system.actorOf(Greeter.props("Good day", printer), "goodDayGreeter")
  //#create-actors

  //#main-send-messages
  howdyGreeter ! WhoToGreet("Akka")
  howdyGreeter ! Greet

  howdyGreeter ! WhoToGreet("Lightbend")
  howdyGreeter ! Greet

  helloGreeter ! WhoToGreet("Scala")
  helloGreeter ! Greet

  goodDayGreeter ! WhoToGreet("Play")
  goodDayGreeter ! Greet
  //#main-send-messages
}
//#main-class
//#full-example
