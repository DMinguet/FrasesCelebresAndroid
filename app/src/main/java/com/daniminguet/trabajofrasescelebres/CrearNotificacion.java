package com.daniminguet.trabajofrasescelebres;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CrearNotificacion extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startNotificationService = new Intent(context, NotificationService.class);
        context.startService(startNotificationService);
    }
}
