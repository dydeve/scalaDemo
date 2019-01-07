package chapter01

import java.math.BigInteger

/**
  * @Description:
  * @Date 下午10:56 2019/1/5
  * @Author: joker
  */
object NoName {

  //factorial(30) BigInt = 265252859812191058636308480000000 which won't overflow
  //final class BigInt(val bigInteger: BigInteger)
  def factorial(x: BigInt): BigInt = {
    if (x == 0) 1 else x * factorial(x - 1)
  }

  def factorial(x: BigInteger) = {
    if (x == BigInteger.ZERO)
      BigInteger.ONE
    else
      x.multiply(x.subtract(BigInteger.ONE))
  }

  val nameHasUpperCase = "".exists(_.isUpper)

}

