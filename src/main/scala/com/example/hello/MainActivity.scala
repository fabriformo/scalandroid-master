package com.example.hello

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.{Button, CheckBox, EditText, TextView, Toast}
import android.view.View
import android.view.View.OnClickListener
import mymessage.MyMessage
//import android.support.v7.app.AppCompatActivity
//import android.graphics.drawable.Animatable
//import com.example.hello.TypedResource.TypedActivity

class MainActivity extends Activity  {
    // allows accessing `.value` on TR.resource.constants
    implicit val context = this
    var target1: Boolean = false
    var target2: Boolean = false
    var connect: Boolean = false
    var thread: ClientThread= _

    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val handler = new MyHandler

        val button1 = findViewById(R.id.button1).asInstanceOf[Button]
        val button2 = findViewById(R.id.button).asInstanceOf[Button]
        val button3 = findViewById(R.id.button3).asInstanceOf[Button]
        val button4 = findViewById(R.id.button4).asInstanceOf[Button]
        val button5 = findViewById(R.id.button5).asInstanceOf[Button]
        val textviewTemp = findViewById(R.id.textViewTemp).asInstanceOf[TextView]
        val textviewPres = findViewById(R.id.textViewPres).asInstanceOf[TextView]
        val textviewValTemp = findViewById(R.id.textViewValTemp).asInstanceOf[TextView]
        val textviewValPres = findViewById(R.id.textViewValPres).asInstanceOf[TextView]
        val textviewModifica = findViewById(R.id.textViewModifica).asInstanceOf[TextView]
        val editTextTemp = findViewById(R.id.editText).asInstanceOf[TextView]
        val editTextPres = findViewById(R.id.editTexPres).asInstanceOf[TextView]

        button1.setOnClickListener(new View.OnClickListener {
            override def onClick(v: View): Unit = {
                if(!connect)
                {
                    thread = new ClientThread(handler)
                    thread.start()
                    connect = true
                    button1.setText("DISCONNETTI")
                    textviewTemp.setVisibility(View.VISIBLE)
                    textviewPres.setVisibility(View.VISIBLE)
                    button4.setVisibility(View.VISIBLE)
                    button5.setVisibility(View.VISIBLE)
                }
                else
                {
                    thread.finisci()
                    connect = false
                    button1.setText("CONNETTI")
                    textviewTemp.setVisibility(View.INVISIBLE)
                    textviewPres.setVisibility(View.INVISIBLE)
                    button4.setVisibility(View.INVISIBLE)
                    button5.setVisibility(View.INVISIBLE)
                    textviewModifica.setVisibility(View.INVISIBLE)
                    editTextTemp.setVisibility(View.INVISIBLE)
                    textviewValTemp.setVisibility(View.INVISIBLE)
                    button2.setVisibility(View.INVISIBLE)
                    button3.setVisibility(View.INVISIBLE)
                    editTextPres.setVisibility(View.INVISIBLE)
                    textviewValPres.setVisibility(View.INVISIBLE)
                    button4.setText("ATTIVA")

                }
            }
        })

        button4.setOnClickListener(new OnClickListener {
            override def onClick(v: View): Unit = {
                if(!target1){
                    target1 = true
                    thread.invia(new MyMessage(1,"",1))
                    textviewModifica.setVisibility(View.VISIBLE)
                    editTextTemp.setVisibility(View.VISIBLE)
                    textviewValTemp.setVisibility(View.VISIBLE)
                    button2.setVisibility(View.VISIBLE)
                    button4.setText("DISATTIVA")
                    //thread.invia(new MyMessage(44,"ff",0))
                }
                else{
                    target1 = false
                    thread.invia(new MyMessage(3,"",1))
                    //textviewModifica.setVisibility(View.INVISIBLE)
                    editTextTemp.setVisibility(View.INVISIBLE)
                    textviewValTemp.setVisibility(View.INVISIBLE)
                    button2.setVisibility(View.INVISIBLE)
                    button4.setText("ATTIVA")
                }
            }
        })

        button5.setOnClickListener(new OnClickListener {
            override def onClick(v: View): Unit = {
                if(!target2){
                    target2 = true
                    thread.invia(new MyMessage(1,"",2))
                    textviewModifica.setVisibility(View.VISIBLE)
                    editTextPres.setVisibility(View.VISIBLE)
                    textviewValPres.setVisibility(View.VISIBLE)
                    button3.setVisibility(View.VISIBLE)
                    button5.setText("DISATTIVA")
                }
                else{
                    target2 = false
                    thread.invia(new MyMessage(3,"",2))
                    //textviewModifica.setVisibility(View.INVISIBLE)
                    editTextPres.setVisibility(View.INVISIBLE)
                    textviewValPres.setVisibility(View.INVISIBLE)
                    button3.setVisibility(View.INVISIBLE)
                    button5.setText("ATTIVA")
                }
            }
        })

        button2.setOnClickListener(new OnClickListener {
            override def onClick(v: View): Unit = {
                val m = new MyMessage(2,findViewById(R.id.editText).asInstanceOf[EditText].getText.toString,1)
                thread.invia(m)
            }
        })

        button3.setOnClickListener(new OnClickListener {
            override def onClick(v: View): Unit = {
                val m = new MyMessage(2,findViewById(R.id.editTexPres).asInstanceOf[EditText].getText.toString,2)
                thread.invia(m)
            }
        })


        //thread = new ClientThread()
        //thread.start()
    }

    override def onBackPressed(): Unit = {
        thread.finisci()
        super.onBackPressed()
    }

    class MyHandler extends Handler {
        override def handleMessage(msg: Message): Unit = {
            val bundle = msg.getData
            if (bundle.containsKey("refresh")) {
                //val value = bundle.getString("refresh")
                val value = bundle.getSerializable("refresh")
                if(value.asInstanceOf[MyMessage].topic == 1)
                    findViewById(R.id.textViewValTemp).asInstanceOf[TextView].setText(value.asInstanceOf[MyMessage].message)
                else if(value.asInstanceOf[MyMessage].topic == 2)
                    findViewById(R.id.textViewValPres).asInstanceOf[TextView].setText(value.asInstanceOf[MyMessage].message)
            }
        }
    }
}