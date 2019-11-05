package com.spbu.allergy.seasons

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import java.text.DateFormatSymbols
import java.util.*

class CustomCalendar(private val context: Context) {

    fun getYear(
        year: Int = Calendar.getInstance()[Calendar.YEAR],
        rowsAndColumns: Pair<Int, Int> = 4 to 3,
        floweringRanges: Array<Pair<Calendar, Calendar>> = emptyArray()
    ): TableLayout {

        val annualCalendar = TableLayout(context)
        for (row in 1..rowsAndColumns.first) {
            val monthsRow = TableRow(context)
            for (column in 1..rowsAndColumns.second) {
                monthsRow.addView(
                    getMonth(
                        3 * (row - 1) + column - 1,
                        highlightRanges = floweringRanges
                    )
                )
            }
            annualCalendar.addView(monthsRow)
        }
        annualCalendar.isStretchAllColumns = true
        return annualCalendar
    }

    fun getMonth(
        monthNumber: Int = Calendar.getInstance()[Calendar.MONTH],
        year: Int = Calendar.getInstance()[Calendar.YEAR],
        highlightRanges: Array<Pair<Calendar, Calendar>> = emptyArray(),
        isBigSize: Boolean = false
    ): TableLayout {

        val monthTable = TableLayout(context)
        val firstDayOfMonth =
            GregorianCalendar(Calendar.getInstance()[Calendar.YEAR], monthNumber, 1)
        var firstDayIndex = (firstDayOfMonth[Calendar.DAY_OF_WEEK] + 6) % 7
        if (firstDayIndex == 0) firstDayIndex = 7
        val dayCount = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val extraDays = 1 - firstDayIndex
        val highLightDays = getHighLightDays(highlightRanges, monthNumber, dayCount)
        val textSize = if (isBigSize) 30f else 13f
        val color = Color.parseColor("#7F393F")


        for (row in 1..6) {
            val week = TableRow(context)
            for (day in 1..7) {
                val textView = TextView(context)
                val dayNumber = extraDays + 7 * (row - 1) + day
                if (dayNumber in 1..dayCount) {
                    textView.text = " $dayNumber "
                    if (highLightDays.contains(dayNumber))
                        textView.setBackgroundColor(color)
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(Color.WHITE)
                    textView.textSize = textSize
                    textView.setPadding(0, 2, 0, 0)
                } else textView.text = ""
                week.addView(textView)
            }
            monthTable.addView(week)
        }
        monthTable.setPadding(10)
        val tv = TextView(context)
        tv.setTextColor(Color.WHITE)
        tv.gravity = Gravity.CENTER
        tv.textSize = if (isBigSize) 50f else 20f
        tv.text = DateFormatSymbols(Locale.ENGLISH).months[monthNumber]
        tv.typeface = Typeface.DEFAULT_BOLD
        tv.setPadding(0, 0, 0, if (isBigSize) 20 else 5)
        monthTable.addView(tv, 0)
        return monthTable
    }

    private fun getHighLightDays(
        highlightRanges: Array<Pair<Calendar, Calendar>>,
        monthNumber: Int,
        dayCount: Int
    ): Set<Int> {
        var highLightDays = emptySet<Int>()
        highlightRanges.forEach { range ->

            if (range.first < range.second
                && range.first[Calendar.MONTH] <= monthNumber + 1
                && range.second[Calendar.MONTH] >= monthNumber + 1
            ) {
                val firstRangeDay =
                    if (range.first[Calendar.MONTH] < monthNumber + 1) 1 else range.first[Calendar.DAY_OF_MONTH]
                val lastRangeDay =
                    if (range.second[Calendar.MONTH] > monthNumber + 1) dayCount else range.second[Calendar.DAY_OF_MONTH]

                highLightDays = highLightDays.plus(firstRangeDay..lastRangeDay)
            }
        }
        return highLightDays
    }
}