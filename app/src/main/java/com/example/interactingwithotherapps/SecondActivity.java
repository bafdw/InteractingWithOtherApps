package com.example.interactingwithotherapps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(Intent.EXTRA_TEXT)){
            String shareText = extras.getString(Intent.EXTRA_TEXT);

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText("Sharing: " + shareText);
        }
    }

    static final String RESULT_NAME = "com.example.interactingwithotherapps.RESULT_NAME";
    public void setResult(View view) {
        Button button = (Button) view;
        String stringResult = button.getText().toString();

        // Create intent to deliver some kind of result data
        Intent resultIntent = new Intent("com.example.interactingwithotherapps.RESULT_ACTION");
        resultIntent.putExtra(RESULT_NAME, stringResult);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
