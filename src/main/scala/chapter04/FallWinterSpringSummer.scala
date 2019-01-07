package chapter04

import CheckSumAccumulator.calculate
/**
  * @Description:
  * @Date 下午3:28 2019/1/7
  * @Author: joker
  */
object FallWinterSpringSummer extends App {

  for (season <- List("fall", "winter", "spring"))
    println(season + ": " + calculate(season))

}
