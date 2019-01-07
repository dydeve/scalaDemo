/**
  * 基础类型和操作
  *
  * scala的==会判断左侧是否为null，不为null，会调用equals方法
  *
  * java中==，比较基本类型的值的相等性，比较引用的引用相等性(reference equality)(两个变量指向JVM堆上的同一个对象)
  * scala中比较引用相等性用 eq 方法，不相等用 neq 方法
  *
  * 每个基础类型，都有对应的"富包装类"：RichByte RichLong，StringOps等
  */
package object chapter05 {

}
