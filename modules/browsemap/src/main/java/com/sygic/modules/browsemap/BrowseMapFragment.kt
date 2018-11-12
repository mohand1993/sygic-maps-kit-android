package com.sygic.modules.browsemap

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sygic.modules.browsemap.databinding.LayoutBrowseMapBinding
import com.sygic.sdk.map.MapFragment
import com.sygic.sdk.map.MapView
import com.sygic.sdk.map.listeners.OnMapInitListener
import com.sygic.sdk.online.OnlineManager
import android.arch.lifecycle.ViewModelProviders
import com.sygic.modules.browsemap.viewmodel.BrowseMapFragmentViewModel

class BrowseMapFragment : MapFragment() {

    private lateinit var viewModel: BrowseMapFragmentViewModel

    override fun onInflate(context: Context, attrs: AttributeSet?, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        viewModel = ViewModelProviders.of(this, BrowseMapFragmentViewModel.ViewModelFactory(requireActivity().application, attrs))
                .get(BrowseMapFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: LayoutBrowseMapBinding = LayoutBrowseMapBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        (binding.root as ViewGroup).addView(super.onCreateView(inflater, container, savedInstanceState), 0)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ToDo
        getMapAsync(object : OnMapInitListener {
            override fun onMapReady(mapView: MapView) {
                Log.d("BrowseMapFragment", "onMapReady()")
                OnlineManager.getInstance().enableOnlineMapStreaming(true)
                /*if (cameraInitialLatitude != -1f && cameraInitialLongitude != -1f) {
                    mapView.cameraModel.position =
                            GeoCoordinates(cameraInitialLatitude.toDouble(), cameraInitialLongitude.toDouble())
                }
                if (cameraInitialLatitude != -1f) {
                    mapView.cameraModel.zoomLevel = cameraInitialZoom
                }*/
            }

            override fun onMapInitializationInterrupted() {}
        })
    }
}