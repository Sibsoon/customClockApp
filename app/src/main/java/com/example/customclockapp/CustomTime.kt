package com.example.customclockapp

import android.util.Log
import java.util.*
import kotlin.properties.Delegates

class CustomTime() {

    var currentMilSec: Long by Delegates.observable(
        System.currentTimeMillis()
    )
    { _, oldValue, newValue -> onCurrentMilSecChanged?.invoke(oldValue, newValue) }

    var onCurrentMilSecChanged : ((Long, Long) -> Unit)? = null

    var second = 0
    var minute = 0
    var hour = 0
    var day = 0
    var month = 0
    var year= 0

    fun updateMembers() {
        this.currentMilSec = System.currentTimeMillis() - POINT_ZERO
        //Log.d("milisec",currentMilSec.toString())
        this.second = (this.currentMilSec/1000 * 6/5).toInt()
        this.minute = this.second/60
        this.hour = 9 + (this.minute /60)
        this.day = this.hour/24
        this.year = 2020 + (this.day /365)
    }

    fun getTimeString(): String {

        var timeString: String = getMTimeString() +":"
        if(this.second % 60 < 10){
            timeString += "0"
        }
        timeString += (this.second % 60).toString()
        return timeString
    }

    fun getMTimeString(): String{
        var timeString = ""
        if (this.hour%24 < 10) {
            timeString += "0"
        }
        timeString += (this.hour%24).toString() +":"
        if(this.minute%60 <10){
            timeString += "0"
        }
        timeString += (this.minute%60).toString()
        return  timeString
    }

    fun getDateString(): String {
        return WEEK_DAYS[(this.day%8).toInt()]+ " " + (20 + (this.day%31)).toString() + " Juli, " + this.year.toString()
    }

}