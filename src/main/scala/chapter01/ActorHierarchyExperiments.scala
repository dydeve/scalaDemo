package chapter01

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.io.StdIn

object PrintMyActorRefActor {
  def props: Props =
    Props(new PrintMyActorRefActor)
}

class PrintMyActorRefActor extends Actor {
  override def receive: Receive = {
    case "printit" ⇒
      val secondRef = context.actorOf(Props.empty, "second-actor")
      println(s"Second: $secondRef")
  }
}

//#start-stop
object StartStopActor1 {
  def props: Props =
    Props(new StartStopActor1)
}

class StartStopActor1 extends Actor {
  override def preStart(): Unit = {
    println("first started")
    context.actorOf(StartStopActor2.props, "second")
  }

  override def postStop(): Unit = println("first stopped")

  override def receive: Receive = {
    case "stop" ⇒ context.stop(self)
  }
}

object StartStopActor2 {
  def props: Props =
    Props(new StartStopActor2)
}

class StartStopActor2 extends Actor {
  override def preStart(): Unit = println("second started")

  override def postStop(): Unit = println("second stopped")

  // Actor.emptyBehavior is a useful placeholder when we don't
  // want to handle any messages in the actor.
  override def receive: Receive = Actor.emptyBehavior
}

//#start-stop

//#supervise
object SupervisingActor {
  def props: Props =
    Props(new SupervisingActor)
}

class SupervisingActor extends Actor with ActorLogging {
  val child = context.actorOf(SupervisedActor.props, "supervised-actor")

  override def receive: Receive = {
    case "failChild" ⇒
      log.info("" + sender())
      child ! "fail"
  }
}

object SupervisedActor {
  def props: Props =
    Props(new SupervisedActor)
}

class SupervisedActor extends Actor with ActorLogging{
  override def preStart(): Unit = log.info("supervised actor started  " + context.self)

  override def postStop(): Unit = log.info("supervised actor stopped  " + context.self)

  override def receive: Receive = {
    case "fail" ⇒
      log.info("supervised actor fails now  " + sender())
      throw new Exception("I failed!")
  }
}

//#supervise
/**
  * First: Actor[akka://testSystem/user/first-actor#-1251493204]
  * Second: Actor[akka://testSystem/user/first-actor/second-actor#1980440964]
  * >>> Press ENTER to exit <<<
  */
object ActorHierarchyExperiments extends App {
  /**
    * First: Actor[akka://testSystem/user/first-actor#-1251493204]
    * Second: Actor[akka://testSystem/user/first-actor/second-actor#1980440964]
    * >>> Press ENTER to exit <<<
    */
  val system = ActorSystem("testSystem")
  //
  //  val firstRef = system.actorOf(PrintMyActorRefActor.props, "first-actor")
  //  println(s"First: $firstRef")
  //  firstRef ! "printit"
  //
  //  println(">>> Press ENTER to exit <<<")
  //  try StdIn.readLine()
  //  finally system.terminate()

  /**
    * When we stopped actor first, it stopped its child actor, second, before stopping itself.
    * This ordering is strict, all postStop() hooks of the children are called before the postStop() hook of the parent is called.
    */
  //  val first = system.actorOf(StartStopActor1.props, "first")
  //  first ! "stop"

  /**
    * supervised actor started
    * supervised actor fails now
    * supervised actor stopped
    * supervised actor started
    * [ERROR] [12/31/2018 18:01:56.549] [testSystem-akka.actor.default-dispatcher-5] [akka://testSystem/user/supervising-actor/supervised-actor] I failed!
    * java.lang.Exception: I failed!
    * at chapter01.SupervisedActor$$anonfun$receive$4.applyOrElse(ActorHierarchyExperiments.scala:82)
    * at akka.actor.Actor.aroundReceive(Actor.scala:517)
    * at akka.actor.Actor.aroundReceive$(Actor.scala:515)
    * at chapter01.SupervisedActor.aroundReceive(ActorHierarchyExperiments.scala:74)
    * at akka.actor.ActorCell.receiveMessage(ActorCell.scala:588)
    * at akka.actor.ActorCell.invoke(ActorCell.scala:557)
    * at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:258)
    * at akka.dispatch.Mailbox.run(Mailbox.scala:225)
    * at akka.dispatch.Mailbox.exec(Mailbox.scala:235)
    * at akka.dispatch.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)
    * at akka.dispatch.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)
    * at akka.dispatch.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)
    * at akka.dispatch.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)
    */
  val supervisingActor = system.actorOf(SupervisingActor.props, "supervising-actor")
  supervisingActor ! "failChild"//Actor[akka://testSystem/deadLetters]
}