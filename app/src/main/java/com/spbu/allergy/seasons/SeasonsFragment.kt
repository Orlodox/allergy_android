package com.spbu.allergy.seasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.spbu.allergy.R
import com.spbu.allergy.buisnessLogic.Data
import com.spbu.allergy.buisnessLogic.UpdatableFragment
import kotlinx.android.synthetic.main.fragment_seasons.*
import java.util.*


class SeasonsFragment : Fragment(), UpdatableFragment {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seasons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAnnualCalendar()
    }

    private fun initAnnualCalendar() {
        calendarPlace.addView(
            CustomCalendar(this.context!!).getYear(
                floweringRanges = arrayOf(
                    GregorianCalendar(2019, 3, 25) to GregorianCalendar(2019, 7, 18),
                    GregorianCalendar(2019, 6, 2) to GregorianCalendar(2019, 9, 3)
                )
            )
        )
    }


    override fun update(currentData: Data) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}