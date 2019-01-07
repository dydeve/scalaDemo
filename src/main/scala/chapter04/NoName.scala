package chapter04

/**
  * @Description:
  * @Date 下午1:46 2019/1/7
  * @Author: joker
  */
class NoName {

  var x = 0
  var y = 0

  x
  + y
  //会被解析为 x   + y。
  (x
  + y)

  x + //当使用中缀(infix)操作符时，scala风格是将其放在行尾而不是行首
  y

}