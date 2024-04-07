package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.time.LocalTime;

public class ProcessThread extends Thread {
    private int medieAritmetica = 0;
    private int medieGeometrica = 0;
    private boolean isRunning = true;
    private Context context;

    public ProcessThread(Context context, int input1, int input2) {
        this.context = context;
        this.medieAritmetica = (input1 + input2) / 2;
        this.medieGeometrica = (int) Math.sqrt(input1 * input2);
    }

    @Override
    public void run() {
        Log.d("ProcessThread", "processing method has started");
        while (isRunning) {
            sleep();
            sendMessage();
        }
        Log.d("ProcessThread", "processing method ended");
    }

    private void sleep() {
        try {
            Thread.sleep(Constants.SLEEP_TIME);
        } catch(InterruptedException interruptedException) {

        }
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_STRING);
        String broadcastMessage = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            broadcastMessage = "Time: " + LocalTime.now() + ", media aritmetica = " + medieAritmetica + ", media geometrica = " + medieGeometrica;
        } else {
            broadcastMessage = "Media aritmetica = " + medieAritmetica + ", media geometrica = " + medieGeometrica;
        }

        Log.d("PracticalTest01Service - ProcessingThread", "Message produced: " + broadcastMessage);

        intent.putExtra(Constants.BROADCAST_RECEIVER_EXTRA, broadcastMessage);
        context.sendBroadcast(intent);
    }

    public void stopThread() {
        isRunning = false;
    }

}
