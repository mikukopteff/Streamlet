package streamlet

import scala.util.DynamicVariable
import scala.collection.mutable.HashMap

abstract class Streamlet {

  type Params = java.util.Map[String,Array[String]]
  val Map = new HashMap[(String, String), ( Any => String)]()
  val paramsMap = new DynamicVariable[Params](null)

  def params(name:String):String = paramsMap.value.get(name)(0)

  def doServerSideFunction(request: String, fun: String) {
      try {
        //paramsMap.withValue(request.getParameterMap.asInstanceOf[Params]) {
          Map(request, fun)()
        //}
      }
       catch {
         case ex:NoSuchElementException => println("requesting "+request+" "+ fun+" but only have "
                                                   +Map)
       }
  }

  def play()(fun: =>Any) = Map.put(("play", ""), x => fun.toString)
  def function(name: String)(fun: => Any) = Map.put(("function", name), x => fun.toString)
}