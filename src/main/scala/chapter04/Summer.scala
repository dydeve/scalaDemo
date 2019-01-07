package chapter04

import CheckSumAccumulator.calculate
/**
  * @Description:
  * @Date 下午3:17 2019/1/7
  * @Author: joker
  */
object Summer {//类名和文件名一致

  def main(args: Array[String]): Unit = {
    for (arg <- args) {
      println(arg + ": " + calculate(arg))
    }
  }

}
