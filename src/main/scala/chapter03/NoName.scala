package chapter03

import scala.collection.immutable.HashSet
import scala.collection.mutable
import scala.io.Source

/**
  * @Description:
  * @Date 下午5:34 2019/1/6
  * @Author: joker
  */
class NoName {

  val args = Array("", "", "")
  /*val greetStrings = new Array[String](3)

  greetStrings(0) = "Hello"
  greetStrings(1) = ", "
  greetStrings(2) = "world!\n"
  greetStrings.update(2, "world!\n")*/

  //val greetStrings = Array(1, 2, 3)
  val greetStrings = Array.apply(1, 2, 3) //伴生对象 companion object

  for (i <- 0 to 2) {
    print(greetStrings(i))
    println(greetStrings.apply(i))
  }

  val oneTwo = List(1, 2)
  val threeFour = List(3, 4)
  val oneTwoThreeFour = oneTwo ::: threeFour
  println(oneTwo + " and " + threeFour + " were not mutated.")
  println("Thus, " + oneTwoThreeFour + " is a new list.")

  val twoThree = List(2, 3)
  val oneTwoThree = 1 :: twoThree // = twoThree.::(1)  以：结尾，都是右操作元
  println(oneTwoThree) //实际new了List，保证了不可变
  //console: List(1, 2, 3)

  val one_Two_Three = 1 :: 2 :: 3 :: Nil
  println(one_Two_Three)

  //tuple
  val pair: Tuple2[Int, String] = (99, "Luftballons")
  println(pair._1)
  println(pair._2)

  //set
  ////immutable
  var jetSet = Set("Boeing", "Airbus") //must be var
  jetSet += "Lear"
  println(jetSet.contains("Cessna"))
  ////mutable
  val movieSet = mutable.Set("Hitch", "Poltergeist") //can be val
  movieSet += "Shrek"
  println(movieSet)

  val hashSet = HashSet("Tomatoes", "Chilies")
  println(hashSet + "Coriander")

  //map
  val treasureMap = mutable.Map[Int, String]()
  treasureMap += (1 -> "Go to island.")
  val tp: Tuple2[Int, String] = (1).->("Go to island.")
  //scala允许任何对象调用->，即"隐式转换 impllict conversion" see in chapter21
  treasureMap += (2 -> "Find big X on ground.")
  treasureMap += (3 -> "Dig.")
  println(treasureMap(2))

  val romanNumeral = Map(1 -> "I", 2 -> "II", 3 -> "III", 4 -> "IV", 5 -> "V")
  println(romanNumeral(4))


  //functional
  def printArgs(args: Array[String]): Unit = {
    var i = 0 //指令型
    while (i < args.length) {
      println(args(i))
      i += 1
    }
  }

  ////not cure functional,
  def printArgs1(args: Array[String]): Unit = {
    //有副作用的函数式的特征，返回值是unit，不返回任何有意义的值
    for (arg <- args) {
      println(arg) //副作用，
    }
    //or
    args.foreach(println)
  }

  //no var or 副作用
  def formatArgs(args: Array[String]) = args.mkString("\n")


  //每个有用的程序都有副作用，不然对外部世界没有价值。
  //鼓励副作用最小，方便测试
  println(formatArgs(null))

  val res = formatArgs(Array("zero", "one", "two"))
  assert(res == "zero\none\ntwo")

  //read line from text
  if (args.length > 0) {
    for (line <- Source.fromFile(args(0)).getLines()) {
      // Iterator[String]
      println(line.length + " " + line)
    }
  }
  /**
    * scala countchars1.scala countchars1.scala
    *
    * 22 import scala.io.Source
    * 0
    * 22 if (args.length > 0) {
    * 0
    * 51   for (line <- Source.fromFile(args(0)).getLines())
    * 37     println(line.length + " " + line)
    * 1 }
    * 4 else
    * 46   Console.err.println("Please enter filename")
    */

  //想格式化，就得遍历两次。所以赋值给val变量，防止io两次，这样又会导致文本内容被全部存到内存里
  val lines = Source.fromFile(args(0)).getLines().toList//迭代器迭代一次就会消耗，所以用list

  def widthOfLength(s: String) = s.length

  /*var maxLength = 0
  for (line <- lines) {
    maxLength = maxLength.max(widthOfLength(line))
  }*/
  val longestLine = lines.reduceLeft(
    (a, b) => if (a.length > b.length) a else b
  )

  val maxWidth = widthOfLength(longestLine)

  for (line <- lines) {
    val numSpaces = maxWidth - widthOfLength(line)
    val padding = " " * numSpaces
    println(padding + line.length + " | " + line)
  }

}
