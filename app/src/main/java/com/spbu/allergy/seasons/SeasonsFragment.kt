package com.spbu.allergy.seasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.spbu.allergy.R
import com.spbu.allergy.buisnessLogic.Data
import com.spbu.allergy.buisnessLogic.UpdatableFragment
import kotlinx.android.synthetic.main.fragment_seasons.*
import java.text.DateFormatSymbols
import java.util.*
import kotlin.random.Random


class SeasonsFragment : Fragment(), UpdatableFragment {

    var sampleData = mutableMapOf<GregorianCalendar, MutableMap<String, Array<Float>>>()
    private var riskRanges: Array<Pair<Calendar, Calendar>> = arrayOf()
    private var choseMonth = -1
    private val allergenList = arrayOf("Bereza", "Lipa", "Doob", "Olha", "Topol", "Sosna")
    private val monthList = Array(12) { i -> DateFormatSymbols(Locale.ENGLISH).months[i] }
    private var choseAllergen = allergenList.first()

    private fun generateRandomSampleData(allergenList: Array<String>): MutableMap<GregorianCalendar, MutableMap<String, Array<Float>>> {
        val sampleData = mutableMapOf<GregorianCalendar, MutableMap<String, Array<Float>>>()
        val currentYear = GregorianCalendar.getInstance()[Calendar.YEAR]
        for (month in 1..12) {
            val daysInMonth =
                GregorianCalendar(currentYear, month - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH)
            for (day in 1..daysInMonth) {
                val keyDate = GregorianCalendar(currentYear, month - 1, day)
                sampleData[keyDate] = mutableMapOf()

                allergenList.map { allergen ->
                    sampleData.getValue(keyDate)[allergen] =
                        Array(10) { Random.nextFloat() }
                }.toTypedArray()
            }
        }
        return sampleData
    }

    private fun getRiskRangesFromData(
        data: Map<GregorianCalendar, Map<String, Array<Float>>>,
        allergen: String,
        concentrationLimit: Float
    ): Array<Pair<Calendar, Calendar>> {
        val rangesList: MutableList<Pair<Calendar, Calendar>> = mutableListOf()
        var isLastRangeClosed = true
        data.keys.forEach {
            val concentrationValue = data.getValue(it).getValue(allergen)[0]
            if (concentrationValue >= concentrationLimit)
                if (isLastRangeClosed) {
                    rangesList.add(it.clone() as Calendar to it.clone() as Calendar)
                    isLastRangeClosed = false
                } else rangesList[rangesList.lastIndex] =
                    rangesList.last().first to it.clone() as Calendar//getInstance().timeInMillis=it.timeInMillis
            else isLastRangeClosed = true
        }
        return rangesList.toTypedArray()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_seasons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sampleData = generateRandomSampleData(allergenList)
        initFloweringCalendar()
        initSpinners(allergenList)
    }

    private fun initFloweringCalendar() {

        val onMonthClickListener: View.OnClickListener = View.OnClickListener { month: View ->
            run {
                var monthNumber = -1
                (calendarPlace.children.first() as TableLayout).children.forEach {
                    val monthPos = (it as TableRow).indexOfChild(month)
                    if (monthPos != -1) {
                        monthNumber = (calendarPlace.children.first() as TableLayout)
                            .indexOfChild(it) * 3 + monthPos
                        choseMonth = monthNumber
                        monthSpinner.visibility = View.VISIBLE
                        monthSpinner.setSelection(monthNumber, true)
                        return@forEach
                    }
                }
                calendarPlace.removeAllViews()
                calendarPlace.addView(
                    CustomCalendar(this.context!!).getMonth(
                        monthNumber = monthNumber,
                        highlightRanges = riskRanges,
                        isBigSize = true
                    )
                )
            }
        }

        riskRanges = getRiskRangesFromData(sampleData, choseAllergen, 0.7f)
        calendarPlace.removeAllViews()

        if (choseMonth == -1)
            calendarPlace.addView(
                CustomCalendar(this.context!!).getYear(
                    floweringRanges = riskRanges,
                    onMonthClickListener = onMonthClickListener
//                floweringRanges = arrayOf(
//                    GregorianCalendar(2019, 3, 25) to GregorianCalendar(2019, 7, 18),
//                    GregorianCalendar(2019, 6, 2) to GregorianCalendar(2019, 9, 3)
//            )
                )
            )
        else calendarPlace.addView(
            CustomCalendar(this.context!!).getMonth(
                monthNumber = choseMonth,
                highlightRanges = riskRanges,
                isBigSize = true
            )
        )
    }

    private fun initSpinners(allergenList: Array<String>) {
        val allergenSpinnerAdapter = ArrayAdapter<String>(
            this.context!!, R.layout.spinner_item, allergenList
        )
        allergenSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        allergenSpinner.adapter = allergenSpinnerAdapter
        allergenSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                selectedItem: View?,
                selectedItemPosition: Int,
                selectedItemID: Long
            ) {
                if (selectedItem != null) {
                    choseAllergen = (selectedItem as TextView).text.toString()
                    initFloweringCalendar()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        val monthSpinnerAdapter =
            ArrayAdapter<String>(this.context!!, R.layout.spinner_item, monthList)
        monthSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        monthSpinner.adapter = monthSpinnerAdapter
        monthSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                selectedItem: View?,
                selectedItemPosition: Int,
                selectedItemID: Long
            ) {
                if (selectedItem != null) {
                    choseMonth = selectedItemPosition
                    initFloweringCalendar()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        if (choseMonth == -1) monthSpinner.visibility = View.GONE
        else monthSpinner.visibility = View.VISIBLE
    }


    override fun update(currentData: Data) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onBackPressed(): Boolean {
        if (choseMonth != -1) {
            choseMonth = -1
            monthSpinner.visibility = View.GONE
            initFloweringCalendar()
            return true
        }
        return false
    }
}