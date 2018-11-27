package com.example.lhan.lab7
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false
    private lateinit var map: GoogleMap
    val slo = LatLng(35.287, -120.5)
    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Parse URL json into custom data class
        val url = "https://code.org/schools.json"
        GetSchoolAsyncTask().execute(url)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(slo, 9.5f))
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        map.setInfoWindowAdapter(InfoWindowCustom(this))
    }

    inner class GetSchoolAsyncTask : AsyncTask<String, String, String>() {
        override fun onPreExecute() {}

        override fun doInBackground(vararg urls: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val url = URL(urls[0])
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS

                val inString = urlConnection.inputStream.bufferedReader().readText()

                publishProgress(inString)
            } catch (ex: Exception) {
                println("HttpURLConnection exception" + ex)
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?){
            try {
                var schoolData = Gson().fromJson(values[0], SchoolList::class.java)
                val schoolInfo = schoolData.schools as List<School>?

                for (next in schoolInfo.orEmpty()) {
                    try {
                        if (next.zip?.substring(0, 2) == "93") {
                            val markerOption = MarkerOptions()
                            markerOption.position(LatLng(next.latitude as Double, next.longitude as Double))
                            val marker = map.addMarker(markerOption)
                            marker.tag = next
                        }
                    } catch (ex: StringIndexOutOfBoundsException){}
                }
            } catch (ex: Exception) {
                println("JSON parsing exception" + ex.printStackTrace())
            }
        }
        override fun onPostExecute(result: String?) {}
    }
}
