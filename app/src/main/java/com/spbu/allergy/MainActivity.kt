package com.spbu.allergy

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.spbu.allergy.main.MainFragment
import com.spbu.allergy.seasons.MapFragment
import com.spbu.allergy.seasons.SeasonsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    private lateinit var mainFragment: MainFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var seasonsFragment: SeasonsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMainData()
        onBottomMenuClickListener(R.id.bottomMenu_main)

    }

    private fun initMainData() {
        mainFragment = MainFragment()
        mapFragment = MapFragment()
        seasonsFragment = SeasonsFragment()
        bottomMenu.setOnNavigationItemSelectedListener { onBottomMenuClickListener(it.itemId) }
    }

    private fun onBottomMenuClickListener(menuItemID: Int): Boolean {
        when (menuItemID) {
            R.id.bottomMenu_main -> {
                supportFragmentManager.beginTransaction().replace(fragmentContainer.id, mainFragment).commit()
                titleOfTopActionBar.text = "Main"
            }
            R.id.bottomMenu_map -> {
                supportFragmentManager.beginTransaction().replace(fragmentContainer.id, mapFragment).commit()
                titleOfTopActionBar.text = "Map"
            }
            R.id.bottomMenu_seasons -> {
                supportFragmentManager.beginTransaction().replace(fragmentContainer.id, seasonsFragment).commit()
                titleOfTopActionBar.text = "Seasons"
            }
        }
        return true
    }

}
