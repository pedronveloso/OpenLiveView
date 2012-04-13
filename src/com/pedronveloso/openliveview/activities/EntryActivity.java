package com.pedronveloso.openliveview.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.pedronveloso.openliveview.R;

/**
 * Author: Pedro Veloso
 */
public class EntryActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_screen);
        Button banProceed = (Button) findViewById(R.id.btn_next);
        banProceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(EntryActivity.this,MainActivity.class));
            }
        });
    }
}