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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ALMAHRI on 10/24/17.
 */

public class ChaletsListFragment extends Fragment {

    private ArrayList<Chalet> promotChalets = new ArrayList<>();
    private ArrayList<Chalet> chalets = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ListView listView;
    private ListView vipListView;
    int i = 0;

    private RecyclerView recyclerView;
    private ChaletListRV mAdapter;

    private RecyclerView promotView;
    private ChaletListRV promotAdapter;
    ProgressBar mProgressBar;
    public ChaletsListFragment() {
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
        final View view = inflater.inflate(R.layout.activity_chalet_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChaletListRV(getContext() ,chalets);
        recyclerView.setAdapter(mAdapter);

        promotView = (RecyclerView) view.findViewById(R.id.promotion_view);
        final RecyclerView.LayoutManager promotLayoutManager = new LinearLayoutManager(view.getContext());
        promotView.setLayoutManager(promotLayoutManager);
        promotView.setItemAnimator(new DefaultItemAnimator());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("promotion");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterable<DataSnapshot> kkk = snapshotIterator.

                Iterator<DataSnapshot> iterator = snapshotIterator.ge
                while ((iterator.hasNext())) {
                    Chalet chalet = iterator.next().getValue(Chalet.class);
                        chalet.setPromotion("1");
                        promotChalets.add(chalet);
                }
                promotAdapter = new ChaletListRV(getContext() ,promotChalets);
                promotView.setAdapter(promotAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // You don't need anything here
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chalets");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                        while ((iterator.hasNext())) {
                            Chalet chalet = iterator.next().getValue(Chalet.class);
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