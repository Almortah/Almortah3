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

    private ArrayList<Chalet> chalets = new ArrayList<>();
    private ListView listView;
    private ListView vipListView;
    int i = 0;

    private RecyclerView recyclerView;
    private ChaletListRV mAdapter;
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
        mAdapter = new ChaletListRV(getContext() ,chalets);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
                            chalets.add(chalet);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


       /* mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        CountDownTimer mCountDownTimer;
        mProgressBar.setProgress(i);
        mCountDownTimer=new CountDownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i + millisUntilFinished);
                i++;
                mProgressBar.setProgress((int) i *5000/(5000/1000));

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                mProgressBar.setProgress(100);
            }
        };
        mCountDownTimer.start();*/


       // Bundle user = container.getContext().getIntent().getExtras();
        //chalets = new ArrayList<>();

        return view;
    }

    

}