package com.example.beberagua;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnNotify;
    private EditText editTextInterval;
    private TimePicker timePicker;
    private int hour, minute, interval;
    private Boolean activated = false;

    //Local database for storing small amounts of data
    private SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.btn_notify);
        editTextInterval = findViewById(R.id.txt_edit_editMinutes);
        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);
        activated = preferences.getBoolean("activated", false);

        if (activated) {
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            btnNotify.setText(R.string.pause);
            activated = true;

            hour = preferences.getInt("hour", timePicker.getCurrentHour());
            timePicker.setCurrentHour(hour);
            minute = preferences.getInt("minute", timePicker.getCurrentMinute());
            timePicker.setCurrentMinute(minute);
            interval = preferences.getInt("interval", 0);
            editTextInterval.setText(String.valueOf(interval));

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickNotify(View view) {
        String sInterval = editTextInterval.getText().toString();
        if (sInterval.isEmpty()) {
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_LONG).show();
            return;
        }

        interval = Integer.parseInt(sInterval);
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();

        if (!activated) {
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            btnNotify.setText(R.string.pause);
            activated = true;

            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean("activated", activated);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);

            //Confirm changes in db
            editor.apply();
        } else {
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.design_default_color_primary));
            btnNotify.setText(R.string.btn_notify_text);
            activated = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("activated");
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();
        }

        Log.d("test", "Hour: " + hour + " Minute: " + minute + " Inteval: " + interval);
    }


}