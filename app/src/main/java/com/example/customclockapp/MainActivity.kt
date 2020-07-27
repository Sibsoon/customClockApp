package com.example.customclockapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

//const val POINT_ZERO = 1594218958855    //8.jul.2020 16.35
const val POINT_ZERO = 1595228400000      //20.juli.2020. 9:00am

val WEEK_DAYS = mapOf<Int,String>(0 to "Mäntig", 1 to "Dsistig", 2 to "Mittwuch", 4 to "Donnstig", 5 to "Fritig", 3 to "Plutig", 6 to "Samstig", 7 to "Sunntig")

var mp: MediaPlayer? = null

class MainActivity : AppCompatActivity() {

    var run = true //set it to false if you want to stop the timer
    var mHandler: Handler = Handler()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    var context: Context? = null
    private var alarmSet: Boolean = false
    var customTime :CustomTime = CustomTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate","MainActivity")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val showButton = findViewById<ImageButton>(R.id.imageButton)
        showButton.setOnClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        }*/

        //***************************************************

        val showTime: TextView = findViewById<TextView>(R.id.showTime)
        val showDate: TextView = findViewById<TextView>(R.id.showDate)
        //var customTime :CustomTime = CustomTime()

        customTime.onCurrentMilSecChanged = { oldValue, newValue ->
            showTime.setText(customTime.getTimeString())
            showDate.setText(customTime.getDateString())
        }

        //******************widget initialization*************
        val man = AppWidgetManager.getInstance(this)
        val ids = man.getAppWidgetIds(
            ComponentName(this, CustomTimeWidget::class.java)
        )
        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra("time",  customTime.getTimeString())
        updateIntent.putExtra("date", customTime.getDateString())
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(updateIntent)


        timer(customTime)

        /********************ALARM SETUP **********************/
        // put in Onclick function or something
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        context = applicationContext


        val timePicker = findViewById<TimePicker>(R.id.timePicker)

    }





    // private fun setAlarm(context: Context, requestId: Int){
   @RequiresApi(Build.VERSION_CODES.M)
   fun setAlarm(view: View){
        val myIntent: Intent = Intent(context,AlarmReceiver::class.java)
      // alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
      // alarmIntent = PendingIntent.getService(context, 0, myIntent, 0)
       alarmIntent = PendingIntent.getBroadcast(context,0,myIntent, 0)

        val hrs: Int = customTime.hour %24
        val min: Int = customTime.minute %60

       val timePicker: TimePicker = findViewById<TimePicker>(R.id.timePicker)
        val setHrs = timePicker.hour
        val setMin = timePicker.minute

        //Toast.makeText(context, "custom: $hrs : $min, set: $setHrs : $setMin ",Toast.LENGTH_LONG).show();
        var leftHrs: Int = 0
        var leftMin: Int = 0
        if(setHrs < hrs) {
            leftHrs = 23 - (hrs - setHrs)
        }else if(setHrs == hrs){
            if(setMin < min){
                leftHrs = 23 - (hrs - setHrs)
            }else{
                leftHrs = setHrs - hrs
            }
        } else{
            leftHrs = setHrs - hrs
        }
        if(setMin < min){
            leftMin = 60 - (min - setMin)
        }else{
            leftMin = setMin - min
        }
        //set a repeated alarm
       /*
        var timeToRepeat: Long = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES
        alarmMgr?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            timeToRepeat, //repeat time
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent
        )*/
        var seconds = leftHrs * 3600 + leftMin*60
        seconds = seconds*6/5   //make it faster
        Toast.makeText(this, "Alarm set in $leftHrs hours and $leftMin minutes", Toast.LENGTH_SHORT).show()
       //set a single alarm
        var timeToRing: Long = SystemClock.elapsedRealtime() + seconds*1000
        Log.d("timeToRing",timeToRing.toString())
        Log.d("elapsedRealTime",SystemClock.elapsedRealtime().toString())
        Log.d("currentTimeMillis",System.currentTimeMillis().toString())
//        Toast.makeText(context, "Alarm in $seconds seconds",Toast.LENGTH_SHORT).show();
        //alarmMgr?.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToRing, alarmIntent)
        alarmMgr?.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  timeToRing, alarmIntent)
        //set alarm loading at boot
        /*
        val receiver = ComponentName(context!!, BootReceiver::class.java)
        context?.packageManager?.setComponentEnabledSetting( receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )*/
        alarmSet = true
    }

    fun cancelAlarm(view: View){
        Log.d("OnClick","cancelAlarm")
        //if(alarmSet) {


        //cancel alarm
        alarmMgr?.cancel(alarmIntent)

        //disable boot loading

        val receiver = ComponentName(context!!, BootReceiver::class.java)
        context?.packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        mp?.stop()
        alarmSet = false
        //}
    }

    fun setTimer(view: View) {
        // Do something in response to button click

        Log.d("OnClick","setTimer")
    }

    fun startStopwatch(view: View) {
        //TODO start a simple timer and show it
        Log.d("OnClick","startStopwatch")
    }
    fun stopStopwatch(view: View) {
        //TODO stop the counting of the stopwatch timer
        Log.d("OnClick","stopStopwatch")
    }
    fun resetStopwatch(view:View) {
        //TODO set stopwatch timer to 0:00

        Log.d("OnClick","resetStopwatch")
    }

    private fun timer(customTime : CustomTime) {
        Thread(Runnable {
            while (run) {
                try {
                    Thread.sleep(100)
                    mHandler.post(Runnable {

                        //customTime.currentMilSec = System.currentTimeMillis() - POINT_ZERO
                        customTime.updateMembers()
                        // Send a broadcast so that the Operating system updates the widget
                        val man = AppWidgetManager.getInstance(this)
                        val ids = man.getAppWidgetIds(
                            ComponentName(this, CustomTimeWidget::class.java)
                        )
                        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                        updateIntent.putExtra("time",  customTime.getTimeString())
                        updateIntent.putExtra("date", customTime.getDateString())
                        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                        sendBroadcast(updateIntent)
                    })
                } catch (e: Exception) {
                }
            }
        }).start()
    }

}

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.BOOT_COMPLETED"){
            //set alarm here
        }
    }
}

class AlarmReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context,"ALARM",Toast.LENGTH_SHORT).show();
        mp = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        //TODO while not disabled keep playing
        mp?.start()
    }
}
