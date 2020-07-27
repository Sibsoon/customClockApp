package com.example.customclockapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast


/**
 * Implementation of App Widget functionality.
 */
class CustomTimeWidget : AppWidgetProvider() {


    var customTime :CustomTime = CustomTime()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them


        customTime.updateMembers()
        var timeStr: String = customTime.getMTimeString()
        var dateStr: String = customTime.getDateString()

        //for (appWidgetId in appWidgetIds) {
        for (i in 0 until appWidgetIds.size){

            var appWidgetId: Int = appWidgetIds[i];

            // Create an Intent to launch Activity
            var intent: Intent = Intent(context, MainActivity::class.java)
            //var pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val pendingIntent = PendingIntent.getBroadcast(  context, 0, intent,0 )
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val views: RemoteViews = RemoteViews(context.getPackageName(), R.layout.custom_time_widget);

            views.setOnClickPendingIntent(R.id.showTime, pendingIntent);
            views.setOnClickPendingIntent(R.id.showTime,
                PendingIntent.getActivity(context, 0, intent, 0))

            views.setTextViewText(R.id.showTime, timeStr)
            views.setTextViewText(R.id.showDate, dateStr)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)




         }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onEnabled(context: Context) {
        Log.d("onEnable","CustomTimeWidget")
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
/*
        try {
            timeStr = intent?.getStringExtra("time")!!
            dateStr = intent?.getStringExtra("date")!!

        } catch (e: KotlinNullPointerException) {
            Log.d("Exception", e.toString())
        } finally {
            // optional finally block
        }*/
    }
}


