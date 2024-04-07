package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class PracticalTest01Service extends Service {
    private static final String TAG = "PracticalTest01Service";
    private static final String CHANNEL_ID = "11";
    private static final String CHANNEL_NAME = "ForegroundServiceChannel";

    private ProcessThread processThread = null;

    private void dummyNotification() {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel);
        }
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
        }
        startForeground(1, notification);
    }

    private void dummyNotification2() {
        NotificationChannel channel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("")
                    .build();

            startForeground(1, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        dummyNotification();
        dummyNotification2();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand() callback method was invoked.");
        Log.d(TAG, "The message is: "
                + intent.getIntExtra(Constants.INPUT1, 0) + " "
                + intent.getIntExtra(Constants.INPUT2, 0));

        processThread = new ProcessThread(
                PracticalTest01Service.this,
                intent.getIntExtra(Constants.INPUT1, 0),
                intent.getIntExtra(Constants.INPUT2, 0)
        );

        processThread.start();

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        processThread.stopThread();
        Log.d(TAG, "onDestroyCommand() callback method was invoked.");
        super.onDestroy();
    }
}
