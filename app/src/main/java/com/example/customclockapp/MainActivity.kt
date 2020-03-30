package com.example.customclockapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.properties.Delegates.observable


class MainActivity : AppCompatActivity() {

    var run = true //set it to false if you want to stop the timer
    var mHandler: Handler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val showButton = findViewById<ImageButton>(R.id.imageButton)
        showButton.setOnClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        }

        //***************************************************33

        val showTime: TextView = findViewById<TextView>(R.id.showTime)
        val showDate: TextView = findViewById<TextView>(R.id.showDate)
        var customTime :CustomTime = CustomTime()

        customTime.onTimeChanged = { oldValue, newValue ->
            showTime.setText(newValue)
        }
        customTime.onDateChanged = { oldValue, newValue ->
            showDate.setText(newValue)
        }
        timer(customTime)
        //**********************widget************************
        //val intent = Intent(this@MainActivity, CustomTimeWidget::class.java)
        //intent.putExtra("time", customTime.timeStr)
        //intent.putExtra("date", customTime.dateStr)
        //startActivity(intent)     //breaks everything

    }




    private fun timer(customTime : CustomTime) {
        Thread(Runnable {
            while (run) {
                try {
                    Thread.sleep(100)
                    mHandler.post(Runnable {
                        val c = Calendar.getInstance()
                        customTime.timeStr = "${c[Calendar.HOUR_OF_DAY]}:${c[Calendar.MINUTE]}:${c[Calendar.SECOND]}"
                        customTime.dateStr = "${c.getDisplayName(c[Calendar.DAY_OF_WEEK],Calendar.LONG,Locale.getDefault())}, ${c[Calendar.DAY_OF_MONTH]} ${c.getDisplayName(c[Calendar.MONTH],Calendar.LONG, Locale.getDefault())}"
                    })
                } catch (e: Exception) {
                }
            }
        }).start()
    }

}

class CustomTime {
    val c = Calendar.getInstance()
    var timeStr: String by observable("${c[Calendar.HOUR_OF_DAY
    ]}:${c[Calendar.MINUTE]}:${c[Calendar.SECOND]}") { _, oldValue, newValue ->
        onTimeChanged?.invoke(oldValue, newValue)
    }
    var dateStr: String by observable(
        "${c.getDisplayName(c[Calendar.DAY_OF_WEEK],Calendar.LONG,Locale.getDefault())}, ${c[Calendar.DAY_OF_MONTH]} ${c.getDisplayName(c[Calendar.MONTH],Calendar.LONG, Locale.getDefault())}")
        { _, oldValue, newValue -> onDateChanged?.invoke(oldValue, newValue) }


    var onTimeChanged: ((String, String) -> Unit)? = null
    var onDateChanged: ((String, String) -> Unit)? = null
}
