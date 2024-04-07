package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PracticalTest01BroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null) {
            if(action.equals(Constants.ACTION_STRING)) {
                String dataReceived = intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA);
                Log.d("PracticalTest01BroadcastReceiver", "Received message: " + dataReceived);
            }
        }

    }
}
