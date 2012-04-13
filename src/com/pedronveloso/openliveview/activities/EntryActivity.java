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
public class EntryActivity extends Activity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_screen);
        Button btnProceed = (Button) findViewById(R.id.btn_next);
        btnProceed.setOnClickListener(this);
        btnProceed = (Button) findViewById(R.id.btn_next_dev);
        btnProceed.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                startActivity(new Intent(EntryActivity.this,UserMainActivity.class));
                break;

            case R.id.btn_next_dev:
                startActivity(new Intent(EntryActivity.this,DevMainActivity.class));
                break;
        }
    }
}