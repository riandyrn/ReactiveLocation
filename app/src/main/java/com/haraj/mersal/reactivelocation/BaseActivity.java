package com.haraj.mersal.reactivelocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by riandyrn on 3/30/16.
 */
public class BaseActivity extends AppCompatActivity implements ActivityResultGPSEnabledCallback {

    /**
     * Base class providing awareness towards
     * GPS status
     */

    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new BasePresenter(this);
        presenter.registerReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setIsOnForeground(true);
    }

    @Override
    protected void onPause() {
        presenter.setIsOnForeground(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.unregisterReceiver();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.handleActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onResultGPSEnabled() {}
}
