package com.example.instatram.ui.main.Maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instatram.R
import androidx.lifecycle.ViewModelProviders

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.lifecycle.Observer
import com.example.instatram.ui.main.PageViewModel
import com.google.android.gms.maps.OnMapReadyCallback

class Maps : Fragment() {
    private lateinit var viewModel: PageViewModel
    private var callback = OnMapReadyCallback { googleMap ->
        viewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)
        viewModel.TramData.observe(this, Observer
        {for (tram in it) { val station = LatLng(tram.lat.toDouble(),tram.lon.toDouble())
            googleMap?.addMarker(MarkerOptions().position(station).title(tram.name))
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(station))

            googleMap?.setMinZoomPreference(8.0f)
            googleMap?.setMaxZoomPreference(20.0f)}

        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fun hmm():Boolean{
            return true
        }

      return inflater.inflate(R.layout.fragment_maps, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    companion object {

        fun newInstance() =
                Maps().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}




