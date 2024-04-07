package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {

    private Button okButton;
    private Button cancelButton;
    private TextView countTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        okButton = findViewById(R.id.ok_button);
        cancelButton = findViewById(R.id.cancel_button);
        countTextView = findViewById(R.id.count_text_view);

        Intent intent = getIntent();
        int count = intent.getIntExtra(Constants.COUNT, 0);

        countTextView.setText("Count: " + count);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToParent = new Intent();
                intentToParent.putExtra(Constants.COUNT, count);
                setResult(RESULT_OK, intentToParent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToParent = new Intent();
                setResult(RESULT_CANCELED, intentToParent);
                finish();
            }
        });

    }
}