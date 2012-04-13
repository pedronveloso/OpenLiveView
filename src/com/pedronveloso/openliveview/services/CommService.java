package com.pedronveloso.openliveview.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Communication server. Handlers communication in the background
 */
public class CommService extends Service {


    public IBinder onBind(Intent intent) {
        return null;
    }

}
