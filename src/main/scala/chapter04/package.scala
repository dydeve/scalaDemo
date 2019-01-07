/**
  * 类和对象
  *
  *
  * 自动分隔语句的规则：
  * 除非一下任意一条为true，否则代码行的末尾就会被当成分号处理
  *
  * 1. 当前行以不能作为语句结尾的词结尾，如英文句点或中缀操作符
  * 2. 下一行以不能作为语句开头的词开头
  * 3. 当前行的行尾出现圆括号(...)或方括号[...]
  *
  * scala比java更面向对象，类不允许有static成员，针对此类场景，提供了单例对象(singleton object)
  *
  * 注意：
  * scala在每个源码文件都隐式引入了java.lang 和 scala包的成员，以及名为Predef的单例对象的所有成员。
  *
  *
  *
  */
package object chapter04 {

}
