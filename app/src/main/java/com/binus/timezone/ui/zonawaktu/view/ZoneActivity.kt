package com.binus.timezone.ui.zonawaktu.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.binus.timezone.databinding.ActivityMainBinding
import com.binus.timezone.prefs.AppPreferences
import com.binus.timezone.ui.zonawaktu.adapter.TimezoneAdapter
import com.binus.timezone.ui.zonawaktu.model.Timezone
import com.binus.timezone.ui.zonawaktu.viewmodel.TimezoneViewModel

class ZoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapterTimezone: TimezoneAdapter
    private val viewModel = TimezoneViewModel()
    private val mData = mutableListOf<Timezone>()

    private var parDatetime = ""
    private var parImage = ""
    private var parLatlng = ""
    private var parTimezone = ""
    private var parUtcDatetime = ""
    private var parUtcOffset = ""

    companion object {
        lateinit var mActivity: ZoneActivity
        lateinit var appPreferences: AppPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mActivity = this
        appPreferences = AppPreferences(this)


        adapterTimezone = TimezoneAdapter(mData, this)

        initPrefs()
        initUi()
        initAction()
        initData()
    }

    private fun initPrefs() {
//        appPreferences.removeDatetime()
//        appPreferences.removeImage()
//        appPreferences.removeLatlang()
//        appPreferences.removeTimezone()
//        appPreferences.removeUtcDatetime()
//        appPreferences.removeUtcOffset()
//        appPreferences.clearPreferences()

        parDatetime = appPreferences.getDatetime().toString()
        parImage = appPreferences.getImage().toString()
        parLatlng = appPreferences.getLatlang().toString()
        parTimezone = appPreferences.getTimezone().toString()
        parUtcDatetime = appPreferences.getUtcDatetime().toString()
        parUtcOffset = appPreferences.getUtcOffset().toString()
    }

    private fun initUi() {
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(this@ZoneActivity, LinearLayoutManager.VERTICAL, false)
            adapter = adapterTimezone
        }
    }

    private fun initAction() {
        adapterTimezone.onClickItems = {item ->
            Log.i("ZoneActivity", "parDatetime : $parDatetime")
            Log.i("ZoneActivity", "parImage : $parImage")
            Log.i("ZoneActivity", "parLatlng : $parLatlng")
            Log.i("ZoneActivity", "parTimezone : $parTimezone")
            Log.i("ZoneActivity", "parUtcDatetime : $parUtcDatetime")
            Log.i("ZoneActivity", "parUtcOffset : $parUtcOffset")
            val intent = Intent(this, DetailZoneActivity::class.java)
//            intent.putExtras(Bundle().apply {
//                putString("datetime", "$parDatetime")
//                putString("image", "$parImage")
//                putString("latlng", "$parLatlng")
//                putString("timezone", "$parTimezone")
//                putString("utc_datetime", "$parUtcDatetime")
//                putString("utc_offset", "$parUtcOffset")
//            })
            startActivity(intent)
        }
    }

    private fun initData() {
        modelTimezone()
        initDataTimeZone()
//        initDataTimeZoneDetail()
    }

    private fun modelTimezone(){
        viewModel.getDataListViewModel()
//        viewModel.getDataListDetailViewModel()
    }

    private fun initDataTimeZone() {
        viewModel.dataList.observe(this) { giphies ->
            giphies.let {
                val value = it.record
                if (value.timezone.isNotEmpty()){
                    mData.addAll(value.timezone)
                    adapterTimezone.notifyDataSetChanged()
                } else {
                    mData.clear()
                    adapterTimezone.notifyDataSetChanged()
                }
            }
        }

        viewModel.onError.observe(this){
            mData.clear()
            adapterTimezone.notifyDataSetChanged()
        }
    }

//    private fun initDataTimeZoneDetail() {
//        viewModel.dataListDetail.observe(this) { giphies ->
//            giphies.let {
//                val value = it
//                parDatetime= value.datetime
//                parImage = value.image
//                parLatlng = value.utc_datetime
//                parTimezone = value.timezone
//                parUtcDatetime = value.utc_datetime
//                parUtcOffset = value.utc_offset
//
//                appPreferences.setDatetime(parDatetime)
//                appPreferences.setImage(parImage)
//                appPreferences.setLatlang(parLatlng)
//                appPreferences.setTimezone(parTimezone)
//                appPreferences.setUtcDatetime(parUtcDatetime)
//                appPreferences.setUtcOffset(parUtcOffset)
//            }
//        }
//
//        viewModel.onError.observe(this){
//            //
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        appPreferences.clearPreferences()
    }
}