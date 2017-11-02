package com.almortah.almortah;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ALMAHRI on 10/31/17.
 */

public class MyChaletsListFragment extends Fragment {
    private ArrayList<Chalet> chalets = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ListView listView;
    private ListView vipListView;
    int i = 0;

    private RecyclerView recyclerView;
    private MyChaletRV mAdapter;
    ProgressBar mProgressBar;
    public MyChaletsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.my_chalets_frag, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyChaletRV(getContext() ,chalets);
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView.setAdapter(mAdapter);
        // You don't need anything here
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chalets");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while ((iterator.hasNext())) {
                    Chalet chalet = iterator.next().getValue(Chalet.class);
                    if (chalet.getOwnerID().equals(userID))
                        chalets.add(chalet);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return view;
    }



}