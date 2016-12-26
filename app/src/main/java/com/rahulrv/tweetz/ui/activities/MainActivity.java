package com.rahulrv.tweetz.ui.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rahulrv.tweetz.MyApplication;
import com.rahulrv.tweetz.R;
import com.rahulrv.tweetz.api.TwitterApi;
import com.rahulrv.tweetz.databinding.ActivityMainBinding;
import com.rahulrv.tweetz.ui.TrendsAdapter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Main launcher activity of the app.
 */
public class MainActivity extends Activity {

    @Inject TwitterApi twitterApi;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setActionBar(binding.toolbar);
        ((MyApplication) getApplication()).getComponent().inject(this);
        binding.setIsLoading(true);
    }

    @Override protected void onStart() {
        super.onStart();
        twitterApi.getTrends("2487956")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(trendsResponses -> trendsResponses.get(0))
                .subscribe(trendsResponse -> {
                    Log.d("gg", "dd");
                    binding.setIsLoading(false);
                    TrendsAdapter trendsAdapter = new TrendsAdapter(trendsResponse.trends());
                    binding.trends.setAdapter(trendsAdapter);
                }, throwable -> {
                    Log.d("gg", "dd");
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                View searchMenuView = binding.toolbar.findViewById(R.id.menu_search);
                Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                        getString(R.string.transition_search_back)).toBundle();
                startActivityForResult(new Intent(this, SearchActivity.class), 0, options);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
