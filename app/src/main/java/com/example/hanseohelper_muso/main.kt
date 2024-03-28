package com.example.hanseohelper_muso

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.example.hanseohelper_muso.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_start.*

import android.content.Context
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.TextView
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class main : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding    // 뷰 바인딩
    private lateinit var mapView: MapView    // 카카오맵 뷰
    private val eventListener = MarkerEventListener(this)
    private val ACCESS_FINE_LOCATION = 1000     // Request Code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mapView = MapView(this)
        binding.mapView.addView(mapView)

        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))   // 커스텀 말풍선
        mapView.setPOIItemEventListener(eventListener)   // 마커 클릭 이벤트 리스너 등록

        // 한서대학교 서산캠 위도, 경도
        val mapPoint1 = MapPoint.mapPointWithGeoCoord(36.69060099, 126.58427508)

        // 지도의 중심점을 한서대로 설정
        mapView.setMapCenterPoint(mapPoint1, true)
        mapView.setZoomLevel(2, true)

        // 마커 생성 (한서대학교 서산캠퍼스)
        val marker1 = MapPOIItem()
        marker1.itemName = "짐 옮겨주세요."
        marker1.mapPoint = mapPoint1
        marker1.markerType = MapPOIItem.MarkerType.BluePin
        marker1.selectedMarkerType = MapPOIItem.MarkerType.RedPin

        mapView.addPOIItem(marker1)

        // 서울시청 마커 추가
        val marker2 = MapPOIItem()
        marker2.apply {
            itemName = "서울시청"   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(37.5666805, 126.9784147)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            isCustomImageAutoscale = false
            setCustomImageAnchor(0.5f, 1.0f)
        }
        mapView.addPOIItem(marker2)

        // 학교 앞 집 마커 추가
        val marker3 = MapPOIItem()
        marker3.apply {
            itemName = "벌레 잡아주세요"   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(36.69076689, 126.57921693)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            isCustomImageAutoscale = false
            setCustomImageAnchor(0.5f, 1.0f)
        }
        mapView.addPOIItem(marker3)

        val intent_id_main = intent.extras?.getString("ID")
        intent.putExtra("ID",intent_id_main)

        binding.btnMyinfoMain.setOnClickListener {
            var popup = PopupMenu(this, it)
            menuInflater.inflate(com.example.hanseohelper_muso.R.menu.popup, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it?.itemId) {
                    com.example.hanseohelper_muso.R.id.popup_menu1 -> {
                        val intent = Intent(this, myinfo::class.java)
                        intent.putExtra("ID",intent_id_main)
                        startActivity(intent)
                    }
                    com.example.hanseohelper_muso.R.id.popup_menu2 -> {
                        val intent = Intent(this, myclient::class.java)
                        intent.putExtra("ID",intent_id_main)
                        startActivity(intent)
                    }
                    com.example.hanseohelper_muso.R.id.popup_menu3 -> {
                        val intent = Intent(this, myservice::class.java)
                        intent.putExtra("ID",intent_id_main)
                        startActivity(intent)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popup.show()
        }

        btn_list_main.setOnClickListener {
            val intent = Intent(this, list::class.java)
            intent.putExtra("ID",intent_id_main)
            startActivity(intent)
        }

        btn_request.setOnClickListener {
            val intent = Intent(this, request::class.java)
            intent.putExtra("ID",intent_id_main)
            startActivity(intent)
        }

    }

    // 커스텀 말풍선
    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(com.example.hanseohelper_muso.R.layout.balloon_layout, null)
        val name: TextView = mCalloutBalloon.findViewById(com.example.hanseohelper_muso.R.id.ball_tv_name)
        val address: TextView = mCalloutBalloon.findViewById(com.example.hanseohelper_muso.R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            name.text = poiItem?.itemName
            address.text = "비용"
            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 클릭 시
            address.text = "getPressedCalloutBalloon"
            return mCalloutBalloon
        }
    }

    //마커 클릭 이벤트 리스너
    class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            // 마커 클릭 시
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
            // 말풍선 클릭 시
            // 이 함수도 작동하지만 아래의 함수 사용
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?, buttonType: MapPOIItem.CalloutBalloonButtonType?) {
            // 말풍선 클릭 시
            val builder = AlertDialog.Builder(context)
            val itemList = arrayOf("토스트", "의뢰 수락", "취소")
            builder.setTitle("${poiItem?.itemName}")
            builder.setItems(itemList) { dialog, which ->
                when(which) {
                    0 -> Toast.makeText(context, "토스트", Toast.LENGTH_SHORT).show()  // 토스트
                    1 -> mapView?.removePOIItem(poiItem)    // 마커 삭제
                    2 -> dialog.dismiss()   // 대화상자 닫기
                }
            }
            builder.show()
        }

        override fun onDraggablePOIItemMoved(mapView: MapView?, poiItem: MapPOIItem?, mapPoint: MapPoint?) {
            // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
        }
    }

    fun onDraggablePOIItemMoved(mapView: MapView?, poiItem: MapPOIItem?, mapPoint: MapPoint?) {
        // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
    }


    // 위치 권한 확인
    private fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절 (다시 한 번 물어봄)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    // 다시 묻지 않음 클릭 (앱 정보 화면으로 이동)
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
            // 권한이 있는 상태
        }
    }



    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        val intent = Intent(this, request::class.java)
        //intent.putExtra("data", "test data");
        startActivityForResult(intent, 1)
        return true
        return super.onOptionsItemSelected(item)
    }
}