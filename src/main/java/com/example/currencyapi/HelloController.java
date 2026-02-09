package com.example.currencyapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class HelloController {
    public TextField baseTextField;
    public TextField rateTextField;
    public TextField convertionTextField;

    public void initialize() throws Exception {
    }

    public void getRate() throws Exception {
        String baseTypedIn = baseTextField.getText();
        String conversionTypedIn = convertionTextField.getText();
        // ??? get baseTypedIn from a text field
        //https://api.frankfurter.dev/v1/latest?base=EUR&symbols=USD
        String yourAPIurl = "https://api.frankfurter.dev/v1/latest?base=" + baseTypedIn + "&symbols=" + conversionTypedIn;
        String yourAPIkey = "YOUR API KEY";
        URL APIurl = new URL(yourAPIurl);
        HttpURLConnection APIconnection = (HttpURLConnection) APIurl.openConnection();
        APIconnection.setRequestMethod("GET");
        APIconnection.setRequestProperty("Accept", "application/json");
        APIconnection.setRequestProperty("Authorization", yourAPIkey);
        InputStreamReader APIinStream = new InputStreamReader(APIconnection.getInputStream());
        BufferedReader APIreader = new BufferedReader(APIinStream);
        StringBuilder JSONstring = new StringBuilder();
        String line;
        while ((line = APIreader.readLine()) != null) {
            JSONstring.append(line);
        }
        APIreader.close();
        System.out.println(JSONstring);

        // JSONString has already been read from URL
        // read 1 JSON object ("key":"value" pairs) into fields of MODEL object
        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyData myData = objectMapper.readValue(JSONstring.toString(), CurrencyData.class);
        System.out.println("OBJECT: " + myData);

        rateTextField.setText(String.valueOf(myData.ratesData.getUSD()));
    }

    public void convertCurrency() {
        //baseTextField = baseTypedIn;.
    }
}