package com.dupleit.mapmarkers.dynamicmapmarkers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by mandeep on 18/12/17.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
        }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
        }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
        .inflate(R.layout.map_custom_infowindow, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);


        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        /*InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
        "drawable", context.getPackageName());
        img.setImageResource(imageId);

        hotel_tv.setText(infoWindowData.getHotel());
        food_tv.setText(infoWindowData.getFood());
        transport_tv.setText(infoWindowData.getTransport());
*/
        return view;
        }

}
