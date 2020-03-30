package com.example.customclockapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class CustomTimeWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    //val intent = Intent()
    //var timeStr = intent.getStringExtra("time")
    //var dateStr = intent.getStringExtra("date")



    //**********************************

    var timeStr = "12:34"
    var dateStr = "tuesday, 22 June"

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.custom_time_widget)
    views.setTextViewText(R.id.showTime, timeStr)
    views.setTextViewText(R.id.showDate, dateStr)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}