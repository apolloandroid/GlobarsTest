package com.example.globarstest.ui.map

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.globarstest.R
import com.example.globarstest.data.models.Position
import com.example.globarstest.data.models.Vehicle
import com.example.globarstest.databinding.FragmentMapBinding
import com.example.globarstest.di.AppModule
import com.example.globarstest.di.DaggerAppComponent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.vehicle_marker.view.*
import javax.inject.Inject


class MapFragment : Fragment(), GoogleMap.OnMarkerClickListener {
    @Inject
    lateinit var viewModel: MapViewModel
    private lateinit var binding: FragmentMapBinding
    private var vehiclesMap: GoogleMap? = null
    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings?.isZoomControlsEnabled = true
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f))
        googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        vehiclesMap = googleMap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = this
        initObservers()
        val token = arguments?.getSerializable(viewModel.TOKEN_TAG).toString()
        viewModel.getSessionId(token)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun injectFragment() {
        val applicationComponent = DaggerAppComponent.builder()
            .appModule(AppModule(requireContext()))
            .build()
        applicationComponent?.injectMapsFragment(this)
    }

    private fun initObservers() {
        viewModel.sessionId.observe(viewLifecycleOwner, Observer {
            viewModel.loadVehicles()
        })
        viewModel.vehicles.observe(viewLifecycleOwner, Observer { vehicles ->
            vehicles.forEach {
                addVehicleToMenu(it)
                setMarkerToVehicle(it)
            }
            displayFirstVehicleInMenu(vehicles.firstOrNull()!!)
        })
        viewModel.showNoInternetConnectionMessage.observe(viewLifecycleOwner, Observer { event ->
            if (event) showNoInternetConnectionMessage()
        })
    }

    private fun addVehicleToMenu(vehicle: Vehicle) {
        binding.toolbar.menu.add(vehicle.name)
            .setOnMenuItemClickListener {
                displaySelectedVehicle(vehicle)
                return@setOnMenuItemClickListener true
            }
    }

    private fun setMarkerToVehicle(vehicle: Vehicle) {
        val vehiclePosition: Position = vehicle.position!!
        val target = LatLng(vehiclePosition.lt, vehiclePosition.ln)
        val icon = layoutInflater.inflate(R.layout.vehicle_marker, null)
        icon.text_vehicle_name.text = vehicle.name
        val iconGenerator = IconGenerator(activity)
        iconGenerator.setContentView(icon)
        vehiclesMap?.addMarker(
            MarkerOptions()
                .position(target)
                .alpha(if (vehicle.eye) 1f else 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
        )
    }

    private fun displayFirstVehicleInMenu(vehicle: Vehicle) {
        displaySelectedVehicle(vehicle)
    }

    private fun displaySelectedVehicle(vehicle: Vehicle) {
        val vehiclePosition: Position = vehicle.position!!
        val vehicleLatLng = LatLng(vehiclePosition.lt, vehiclePosition.ln)
        vehiclesMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleLatLng, 14f))
        vehiclesMap?.animateCamera(CameraUpdateFactory.zoomTo(14.0f))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val markerPosition = marker?.position
        val markerLatLng = LatLng(markerPosition!!.latitude, markerPosition.longitude)
        vehiclesMap?.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng))
        return true
    }

    private fun showNoInternetConnectionMessage() {
        Snackbar.make(
            binding.root,
            getString(R.string.no_internet_connection_message_text),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}