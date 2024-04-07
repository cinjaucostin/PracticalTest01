package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private Button pressMeButton;
    private Button pressMeTooButton;
    private Button navigateToSecondaryActivityButton;
    private EditText firstNumberEditText;
    private EditText secondNumberEditText;
    private PracticalTest01BroadcastReceiver broadcastReceiver;
    private int serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    class OnClickGeneralListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if(id == R.id.press_me_button) {
                handlePressMeButton();
            } else if(id == R.id.press_me_too_button) {
                handlePressMeTooButton();
            } else if(id == R.id.navigate_to_secondary_activity_button) {
                handleNavigateToSecondaryActivity();
            }

        }
    }

    public void handlePressMeButton() {
        firstNumberEditText.setText(String.valueOf(Integer.parseInt(String.valueOf(firstNumberEditText.getText())) + 1));

        if (Integer.parseInt(firstNumberEditText.getText().toString()) +
                Integer.parseInt(secondNumberEditText.getText().toString()) >
                Constants.NUMBER_OF_CLICKS_THRESHOLD
                && serviceStatus == Constants.SERVICE_STOPPED) {
            Intent serviceIntent = new Intent(this, PracticalTest01Service.class);
            serviceIntent.putExtra(Constants.INPUT1, Integer.parseInt(firstNumberEditText.getText().toString()));
            serviceIntent.putExtra(Constants.INPUT2, Integer.parseInt(secondNumberEditText.getText().toString()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            serviceStatus = Constants.SERVICE_STARTED;
        }
    }

    public void handlePressMeTooButton() {
        secondNumberEditText.setText(String.valueOf(Integer.parseInt(String.valueOf(secondNumberEditText.getText())) + 1));

        if (Integer.parseInt(firstNumberEditText.getText().toString()) +
                Integer.parseInt(secondNumberEditText.getText().toString()) >
                Constants.NUMBER_OF_CLICKS_THRESHOLD
                && serviceStatus == Constants.SERVICE_STOPPED) {
            Intent serviceIntent = new Intent(this, PracticalTest01Service.class);
            serviceIntent.putExtra(Constants.INPUT1, Integer.parseInt(firstNumberEditText.getText().toString()));
            serviceIntent.putExtra(Constants.INPUT2, Integer.parseInt(secondNumberEditText.getText().toString()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            serviceStatus = Constants.SERVICE_STARTED;
        }

    }

    public void handleNavigateToSecondaryActivity() {
//        Intent goToSecondaryActivtyIntent = new Intent(
//                PracticalTest01MainActivity.this,
//                PracticalTest01SecondaryActivity.class
//        );
//
//        OR

        Intent goToSecondaryActivtyIntent = new Intent();
        goToSecondaryActivtyIntent.setComponent(new ComponentName(
                "ro.pub.cs.systems.eim.practicaltest01",
                "ro.pub.cs.systems.eim.practicaltest01.PracticalTest01SecondaryActivity"));


        int countSum = Integer.parseInt(firstNumberEditText.getText().toString()) +
                Integer.parseInt(secondNumberEditText.getText().toString());
        goToSecondaryActivtyIntent.putExtra(Constants.COUNT, countSum);

        activityResultLauncher.launch(goToSecondaryActivtyIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if(result.getData() != null) {
                    int count = result.getData().getIntExtra(Constants.COUNT, 0);
                    Toast.makeText(this, "The activity returned with OK, count = " + count, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "The activity returned with CANCEL", Toast.LENGTH_LONG).show();
            }
        });

        pressMeButton = findViewById(R.id.press_me_button);
        pressMeTooButton = findViewById(R.id.press_me_too_button);
        navigateToSecondaryActivityButton = findViewById(R.id.navigate_to_secondary_activity_button);
        firstNumberEditText = findViewById(R.id.number1);
        secondNumberEditText = findViewById(R.id.number2);

        firstNumberEditText.setText("0");
        secondNumberEditText.setText("0");

        pressMeButton.setOnClickListener(new OnClickGeneralListener());
        pressMeTooButton.setOnClickListener(new OnClickGeneralListener());
        navigateToSecondaryActivityButton.setOnClickListener(new OnClickGeneralListener());

        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_STRING);
        broadcastReceiver = new PracticalTest01BroadcastReceiver();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.INPUT1, firstNumberEditText.getText().toString());
        outState.putString(Constants.INPUT2, secondNumberEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState.containsKey(Constants.INPUT1)) {
            firstNumberEditText.setText(savedInstanceState.getString(Constants.INPUT1));
        } else {
            firstNumberEditText.setText("0");
        }

        if(savedInstanceState.containsKey(Constants.INPUT2)) {
            secondNumberEditText.setText(savedInstanceState.getString(Constants.INPUT2));
        } else {
            secondNumberEditText.setText("0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("PracticalTest01MainActivity", "onResume() callback method invoked.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        }
    }

    @Override
    protected void onPause() {
        Log.d("PracticalTest01MainActivity", "onPause() callback method invoked.");
        unregisterReceiver(broadcastReceiver);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("PracticalTest01MainActivity", "onPause() callback method invoked.");
        Intent intent = new Intent(PracticalTest01MainActivity.this, PracticalTest01Service.class);
        stopService(intent);
        serviceStatus = Constants.SERVICE_STOPPED;

        Log.d("PracticalTest01MainActivity", "onDestroyCommand() callback method was invoked.");
        super.onDestroy();
    }
}