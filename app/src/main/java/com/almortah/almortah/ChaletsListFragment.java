package com.almortah.almortah;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.JoinableLayout;
import su.j2e.rvjoiner.RvJoiner;

/**
 * Created by ALMAHRI on 10/24/17.
 */

public class ChaletsListFragment extends Fragment {

    private ArrayList<Chalet> promotChalets = new ArrayList<>();
    private ArrayList<Chalet> chalets = new ArrayList<>();
    int i = 0;
    private ChaletListRV mAdapter;
    private ChaletListRV promotAdapter;

    private RecyclerView rv;
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
        mAdapter = new ChaletListRV(getContext() ,chalets);
        promotAdapter = new ChaletListRV(getContext() ,promotChalets);
        rv = (RecyclerView) view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        //construct a joiner
        RvJoiner rvJoiner = new RvJoiner();
        rvJoiner.add(new JoinableLayout(R.layout.nav_header));
        rvJoiner.add(new JoinableAdapter(promotAdapter));
        rvJoiner.add(new JoinableLayout(R.layout.logo));
        rvJoiner.add(new JoinableAdapter(mAdapter));

        //set join adapter to your RecyclerView
        rv.setAdapter(rvJoiner.getAdapter());
        // You don't need anything here
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chalets");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                        while ((iterator.hasNext())) {
                            Chalet chalet = iterator.next().getValue(Chalet.class);
                            Log.e("CHALET ID:::",chalet.getId());
                            if (chalet.getPromotion().equals("1"))
                                promotChalets.add(chalet);
                            chalets.add(chalet);
                        }
                        mAdapter.notifyDataSetChanged();
                        promotAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return view;
    }

    

}