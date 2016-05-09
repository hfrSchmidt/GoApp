package com.mc1.dev.goapp;


import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.net.Uri;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class NewGame extends AppCompatActivity {

    private Switch extendedOptSwitch;
    private Switch randomBnWSwitch;
    private ViewStub extendedOptionsStub;
    private View extendedOptionsView;
    private Spinner boardSizeSpinner;
    private TextView currentHandicapStones;
    private Button startGameButton;

    // ----------------------------------------------------------------------
    // function onCreate
    // ----------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        extendedOptionsStub = (ViewStub) findViewById(R.id.extendedOptionsStub);

        boardSizeSpinner = (Spinner) findViewById(R.id.boardSizeSpinner);
        if (boardSizeSpinner != null) {
            fillSpinner(boardSizeSpinner, fetchMapSizeElements());
        }

        startGameButton = (Button) findViewById(R.id.startGameButton);

        // --------------------------------------------
        // handle extendedOptionsSwitch
        // --------------------------------------------
        extendedOptSwitch = (Switch) findViewById(R.id.extendedOptionsSwitch);
        extendedOptSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton switchView, boolean isChecked){
                if (extendedOptSwitch != null) {
                    if(isChecked){
                        extendedOptSwitch.setText(R.string.extended_options_switch_on);
                        // handle extended options menu
                        if (extendedOptionsView == null) { // initial set to "on"
                            extendedOptionsStub.inflate();
                            fillExtendedOptions();  // load data from options xml
                            extendedOptionsView = findViewById(R.id.extendedOptionsView);

                            // set new layout parameter to button, so it will be below the extOüptsView instead of the stub
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) startGameButton.getLayoutParams();
                            params.addRule(RelativeLayout.BELOW, R.id.extendedOptionsView);
                        }
                        else { // every other case of switch being flipped
                            extendedOptionsView.setVisibility(View.VISIBLE);
                        }
                    }
                    else{ // if !isChecked
                        extendedOptSwitch.setText(R.string.extended_options_switch_off);
                        if (extendedOptionsView != null && startGameButton != null) { // protect from initial missing view, as it is inflated on first call
                            extendedOptionsView.setVisibility(View.GONE);
                        }
                    }
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

    // ----------------------------------------------------------------------
    // function onFragmentInteraction
    // handles data interaction with the extended options fragment
    // if the extended options switch is turned on
    // ----------------------------------------------------------------------
    public void onFragmentInteraction(Uri uri) {

    }

    // ----------------------------------------------------------------------
    // function fillSpinner
    // creates an adapter for input spinners and transforms given data
    // to option elements
    // ----------------------------------------------------------------------
    public void fillSpinner(Spinner spinner, ArrayList<String> elements) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // ----------------------------------------------------------------------
    // function fetchMapSizeElements()
    // fetches data for map sizes from the options resource file
    // @return all configured map size choices
    // ----------------------------------------------------------------------
    private ArrayList<String> fetchMapSizeElements() {
        XmlResourceParser xrp = getResources().getXml(R.xml.options);
        ArrayList<String> allSizes = new ArrayList<>();

        try {
            int eventType = xrp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_TAG :
                        if (xrp.getName().equals("Size")) {
                            String oneNumber = xrp.getAttributeValue(null, "value");
                            allSizes.add(oneNumber + " x " + oneNumber); // create the strings for the "height x width" options
                        }
                        break;
                }
                eventType = xrp.next();
            }
            xrp.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return allSizes;
    }


    // ----------------------------------------------------------------------
    // function fillExtendedOptions
    // fills the input-elements of the extended options
    // menu. also picks the needed elements from the layout
    //
    // needs to be called after inflating the extendedOptionsStub to a full view
    // only needs to be called once! in the whole activity lifecycle
    //
    // calls fillHandicapSeekBar
    //
    // ----------------------------------------------------------------------

    private void fillExtendedOptions() {
        Spinner timeModeSpinner = (Spinner) findViewById(R.id.timeModeSpinner);
        Spinner komiSpinner = (Spinner) findViewById(R.id.komiSpinner);
        SeekBar handicapSeekBar = (SeekBar) findViewById(R.id.handicapSeekBar);

        if (timeModeSpinner != null && komiSpinner != null && handicapSeekBar != null) {
            fillSpinner(timeModeSpinner, fetchTimeModeElements());
            fillSpinner(komiSpinner, fetchKomiElements());
            fillHandicapSeekBar(handicapSeekBar);
        }
    }

    // ----------------------------------------------------------------------
    // function fetchTimeModeElements()
    // fetches data for time modes from the options resource file
    // @return all configured time mode choices
    // ----------------------------------------------------------------------
    private ArrayList<String> fetchTimeModeElements() {
        XmlResourceParser xrp = getResources().getXml(R.xml.options);
        ArrayList<String> allModes = new ArrayList<>();

        try {
            int eventType = xrp.getEventType();
            String text = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.TEXT : text = xrp.getText(); break;
                    case XmlPullParser.END_TAG :
                        if (xrp.getName().equals("Mode")) {
                            allModes.add(text);
                        }
                        break;
                }
                eventType = xrp.next();
            }
            xrp.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return allModes;
    }

    // ----------------------------------------------------------------------
    // function fetchKomiElements()
    // fetches data for komi settings from the options resource file
    // @return all configured komi choices
    // ----------------------------------------------------------------------
    private ArrayList<String> fetchKomiElements() {
        XmlResourceParser xrp = getResources().getXml(R.xml.options);
        ArrayList<String> allKomi = new ArrayList<>();

        try {
            int eventType = xrp.getEventType();
            String text = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.TEXT: text = xrp.getText(); break;
                    case XmlPullParser.END_TAG :
                        if (xrp.getName().equals("Komi")) {
                            allKomi.add(text);
                        }
                        break;
                }
                eventType = xrp.next();
            }
            xrp.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return allKomi;
    }

    // ----------------------------------------------------------------------
    // function fillHandicapSeekBar
    // reads the data from options resource file and fills the spinner with
    // the parsed data
    //
    // needs to be called after inflating the extendedOptionsStub to a full
    // view, if not, the spinner will be empty
    // called in fillExtendedOptions
    // ----------------------------------------------------------------------
    private void fillHandicapSeekBar(SeekBar handicapSeekBar) {
        currentHandicapStones = (TextView) findViewById(R.id.currentHandicapStones);

        if (currentHandicapStones != null && handicapSeekBar != null) {
            String displayedText = getResources().getString(R.string.label_amountOfHandicapStones) + handicapSeekBar.getProgress();
            // Initialize the textview with '0'.
            currentHandicapStones.setText(displayedText);

            handicapSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int hcStones = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO: Make the SeekBar only show the values 0 and 2-9
                   if (progress == 0) {
                       hcStones = progress;
                   }
                   else {
                        hcStones = progress+1;
                   }
                    String newHcStones = getResources().getString(R.string.label_amountOfHandicapStones) + hcStones;
                    currentHandicapStones.setText(newHcStones);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    String newHcStones = getResources().getString(R.string.label_amountOfHandicapStones) + hcStones;
                    currentHandicapStones.setText(newHcStones);
                }
            });
        }
    }

    // ----------------------------------------------------------------------
    // function startGame
    // will create a new Instance of GameMetaInformation, which is used to
    // create a new RunningGame from the GameController
    //
    // is called, when the startGameButton is activated
    // ----------------------------------------------------------------------
    public void startGame() {

        GameMetaInformation gmi = new GameMetaInformation();

        // TODO fill gmi and call controller
    }

}
