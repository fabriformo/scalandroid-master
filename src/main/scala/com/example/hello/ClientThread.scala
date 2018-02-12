package com.example.hello

import android.os.Handler
import java.io._

import scala.concurrent.Future
import java.net._

import android.os.Bundle
import mymessage.MyMessage

import scala.concurrent.ExecutionContext.Implicits.global

class ClientThread(var handler:Handler) extends Thread {

  var socket: Socket=_
  //modifica
  var out: ObjectOutputStream= _
  var in: ObjectInputStream= null
  var stopped= false
  var i: Int = 1
  var update: Boolean = false
  var updateMessage: MyMessage = _

  override def run(): Unit = {
    val serverAddr = InetAddress.getByName("151.97.150.86")
    socket = new Socket(serverAddr,4000)
    out = new ObjectOutputStream(socket.getOutputStream)
    in = new ObjectInputStream(socket.getInputStream)
    Future {
      while (!stopped) {
        //out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream)), true)
        //out.println("a")

        if(socket.getInputStream.available() > 0){
          //in = new BufferedReader(new InputStreamReader(socket.getInputStream))
          val obj = in.readObject()
          notifyMessage(obj.asInstanceOf[MyMessage])
        }
        //out.writeObject(new MyMessage(i,"c",0))
        //i+=1
        out.flush()
        if(update) {
          out.writeObject(updateMessage)
          //out.flush()
          update=false
        }
        Thread.sleep(500)
      }
      out.writeObject(new MyMessage(0,"",0))
    }
  }

  def notifyMessage(obj: MyMessage): Unit = {
    val msg = handler.obtainMessage
    val b = new Bundle
    //b.putString("refresh", "" + str)
    b.putSerializable("refresh", obj)
    msg.setData(b)
    handler.sendMessage(msg)
  }

  def invia(m:MyMessage): Unit = {
    //Thread.sleep(600)
    //out.writeObject(m)
    update = true
    updateMessage = m
  }


  def finisci(): Unit ={
    stopped=true
    Thread.sleep(600)
    socket.close()
  }

}
