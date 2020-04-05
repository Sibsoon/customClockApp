package com.example.customclockapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.util.Log

/**
 * Implementation of App Widget functionality.
 */
class CustomTimeWidget : AppWidgetProvider() {

    var timeStr: String = "00:00"
    var dateStr: String = ""

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, timeStr, dateStr)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        timeStr = intent?.getStringExtra("time")!!
        dateStr = intent.getStringExtra("date")!!
        super.onReceive(context, intent)
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    timeStr: String, dateStr: String
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.custom_time_widget)
    views.setTextViewText(R.id.showTime, timeStr)
    views.setTextViewText(R.id.showDate, dateStr)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
