package com.ahmadfma.project_sistem_informasi.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

const val POST = 0
const val COMMENT = 1
const val STORY = 2

class DateHelper {
    companion object {

        fun formatToString(date: Date): String {
            Log.d("DateHelper", "formatToString: before format : $date")
            val format = "dd/MM/yyyy HH:mm:ss"
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            val date = formatter.format(date)
            Log.d("DateHelper", date)
            return date
        }

        fun getCurrentDateTime(): Date {
            return Calendar.getInstance().time
        }


        //cari selisih waktu
        fun findTimeDifference(postDate: String, type: Int): String {
            val currentDate = formatToString(getCurrentDateTime())
            val tempP = postDate.split(" ")
            val tempC = currentDate.split(" ")

            //post date & time
            val pDate = tempP[0].split("/")
            val pTime = tempP[1].split(":")

            //current date & time
            val cDate = tempC[0].split("/")
            val cTime = tempC[1].split(":")

            if(type == POST) {
                return when {
                    pDate[0] != cDate[0] -> { //check day
                        "${(cDate[0].toInt() - pDate[0].toInt()).absoluteValue} hari yang lalu"
                    }
                    pDate[1] != cDate[1] -> { //check month
                        "${(cDate[1].toInt() - pDate[1].toInt()).absoluteValue} bulan yang lalu"
                    }
                    pDate[2] != cDate[2] -> { //check year
                        "${(cDate[2].toInt() - pDate[2].toInt()).absoluteValue} tahun yang lalu"
                    }
                    pTime[0] != cTime[0] -> { // check hour
                        "${(cTime[0].toInt() - pTime[0].toInt()).absoluteValue} jam yang lalu"
                    }
                    pTime[1] != cTime[1] -> { //check minute
                        "${(cTime[1].toInt() - pTime[1].toInt()).absoluteValue} menit yang lalu"
                    }
                    pTime[2] != cTime[2] -> { //check second
                        "${(cTime[2].toInt() - pTime[2].toInt()).absoluteValue} detik yang lalu"
                    }
                    else -> {
                        "1 detik yang lalu"
                    }
                }
            } else {
                return when {
                    pDate[0] != cDate[0] -> { //check year
                        "${(cDate[0].toInt() - pDate[0].toInt()).absoluteValue} tahun"
                    }
                    pDate[1] != cDate[1] -> { //check month
                        "${(cDate[1].toInt() - pDate[1].toInt()).absoluteValue} bulan"
                    }
                    pDate[2] != cDate[2] -> { //check day
                        "${(cDate[2].toInt() - pDate[2].toInt()).absoluteValue} hari"
                    }
                    pTime[0] != cTime[0] -> { // check hour
                        "${(cTime[0].toInt() - pTime[0].toInt()).absoluteValue} jam"
                    }
                    pTime[1] != cTime[1] -> { //check minute
                        "${(cTime[1].toInt() - pTime[1].toInt()).absoluteValue} menit"
                    }
                    pTime[2] != cTime[2] -> { //check second
                        "${(cTime[2].toInt() - pTime[2].toInt()).absoluteValue} detik"
                    }
                    else -> {
                        "1 detik"
                    }
                }
            }
        }
    }
}