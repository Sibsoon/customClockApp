package com.example.customclockapp

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
        Log.d("onCreate","MainActivity")
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
                        customTime.dateStr = "${c.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,Locale.getDefault())}, ${Calendar.DAY_OF_MONTH} ${c.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault())}"


                        // Send a broadcast so that the Operating system updates the widget
                        val man = AppWidgetManager.getInstance(this)
                        val ids = man.getAppWidgetIds(
                            ComponentName(this, CustomTimeWidget::class.java)
                        )
                        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                        updateIntent.putExtra("time", customTime.timeStr)
                        updateIntent.putExtra("date", customTime.dateStr)
                        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                        sendBroadcast(updateIntent)
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
