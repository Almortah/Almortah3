package com.almortah.almortah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.almortah.almortah.R.id.chaletLocation;

/**
 * Created by ziyadalkhonein on 10/9/17.
 */

public class ChaletAdapter extends ArrayAdapter<Chalet> {
    private Context context;
    private RatingBar chaletRating;
   // private ImageView img1;
    private boolean zoomOut =  false;

    public class ViewHolder{
        //put all of your textviews and image views and
        //all views here like this
        TextView chaletName;
        ImageView img1;
        RatingBar chaletRating;
        TextView price;
        TextView chaletLocation;
    }


    public ChaletAdapter(Activity context, ArrayList<Chalet> chalets){
        super(context,0,chalets);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        final ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chalet_list_item, parent, false);
            holder = new ViewHolder();
            holder.chaletName = (TextView) listItemView.findViewById(R.id.chaletName);
            holder.img1 = (ImageView) listItemView.findViewById(R.id.chaletImg);
            holder.price = (TextView) listItemView.findViewById(R.id.normalPrice);
            holder.chaletLocation = (TextView) listItemView.findViewById(chaletLocation);
            holder.chaletRating = (RatingBar) listItemView.findViewById(R.id.chaletRating);
            listItemView.setTag(holder);
        } else {

       /* if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chalet_list_item, parent, false);
        }*/

            final Chalet chalet = getItem(position);
            holder = (ViewHolder) listItemView.getTag();

            holder.chaletName.setText(chalet.getName());
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm());
            StorageReference tmp = storageReference.child(String.valueOf(1));

            final View finalListItemView1 = listItemView;
            tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {

                    InputStream inputStream = null;
                    try {
                        inputStream = context.getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream, null, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;


                    Glide.with(finalListItemView1.getContext())
                            .load(uri)
                            .into(holder.img1);

                }
            });

            holder.price.setText(chalet.getNormalPrice());
            //chaletRating.setText(""+chalet.getChaletRating());
            holder.chaletLocation.setText("" + chalet.getLatitude() + chalet.getLongitude());

            final View finalListItemView = listItemView;
            finalListItemView.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         Intent toChaletInfo = new Intent(context, ChaletInfoCustomer.class);
                                                         toChaletInfo.putExtra("name", chalet.getName());
                                                         toChaletInfo.putExtra("normalPrice", chalet.getNormalPrice());
                                                         toChaletInfo.putExtra("weekendPrice", chalet.getWeekendPrice());
                                                         toChaletInfo.putExtra("eidPrice", chalet.getEidPrice());
                                                         toChaletInfo.putExtra("images", chalet.getChaletNm());
                                                         toChaletInfo.putExtra("ownerID", chalet.getOwnerID());
                                                         toChaletInfo.putExtra("latitude", chalet.getLatitude());
                                                         toChaletInfo.putExtra("longitude", chalet.getLongitude());

                                                         toChaletInfo.putExtra("chaletNb", chalet.getChaletNm());
                                                         context.startActivity(toChaletInfo);
                                                     }
                                                 }
            );
        }
            return listItemView;
    }
}
