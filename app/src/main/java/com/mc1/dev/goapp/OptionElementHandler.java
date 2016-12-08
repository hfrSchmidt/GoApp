package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class OptionElementHandler
// author Hendrik Schmidt
//
// handles the reading of option parameters needed in multiple activities
// ----------------------------------------------------------------------

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

class OptionElementHandler {
    private static OptionElementHandler ourInstance = new OptionElementHandler();

    static OptionElementHandler getInstance() {
        return ourInstance;
    }

    private OptionElementHandler() {
    }

    // ----------------------------------------------------------------------
    // function fillBoardSizeSpinner
    // populates a given board size spinner with data from the options
    // resource file
    // ----------------------------------------------------------------------
    void fillBoardSizeSpinner(Spinner bsp, Context ctx) {
        fillSpinner(bsp, fetchMapSizeElements(ctx), ctx);
    }

    // ----------------------------------------------------------------------
    // function fillBoardSizeSpinner
    // populates a given time mode spinner with data from the options
    // resource file
    // ----------------------------------------------------------------------
    void fillTimeModeSpinner(Spinner tmsp, Context ctx) {
        fillSpinner(tmsp, fetchElements(ctx, "Mode"), ctx);
    }

    // ----------------------------------------------------------------------
    // function fillBoardSizeSpinner
    // populates a given komi spinner with data from the options
    // resource file
    // ----------------------------------------------------------------------
    void fillKomiSpinner(Spinner ksp, Context ctx) {
        fillSpinner(ksp, fetchElements(ctx, "Komi"), ctx);
    }

    // ----------------------------------------------------------------------
    // function fillRankLevelSpinner
    // populates a given rank level spinner with data from the options
    // resource file
    // ----------------------------------------------------------------------
    void fillRankLevelSpinner(Spinner rlsp, Context ctx) {
        fillSpinner(rlsp, fetchElements(ctx, "Level"), ctx);
    }

    // ----------------------------------------------------------------------
    // function fillSpinner
    // creates an adapter for input spinners and transforms given data
    // to option elements
    // ----------------------------------------------------------------------
    private void fillSpinner(Spinner spinner, ArrayList<String> elements, Context ctx) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, elements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // ----------------------------------------------------------------------
    // function fetchMapSizeElements()
    // fetches data for map sizes from the options resource file
    // @return all configured map size choices
    // ----------------------------------------------------------------------
    private ArrayList<String> fetchMapSizeElements(Context ctx) {
        XmlResourceParser xrp = ctx.getResources().getXml(R.xml.options);
        ArrayList<String> allSizes = new ArrayList<>();

        try {
            int eventType = xrp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (xrp.getName().equals("Size")) {
                            String oneNumber = xrp.getAttributeValue(null, "value");
                            allSizes.add(oneNumber + " x " + oneNumber); // create the strings for the "height x width" options
                        }
                        break;
                }
                eventType = xrp.next();
            }
            xrp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allSizes;
    }

    // ----------------------------------------------------------------------
    // function fetchElements()
    // fetches data for elements identified by the nodeName String from the
    // options resource file
    // @return all configured element choices
    // ----------------------------------------------------------------------
    private ArrayList<String> fetchElements(Context ctx, String nodeName) {
        XmlResourceParser xrp = ctx.getResources().getXml(R.xml.options);
        ArrayList<String> allNodes = new ArrayList<>();

        try {
            int eventType = xrp.getEventType();
            String text = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        text = xrp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (xrp.getName().equals(nodeName)) {
                            allNodes.add(text);
                        }
                        break;
                }
                eventType = xrp.next();
            }
            xrp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allNodes;
    }

    public void fillRankPicker(NumberPicker rankPicker, Context cxt) {
        // TODO
       /* String[] values = ["1 Schüler", "2 Schüler", "1 Dan", "2 Dan"];
        rankPicker.setDisplayedValues(values); */
    }
}
