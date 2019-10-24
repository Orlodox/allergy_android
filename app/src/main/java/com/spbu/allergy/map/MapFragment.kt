package com.spbu.allergy.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.spbu.allergy.R
import com.spbu.allergy.R.layout.fragment_map
import com.spbu.allergy.UpdateableFragment


class MapFragment(hasGeolocationPermission:()->Boolean) : Fragment(), UpdateableFragment{

    private var geolocationPermissions = hasGeolocationPermission
    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var forecastSeekBar : SeekBar
    private lateinit var forecastMessageItem : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(fragment_map, container, false)

        mMapView = rootView.findViewById(R.id.mapView)

        mMapView.onCreate(savedInstanceState)

        mMapView.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            // For showing a move to my location button
            if (geolocationPermissions()) {
                googleMap.isMyLocationEnabled = true
            }

            val punk = LatLng(59.874, 29.828)
            // For moving automatically to the location of the marker
            val cameraPosition = CameraPosition.Builder().target(punk).zoom(13f).build()
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

        forecastSeekBar = rootView.findViewById(R.id.seekBarForecast)
        forecastMessageItem = rootView.findViewById(R.id.progress)
        forecastSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                forecastMessageItem.text = "Current forecast for ${forecastSeekBar.progress} days"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        return rootView
    }
    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun Update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}