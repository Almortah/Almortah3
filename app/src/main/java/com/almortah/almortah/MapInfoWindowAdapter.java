package com.almortah.almortah;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ziyadalkhonein on 10/23/17.
 */

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View popup=null;
    private LayoutInflater inflater=null;
    private HashMap<String, Uri> images=null;
    private Context ctxt=null;
    private int iconWidth=-1;
    private int iconHeight=-1;
    private Marker lastMarker=null;

    MapInfoWindowAdapter(Context ctxt, LayoutInflater inflater,
                 HashMap<String, Uri> images) {
        this.ctxt=ctxt;
        this.inflater=inflater;
        this.images=images;

    }
    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup = inflater.inflate(R.layout.marker_tag, null);
        }
        if (lastMarker == null
                || !lastMarker.getId().equals(marker.getId())) {
            lastMarker=marker;

           // TextView tv=(TextView)popup.findViewById(R.id.title);

           // tv.setText(marker.getTitle());
            //tv=(TextView)popup.findViewById(R.id.snippet);
           // tv.setText(marker.getSnippet());
            Uri image=images.get(marker.getId());
            final ImageView icon=(ImageView)popup.findViewById(R.id.chaletImg);

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference tmp = storageReference.child(String.valueOf(1));
            tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ctxt)
                            .load(uri)
                            .into(icon);
                }
            });






            if (image == null) {
                icon.setVisibility(View.GONE);
            }
            else {
            //    Glide.with(ctxt).load(image)
              //          .into(icon, new MarkerCallback(marker));
                Glide.with(ctxt).load(image).into(icon);
                marker.showInfoWindow();
            }
        }
        return(popup);
    }

}



