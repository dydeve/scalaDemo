package chapter04

import scala.collection.mutable

/**
  * 类和伴生对象可以互相访问私有成员
  */
class CheckSumAccumulator {//伴生类 companion class

  private var sum = 0

  /*def add(b: Byte): Unit = {
    //b = 1 不能编译，因为scala方法参数是val，因为更容易推敲，不像var需进一步查证是否被重新赋值过
    sum += b
  }

  def checkSum(): Int = {
    ~(sum & 0xFF) + 1
  }*/

  /**
    * 副作用通常指：改变方法外部的状态 或 执行I/O操作
    * 仅仅因为副作用而被执行的方法 称为 过程(procedure)
    * @param b
    */
  def add(b: Byte): Unit = sum += b

  def checksum(): Int = ~(sum & 0xFF) + 1
}

//伴生对象 companion object
object CheckSumAccumulator {

  /**
    * 以牺牲内存换取计算时间，提升性能
    * 也可使用弱引用的映射，如scala.collection.jcl的WeakHashMap，内存吃紧时，缓存中的条目可被垃圾回收掉
    */
  private val cache = mutable.Map.empty[String, Int]

  def calculate(s: String): Int = {
    if (cache.contains(s))
      cache(s)
    else {
      val acc = new CheckSumAccumulator
      for (c <- s)
        acc.add(c.toByte)//Char
      val cs = acc.checksum()
      cache += (s -> cs)
      cs
    }


  }
}
