package com.haraj.mersal.reactivelocation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnStart;
    private Button btnStop;
    private Button btnNext;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
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

}
