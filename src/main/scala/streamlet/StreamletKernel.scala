package streamlet

import scala.util.DynamicVariable
import scala.collection.mutable.HashMap

trait Streamlet {

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
         case ex:NoSuchElementException => println(request + " called - not mapped")
       }
  }

  def play()(fun: => Any) = Map.put(("play", ""), x => fun.toString)
  def function(name: String)(fun: => Any) = Map.put(("function", name), x => fun.toString)
  def connect()(fun: => Any) = Map.put(("connect", ""), x => fun.toString)
  def disconnect()(fun: => Any) = Map.put(("disconnect", ""), x => fun.toString)
  def streamCreate()(fun: => Any) = Map.put(("streamCreate", ""), x => fun.toString)
  def streamDestroy()(fun: => Any) = Map.put(("streamDestroy", ""), x => fun.toString)
}