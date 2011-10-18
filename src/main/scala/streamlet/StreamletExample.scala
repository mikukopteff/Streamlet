package streamlet

class StreamletExample extends StreamletForWowza {

  function("myfunction") {
    println("my function called")
  }

  play(){
    println("play called")
  }

  connect(){
    println("client connected")
  }

  disconnect(){
    println("disconnected client")
  }
}