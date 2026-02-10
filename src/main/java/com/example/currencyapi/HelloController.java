package com.example.currencyapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.plaf.ColorUIResource;
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
        //String yourAPIurl = "https://api.frankfurter.dev/v1/latest?symbols=USD";
        String yourAPIurl = "https://api.frankfurter.dev/v1/latest?base=" + "EUR" + "&symbols=" + "USD";
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
        JsonNode jsonNode = objectMapper.readTree(JSONstring.toString());
        String amountString = jsonNode.get("amount").toString();
        String baseString = jsonNode.get("base").toString();
        String usdRate = null;
        try {
            usdRate = jsonNode.get("rates").get("USD").toString();
        } catch (Exception ex) {
            System.out.println("NO USD rate");
        }
        String eurRate = null;
        try {
            eurRate = jsonNode.get("rates").get("EUR").toString();
        } catch (Exception ex) {
            System.out.println("NO EUR rate");
        }

        CurrencyData myData = new CurrencyData();
        RatesData myRates = new RatesData();
        myData.setRatesData(myRates);
        myData.setAmount(Float.parseFloat(amountString));
        myData.setBase(baseString);
        Float rateWeGot = 0f;
        if (usdRate != null) {
            myData.getRatesData().setUSD(Float.parseFloat(usdRate));
            rateWeGot = Float.parseFloat(usdRate);
        }
        if (eurRate != null) {
            myData.getRatesData().setEUR(Float.parseFloat(eurRate));
            rateWeGot = Float.parseFloat(eurRate);

        }
        System.out.println("OBJECT: " + myData);

        rateTextField.setText(String.valueOf(rateWeGot));
    }

    public void convertCurrency() {
        //baseTextField = baseTypedIn;
    }
}