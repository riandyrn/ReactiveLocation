package com.haraj.mersal.reactivelocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haraj.mersal.reactivelocation.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView textView;
    private Button btnStart;
    private Button btnStop;
    private Button btnNext;

    private MainPresenter presenter;

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

        final Activity activity = this;
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, NextActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.getClass().getSimpleName(), "onResume called");
    }

    @Override
    protected void onPause() {
        Log.d(this.getClass().getSimpleName(), "onPause called");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(this.getClass().getSimpleName(), "onDestroy called");
        presenter.stopListeningLocationUpdates();
        super.onDestroy();
    }


    public void setTextView(String text) {
        textView.setText(text);
    }

    @Override
    public void onResultGPSEnabled() {
        super.onResultGPSEnabled();
        presenter.handleOnResultGPSEnabled();
    }
}
