package mymessage

class MyMessage(_tipo:Int, _message:String, _topic:Int) extends Serializable {
  def tipo: Int = _tipo
  def message: String = _message
  def topic: Int = _topic
}

