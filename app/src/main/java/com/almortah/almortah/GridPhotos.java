package com.almortah.almortah;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class GridPhotos extends AppCompatActivity {
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private Chalet chalet;
    private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_photos);
        chalet = (Chalet) getIntent().getExtras().getParcelable("chalet");

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.photo_item, mGridData);
        mGridView.setAdapter(mGridAdapter);
        //Start download
        new AsyncHttpTask().execute();
        mProgressBar.setVisibility(View.VISIBLE);
    }
    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            for(int i = 0; i < 0; i++) {

            }
            return result;
        }
        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            mProgressBar.setVisibility(View.GONE);
        }
    }
}