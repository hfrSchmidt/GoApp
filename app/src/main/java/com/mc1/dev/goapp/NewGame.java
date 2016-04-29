package com.mc1.dev.goapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class NewGame extends AppCompatActivity {


    private TextView switchStatus;
    private Switch  extendedOptSwitch;
    private Switch randomBnWSwitch;

    // ----------------------------------------------------------------------
    // function onCreate
    // ----------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        // --------------------------------------------
        // handle extendedOptionsSwitch
        // --------------------------------------------
        extendedOptSwitch = (Switch) findViewById(R.id.extendedOptionsSwitch);
        extendedOptSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton switchView, boolean isChecked) {

                if(isChecked){
                    extendedOptSwitch.setText(R.string.extended_options_switch_on);
                }
                else{
                    extendedOptSwitch.setText(R.string.extended_options_switch_off);
                }
            }
        });

        // --------------------------------------------
        // handle randomBnWSwitch
        // --------------------------------------------
        randomBnWSwitch = (Switch) findViewById(R.id.randomBnWSwitch);
        randomBnWSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton switchView, boolean isChecked) {

                TextView black = (TextView) findViewById(R.id.blackName);
                TextView white = (TextView) findViewById(R.id.whiteName);
                if (black != null && white != null) { // remove null pointer warnings -> else branch would be failure to load the gui
                    if (isChecked) {
                        randomBnWSwitch.setText(R.string.randomBnW_switch_on);

                        black.setText(R.string.label_player_1);
                        white.setText(R.string.label_player_2);
                    }
                    else {
                        randomBnWSwitch.setText(R.string.randomBnW_switch_off);
                        black.setText(R.string.label_black);
                        white.setText(R.string.label_white);
                    }
                }
            }
        });
    }
}
