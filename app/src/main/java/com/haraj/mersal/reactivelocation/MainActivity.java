package com.haraj.mersal.reactivelocation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnStart;
    private Button btnStop;
    private Button btnNext;

    private MainPresenter presenter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(this.getClass().getSimpleName(), "onCreate called");

        presenter = new MainPresenter(this);

        textView = (TextView) findViewById(R.id.text_view);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnNext = (Button) findViewById(R.id.btn_next);

        final Activity activity = this;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startListeningLocationUpdates();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.stopListeningLocationUpdates();
            }
        });

        //final Activity activity = this;
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, NextActivity.class));
                finish();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(this.getClass().getSimpleName(), "Context: " + context.toString());
                presenter.handleBroadcast(context, intent);
            }
        };

        registerReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.getClass().getSimpleName(), "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(this.getClass().getSimpleName(), "onPause called");
    }

    @Override
    protected void onDestroy() {
        Log.d(this.getClass().getSimpleName(), "onDestroy called");
        unregisterReceiver();
        presenter.stopListeningLocationUpdates();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.handleActivityResult(requestCode, resultCode, data);
    }

    public void setTextView(String text) {
        textView.setText(text);
    }

    private void registerReceiver() {
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_LOCATION));
    }

    private void unregisterReceiver() {
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(broadcastReceiver);
    }

}
