package com.daniminguet.trabajofrasescelebres;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends IntentService {
    private final int REQUEST_POST_NOTIFICATIONS = 1;
    private final String ID_CANAL = "Canal1";
    private boolean postNotificationsGranted;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        IAPIService apiService = RestClient.getInstance();

        Notification.Builder builder = new Notification.Builder(this);

        apiService.getFrases().enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(Call<List<Frase>> call, Response<List<Frase>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    Date fechaHoy = new Date();

                    for (Frase fraseDia : response.body()) {
                        if (fraseDia.getFechaprogramada().equalsIgnoreCase(sdf.format(fechaHoy))) {
                            builder.setContentTitle("Frase del día");
                            builder.setContentText(fraseDia.getTexto());
                            builder.setSmallIcon(android.R.drawable.star_big_on);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {
                    Toast.makeText(NotificationService.this, "No se han podido obtener las frases", Toast.LENGTH_LONG).show();
            }
        });

        Intent notifyIntent = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(REQUEST_POST_NOTIFICATIONS, notificationCompat);
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IAPIService apiService = RestClient.getInstance();

        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {android.Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_POST_NOTIFICATIONS);
        }

        if (postNotificationsGranted) {
            apiService.getFrases().enqueue(new Callback<List<Frase>>() {
                @Override
                public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                    if(response.isSuccessful()) {
                        assert response.body() != null;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        Date fechaHoy = new Date();

                        for (Frase fraseDia : response.body()) {
                            if (fraseDia.getFechaprogramada().equalsIgnoreCase(sdf.format(fechaHoy))) {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, ID_CANAL);
                                builder.setSmallIcon(android.R.drawable.star_big_on)
                                        .setContentTitle("Frase del día:")
                                        .setContentText(fraseDia.getTexto());

                                Intent intent = new Intent(NotificationService.this, CrearNotificacion.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
                                builder.setContentIntent(pendingIntent);

                                ((NotificationManager) (getSystemService(Context.NOTIFICATION_SERVICE))).notify(1, builder.build());

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(2023, 2, 14, 20, 27, 0);

                                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Frase>> call, @NonNull Throwable t) {
                    Toast.makeText(NotificationService.this, "No se han podido obtener las frases", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                postNotificationsGranted = true;
            }
        }
    }

     */
}
