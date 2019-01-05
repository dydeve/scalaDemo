package tutorial_2

import akka.actor.ActorSystem

import scala.io.StdIn

/**
  * @Description:
  * @Date 下午4:25 2019/1/1
  * @Author: joker
  */
object IotApp {

  /**
    * [INFO] [01/01/2019 16:31:45.551] [iot-system-akka.actor.default-dispatcher-2] [akka://iot-system/user/iot-supervisor] IoT Application started
    *
    * [INFO] [01/01/2019 16:32:27.561] [iot-system-akka.actor.default-dispatcher-4] [akka://iot-system/user/iot-supervisor] IoT Application stopped
    */
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("iot-system")

    try {
      // Create top level supervisor
      val supervisor = system.actorOf(IotSupervisor.props, "iot-supervisor")
      // Exit the system after ENTER is pressed
      StdIn.readLine()
    } finally {
      system.terminate
    }
  }

}
