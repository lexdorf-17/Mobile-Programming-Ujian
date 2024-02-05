package com.binus.timezone.ui.zonawaktu.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.binus.timezone.R
import com.binus.timezone.databinding.ActivityDetailZoneBinding
import com.binus.timezone.prefs.AppPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.UnknownHostException
import java.util.Locale

class DetailZoneActivity : AppCompatActivity(), OnMapReadyCallback, OnMapsSdkInitializedCallback {

    private lateinit var binding: ActivityDetailZoneBinding

    private lateinit var mGoogleMap: GoogleMap
    private var mapFrag: SupportMapFragment? = null
    private var mFusedLocClient: FusedLocationProviderClient? = null

    private var parDatetime = ""
    private var parImage = ""
    private var parLatlng = ""
    private var parTimezone = ""
    private var parUtcDatetime = ""
    private var parUtcOffset = ""

    private var textCoordinate: String = ""
    private var parLat: String = ""
    private var parLong: String = ""
    private var currentMarker: Marker? = null
    private var currentLatLng: LatLng? = null
    private var parAddress: String = ""
    private val filterCoordinate = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    companion object {
        lateinit var mActivity: DetailZoneActivity
        lateinit var appPreferences: AppPreferences
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            appPreferences.removeDatetime()
            appPreferences.removeImage()
            appPreferences.removeLatlang()
            appPreferences.removeTimezone()
            appPreferences.removeUtcDatetime()
            appPreferences.removeUtcOffset()
            appPreferences.clearPreferences()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailZoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mActivity = this
        appPreferences = AppPreferences(this)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        mFusedLocClient = LocationServices.getFusedLocationProviderClient(this)
        mapFrag = supportFragmentManager.findFragmentById(R.id.activityMaps) as SupportMapFragment?
        mapFrag?.getMapAsync(this)

        initPrefs()
//        initParams()
//        initMaps()
        initUi()
    }

    private fun initPrefs() {
        parDatetime = ZoneActivity.appPreferences.getDatetime().toString()
        parImage = ZoneActivity.appPreferences.getImage().toString()
        parLatlng = ZoneActivity.appPreferences.getLatlang().toString()
        parTimezone = ZoneActivity.appPreferences.getTimezone().toString()
        parUtcDatetime = ZoneActivity.appPreferences.getUtcDatetime().toString()
        parUtcOffset = ZoneActivity.appPreferences.getUtcOffset().toString()

        textCoordinate = parLatlng
        val parseCoordinate = formatStringToLatLong(parLatlng)
        parLat = parseCoordinate.first.toString()
        parLong = parseCoordinate.second.toString()
        currentLatLng = LatLng(parseCoordinate.first,parseCoordinate.second)

        Log.i("DetailZoneActivity", "parDatetime : $parDatetime")
        Log.i("DetailZoneActivity", "parImage : $parImage")
        Log.i("DetailZoneActivity", "parLatlng : $parLatlng")
        Log.i("DetailZoneActivity", "parTimezone : $parTimezone")
        Log.i("DetailZoneActivity", "parUtcDatetime : $parUtcDatetime")
        Log.i("DetailZoneActivity", "parUtcOffset : $parUtcOffset")
    }

//    private fun initParams(){
//        if (intent.hasExtra("datetime")) {
//            intent.getStringExtra("datetime")!!.let {
//                parDatetime = it
//            }
//        }
//
//        if (intent.hasExtra("image")) {
//            intent.getStringExtra("image")!!.let {
//                parImage = it
//            }
//        }
//
//        if (intent.hasExtra("latlng")) {
//            intent.getStringExtra("latlng")!!.let {
//                parLatlng = it
//                textCoordinate = parLatlng
//                val parseCoordinate = formatStringToLatLong(parLatlng)
//                parLat = parseCoordinate.first.toString()
//                parLong = parseCoordinate.second.toString()
//                currentLatLng = LatLng(parseCoordinate.first,parseCoordinate.second)
//            }
//        }
//
//        if (intent.hasExtra("timezone")) {
//            intent.getStringExtra("timezone")!!.let {
//                parTimezone = it
//            }
//        }
//        if (intent.hasExtra("utc_datetime")) {
//            intent.getStringExtra("utc_datetime")!!.let {
//                parUtcDatetime = it
//            }
//        }
//
//        if (intent.hasExtra("utc_offset")) {
//            intent.getStringExtra("utc_offset")!!.let {
//                parUtcOffset = it
//            }
//        }
//
//        Log.i("DetailZoneActivity", "parDatetime : $parDatetime")
//        Log.i("DetailZoneActivity", "parImage : $parImage")
//        Log.i("DetailZoneActivity", "parLatlng : $parLatlng")
//        Log.i("DetailZoneActivity", "parTimezone : $parTimezone")
//        Log.i("DetailZoneActivity", "parUtcDatetime : $parUtcDatetime")
//        Log.i("DetailZoneActivity", "parUtcOffset : $parUtcOffset")
//
//        intent.replaceExtras(Bundle())
//    }

