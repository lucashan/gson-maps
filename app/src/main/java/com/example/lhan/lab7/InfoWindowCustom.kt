package com.example.lhan.lab7

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_info_window_custom.view.*


class InfoWindowCustom(val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker?): View {

        var infoView = (context as Activity).layoutInflater.inflate(R.layout.activity_info_window_custom, null)
        var infoWindow: School? = p0?.tag as School?

        infoView.name.text = infoWindow?.name
        infoView.city.text = infoWindow?.city
        infoView.state.text = infoWindow?.state
        infoView.zip.text = infoWindow?.zip
        infoView.latitude.text = infoWindow?.latitude.toString()
        infoView.longitude.text = infoWindow?.longitude.toString()

        return infoView
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }
}
