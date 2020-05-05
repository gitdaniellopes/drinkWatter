package br.com.daniel.drinkwater;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_NOTIFY = "key_notify";
    private static final String KEY_INTERNAL = "key_internal";
    private static final String KEY_HOUR = "key_hour";
    private static final String KEY_MINUTE = "key_minute";
    private TimePicker timePicker;
    private EditText editMinute;
    private Button buttonNotify;
    private String sInterval;
    private int hour;
    private int minute;
    private boolean activated;
    private int interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind();
        final SharedPreferences storage = createdBank();
        get24Hour();
        findInfoSave(storage);
        clickButton(storage);
    }

    private void clickButton(final SharedPreferences storage) {
        buttonNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activated) {
                    sInterval = editMinute.getText().toString();
                    if (sInterval.isEmpty()) {
                        Toast.makeText(MainActivity.this, R.string.validation,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    getPikerEEdit();
                    setLayoutButtonNoActivated();
                    save(storage);
                    activated = true;

                } else {
                    setLayoutButtonIsActivated();
                    activated = false;
                    remove(storage);
                }

            }
        });
    }

    private void setLayoutButtonIsActivated() {
        buttonNotify.setText(R.string.notify);
        buttonNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                R.color.colorPrimary));
    }

    private void setLayoutButtonNoActivated() {
        buttonNotify.setText(R.string.pause);
        buttonNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                android.R.color.black));
    }

    private void findInfoSave(SharedPreferences storage) {
        activated = storage.getBoolean(KEY_NOTIFY, false);
        verifyActivated(storage);
    }

    private void verifyActivated(SharedPreferences storage) {
        if (activated) {
            setLayoutButtonNoActivated();
            editMinute.setText(String.valueOf(storage.getInt(KEY_INTERNAL, 0)));
            timePicker.setCurrentHour(storage.getInt(KEY_HOUR, timePicker.getCurrentHour()));
            timePicker.setCurrentMinute(storage.getInt(KEY_MINUTE, timePicker.getCurrentMinute()));
        } else {
            setLayoutButtonIsActivated();
        }
    }

    private void remove(SharedPreferences storage) {
        SharedPreferences.Editor edit = storage.edit();
        edit.putBoolean(KEY_NOTIFY, false);
        edit.remove(KEY_INTERNAL);
        edit.remove(KEY_HOUR);
        edit.remove(KEY_MINUTE);
        edit.apply();
    }

    private void save(SharedPreferences storage) {
        SharedPreferences.Editor edit = storage.edit();
        edit.putBoolean(KEY_NOTIFY, true);
        edit.putInt(KEY_INTERNAL, interval);
        edit.putInt(KEY_HOUR, hour);
        edit.putInt(KEY_MINUTE, minute);
        edit.apply();
    }

    private SharedPreferences createdBank() {
        return getSharedPreferences("banck", Context.MODE_PRIVATE);
    }

    private void get24Hour() {
        timePicker.setIs24HourView(true);
    }

    private void getPikerEEdit() {
        interval = Integer.parseInt(sInterval);
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
    }

    private void bind() {
        timePicker = findViewById(R.id.time_piker);
        editMinute = findViewById(R.id.edit_number_interval);
        buttonNotify = findViewById(R.id.button_notify);
    }
}