    fun formatStringToLatLong(strLatLng: String): Pair<Double, Double> {
        try {
            val latLong = strLatLng.split(",".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val latitude = latLong[0].toDouble()
            val longitude = latLong[1].toDouble()
            return Pair(latitude, longitude)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(0.0, 0.0)
    }

    private fun initUi(){
        binding.btnBack.setOnClickListener {
            finish()
            appPreferences.clearPreferences()
        }

        binding.layoutDetailZone.tvNegara.text = parTimezone
        binding.layoutDetailZone.tvWaktu.text = parLatlng
        binding.layoutDetailZone.tvUtc.text = parUtcDatetime
        binding.layoutDetailZone.tvDatetime.text = parDatetime
        binding.layoutDetailZone.tvOffset.text = parUtcOffset
    }

//    private fun initMaps() {
//        mFusedLocClient = LocationServices.getFusedLocationProviderClient(this)
//        mapFrag = supportFragmentManager.findFragmentById(R.id.activityMaps) as SupportMapFragment?
//        mapFrag?.getMapAsync(this)
//    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }else{
            mFusedLocClient?.lastLocation?.addOnSuccessListener(this) { location ->
                if (location != null) {
                    if(parLat.trim().isEmpty() && parLong.trim().isEmpty()){
                        val parLatitude: String = if(location.latitude.toString().contains(filterCoordinate)){
                            val formatLatitude = BigDecimal(location.latitude)
                            "${formatLatitude.setScale(15, RoundingMode.HALF_UP)}"
                        }else{
                            location.latitude.toString()
                        }
                        val parLongitude: String = if(location.longitude.toString().contains(filterCoordinate)){
                            val formatLongitude = BigDecimal(location.longitude)
                            "${formatLongitude.setScale(15, RoundingMode.HALF_UP)}"
                        }else{
                            location.longitude.toString()
                        }
                        textCoordinate= "$parLatitude,$parLongitude"
                        currentLatLng = LatLng(location.latitude, location.longitude)
                    }else{
                        textCoordinate = "$parLat,$parLong"
                        currentLatLng = LatLng(parLat.toDouble(), parLong.toDouble())
                    }
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 8f), object : GoogleMap.CancelableCallback{
                        override fun onCancel() {
                            //
                        }

                        override fun onFinish() {
                            //
                        }
                    })
                    drawMarker(currentLatLng!!)
                }
            }
        }
    }

    private fun drawMarker(latlong: LatLng) {
        currentLatLng = latlong

        // get old position camera
        val oldPosition = mGoogleMap.cameraPosition.target
        mGoogleMap.setOnCameraMoveStartedListener {
            // drag started
            if(currentMarker != null){
                currentMarker?.remove()
                currentMarker?.isVisible = false
            }
//            binding.markerLayout.visibility = View.VISIBLE
        }

        mGoogleMap.setOnCameraIdleListener {
            val newPosition = mGoogleMap.cameraPosition.target
            val parLatitude: String = if(newPosition.latitude.toString().contains(filterCoordinate)){
                val formatLatitude = BigDecimal(newPosition.latitude)
                "${formatLatitude.setScale(15, RoundingMode.HALF_EVEN)}"
            }else{
                newPosition.latitude.toString()
            }
            val parLongitude: String = if(newPosition.longitude.toString().contains(filterCoordinate)){
                val formatLongitude = BigDecimal(newPosition.longitude)
                "${formatLongitude.setScale(15, RoundingMode.HALF_EVEN)}"
            }else{
                newPosition.longitude.toString()
            }
            textCoordinate= "$parLatitude,$parLongitude"
            if (newPosition != oldPosition) {
                // drag ended
                val markerOptions = MarkerOptions()
                    .position(newPosition)
                    .title(getTheAddress(newPosition.latitude, newPosition.longitude))
                    .draggable(false)
                currentMarker = mGoogleMap.addMarker(markerOptions)
                currentMarker?.isVisible = true
//                binding.markerLayout.visibility = View.GONE
            }
        }
    }


    @Suppress("DEPRECATION")
    private fun getTheAddress(lat: Double, lon: Double): String {
        var addressTitle = ""
        try{
            val geocoder = Geocoder(this, Locale.getDefault())

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2){
                geocoder.getFromLocation(lat, lon, 1) { addresses ->
                    if(addresses.isNotEmpty()){
                        addressTitle = addresses[0].getAddressLine(0).toString()
//                        binding.tvDetailAddress.text = addresses[0].getAddressLine(0).toString()
//                        binding.tvCountry.text = if(addresses[0].thoroughfare==null){
//                            addresses[0].getAddressLine(0).toString().split(",")[0]
//                        }else{
//                            addresses[0].thoroughfare.toString()
//                        }
                        parAddress = if(addresses[0].thoroughfare==null){
                            addresses[0].getAddressLine(0).toString() + addresses[0].getAddressLine(0).toString().split(",")[0]
                        }else{
                            addresses[0].getAddressLine(0).toString() + addresses[0].thoroughfare.toString()
                        }
                    }
                }
            }else{
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if(!addresses.isNullOrEmpty()){
                    addressTitle = addresses[0].getAddressLine(0).toString()
//                    binding.tvDetailAddress.text = addresses[0].getAddressLine(0).toString()
//                    binding.tvCountry.text = if(addresses[0].thoroughfare==null){
//                        addresses[0].getAddressLine(0).toString().split(",")[0]
//                    }else{
//                        addresses[0].thoroughfare.toString()
//                    }
                    parAddress = if(addresses[0].thoroughfare==null){
                        addresses[0].getAddressLine(0).toString() + addresses[0].getAddressLine(0).toString().split(",")[0]
                    }else{
                        addresses[0].getAddressLine(0).toString() + addresses[0].thoroughfare.toString()
                    }
                }
            }


        } catch (err: NoClassDefFoundError){
        } catch (err: UnknownHostException) {
        } catch (err: Exception) {
        }
        return addressTitle
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            101 -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                getLastLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        getLastLocation()
    }

    override fun onMapsSdkInitialized(p0: MapsInitializer.Renderer) {}

    override fun onDestroy() {
        super.onDestroy()
        appPreferences.clearPreferences()
    }
}