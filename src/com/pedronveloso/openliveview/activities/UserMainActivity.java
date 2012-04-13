package com.pedronveloso.openliveview.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.pedronveloso.openliveview.R;
import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.services.CommService;

/**
 * User: Pedro Veloso
 */
public class UserMainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_screen);
        Intent commService = new Intent(UserMainActivity.this, CommService.class);
        commService.putExtra(Constants.EXTRA_START_SERVICE,1);
        startService(commService);
    }
}