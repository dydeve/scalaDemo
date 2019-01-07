package chapter05

/**
  * @Description:
  * @Date 下午3:43 2019/1/7
  * @Author: joker
  */
class NoName {

  val escapes = "\\\"\'"//escapes: String = \"'

  //用三个"""表示原生字符：含有大量转义序列 或 跨多行 的字符串
  println("""Welcome to Ultamix 3000.
             Type "HELP" for help.""")
  /**
    * 字符串第二行前面的空格被包含在字符串里
    * Welcome to Ultamix 3000.
    *              Type "HELP" for help.
    */

  println("""|Welcome to Ultamix 3000.
             |Type "HELP" for help.""".stripMargin)
  /**
    * Welcome to Ultamix 3000.
    * Type "HELP" for help.
    */

  //字符串插值器  $
  val name = "reader"
  println(s"hello, $name!")//先求值，然后调用toString

  println(s"the answer is ${6 * 7}.")

  //字符串插值器  raw  不支持转义
  println(raw"no\\\\escape")

  //字符串插值器  f  可以加上printf风格的指令。指令放在表达式之后，以%开始，使用java.util.Formatter中的语法
  println(f"${math.Pi}%.5f")//3.14159

  //如果不给表达式格式化指令， f默认使用%s
  val pi = "Pi"
  println(f"pi is approximately {math.Pi}%.8f.")

  //字符串插值通过在编译期重写代码实现

  val s = "hello world"
  s indexOf "o"//操作符表示法

  s indexOf ("o", 5)//任何方法都是操作符

  1 + 1
  1.+(1)//操作符即方法

  //前缀、后缀，unary_
  -2.0
  (2.0).unary_-

  //隐式转换 implicit conversion
  0 max 5//RichInt
  -2.7 abs//RichDouble

}
