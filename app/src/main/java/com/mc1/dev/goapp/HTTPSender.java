package com.mc1.dev.goapp;

import java.util.logging.Level;

/**
 * Created by Kmp on 08.12.2016.
 */
public class HTTPSender {

    private final String SERVERURL = "localhost/goserver"; // change to hosted url

    public HTTPSender() {

    };

    public void postMatch(String token, String JSONData) {
        HttpURLConnection connection = null;

        jsobj.put("to", token);

        try {
            //Create connection
            URL url = new URL(SERVERURL + "/match/" + token);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key=" + SERVERKEY);

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(jsobj.toJSONString());
            wr.close();

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return false;
        }

        // catch response
        try {
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return checkResponse(response.toString());
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return false;
        }
    }

    public void deleteMatch(String token) {
        HttpURLConnection connection = null;

        jsobj.put("to", token);

        try {
            //Create connection
            URL url = new URL(SERVERURL + "/match/" + token);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(jsobj.toJSONString());
            wr.close();

        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }

        // receive response
        try {
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return checkResponse(response.toString());
        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }
    }
}
