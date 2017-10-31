package com.almortah.almortah;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 10/16/17.
 */

public class MyChaletsAdapter extends ArrayAdapter<Chalet> {
    private Context context;
    private int imgNb;

    public MyChaletsAdapter(Activity context, ArrayList<Chalet> chalets) {
        super(context, 0, chalets);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.my_chalet_list, parent, false);
        }
        Chalet chalet = getItem(position);
        String ownerID = chalet.getOwnerID();
        String chaletNb = chalet.getChaletNm();

        TextView chaletName = (TextView) listItemView.findViewById(R.id.chaletName);
        chaletName.setText(chalet.getName());

        // Reference to an image file in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(ownerID).child(chaletNb);

        // ImageView in your Activity


        return listItemView;

    }


}
