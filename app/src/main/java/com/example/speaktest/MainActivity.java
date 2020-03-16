package com.example.speaktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private TextView textOutput;
    private TextView textViewStatus;
    private Spinner spinner;
    private String language = "en-US";
    private static final String[] langs = {"cs","de","en-US","es","it","fr","nl","pl","pt","ru","sv","tr"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textOutput = (TextView) findViewById(R.id.textOutput);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewStatus.setText("Language: "+language);

        spinner = (Spinner)findViewById(R.id.spinnerLangs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, langs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                language = (String) parent.getItemAtPosition(position);
                textViewStatus.setText("Language: "+language);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        spinner.setSelection(2);
    }

    public void onClick(View v){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        try {
            // Start the Activity and wait for the response
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) { }
    }

    @Override
    //Handle the results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textOutput.setText(result.get(0));
                    float[] confidences = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
                    textViewStatus.setText("Language: "+language+"\nConfidence: "+String.valueOf(confidences[0]));
                }
                break;
            }
        }
    }


}
