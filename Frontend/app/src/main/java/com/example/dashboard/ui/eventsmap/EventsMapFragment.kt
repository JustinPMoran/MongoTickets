package com.example.dashboard.ui.eventsmap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.dashboard.R

class EventsMapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var button: Button
    private val amesLocation = LatLng(42.034534, -93.620369)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("EventsMapFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_eventsmap, container, false)

        button = view.findViewById(R.id.button30)
        button.setOnClickListener {
            Log.d("EventsMapFragment", "Button clicked")
            centerMapOnAmes()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        if (mapFragment == null) {
            Log.e("EventsMapFragment", "Error: Map fragment is null")
        } else {
            Log.d("EventsMapFragment", "Map fragment found, getting map async")
            mapFragment.getMapAsync(this)
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("EventsMapFragment", "onMapReady called")
        mMap = googleMap
        Toast.makeText(context, "Map is ready", Toast.LENGTH_SHORT).show()
        setupMap()
        centerMapOnAmes()
    }

    private fun setupMap() {
        Log.d("EventsMapFragment", "Setting up map")
        mMap?.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
        }
    }

    private fun centerMapOnAmes() {
        Log.d("EventsMapFragment", "Centering map on Ames")
        mMap?.let { map ->
            map.clear() // Clear existing markers
            map.addMarker(MarkerOptions().position(amesLocation).title("Ames, Iowa"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(amesLocation, 12f))
        } ?: Log.e("EventsMapFragment", "Error: Map is null when trying to center on Ames")
    }
}